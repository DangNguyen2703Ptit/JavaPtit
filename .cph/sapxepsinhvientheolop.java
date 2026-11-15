
/*DLab
DLab
Nguyễn Mậu Đăng
Làm bài lớp: 04 · J05020
Đề bài: J05020 — SẮP XẾP SINH VIÊN THEO LỚP
2s · 146.5 MB
Thông tin về mỗi sinh viên gồm:

Mã sinh viên: dãy ký tự không có khoảng trống (không quá 15). Đảm bảo không trùng nhau.
Họ và tên: độ dài không quá 100
Lớp: dãy ký tự không có khoảng trống (không quá 15)
Email: dãy ký tự không có khoảng trống (không quá 15)
Hãy nhập danh sách sinh viên và sắp xếp theo lớp tăng dần (thứ tự từ điển)

Input

Dòng đầu ghi số sinh viên.

Mỗi sinh viên ghi trên 4 dòng lần lượt là: mã, họ tên, lớp, email.

Có không quá 1000 sinh viên trong danh sách.

Output

Ghi ra danh sách sinh viên đã sắp xếp theo lớp. Mỗi sinh viên trên một dòng, các thông tin cách nhau một khoảng trống.

Nếu 2 sinh viên có cùng lớp thì sắp xếp theo mã tăng dần (thứ tự từ điển)

Ví dụ


Input

Output

4


B16DCCN011


Nguyen Trong Duc Anh


D16CNPM1


sv1@stu.ptit.edu.vn


B15DCCN215


To Ngoc Hieu


D15CNPM3


sv2@stu.ptit.edu.vn


B15DCKT150


Nguyen Ngoc Son


D15CQKT02-B


sv3@stu.ptit.edu.vn


B15DCKT199


Nguyen Trong Tung


D15CQKT03-B


sv4@stu.ptit.edu.vn


	
B15DCCN215 To Ngoc Hieu D15CNPM3 sv2@stu.ptit.edu.vn


B15DCKT150 Nguyen Ngoc Son D15CQKT02-B sv3@stu.ptit.edu.vn


B15DCKT199 Nguyen Trong Tung D15CQKT03-B sv4@stu.ptit.edu.vn


B16DCCN011 Nguyen Trong Duc Anh D16CNPM1 sv1@stu.ptit.edu.vn



 

 


Tải lên file mã nguồn hoặc .zip/.rar/.7z (project nhiều file) để nộp

Hỗ trợ .cpp, .py, .java… hoặc .zip / .rar / .7z (project).

Java
Lịch sử nộp của tôi — bài J05020
ID	Thời gian	Ngôn ngữ	Kết quả	Thời gian chạy	Bộ nhớ
Chưa có bài nộp
DLab */
import java.util.*;
public class Main {
    static char up(char x) {
        if ('a' <= x && x <= 'z')
            return (char) ((int) x - 32);
        return x;
    }

    static String re(String name) {
        String[] a = name.split("[' ']+");
        String r = "";
        for (int i = 0; i < a.length; i++) {
            if (a[i].length() == 0)
                continue;
            String s = Character.toString(up(a[i].charAt(0))) + a[i].substring(1);
            r += s + " ";
        }
        return r;
    }

    static class student {
        String code, name, method, mail;

        public student(String c, String n, String m, String em) {
            code = c;
            name = n;
            method = m;
            mail = em;
        }

        public boolean big(student b) {
            if (code.compareTo(b.code) > 0)
                return true;
            return false;
        }

        public String toString() {
            return code + " " + name + ' ' + method + " " + mail;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<student> a = new ArrayList<>();
        sc.nextLine();
        for (int i = 0; i < n; i++) {
            a.add(new student(sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine()));
        }
        a.sort((student x, student y) ->{
            if(x.method.compareTo(y.method) == 0) return x.code.compareTo(y.code);
            return x.method.compareTo(y.method);
        });
        a.forEach(e->System.out.println(e));
    }
}