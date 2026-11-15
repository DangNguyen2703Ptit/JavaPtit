/*DLab
DLab
Nguyễn Mậu Đăng
Làm bài lớp: 04 · J05010
Đề bài: J05010 — SẮP XẾP DANH SÁCH MẶT HÀNG
2s · 146.5 MB
Hãy sắp xếp danh sách các mặt hàng theo lợi nhuận giảm dần. Mỗi mặt hàng gồm các thông tin: Mã mặt hàng (là một số nguyên, tự động tăng, tính từ 1); Tên mặt hàng, nhóm hàng: là các xâu ký tự; Giá mua, giá bán: là các số thực (không quá 9 chữ số)

Dữ liệu vào

Dòng đầu chứa số mặt hàng. Mỗi mặt hàng viết trên 4 dòng: Dòng 1: Tên mặt hàng. Dòng 2: Nhóm hàng. Dòng 3: Giá mua. Dòng 4: Giá bán

Kết quả: Ghi ra danh sách mặt hàng đã sắp xếp theo lợi nhuận giảm dần (lợi nhuận tính bằng giá bán trừ đi giá mua). Mỗi mặt hàng viết trên một dòng gồm: mã, tên, nhóm hàng và lợi nhuận (với 2 chữ số sau dấu phẩy). Các thông tin cách nhau đúng 1 khoảng trống.

Ví dụ:


Input	Output
3


May tinh SONY VAIO


Dien tu


16400


17699


Tu lanh Side by Side


Dien lanh


18300


25999


Banh Chocopie


Tieu dung


27.5

37


	
2 Tu lanh Side by Side Dien lanh 7699.00


1 May tinh SONY VAIO Dien tu 1299.00


3 Banh Chocopie Tieu dung 9.50



 


Tải lên file mã nguồn hoặc .zip/.rar/.7z (project nhiều file) để nộp

Hỗ trợ .cpp, .py, .java… hoặc .zip / .rar / .7z (project).

Java
Lịch sử nộp của tôi — bài J05010
ID	Thời gian	Ngôn ngữ	Kết quả	Thời gian chạy	Bộ nhớ
Chưa có bài nộp
DLab */
import java.util.*;
public class Main {
    static String toD(String s) {
        String[] d = s.split("/");
        for (int i = 0; i <= 1; i++) {
            while (d[i].length() < 2)
                d[i] = "0" + d[i];
        }
        return d[0] + "/" + d[1] + "/" + d[2];
    }

    static String stdName(String s) {
        String[] n = s.toLowerCase().trim().split("\\s+");
        String r = "";
        for (int i = 0; i < n.length; i++) {
            r = r + Character.toString(n[i].charAt(0)).toUpperCase() + n[i].substring(1) + " ";
        }
        return r;
    }

    static class sv {
        String  name, date;
        int code;
        double ex;
        
        public sv(int i, String n, String d, double a, double b) {
            code = i;
            name = n;
            date = d;
            ex = b-a;
        }
        
        @Override
        public String toString() {
            return code + " " + name + ' ' + date + ' ' + String.format("%.2f", ex);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<sv> a = new ArrayList<>();
        int t = sc.nextInt();
        for (int i = 1; i <= t; i++) {
            sc.nextLine();
            a.add(new sv(i, sc.nextLine(), sc.nextLine(), sc.nextDouble(), sc.nextDouble()));
        }
        a.sort((sv x, sv y) -> {
            return (int)(y.ex - x.ex);
        });
        a.forEach(e -> {
            System.out.println(e);
        });
    }
}
