import hid
import time
import math
import struct
import random
import ctypes
import pyaudiowpatch as pyaudio

# ==========================================
# TÙY CHỈNH TỐC ĐỘ CHUYỂN HIỆU ỨNG & BEAT:
AUTO_SWITCH_ON_DROP = True     # Tự nhảy hiệu ứng ngay khi có tiếng bass/drop
DROP_SWITCH_COOLDOWN = 0.6     # Khoảng cách tối thiểu giữa 2 lần đổi theo beat (giây)
AUTO_SWITCH_SECONDS = 4.0      # Tự đổi sang hiệu ứng khác sau mỗi 4 giây nếu nhạc êm

SMALL_BEAT = 1.15              # Ngưỡng beat nhỏ
MID_BEAT = 1.35                # Ngưỡng beat vừa
BIG_BEAT = 1.48                # Ngưỡng beat mạnh / drop
MIN_CMD_INTERVAL = 0.085       # Nhịp gửi lệnh an toàn cho chip TK61 Pro

# Giữ toàn bộ các hiệu ứng (1..20), CHỈ BỎ [10, 11, 12, 13, 14] (TrigSingle, 3031, 3042, Tide, 3046)
MODES = [6, 1, 2, 3, 4, 5, 7, 8, 9, 15, 16, 17, 18, 19, 20]
# ==========================================

VK_RCONTROL = 0xA3  # Phím Ctrl bên phải để đổi hiệu ứng thủ công

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
    # Tắt Monochrome (byte 11 = 0), khóa cứng brightness = 4, speed = 4, val = 255
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

    mode_idx = 0
    hue = 0
    set_led(kb, MODES[mode_idx], hue, switch_mode=True)
    print(f"Dang chay tat ca hieu ung RGB theo nhac tu: {device['name']}")
    print("Bam [Right Ctrl] de doi ngay | Nhan Ctrl+C trong Terminal de dung.")
    print(f"-> Hieu ung hien tai: mode = {MODES[mode_idx]}")

    avg_energy = 100.0
    last_send_time = 0.0
    last_switch_time = time.time()
    hold_until = 0.0
    current_tier = 0

    while True:
        now = time.time()

        # 1. Bấm Right Ctrl để đổi hiệu ứng ngay lập tức
        if (user32.GetAsyncKeyState(VK_RCONTROL) & 0x0001) and (now - last_switch_time >= 0.25):
            mode_idx = (mode_idx + 1) % len(MODES)
            set_led(kb, MODES[mode_idx], hue, switch_mode=True)
            print(f"-> [Thu cong] Chuyen sang: mode = {MODES[mode_idx]}")
            last_switch_time = now
            last_send_time = now

        # 2. Tự động đổi hiệu ứng nhanh sau mỗi 4 giây
        elif AUTO_SWITCH_SECONDS > 0 and (now - last_switch_time >= AUTO_SWITCH_SECONDS):
            mode_idx = (mode_idx + 1) % len(MODES)
            set_led(kb, MODES[mode_idx], hue, switch_mode=True)
            print(f"-> [Tu dong {AUTO_SWITCH_SECONDS}s] Doi sang: mode = {MODES[mode_idx]}")
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
            switched = False

            if tier == 3:
                hue = (hue + random.randint(85, 140)) % 256
                if AUTO_SWITCH_ON_DROP and (now - last_switch_time >= DROP_SWITCH_COOLDOWN):
                    mode_idx = (mode_idx + 1) % len(MODES)
                    switched = True
                    last_switch_time = now
                    print(f"-> [Drop] Doi nhanh sang: mode = {MODES[mode_idx]}")
                set_led(kb, MODES[mode_idx], hue, switch_mode=switched)
                hold_until = now + 0.42
            elif tier == 2:
                hue = (hue + 38) % 256
                set_led(kb, MODES[mode_idx], hue)
                hold_until = now + 0.22
            else:
                hue = (hue + 20) % 256
                set_led(kb, MODES[mode_idx], hue)
                hold_until = now + 0.11
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