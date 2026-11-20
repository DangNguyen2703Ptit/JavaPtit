import hid

v = 0x36AE
p = 0xFEAD

for d in hid.enumerate(v, p):
    print(hex(d['usage_page']), hex(d['usage']), d['path'])
    if d['usage_page'] == 0xFF00 and d['usage'] == 0x0002:
        h = hid.device()
        h.open_path(d['path'])
        print("OK")
        h.close()