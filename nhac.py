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

BRIGHTNESS = 4             # Khóa cứng độ sáng tối đa (4/4)
MODES = list(range(1, 21))
# ==========================================

VK_RCONTROL = 0xA3  # Phím Ctrl phải: Đổi hiệu ứng (1 -> 20)
VK_RMENU = 0xA5     # Phím Alt phải: Chỉnh tốc độ riêng cho hiệu ứng đang bật (1 -> 4)

# Lưu tốc độ riêng cho từng hiệu ứng (mặc định đều là 4)
mode_speeds = {m: 4 for m in MODES}

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

def set_led(h, mode, hue, speed=4, switch_mode=False):
    # Byte 11 = 1: BẬT Monochrome | Brightness = 4 | Speed = 1..4 tùy từng mode
    s = max(1, min(4, int(speed)))
    if switch_mode:
        send_cmd(h, [22, 0, 0, 0, 1, 0, mode])
    send_cmd(h, [11, 11, 0, 0, 1, 0, mode, BRIGHTNESS, s, 1, 1, 7, hue & 0xFF, 255, 255])

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

    mode_idx = 5  # Bắt đầu tại mode = 6
    hue = 0
    cur_mode = MODES[mode_idx]
    set_led(kb, cur_mode, hue, speed=mode_speeds[cur_mode], switch_mode=True)

    print(f"Dang chay Monochrome theo beat tu: {device['name']}")
    print("Phim tat: [Right Ctrl] = Doi hieu ung | [Right Alt] = Doi toc do (1..4) | Ctrl+C = Dung")
    print(f"-> Hieu ung: mode = {cur_mode} ({mode_idx + 1}/20) | Toc do: {mode_speeds[cur_mode]}/4 | Do sang: {BRIGHTNESS}/4")

    avg_energy = 100.0
    last_send_time = 0.0
    last_switch_time = 0.0
    last_speed_time = 0.0
    hold_until = 0.0
    current_tier = 0

    while True:
        now = time.time()
        cur_mode = MODES[mode_idx]

        # 1. Bấm Right Ctrl: Đổi hiệu ứng
        if (user32.GetAsyncKeyState(VK_RCONTROL) & 0x0001) and (now - last_switch_time >= 0.25):
            mode_idx = (mode_idx + 1) % len(MODES)
            cur_mode = MODES[mode_idx]
            set_led(kb, cur_mode, hue, speed=mode_speeds[cur_mode], switch_mode=True)
            print(f"-> Doi hieu ung: mode = {cur_mode} ({mode_idx + 1}/20) | Toc do: {mode_speeds[cur_mode]}/4 | Do sang: {BRIGHTNESS}/4")
            last_switch_time = now
            last_send_time = now

        # 2. Bấm Right Alt: Chỉnh tốc độ (1 -> 2 -> 3 -> 4) cho hiệu ứng hiện tại
        if (user32.GetAsyncKeyState(VK_RMENU) & 0x0001) and (now - last_speed_time >= 0.25):
            mode_speeds[cur_mode] = (mode_speeds[cur_mode] % 4) + 1
            set_led(kb, cur_mode, hue, speed=mode_speeds[cur_mode], switch_mode=True)
            print(f"-> Chinh toc do mode {cur_mode}: Toc do {mode_speeds[cur_mode]}/4 | Do sang: {BRIGHTNESS}/4")
            last_speed_time = now
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
            set_led(kb, cur_mode, hue, speed=mode_speeds[cur_mode])
            last_send_time = now

        elif current_tier > 0 and now >= hold_until and (now - last_send_time >= MIN_CMD_INTERVAL):
            hue = (hue + 8) % 256
            set_led(kb, cur_mode, hue, speed=mode_speeds[cur_mode])
            current_tier = 0
            last_send_time = now

except KeyboardInterrupt:
    set_led(kb, 6, 255, speed=4, switch_mode=True)
    print("\nDa dung va khoi phuc den goc!")
finally:
    if 'stream' in locals():
        stream.stop_stream()
        stream.close()
    p.terminate()
    kb.close()