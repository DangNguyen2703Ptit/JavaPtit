import hid
import time

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

def set_mode(h, mode):
    send_cmd(h, [22, 0, 0, 0, 1, 0, mode])

def set_hsv(h, mode, hue, sat=255, val=255, brightness=4):
    # mode: thay bằng số hiệu ứng bạn thấy sáng đứng yên hoặc ưng ý nhất lúc quét (1..15)
    send_cmd(h, [11, 11, 0, 0, 1, 0, mode, brightness, 4, 1, 1, 7, hue & 0xFF, sat & 0xFF, val & 0xFF])

kb = open_kb()
if not kb:
    print("Khong tim thay ban phim!")
else:
    MODE = 3  # Đổi số này (1..15) theo hiệu ứng nền bạn thích nhất lúc nãy
    set_mode(kb, MODE)
    print(f"Dang chay cau vong tren mode = {MODE}... Nhan Ctrl+C de dung.")
    
    try:
        hue = 0
        while True:
            set_hsv(kb, MODE, hue)
            hue = (hue + 2) % 256  # Tăng số 2 nếu muốn đổi màu nhanh hơn
            time.sleep(0.025)
    except KeyboardInterrupt:
        # Khôi phục lại cấu hình gốc của bàn phím khi dừng
        set_mode(kb, 6)
        send_cmd(kb, [11, 11, 0, 0, 1, 0, 6, 4, 4, 1, 1, 7, 255, 255, 60])
        print("Da khoi phuc hieu ung goc!")
    finally:
        kb.close()