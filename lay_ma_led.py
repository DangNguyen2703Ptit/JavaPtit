import re

asar_path = (
    r'C:\Users\ADMIN\AppData\Local\Programs\SDTech Options\resources\app.asar'
)
with open(asar_path, 'rb') as f:
    data = f.read().decode('latin1')

for m in re.finditer(r'2027', data):
    start = max(0, m.start() - 150)
    end = min(len(data), m.end() + 250)
    snippet = ''.join(
        c if 32 <= ord(c) < 127 else ' ' for c in data[start:end]
    )
    print(snippet)
    print('-' * 40)