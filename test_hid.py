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
        import hid

h = hid.device()
for d in hid.enumerate(0x36AE, 0xFEAD):
    if d['usage_page'] == 0xFF00 and d['usage'] == 0x0002:
        h.open_path(d['path'])
        for report_id in range(0, 16):
            try:
                res = h.get_feature_report(report_id, 65)
                if res:
                    print(f"ID {report_id}:", [hex(x) for x in res])
            except Exception:
                pass
        h.close()