import hid
import time
import math
import struct
import random
import ctypes
import pyaudiowpatch as pyaudio

# ==========================================
# CẤU HÌNH NHẠY BEAT & FULL 20 HIỆU ỨNG:
SMALL_BEAT = 1.15          # Ngưỡng beat nhỏ (vùng sáng hẹp)
MID_BEAT = 1.35            # Ngưỡng beat vừa (vùng sáng trung bình)
BIG_BEAT = 1.50            # Ngưỡng beat mạnh / drop (bung sáng toàn phím)
MIN_CMD_INTERVAL = 0.085   # Nhịp gửi lệnh an toàn cho chip TK61 Pro

# Đầy đủ toàn bộ 20 hiệu ứng từ 1 đến 20
MODES = list(range(1, 21))
# ==========================================

VK_RCONTROL = 0xA3  # Phím Ctrl bên phải để đổi hiệu ứng

def open_kb():
    for d in hid.enumerate(0x36AE, 0xFEAD):
        if d['usage_page'] == 0xFF00 and d['usage'] == 0x0002:
            h = hid.device()
            h.open_path(d['path'])
            return h
    return None

def send_cmd(h, payload):
    pkt = [0, 6] + payload
    pkt += [0] * (65 - len(pkt))
    h.write(pkt)
    return h.read(64, timeout_ms=50)

def set_led(h, mode, hue, switch_mode=False):
    # Byte 11 = 0: Tắt Monochrome (RGB đa sắc)
    # Brightness = 4, Speed = 4, Val = 255: Luôn sáng tối đa mọi lúc
    if switch_mode:
        send_cmd(h, [22, 0, 0, 0, 1, 0, mode])
    send_cmd(h, [11, 11, 0, 0, 1, 0, mode, 4, 4, 1, 0, 7, hue & 0xFF, 255, 255])

def calc_rms(raw_bytes):
    count = len(raw_bytes) // 2
    if count == 0:
        return 0.0
    samples = struct.unpack(f"<{count}h", raw_bytes[:count * 2])
    sum_sq = sum(s * s for s in samples)
    return math.sqrt(sum_sq / count)

def get_loopback_device(p):
    if hasattr(p, "get_default_wasapi_loopback"):
        return p.get_default_wasapi_loopback()
    wasapi = p.get_host_api_info_by_type(pyaudio.paWASAPI)
    default_speakers = p.get_device_info_by_index(wasapi["defaultOutputDevice"])
    if not default_speakers.get("isLoopback", False):
        loopbacks = list(p.get_loopback_device_info_generator())
        for loopback in loopbacks:
            if default_speakers["name"] in loopback["name"]:
                return loopback
        if loopbacks:
            return loopbacks[0]
    return default_speakers

kb = open_kb()
if not kb:
    print("Khong tim thay ban phim! Hay tat SDTech Options truoc.")
    exit()

p = pyaudio.PyAudio()
user32 = ctypes.windll.user32

try:
    device = get_loopback_device(p)
    channels = device["maxInputChannels"]
    rate = int(device["defaultSampleRate"])

    stream = p.open(
        format=pyaudio.paInt16,
        channels=channels,
        rate=rate,
        input=True,
        input_device_index=device["index"],
        frames_per_buffer=1024
    )

    mode_idx = 5  # Bắt đầu tại mode = 6 (Bloom); bấm Right Ctrl để duyệt đủ 1 -> 20
    hue = 0
    set_led(kb, MODES[mode_idx], hue, switch_mode=True)
    print(f"Dang chay full 20 hieu ung RGB (Max sang 4, da tat Monochrome) tu: {device['name']}")
    print("Bam [Right Ctrl] de chuyen qua 20 hieu ung | Nhan Ctrl+C trong Terminal de dung.")
    print(f"-> Hieu ung hien tai: mode = {MODES[mode_idx]} ({mode_idx + 1}/20)")

    avg_energy = 100.0
    last_send_time = 0.0
    last_switch_time = 0.0
    hold_until = 0.0
    current_tier = 0

    while True:
        now = time.time()

        # Bấm Right Ctrl để xoay vòng đủ 20 hiệu ứng
        if (user32.GetAsyncKeyState(VK_RCONTROL) & 0x0001) and (now - last_switch_time >= 0.25):
            mode_idx = (mode_idx + 1) % len(MODES)
            set_led(kb, MODES[mode_idx], hue, switch_mode=True)
            print(f"-> Da chuyen sang: mode = {MODES[mode_idx]} ({mode_idx + 1}/20)")
            last_switch_time = now
            last_send_time = now

        data = stream.read(1024, exception_on_overflow=False)
        rms = calc_rms(data)

        avg_energy = avg_energy * 0.90 + rms * 0.10
        ratio = rms / max(125.0, avg_energy)

        tier = 0
        if rms > 125.0:
            if ratio >= BIG_BEAT:
                tier = 3
            elif ratio >= MID_BEAT:
                tier = 2
            elif ratio >= SMALL_BEAT:
                tier = 1

        if tier > 0 and (now >= hold_until or tier > current_tier) and (now - last_send_time >= MIN_CMD_INTERVAL):
            current_tier = tier
            if tier == 3:
                hue = (hue + random.randint(85, 140)) % 256
                hold_until = now + 0.42
            elif tier == 2:
                hue = (hue + 38) % 256
                hold_until = now + 0.22
            else:
                hue = (hue + 20) % 256
                hold_until = now + 0.11
            set_led(kb, MODES[mode_idx], hue)
            last_send_time = now

        elif current_tier > 0 and now >= hold_until and (now - last_send_time >= MIN_CMD_INTERVAL):
            hue = (hue + 8) % 256
            set_led(kb, MODES[mode_idx], hue)
            current_tier = 0
            last_send_time = now

except KeyboardInterrupt:
    set_led(kb, 6, 255, switch_mode=True)
    print("\nDa dung va khoi phuc den goc!")
finally:
    if 'stream' in locals():
        stream.stop_stream()
        stream.close()
    p.terminate()
    kb.close()