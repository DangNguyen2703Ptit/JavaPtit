import time
import hid

VID = 0x36AE
PID = 0xFEAD

# Hai màu chủ đạo Đỏ và Xanh lá giống trên giao diện SDTech Options
RED = (255, 0, 0)
GREEN = (0, 255, 0)

# Sơ đồ 61 phím (theo thứ tự từng hàng từ trái sang phải)
TOTAL_KEYS = 61


def find_device():
    for d in hid.enumerate(VID, PID):
        if d['usage_page'] >= 0xFF00:
            dev = hid.device()
            dev.open_path(d['path'])
            return dev
    return None


dev = find_device()
if not dev:
    print(
        'Không tìm thấy bàn phím! Hãy chắc chắn bạn đã tắt hoàn toàn SDTech Options.'
    )
    exit()

print('Đang chạy hiệu ứng Đỏ - Xanh xen kẽ chạy đuổi... Nhấn Ctrl+C để dừng.')

try:
    step = 0
    while True:
        # Tạo danh sách 61 màu xen kẽ Đỏ - Xanh lá, dịch chuyển theo biến 'step'
        colors = []
        for i in range(TOTAL_KEYS):
            if (i + step) % 2 == 0:
                colors.append(RED)
            else:
                colors.append(GREEN)

        # Gửi dữ liệu màu 61 phím (183 bytes) chia làm 3 gói tin HID 64 bytes
        # Lưu ý: Nếu gói tin chưa khớp firmware hiện tại, hãy đối chiếu header từ file app.asar
        raw_rgb = []
        for r, g, b in colors:
            raw_rgb.extend([r, g, b])

        # Chia thành 3 gói (mỗi gói tối đa 60 bytes dữ liệu RGB + 4 bytes header)
        for pkt_idx in range(3):
            chunk = raw_rgb[pkt_idx * 60 : (pkt_idx + 1) * 60]
            # Header chuẩn cho chế độ UserLight của chip SDTech (Report ID 0x00, Cmd 0x08)
            packet = [0x00, 0x08, 0x02, pkt_idx, len(chunk), 0x00] + chunk
            packet += [0x00] * (65 - len(packet))
            dev.write(packet[:65])

        step += 1
        time.sleep(0.35)  # Tốc độ chạy (0.35 giây đảo 1 lần - có thể giảm xuống 0.15 để chạy nhanh hơn)

except KeyboardInterrupt:
    print('\nĐã dừng hiệu ứng.')
finally:
    dev.close()