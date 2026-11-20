import hid
import time
import math
import struct
import random
import pyaudiowpatch as pyaudio

# ==========================================
# TÙY CHỈNH NGƯỠNG BEAT NHỎ / VỪA / TO:
SMALL_BEAT = 1.15        # Ngưỡng bắt beat nhỏ (vùng sáng hẹp ở tâm)
MID_BEAT = 1.35          # Ngưỡng bắt beat vừa (vùng sáng nửa phím)
BIG_BEAT = 1.55          # Ngưỡng bắt beat mạnh / drop (bung sáng kín 61 phím)
MIN_CMD_INTERVAL = 0.085 # Nhịp gửi lệnh an toàn cho chip TK61 Pro
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

def set_bloom(h, hue, brightness=4, speed=4, val=255):
    # Khóa an toàn brightness (1..4) và speed (1..4) cho mạch TK61 Pro
    b = max(1, min(4, brightness))
    s = max(1, min(4, speed))
    send_cmd(h, [11, 11, 0, 0, 1, 0, 6, b, s, 1, 1, 7, hue & 0xFF, 255, val & 0xFF])

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
    set_bloom(kb, hue, brightness=1, speed=1, val=70)
    print(f"Dang bat nhac (vung sang nho/to theo beat) tu: {device['name']}")
    print("Nhan Ctrl+C de dung.")

    avg_energy = 100.0
    last_send_time = 0.0
    hold_until = 0.0
    current_tier = 0  # 0: nghi, 1: beat nho, 2: beat vua, 3: beat to

    while True:
        data = stream.read(1024, exception_on_overflow=False)
        rms = calc_rms(data)

        avg_energy = avg_energy * 0.90 + rms * 0.10
        now = time.time()
        ratio = rms / max(125.0, avg_energy)

        # Phân loại độ mạnh của beat theo 3 cấp độ vùng sáng
        tier = 0
        if rms > 125.0:
            if ratio >= BIG_BEAT:
                tier = 3
            elif ratio >= MID_BEAT:
                tier = 2
            elif ratio >= SMALL_BEAT:
                tier = 1

        # Kích hoạt khi có beat mới (hoặc beat hiện tại mạnh hơn beat đang ngân)
        if tier > 0 and (now >= hold_until or tier > current_tier) and (now - last_send_time >= MIN_CMD_INTERVAL):
            current_tier = tier
            if tier == 3:
                # Beat to: bung tốc độ 4, sáng tối đa, giữ 0.42s để sóng lan hết bàn phím
                hue = (hue + random.randint(85, 140)) % 256
                set_bloom(kb, hue, brightness=4, speed=4, val=255)
                hold_until = now + 0.42
            elif tier == 2:
                # Beat vừa: lan tốc độ 3, giữ 0.22s để sáng khoảng nửa phím
                hue = (hue + 38) % 256
                set_bloom(kb, hue, brightness=3, speed=3, val=210)
                hold_until = now + 0.22
            else:
                # Beat nhỏ: lan chậm tốc độ 1, chỉ giữ 0.11s rồi thu lại tạo quầng sáng nhỏ
                hue = (hue + 20) % 256
                set_bloom(kb, hue, brightness=2, speed=1, val=150)
                hold_until = now + 0.11
            last_send_time = now

        # Hết thời gian ngân của beat -> thu gọn về nền dịu để tách bạch rõ nhịp sau
        elif current_tier > 0 and now >= hold_until and (now - last_send_time >= MIN_CMD_INTERVAL):
            hue = (hue + 8) % 256
            set_bloom(kb, hue, brightness=1, speed=1, val=75)
            current_tier = 0
            last_send_time = now

except KeyboardInterrupt:
    send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
    set_bloom(kb, 255, brightness=4, speed=4, val=255)
    print("\nDa dung va khoi phuc den goc!")
finally:
    if 'stream' in locals():
        stream.stop_stream()
        stream.close()
    p.terminate()
    kb.close()