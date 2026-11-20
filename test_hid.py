import hid
import time
import math
import struct
import random
import pyaudiowpatch as pyaudio

# ==========================================
# TÙY CHỈNH ĐỘ NHẠY NHẠC:
SENSITIVITY = 1.35       # Giảm xuống 1.15 nếu nhạc nhẹ khó bắt nhịp; tăng lên 1.6 nếu nháy quá dày
MIN_CMD_INTERVAL = 0.09  # Giới hạn tốc độ gửi lệnh (giây) để mạch TK61 Pro không bị nghẽn
# ==========================================

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

def set_led(h, hue, brightness=4, val=255):
    send_cmd(h, [11, 11, 0, 0, 1, 0, 6, brightness, 4, 1, 1, 7, hue & 0xFF, 255, val & 0xFF])

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

    send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
    hue = random.randint(0, 255)
    set_led(kb, hue, brightness=2, val=120)
    print(f"Dang bat nhac tu: {device['name']}")
    print("Hay bat mot bai nhac tren may tinh... Nhan Ctrl+C de dung.")

    avg_energy = 100.0
    last_send_time = 0.0
    is_bright = False

    while True:
        data = stream.read(1024, exception_on_overflow=False)
        rms = calc_rms(data)

        avg_energy = avg_energy * 0.92 + rms * 0.08
        now = time.time()

        if now - last_send_time >= MIN_CMD_INTERVAL:
            if rms > max(150.0, avg_energy * SENSITIVITY):
                hue = (hue + random.randint(45, 115)) % 256
                set_led(kb, hue, brightness=4, val=255)
                last_send_time = now
                is_bright = True
            elif is_bright and rms < avg_energy * 0.85:
                set_led(kb, hue, brightness=1, val=90)
                last_send_time = now
                is_bright = False

except KeyboardInterrupt:
    send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
    set_led(kb, 255, brightness=4, val=60)
    print("\nDa dung va khoi phuc den goc!")
finally:
    if 'stream' in locals():
        stream.stop_stream()
        stream.close()
    p.terminate()
    kb.close()