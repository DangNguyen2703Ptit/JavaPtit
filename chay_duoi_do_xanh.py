import ctypes
import re
import time
import hid

# Mã màu bạn chọn: #fe0400 (Đỏ) và #00fe2f (Xanh lá)
RED = (254, 4, 0)
GREEN = (0, 254, 47)

BRIGHTNESS = 4
VK_RMENU = 0xA5  # Phím Alt phải: Chỉnh tốc độ chạy đuổi (Nhanh <-> Chậm)
VK_RCONTROL = 0xA3  # Phím Ctrl phải: Đổi kiểu chạy (Xen kẽ 1-1 <-> Dải 3-3 <-> Sóng hàng)


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


# Chuyển bàn phím sang chế độ UserLight (mode 16)
def enter_userlight(h):
    send_cmd(h, [22, 0, 0, 0, 1, 0, 16])
    send_cmd(h, [11, 11, 0, 0, 1, 0, 16, BRIGHTNESS, 4, 1, 0, 7, 0, 255, 255])


# Gửi mảng màu RGB cho 61 phím (183 bytes chia thành 4 gói tin chuẩn chip SDTech)
def send_custom_colors(h, colors):
    # Chuyển danh sách 61 tuple (R, G, B) thành mảng phẳng 183 bytes (đệm đủ 240 bytes cho 80 vị trí)
    rgb_bytes = []
    for r, g, b in colors:
        rgb_bytes.extend([r, g, b])
    rgb_bytes += [0] * (240 - len(rgb_bytes))

    # Gửi 4 gói dữ liệu màu từng phím (mỗi gói chứa 20 phím = 60 bytes RGB)
    # Header chuẩn của dòng chip 0x36AE: [13, pkt_index, 0, 0, 60, 0]
    for pkt_idx in range(4):
        chunk = rgb_bytes[pkt_idx * 60 : (pkt_idx + 1) * 60]
        send_cmd(h, [13, pkt_idx, 0, 0, 60, 0] + chunk)


kb = open_kb()
if not kb:
    print('Khong tim thay ban phim! Hay tat SDTech Options truoc.')
    exit()

user32 = ctypes.windll.user32
speeds = [0.40, 0.22, 0.12, 0.06]  # 4 mức tốc độ chạy (giây)
speed_idx = 1
pattern_mode = 0  # 0: Xen kẽ 1-1 | 1: Dải 3 phím | 2: Đảo màu toàn bàn phím

enter_userlight(kb)
print('Dang chay hieu ung #fe0400 & #00fe2f chay duoi!')
print(
    'Phim tat: [Right Ctrl] = Doi kieu chay | [Right Alt] = Doi toc do | Ctrl+C = Dung'
)

try:
    step = 0
    last_key_time = 0.0

    while True:
        now = time.time()

        # Đổi kiểu chạy bằng Right Ctrl
        if (user32.GetAsyncKeyState(VK_RCONTROL) & 0x0001) and (
            now - last_key_time >= 0.25
        ):
            pattern_mode = (pattern_mode + 1) % 3
            print(f'-> Da doi kieu chay: {pattern_mode + 1}/3')
            last_key_time = now

        # Đổi tốc độ bằng Right Alt
        if (user32.GetAsyncKeyState(VK_RMENU) & 0x0001) and (
            now - last_key_time >= 0.25
        ):
            speed_idx = (speed_idx + 1) % len(speeds)
            print(f'-> Toc do chay: muc {speed_idx + 1}/4')
            last_key_time = now

        # Tạo bảng màu 61 phím theo kiểu chạy đang chọn
        colors = []
        for i in range(61):
            if pattern_mode == 0:
                # Xen kẽ 1 Đỏ - 1 Xanh lá chạy đuổi giống ảnh mẫu
                is_green = (i + step) % 2 == 0
            elif pattern_mode == 1:
                # Chạy đuổi theo từng cụm 3 phím Đỏ - 3 phím Xanh lá
                is_green = ((i + step) // 3) % 2 == 0
            else:
                # Nhấp nháy đảo pha giữa phím chẵn và phím lẻ
                is_green = (i % 2) == (step % 2)

            colors.append(GREEN if is_green else RED)

        send_custom_colors(kb, colors)
        step += 1
        time.sleep(speeds[speed_idx])

except KeyboardInterrupt:
    # Khôi phục về hiệu ứng mặc định khi bấm Ctrl+C
    send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
    send_cmd(kb, [11, 11, 0, 0, 1, 0, 6, 4, 4, 1, 0, 7, 255, 255, 255])
    print('\nDa dung va khoi phuc den goc!')
finally:
    kb.close()