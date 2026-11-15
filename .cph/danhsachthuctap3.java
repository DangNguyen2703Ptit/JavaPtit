/*DLab
DLab
Nguyễn Mậu Đăng
Làm bài lớp: 04 · J07038
Đề bài: J07038 — DANH SÁCH THỰC TẬP - 3
2s · 146.5 MB
Để chuẩn bị cho đợt thực tập tốt nghiệp của sinh viên năm cuối, Khoa CNTT1 trao đổi với các doanh nghiệp đối tác và chốt số lượng sinh viên có thể nhận thực tập.

Mỗi doanh nghiệp được mô tả gồm ba thông tin: mã doanh nghiệp, tên doanh nghiệp, số sinh viên có thể nhận. 

Thông tin về mỗi sinh viên gồm:

Mã sinh viên: dãy ký tự không có khoảng trống (không quá 15). Đảm bảo không trùng nhau.
Họ và tên: độ dài không quá 100, có thể chưa chuẩn
Lớp: dãy ký tự không có khoảng trống (không quá 15)
Email: dãy ký tự không có khoảng trống (không quá 30)
Dữ liệu đăng ký thực tập sẽ được nhập chỉ với hai thông tin: mã sinh viên, mã doanh nghiệp.

Hãy viết chương trình nhập danh sách thực tập và hiển thị danh sách thực tập theo doanh nghiệp.

Chú ý: các doanh nghiệp sẽ ưu tiên lấy sinh viên theo thứ tự mã sinh viên (thứ tự từ điển). Giả sử số sinh viên tối đa có thể nhận là N thì từ sinh viên thứ N+1 trở đi theo danh sách đã sắp xếp theo mã sinh viên thì sẽ không được nhận.

Input – 3 file văn bản

SINHVIEN.in

Dòng đầu ghi số sinh viên (không quá 1000)

Mỗi sinh viên ghi trên 4 dòng lần lượt là: mã, họ tên, lớp, email.

DN.in

Dòng đầu ghi số doanh nghiệp.

Mỗi doanh nghiệp ghi trên 3 dòng: Mã doanh nghiệp, Tên doanh nghiệp, Số sinh viên có thể nhận.

THUCTAP.in

Dòng đầu ghi số N là số lượng đăng ký

Tiếp theo là N dòng, mỗi dòng có hai thông tin là mã sinh viên và mã doanh nghiệp.

Tiếp theo là một dòng ghi số M là số doanh nghiệp cần liệt kê danh sách.

Sau đó là M dòng mỗi dòng ghi 1 mã doanh nghiệp

Output

Ghi ra danh sách thực tập của từng doanh nghiệp theo mẫu trong ví dụ. Mỗi sinh viên cần liệt kê mã, tên, lớp, danh sách được sắp xếp theo mã sinh viên tăng dần.

Ví dụ


Input – 3 file văn bản

Output

SINHVIEN.in


2


B15DCKT150


NGUYEN NGOC SON


D15CQKT02-B


sv3@stu.ptit.edu.vn


B15DCKT199


NguyeN   TrONg Tung


D15CQKT02-B


sv4@stu.ptit.edu.vn


 


DN.in


4


VIETTEL


TAP DOAN VIEN THONG QUAN DOI VIETTEL


40


FSOFT


CONG TY TNHH PHAN MEM FPT - FPT SOFTWARE


300


VNPT


TAP DOAN BUU CHINH VIEN THONG VIET NAM


200


SUN


SUN*


50


 


THUCTAP.in


2


B15DCKT150 VIETTEL


B15DCKT199 SUN


1


SUN



DANH SACH THUC TAP TAI SUN*:


B15DCKT199 Nguyen Trong Tung D15CQKT02-B



 


Tải lên file mã nguồn hoặc .zip/.rar/.7z (project nhiều file) để nộp

Hỗ trợ .cpp, .py, .java… hoặc .zip / .rar / .7z (project).

Java
Lịch sử nộp của tôi — bài J07038
ID	Thời gian	Ngôn ngữ	Kết quả	Thời gian chạy	Bộ nhớ
Chưa có bài nộp
DLab */
import java.io.*;
import java.util.*;

public class Main {

    static String cleanName(String s) {
        String[] a = s.trim().split("\\s+");
        for (int i = 0; i < a.length; i++)
            a[i] = Character.toString(a[i].charAt(0)).toUpperCase() + a[i].substring(1).toLowerCase();
        return String.join(" ", a);
    }

    static class sv {
        String code, name, cl, mail;

        public sv(String code, String name, String cl, String mail) {
            this.code = code;
            this.name = cleanName(name);
            this.cl = cl;
            this.mail = mail;
        }

        public String toString() {
            return code + " " + name + " " + cl;
        }
    }
    
    static class dn {
        ArrayList<sv> a;
        String code, name;
        int max;
        
        public dn(String code, String name, int max) {
            a = new ArrayList<>();
            this.code = code;
            this.name = name;
            this.max = max;
        }

        @Override
        public String toString() {
            String s = String.format("DANH SACH THUC TAP TAI %s:\n", name);
            String[] arr = new String[Math.min(a.size(), max)];
            for (int i = 0; i < arr.length; i++)
                arr[i] = a.get(i).toString();
            return s + String.join("\n", arr);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sv_sc = new Scanner(new File("SINHVIEN.in"));
        Scanner dn_sc = new Scanner(new File("DN.in"));
        Scanner tt_sc = new Scanner(new File("THUCTAP.in"));
        HashMap<String, sv> hsv = new HashMap<>();
        HashMap<String, dn> hdn = new HashMap<>();
        int nsv = sv_sc.nextInt();
        sv_sc.nextLine();
        for (int i = 0; i < nsv; i++) {
            sv x = new sv(sv_sc.nextLine(), sv_sc.nextLine(), sv_sc.nextLine(), sv_sc.nextLine());
            hsv.put(x.code, x);
        }
        sv_sc.close();
        int ndn = dn_sc.nextInt();
        for (int i = 0; i < ndn; i++) {
            dn_sc.nextLine();
            dn x = new dn(dn_sc.nextLine(), dn_sc.nextLine(), dn_sc.nextInt());
            hdn.put(x.code, x);
        }
        dn_sc.close();
        int ntt = tt_sc.nextInt();
        for (int i = 0; i < ntt; i++) {
            String sv_code = tt_sc.next();
            String dn_code = tt_sc.next();
            dn x = hdn.get(dn_code);
                x.a.add(hsv.get(sv_code));
        }
        hdn.forEach((k, v) -> {
            v.a.sort((sv x, sv y) -> x.code.compareTo(y.code));
        });
        int q = tt_sc.nextInt();
        while (q-- > 0) {
            String code = tt_sc.next();
            System.out.println(hdn.get(code));
        }
    }
}
