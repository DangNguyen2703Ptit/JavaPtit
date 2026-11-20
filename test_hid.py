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
            # type=1, mode=1 (Static), brightness=4, speed=4, dir=1, color=1 (Monochrome), singleColorIndex=7 (HSV)
            data = [11, 11, 0, 0, 1, 0, 1, 4, 4, 1, 1, 7, hue, 255, 255]
            send_cmd(kb, data)
            hue = (hue + 4) % 256
            time.sleep(0.03)
    except KeyboardInterrupt:
        print("Da dung!")
    finally:
        kb.close()