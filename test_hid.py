import hid
import time
import math
import struct
import random
import ctypes
import pyaudiowpatch as pyaudio

# ==========================================
# TÙY CHỈNH NHẠY NHẠC & DANH SÁCH HIỆU ỨNG:
SENSITIVITY = 1.22       # Độ nhạy bắt beat (càng nhỏ càng dễ bắt nhịp)
MIN_BEAT_GAP = 0.2    # Nhịp cập nhật theo beat (giây)
MODES = [1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
# ==========================================

VK_RCONTROL = 0xA3  # Bấm Right Ctrl để đổi sang hiệu ứng tiếp theo

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

def set_led(h, mode, hue, brightness=4, val=255, switch_mode=False):
    if switch_mode:
        send_cmd(h, [22, 0, 0, 0, 1, 0, mode])
    # Khóa cứng brightness <= 4 và speed = 4 để mạch TK61 Pro luôn sáng đều toàn phím
    send_cmd(h, [11, 11, 0, 0, 1, 0, mode, brightness, 4, 1, 1, 7, hue & 0xFF, 255, val & 0xFF])

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

    mode_idx = 0  # Bắt đầu từ mode = 1 (sáng đều toàn bàn phím, đổi màu tức thì theo beat)
    hue = 0
    set_led(kb, MODES[mode_idx], hue, brightness=3, val=210, switch_mode=True)

    print(f"Dang bat nhac tu: {device['name']}")
    print("Bam phim [Right Ctrl] de DOI HIEU UNG | Nhan Ctrl+C trong Terminal de dung.")
    print(f"-> Hieu ung hien tai: mode = {MODES[mode_idx]}")

    avg_energy = 100.0
    last_beat_time = 0.0
    last_switch_time = 0.0
    is_bright = False

    while True:
        now = time.time()

        # Kiểm tra phím Right Ctrl để đổi hiệu ứng ngay lập tức
        if (user32.GetAsyncKeyState(VK_RCONTROL) & 0x0001) and (now - last_switch_time >= 0.25):
            mode_idx = (mode_idx + 1) % len(MODES)
            set_led(kb, MODES[mode_idx], hue, brightness=4, val=255, switch_mode=True)
            print(f"-> Da chuyen sang hieu ung: mode = {MODES[mode_idx]}")
            last_switch_time = now

        data = stream.read(1024, exception_on_overflow=False)
        rms = calc_rms(data)
        avg_energy = avg_energy * 0.90 + rms * 0.10
        ratio = rms / max(130.0, avg_energy)

        if rms > 130.0 and ratio >= SENSITIVITY and (now - last_beat_time >= MIN_BEAT_GAP):
            if ratio > 1.45:
                hue = (hue + random.randint(95, 145)) % 256
            else:
                hue = (hue + 35) % 256
            set_led(kb, MODES[mode_idx], hue, brightness=4, val=255)
            last_beat_time = now
            is_bright = True
        elif is_bright and (now - last_beat_time >= MIN_BEAT_GAP) and rms < avg_energy * 0.9:
            hue = (hue + 18) % 256
            set_led(kb, MODES[mode_idx], hue, brightness=3, val=210)
            is_bright = False

except KeyboardInterrupt:
    set_led(kb, 6, 255, brightness=4, val=255, switch_mode=True)
    print("\nDa dung va khoi phuc den goc!")
finally:
    if 'stream' in locals():
        stream.stop_stream()
        stream.close()
    p.terminate()
    kb.close()