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
    # 1. Khoi phuc ngay cau hinh goc de bat sang lai den
    send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
    send_cmd(kb, [11, 11, 0, 0, 1, 0, 6, 4, 4, 1, 1, 7, 255, 255, 60])
    print("Da bat lai den goc! Dang quet thu cac che do tu 1 den 15...")

    try:
        # 2. Quet tung mode de xem so may la che do sang tinh doi mau
        for mode in range(1, 16):
            print(f"Dang thu mode = {mode} (Quan sat den tren ban phim)...")
            send_cmd(kb, [22, 0, 0, 0, 1, 0, mode])
            for hue in [0, 85, 170]:  # Đỏ -> Xanh lá -> Xanh dương
                send_cmd(kb, [11, 11, 0, 0, 1, 0, mode, 4, 4, 1, 1, 7, hue, 255, 255])
                time.sleep(0.4)
    except KeyboardInterrupt:
        pass
    finally:
        # Tra lai hieu ung goc sau khi quet xong
        send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
        send_cmd(kb, [11, 11, 0, 0, 1, 0, 6, 4, 4, 1, 1, 7, 255, 255, 60])
        print("Hoan tat!")
        kb.close()