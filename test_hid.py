import hid
import time
import random
import ctypes

# ==========================================
# CẤU HÌNH CƠ BẢN:
BRIGHTNESS = 4   # Độ sáng: 1 đến 4
SPEED = 4        # Tốc độ hiệu ứng: 1 đến 5
# Danh sách các mã hiệu ứng có sẵn trên TK61 Pro (bạn có thể xóa bớt số nào thấy tắt đèn)
MODES = [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
# ==========================================

VK_RCONTROL = 0xA3  # Phím Ctrl bên phải dùng để đổi hiệu ứng

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

def apply_effect(h, mode, hue, switch_mode=False):
    # Chỉ gửi lệnh 22 khi đổi sang hiệu ứng mới để không làm giật nhịp đang chạy
    if switch_mode:
        send_cmd(h, [22, 0, 0, 0, 1, 0, mode])
    send_cmd(h, [11, 11, 0, 0, 1, 0, mode, BRIGHTNESS, SPEED, 1, 1, 7, hue & 0xFF, 255, 255])

def check_keys():
    user32 = ctypes.windll.user32
    switch_effect = bool(user32.GetAsyncKeyState(VK_RCONTROL) & 0x0001)
    any_key = False
    for vk in range(8, 255):
        if vk in (0x11, VK_RCONTROL):  # Bỏ qua phím Ctrl để không tính trùng
            continue
        if user32.GetAsyncKeyState(vk) & 0x0001:
            any_key = True
    return switch_effect, any_key

kb = open_kb()
if not kb:
    print("Khong tim thay ban phim! Hay tat SDTech Options truoc.")
else:
    mode_idx = MODES.index(6)  # Bắt đầu từ hiệu ứng Bloom (6)
    hue = random.randint(0, 255)
    apply_effect(kb, MODES[mode_idx], hue, switch_mode=True)

    print("Dang chay! Go phim bat ky de DOI MAU | Bam [Right Ctrl] de DOI HIEU UNG.")
    print(f"-> Hieu ung hien tai: mode = {MODES[mode_idx]}")

    try:
        while True:
            switch_effect, any_key = check_keys()
            if switch_effect:
                mode_idx = (mode_idx + 1) % len(MODES)
                current_mode = MODES[mode_idx]
                apply_effect(kb, current_mode, hue, switch_mode=True)
                print(f"-> Da chuyen sang hieu ung: mode = {current_mode}")
                time.sleep(0.2)
            elif any_key:
                hue = (hue + random.randint(55, 130)) % 256
                apply_effect(kb, MODES[mode_idx], hue, switch_mode=False)
                time.sleep(0.1)
            time.sleep(0.01)
    except KeyboardInterrupt:
        apply_effect(kb, 6, 255, switch_mode=True)
        print("Da khoi phuc den goc!")
    finally:
        kb.close()