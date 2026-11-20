import hid
import time
import math
import struct
import random
import ctypes
import pyaudiowpatch as pyaudio

# ==========================================
# TÙY CHỈNH BẮT BEAT & TỐC ĐỘ THEO NHẠC:
BEAT_SENSITIVITY = 1.38   # Độ nhạy bắt trống (giảm xuống 1.25 nếu nhạc nhẹ; tăng 1.5 nếu bắt thừa)
MIN_CMD_INTERVAL = 0.065  # Tốc độ phản hồi cực nhanh (~15 lần/giây) và an toàn cho chip
MODES = [1, 6, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15]
# ==========================================

VK_RCONTROL = 0xA3  # Bấm Right Ctrl để đổi hiệu ứng

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
    # Luôn khóa cứng brightness = 4 và val = 255 để đèn sáng hết cỡ mọi lúc
    s = max(1, min(4, int(speed)))
    if switch_mode:
        send_cmd(h, [22, 0, 0, 0, 1, 0, mode])
    send_cmd(h, [11, 11, 0, 0, 1, 0, mode, 4, s, 1, 1, 7, hue & 0xFF, 255, 255])

def calc_bass_rms(raw_bytes, channels, lp_state):
    """Lọc thông thấp tách riêng tiếng trống Bass, loại bỏ tiếng hát & treble"""
    count = len(raw_bytes) // 2
    if count == 0:
        return 0.0, lp_state
    samples = struct.unpack(f"<{count}h", raw_bytes[:count * 2])
    
    alpha = 0.08
    sum_sq = 0.0
    step = max(1, channels)
    n = 0
    for i in range(0, count, step):
        lp_state += alpha * (samples[i] - lp_state)
        sum_sq += lp_state * lp_state
        n += 1
    return math.sqrt(sum_sq / max(1, n)), lp_state

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
        frames_per_buffer=512
    )

    mode_idx = 0
    hue = 0
    set_led(kb, MODES[mode_idx], hue, speed=2, switch_mode=True)
    print(f"Dang bat bass chuan (sang toi da 100%) tu: {device['name']}")
    print("Bam [Right Ctrl] de DOI HIEU UNG | Nhan Ctrl+C trong Terminal de dung.")

    bass_avg = 80.0
    prev_bass = 0.0
    lp_state = 0.0
    tempo_energy = 1.0
    last_send_time = 0.0
    last_beat_time = 0.0
    last_switch_time = 0.0

    while True:
        now = time.time()

        if (user32.GetAsyncKeyState(VK_RCONTROL) & 0x0001) and (now - last_switch_time >= 0.25):
            mode_idx = (mode_idx + 1) % len(MODES)
            set_led(kb, MODES[mode_idx], hue, speed=4, switch_mode=True)
            print(f"-> Da chuyen sang hieu ung: mode = {MODES[mode_idx]}")
            last_switch_time = now
            last_send_time = now

        data = stream.read(512, exception_on_overflow=False)
        bass, lp_state = calc_bass_rms(data, channels, lp_state)

        bass_avg = bass_avg * 0.85 + bass * 0.15
        is_rising = bass > prev_bass
        prev_bass = bass

        if is_rising and bass > 90.0 and bass > bass_avg * BEAT_SENSITIVITY and (now - last_beat_time >= 0.08):
            strength = min(2.0, bass / max(90.0, bass_avg))
            tempo_energy = min(4.0, tempo_energy + 0.85)
            hue = (hue + int(35 * strength) + random.randint(15, 45)) % 256
            last_beat_time = now
        else:
            tempo_energy = max(1.0, tempo_energy - 0.03)
            hue = (hue + int(tempo_energy * 1.5)) % 256

        if now - last_send_time >= MIN_CMD_INTERVAL:
            dyn_speed = int(round(tempo_energy))
            set_led(kb, MODES[mode_idx], hue, speed=dyn_speed)
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