import hid
import time
import random
import ctypes

# ==========================================
# CHỌN HIỆU ỨNG Ở DÒNG DƯỚI ĐÂY:
# "cyberpunk" : Tự động chuyển mượt dải màu Neon Tím - Hồng - Xanh Cyan
# "typing"    : Đổi màu rực rỡ mỗi lần bạn bấm phím
# "fire"      : Ánh lửa Đỏ - Cam - Vàng bập bùng
# "police"    : Chớp đôi Đỏ - Xanh dương kiểu đèn cảnh sát
# ==========================================
EFFECT = "cyberpunk"

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

def set_hsv(h, mode, hue, sat=255, val=255, brightness=4):
    # brightness tối đa của TK61 Pro là 4
    send_cmd(h, [11, 11, 0, 0, 1, 0, mode, brightness, 4, 1, 1, 7, hue & 0xFF, sat & 0xFF, val & 0xFF])

def any_key_pressed():
    user32 = ctypes.windll.user32
    for vk in range(8, 255):
        if user32.GetAsyncKeyState(vk) & 0x0001:
            return True
    return False

kb = open_kb()
if not kb:
    print("Khong tim thay ban phim!")
else:
    MODE = 6
    send_cmd(kb, [22, 0, 0, 0, 1, 0, MODE])
    set_hsv(kb, MODE, 140, 255, 255, brightness=4)
    print(f"Dang chay hieu ung [{EFFECT}]... Nhan Ctrl+C de dung.")

    try:
        if EFFECT == "cyberpunk":
            hue, step = 135, 2
            while True:
                set_hsv(kb, MODE, hue, 255, 255, brightness=4)
                hue += step
                if hue >= 240 or hue <= 135:
                    step = -step
                time.sleep(0.03)

        elif EFFECT == "typing":
            hue = 0
            while True:
                if any_key_pressed():
                    hue = (hue + random.randint(35, 95)) % 256
                    set_hsv(kb, MODE, hue, 255, 255, brightness=4)
                time.sleep(0.015)

        elif EFFECT == "fire":
            while True:
                hue = random.randint(0, 32)
                val = random.randint(190, 255)
                set_hsv(kb, MODE, hue, 255, val, brightness=4)
                time.sleep(random.uniform(0.05, 0.12))

        elif EFFECT == "police":
            while True:
                for _ in range(2):
                    set_hsv(kb, MODE, 0, 255, 255, brightness=4)
                    time.sleep(0.07)
                    set_hsv(kb, MODE, 0, 255, 40, brightness=4)
                    time.sleep(0.05)
                for _ in range(2):
                    set_hsv(kb, MODE, 170, 255, 255, brightness=4)
                    time.sleep(0.07)
                    set_hsv(kb, MODE, 170, 255, 40, brightness=4)
                    time.sleep(0.05)

    except KeyboardInterrupt:
        send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
        set_hsv(kb, 6, 255, 255, 255, brightness=4)
        print("Da dung va giu sang den goc!")
    finally:
        kb.close()