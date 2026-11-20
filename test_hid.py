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

kb = open_kb()
if not kb:
    print("Khong tim thay ban phim!")
else:
    print("Dang chay cau vong... Nhan Ctrl+C de dung.")
    try:
        hue = 0
        while True:
            # Đổi mode = 2 (Sáng tĩnh) và singleColorIndex = 0 (Dùng màu HSV tùy chỉnh)
            data = [11, 11, 0, 0, 1, 0, 2, 4, 4, 1, 1, 0, hue, 255, 255]
            send_cmd(kb, data)
            hue = (hue + 4) % 256
            time.sleep(0.03)
    except KeyboardInterrupt:
        # Khi bấm Ctrl+C, tự động trả về hiệu ứng gốc (mode = 6) của bàn phím
        send_cmd(kb, [11, 11, 0, 0, 1, 0, 6, 4, 4, 1, 0, 7, 255, 255, 255])
        print("Da dung va khoi phuc den goc!")
    finally:
        kb.close()