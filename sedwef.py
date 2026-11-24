import re

asar_path = (
    r'C:\Users\ADMIN\AppData\Local\Programs\SDTech Options\resources\app.asar'
)
with open(asar_path, 'rb') as f:
    data = f.read().decode('latin1')

# Tìm đoạn code gọi lệnh ngay cạnh lệnh đổi LED [11, 11] hoặc lệnh chứa mảng RGB
for m in re.finditer(r'11,\s*11|22,\s*0,\s*0', data):
    start = max(0, m.start() - 250)
    end = min(len(data), m.end() + 450)
    snippet = ''.join(c if 32 <= ord(c) < 127 else ' ' for c in data[start:end])
    print(snippet)
    print('=' * 50)