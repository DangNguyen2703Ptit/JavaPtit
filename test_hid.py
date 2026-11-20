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

def set_hsv(h, mode, hue, sat, val):
    send_cmd(h, [11, 11, 0, 0, 1, 0, mode, 4, 4, 1, 1, 7, hue & 0xFF, sat & 0xFF, val & 0xFF])

kb = open_kb()
if not kb:
    print("Khong tim thay ban phim!")
else:
    MODE = 1  # Chế độ sáng tĩnh toàn bàn phím (nếu tối bạn đổi thành 6)
    send_cmd(kb, [22, 0, 0, 0, 1, 0, MODE])
    print("Dang chay hieu ung nhip tim... Nhan Ctrl+C de dung.")

    # Biên độ sáng mô phỏng 2 nhịp đập liên tiếp (thình - thịch) rồi nghỉ
    heartbeat_vals = [
        30, 80, 160, 255, 180, 90,   # Nhịp 1 (mạnh)
        130, 220, 140, 60, 30,       # Nhịp 2 (nhẹ)
        25, 25, 25, 25, 25, 25       # Quãng nghỉ
    ]

    try:
        while True:
            for v in heartbeat_vals:
                # hue = 245 (Đỏ hồng), sat = 255
                set_hsv(kb, MODE, 245, 255, v)
                time.sleep(0.055)
    except KeyboardInterrupt:
        send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
        set_hsv(kb, 6, 255, 255, 60)
        print("Da khoi phuc den goc!")
    finally:
        kb.close()