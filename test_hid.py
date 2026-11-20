import hid
import time
import random
import ctypes

# ==========================================
# CHỌN 1 TRONG 2 CHẾ ĐỘ:
# "typing"  : Gõ phím nào đổi màu ngay phím đó (không spam chip, cực mượt)
# "rainbow" : Tự động chuyển màu cầu vồng nhịp nhàng
# ==========================================
MODE_TYPE = "typing"

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

def set_color(h, hue, mode=6):
    send_cmd(h, [11, 11, 0, 0, 1, 0, mode, 4, 4, 1, 1, 7, hue & 0xFF, 255, 255])

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
    send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
    set_color(kb, 0)
    print(f"Dang chay [{MODE_TYPE}]... Nhan Ctrl+C de dung.")

    try:
        hue = 0
        if MODE_TYPE == "typing":
            while True:
                if any_key_pressed():
                    hue = (hue + random.randint(40, 100)) % 256
                    set_color(kb, hue)
                    time.sleep(0.12)  # Nghỉ nhẹ để chip xử lý kịp
                time.sleep(0.01)
        else:
            while True:
                set_color(kb, hue)
                hue = (hue + 15) % 256
                time.sleep(0.25)      # Nhịp 0.25s giúp chip không bị nghẽn
    except KeyboardInterrupt:
        send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
        send_cmd(kb, [11, 11, 0, 0, 1, 0, 6, 4, 4, 1, 1, 7, 255, 255, 60])
        print("Da khoi phuc den goc!")
    finally:
        kb.close()