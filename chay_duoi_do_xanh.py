import ctypes
import math
import time
import hid

# Hai mã màu bạn yêu cầu:
RED = (0xFE, 0x04, 0x00)  # #fe0400
GREEN = (0x00, 0xFE, 0x2F)  # #00fe2f

MAX_KEY_INDEX = 126
VK_RCONTROL = 0xA3  # Phím Ctrl phải: Đổi kiểu chạy đuổi (3 kiểu)
VK_RMENU = 0xA5  # Phím Alt phải: Chỉnh tốc độ chạy (4 mức)


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


def enter_userlight_mode(h):
    # Chuyển bàn phím sang chế độ UserLight (mode 16) để nhận màu từng phím
    send_cmd(h, [22, 0, 0, 0, 1, 0, 16])
    send_cmd(h, [11, 11, 0, 0, 1, 0, 16, 4, 4, 1, 0, 7, 0, 255, 255])


def set_key_colors_infos(h, rgb_list):
    # Chuyển đổi chính xác từ hàm setKeyColorsInfos trong app.asar
    count = len(rgb_list)
    total_bytes = 3 * count
    num_packets = math.ceil(total_bytes / 56)

    v = [0] * 1024
    for i, (r, g, b) in enumerate(rgb_list):
        v[3 * i + 0] = r
        v[3 * i + 1] = g
        v[3 * i + 2] = b

    offset = 0
    for pkt_idx in range(num_packets):
        a = [0] * 63
        a[0] = 18
        a[1] = 59
        byte_offset = 56 * pkt_idx
        a[2] = byte_offset & 0xFF
        a[3] = (byte_offset >> 8) & 0xFF

        if pkt_idx == num_packets - 1 and (total_bytes % 56) > 0:
            a[1] = (total_bytes % 56) + 3

        chunk_len = min(56, total_bytes - offset)
        for j in range(chunk_len):
            a[j + 7] = v[offset + j]

        send_cmd(h, a)
        offset += 56


kb = open_kb()
if not kb:
    print('Khong tim thay ban phim! Hay tat han SDTech Options truoc.')
    exit()

user32 = ctypes.windll.user32
speeds = [0.35, 0.20, 0.10, 0.05]  # 4 mức tốc độ (giây)
speed_idx = 1
pattern_mode = 0

enter_userlight_mode(kb)
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
            print(f'-> Kieu chay: {pattern_mode + 1}/3')
            last_key_time = now

        # Đổi tốc độ bằng Right Alt
        if (user32.GetAsyncKeyState(VK_RMENU) & 0x0001) and (
            now - last_key_time >= 0.25
        ):
            speed_idx = (speed_idx + 1) % len(speeds)
            print(f'-> Toc do: muc {speed_idx + 1}/4')
            last_key_time = now

        # Tạo bảng màu cho 126 vị trí trên ma trận phím
        colors = []
        for idx in range(MAX_KEY_INDEX):
            if pattern_mode == 0:
                # Xen kẽ 1 phím Đỏ - 1 phím Xanh lá chạy đuổi liên tục
                is_green = (idx + step) % 2 == 0
            elif pattern_mode == 1:
                # Chạy đuổi theo cụm 3 phím Đỏ - 3 phím Xanh lá
                is_green = ((idx + step) // 3) % 2 == 0
            else:
                # Đảo màu nhấp nháy xen kẽ chẵn/lẻ giống bàn cờ trên hình
                is_green = (idx % 2) == (step % 2)

            colors.append(GREEN if is_green else RED)

        set_key_colors_infos(kb, colors)
        step += 1
        time.sleep(speeds[speed_idx])

except KeyboardInterrupt:
    # Khôi phục lại chế độ đèn mặc định khi bấm Ctrl+C
    send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
    send_cmd(kb, [11, 11, 0, 0, 1, 0, 6, 4, 4, 1, 0, 7, 255, 255, 255])
    print('\nDa dung va khoi phuc den goc!')
finally:
    kb.close()