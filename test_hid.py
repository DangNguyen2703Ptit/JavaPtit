import hid
import time
import random
import ctypes

# ==========================================
# TÙY CHỈNH NHANH:
BRIGHTNESS = 4   # Độ sáng: 1 đến 4
SPEED = 4        # Tốc độ nhịp Bloom: 1 (chậm) đến 5 (nhanh)
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

def set_bloom_color(h, hue):
    send_cmd(h, [11, 11, 0, 0, 1, 0, 6, BRIGHTNESS, SPEED, 1, 1, 7, hue & 0xFF, 255, 255])

def any_key_pressed():
    user32 = ctypes.windll.user32
    for vk in range(8, 255):
        if user32.GetAsyncKeyState(vk) & 0x0001:
            return True
    return False

kb = open_kb()
if not kb:
    print("Khong tim thay ban phim! Hay tat SDTech Options truoc.")
else:
    hue = random.randint(0, 255)
    send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
    set_bloom_color(kb, hue)
    print("Dang chay Bloom + doi mau khi bam phim... Nhan Ctrl+C de dung.")

    try:
        while True:
            if any_key_pressed():
                # Nhảy góc màu từ 55 đến 130 để phím sau luôn khác hẳn màu phím trước
                hue = (hue + random.randint(55, 130)) % 256
                set_bloom_color(kb, hue)
                time.sleep(0.1)
            time.sleep(0.01)
    except KeyboardInterrupt:
        send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
        send_cmd(kb, [11, 11, 0, 0, 1, 0, 6, 4, 4, 1, 1, 7, 255, 255, 60])
        print("Da khoi phuc den goc!")
    finally:
        kb.close()