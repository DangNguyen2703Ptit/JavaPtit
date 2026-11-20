import hid
import time
import math
import struct
import random
import pyaudiowpatch as pyaudio

# ==========================================
# TÙY CHỈNH ĐỘ RỘNG & BIẾN ĐỔI BEAT:
SENSITIVITY = 1.12       # Hạ xuống 1.12 để vùng bắt beat rộng và nhạy hơn
BEAT_HOLD = 0.28         # Thời gian ngân sáng sau mỗi cú đập (giây) -> tăng nếu muốn đập rộng hơn
RAINBOW_DRIFT = 5        # Tốc độ trôi 7 sắc cầu vồng nền
MIN_CMD_INTERVAL = 0.09  # Nhịp gửi lệnh an toàn cho chip TK61 Pro
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

def set_led(h, hue, val=255, speed=4):
    # Giữ brightness = 4 (sáng tối đa), thay đổi speed (1..5) và hue theo độ mạnh của nhạc
    send_cmd(h, [11, 11, 0, 0, 1, 0, 6, 4, speed, 1, 1, 7, hue & 0xFF, 255, val & 0xFF])

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
    hue = 0
    set_led(kb, hue, val=255, speed=4)
    print(f"Dang bat beat rong & doi mau bien thien tu: {device['name']}")
    print("Nhan Ctrl+C de dung.")

    avg_energy = 100.0
    last_send_time = 0.0
    beat_until = 0.0
    beat_count = 0

    while True:
        data = stream.read(1024, exception_on_overflow=False)
        rms = calc_rms(data)

        # Cập nhật mức năng lượng trung bình mượt để nhận diện cả đoạn cao trào lẫn đoạn lắng
        avg_energy = avg_energy * 0.90 + rms * 0.10
        now = time.time()
        ratio = rms / max(120.0, avg_energy)

        # Kích hoạt vùng beat rộng khi âm lượng vượt ngưỡng
        if rms > 120.0 and ratio >= SENSITIVITY:
            beat_until = now + BEAT_HOLD
            if now - last_send_time >= MIN_CMD_INTERVAL:
                beat_count += 1
                # Cứ mỗi 4 nhịp beat hoặc khi gặp cú drop mạnh (ratio > 1.45) -> nhảy màu tương phản lớn
                if ratio > 1.45 or beat_count % 4 == 0:
                    hue = (hue + random.randint(75, 140)) % 256
                else:
                    hue = (hue + random.randint(25, 50)) % 256
                set_led(kb, hue, val=255, speed=5)
                last_send_time = now
                continue

        if now - last_send_time >= MIN_CMD_INTERVAL:
            if now < beat_until:
                # Đang trong vùng ngân của beat: giữ sáng rực 100% và trượt nhanh 7 sắc cầu vồng
                hue = (hue + RAINBOW_DRIFT * 3) % 256
                set_led(kb, hue, val=255, speed=5)
            else:
                # Ngoài vùng beat: trôi cầu vồng êm ở mức sáng rõ (val=190), tốc độ dịu lại (speed=2)
                hue = (hue + RAINBOW_DRIFT) % 256
                set_led(kb, hue, val=190, speed=2)
            last_send_time = now

except KeyboardInterrupt:
    send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
    set_led(kb, 255, val=255, speed=4)
    print("\nDa dung va khoi phuc den goc!")
finally:
    if 'stream' in locals():
        stream.stop_stream()
        stream.close()
    p.terminate()
    kb.close()