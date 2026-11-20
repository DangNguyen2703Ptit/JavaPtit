import hid
import json
import struct
import time

ASAR_PATH = r"C:\Users\ADMIN\AppData\Local\Programs\SDTech Options\resources\app.asar"

def load_tk61_keys():
    """Đọc sơ đồ tọa độ (x, y) và index phần cứng của từng phím từ app.asar"""
    try:
        with open(ASAR_PATH, "rb") as f:
            data = f.read()
        header_size = struct.unpack("<I", data[4:8])[0]
        json_len = struct.unpack("<I", data[12:16])[0]
        header = json.loads(data[16:16 + json_len].decode("utf-8"))
        base_offset = 8 + header_size

        def find_file(node, target):
            if "files" in node:
                for k, v in node["files"].items():
                    if k == target and "offset" in v:
                        return int(v["offset"]), int(v["size"])
                    res = find_file(v, target)
                    if res:
                        return res
            return None

        loc = find_file(header, "36ae_fead.json")
        if loc:
            off, size = loc
            layout = json.loads(data[base_offset + off : base_offset + off + size].decode("utf-8"))
            keys = layout.get("keys", layout if isinstance(layout, list) else [])
            return [k for k in keys if "index" in k and "x" in k and "y" in k]
    except Exception as e:
        print("Dung so do mac dinh 61 phim:", e)
    return []

def is_inside_heart(x, y, cx, cy, sx, sy):
    """Kiểm tra tọa độ (x, y) có nằm trong hình trái tim tâm (cx, cy) không"""
    nx = (x - cx) / sx
    ny = -(y - cy) / sy  # Đảo trục Y vì tọa độ màn hình hướng xuống dưới
    return (nx * nx + ny * ny - 1) ** 3 - (nx * nx) * (ny ** 3) <= 0

def open_kb():
    for d in hid.enumerate(0x36AE, 0xFEAD):
        if d["usage_page"] == 0xFF00 and d["usage"] == 0x0002:
            h = hid.device()
            h.open_path(d["path"])
            return h
    return None

def send_cmd(h, payload):
    pkt = [0, 6] + payload
    pkt += [0] * (65 - len(pkt))
    h.write(pkt)
    return h.read(64, timeout_ms=50)

def send_per_key_rgb(h, rgb_map, total_keys=126):
    """Gửi mảng màu R, G, B từng phím theo chuẩn lệnh 18 của SDTech"""
    raw = [0] * (total_keys * 3)
    for idx, (r, g, b) in rgb_map.items():
        if 0 <= idx < total_keys:
            raw[idx * 3 + 0] = r
            raw[idx * 3 + 1] = g
            raw[idx * 3 + 2] = b

    total_bytes = len(raw)
    chunks = (total_bytes + 55) // 56
    for c in range(chunks):
        offset = c * 56
        chunk_len = min(56, total_bytes - offset)
        pkt = [0] * 65
        pkt[0] = 0
        pkt[1] = 6
        pkt[2] = 18
        pkt[3] = (chunk_len + 3) if (c == chunks - 1 and total_bytes % 56 > 0) else 59
        pkt[4] = offset & 0xFF
        pkt[5] = (offset >> 8) & 0xFF
        for i in range(chunk_len):
            pkt[9 + i] = raw[offset + i]
        h.write(pkt)
        h.read(64, timeout_ms=50)

kb = open_kb()
if not kb:
    print("Khong tim thay ban phim!")
else:
    keys = load_tk61_keys()
    rgb_map = {}
    lit_names = []

    if keys:
        xs = [k["x"] + k.get("w", 1) / 2 for k in keys]
        ys = [k["y"] + k.get("h", 1) / 2 for k in keys]
        cx = (min(xs) + max(xs)) / 2
        cy = (min(ys) + max(ys)) / 2 + 0.15
        sx = (max(xs) - min(xs)) * 0.34
        sy = (max(ys) - min(ys)) * 0.42

        max_idx = max(k["index"] for k in keys) + 1
        for k in keys:
            kx = k["x"] + k.get("w", 1) / 2
            ky = k["y"] + k.get("h", 1) / 2
            if is_inside_heart(kx, ky, cx, cy, sx, sy):
                rgb_map[k["index"]] = (255, 0, 60)  # Đỏ hồng trái tim
                lit_names.append(k.get("name", str(k["index"])))
            else:
                rgb_map[k["index"]] = (0, 0, 0)     # Tắt đèn phím ngoài trái tim
    else:
        # Dự phòng nếu không đọc được file layout: tô vùng phím giữa (R,T,Y,U,D,F,G,H,J,C,V,B,N)
        max_idx = 126
        heart_indices = {18, 19, 20, 21, 32, 33, 34, 35, 36, 46, 47, 48, 49}
        for idx in range(max_idx):
            rgb_map[idx] = (255, 0, 60) if idx in heart_indices else (0, 0, 0)

    # 1. Chuyển bàn phím sang chế độ Custom Light (mode = 0)
    send_cmd(kb, [22, 0, 0, 0, 1, 0, 0])
    send_cmd(kb, [11, 11, 0, 0, 1, 0, 0, 4, 4, 1, 0, 0, 0, 255, 255])

    # 2. Nạp ma trận LED hình trái tim
    send_per_key_rgb(kb, rgb_map, total_keys=max_idx)
    print("Da bat LED hinh trai tim! Cac phim sang:", ", ".join(lit_names))
    print("Nhan Ctrl + C trong Terminal neu muon tra ve den goc.")

    try:
        while True:
            time.sleep(0.5)
    except KeyboardInterrupt:
        send_cmd(kb, [22, 0, 0, 0, 1, 0, 6])
        send_cmd(kb, [11, 11, 0, 0, 1, 0, 6, 4, 4, 1, 1, 7, 255, 255, 60])
        print("Da khoi phuc den goc!")
    finally:
        kb.close()