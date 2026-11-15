/*DLab
DLab
Nguyễn Mậu Đăng
Làm bài lớp: 04 · J05007
Đề bài: J05007 — SẮP XẾP DANH SÁCH ĐỐI TƯỢNG NHÂN VIÊN
2s · 64.0 MB
Một nhân viên làm việc trong công ty được lưu lại các thông tin sau:

Mã nhân viên: được gán tự động tăng, bắt đầu từ 00001
Họ tên: Xâu ký tự không quá 40 chữ cái.
Giới tính: Nam hoặc Nu
Ngày sinh: đúng theo chuẩn dd/mm/yyyy
Địa chỉ: Xâu ký tự không quá 100 chữ cái
Mã số thuế: Dãy số có đúng 10 chữ số
Ngày ký hợp đồng: đúng theo chuẩn dd/mm/yyyy
Viết chương trình nhập danh sách nhân viên (không nhập mã) sau đó sắp xếp theo thứ tự ngày sinh từ già nhất đến trẻ nhất và in ra màn hình danh sách đối tượng nhân viên đã sắp xếp.

Input

Dòng đầu ghi số N là số nhân viên (không quá 40). Mỗi nhân viên ghi trên 6 dòng lần lượt ghi các thông tin theo thứ tự đã ghi trong đề bài. Không có mã nhân viên.

Output

Ghi ra danh sách đầy đủ nhân viên đã sắp xếp, mỗi nhân viên trên một dòng, các thông tin cách nhau đúng một khoảng trống.

Ví dụ


Input

3


Nguyen Van A


Nam


22/10/1982


Mo Lao-Ha Dong-Ha Noi


8333012345


31/12/2013


Ly Thi B


Nu


15/10/1988


Mo Lao-Ha Dong-Ha Noi


8333012346


22/08/2011


Hoang Thi C


Nu


04/02/1981


Mo Lao-Ha Dong-Ha Noi


8333012347


22/08/2011




Output



00003 Hoang Thi C Nu 04/02/1981 Mo Lao-Ha Dong-Ha Noi 8333012347 22/08/2011


00001 Nguyen Van A Nam 22/10/1982 Mo Lao-Ha Dong-Ha Noi 8333012345 31/12/2013


00002 Ly Thi B Nu 15/10/1988 Mo Lao-Ha Dong-Ha Noi 8333012346 22/08/2011




Tải lên file mã nguồn hoặc .zip/.rar/.7z (project nhiều file) để nộp

Hỗ trợ .cpp, .py, .java… hoặc .zip / .rar / .7z (project).

Java
Lịch sử nộp của tôi — bài J05007
ID	Thời gian	Ngôn ngữ	Kết quả	Thời gian chạy	Bộ nhớ
Chưa có bài nộp
DLab */
import java.util.*;
import java.text.*;
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
        String code, name, gender, address, date, tax, day;
        float gpa;
        
        public sv(int i, String n, String c, String d, String add, String t, String day) {
            code = String.format("%05d", i);
            name = stdName(n);
            gender = c;
            date = d;
            address = add;
            tax = t;
            this.day = day;
        }
        
        @Override
        public String toString() {
            return code + ' ' + name + gender + ' ' + date + ' ' + address + ' ' + tax + ' ' + day;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<sv> a = new ArrayList<>();
        int t = sc.nextInt();
        sc.nextLine();
        for (int i = 1; i <= t; i++) {
            a.add(new sv(i, sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine()));
        }
        SimpleDateFormat f = new SimpleDateFormat("dd/mm/yyyy");
        a.sort((sv x, sv y) -> {
            int c = 0;
            try{
                c = f.parse(x.date).compareTo(f.parse(y.date));
            }catch (ParseException e){

            }
            return c;
        });
        a.forEach(e -> System.out.println(e));
    }
}
