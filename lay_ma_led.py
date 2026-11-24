import re
import hid

# 1. Liệt kê tất cả cổng HID của bàn phím để tìm đúng usage_page
print('=== DANH SÁCH CỔNG HID ===')
for d in hid.enumerate(0x36AE, 0xFEAD):
    print(
        f"Interface: {d['interface_number']} | UsagePage: {hex(d['usage_page'])} | Usage: {hex(d['usage'])}"
    )

# 2. Đọc chuỗi lệnh gửi màu từ file app.asar của SDTech Options
asar_path = (
    r'C:\Users\ADMIN\AppData\Local\Programs\SDTech Options\resources\app.asar'
)
with open(asar_path, 'rb') as f:
    data = f.read().decode('latin1')

print('\n=== MÃ LỆNH USERLIGHT TRONG APP.ASAR ===')
for match in re.finditer(r'UserLight|sendFeatureReport|write\(', data):
    start = max(0, match.start() - 120)
    end = min(len(data), match.end() + 200)
    snippet = ''.join(
        c if 32 <= ord(c) < 127 else ' ' for c in data[start:end]
    )
    print(snippet)
    print('-' * 40)