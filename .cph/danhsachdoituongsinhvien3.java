/*Viết chương trình khai báo lớp Sinh Viên gồm các thông tin: Mã SV, Họ tên, Ngày sinh, Lớp và Điểm GPA (dạng số thực). Hàm khởi tạo không có tham số, gán các giá trị thuộc tính ở trạng thái mặc định (xâu ký tự rỗng, giá trị số bằng 0).

Đọc thông tin N thí sinh từ bàn phím (không có mã sinh viên) sau đó sắp xếp theo điểm GPA giảm dần và in ra lần lượt màn hình mỗi dòng 1 sinh viên.

Trong đó Mã SV được tự tạo ra theo quy tắc thêm mã B20DCCN sau đó là giá trị nguyên tự động tăng tính từ 001 (tối đa là 099). Họ tên được xử lý đưa về dạng chuẩn. Ngày sinh được chuẩn hóa về dạng dd/mm/yyyy

Input

Dòng đầu tiên ghi số sinh viên N (0 < N < 50).

Mỗi sinh viên ghi trên 4 dòng lần lượt là Họ tên, Lớp, Ngày sinh và Điểm GPA.

Trong đó:

Họ tên không quá 30 chữ cái.
Lớp theo đúng định dạng thường dùng ở PTIT
Ngày sinh có đủ 3 phần ngày tháng năm nhưng có thể chưa đúng chuẩn dd/mm/yyyy.
Điểm GPA đảm bảo trong thang điểm 4 với 2 nhiều nhất 2 số sau dấu phẩy. 
Dữ liệu đảm bảo không có hai sinh viên nào có điểm GPA bằng nhau.

Output

Ghi ra danh sách lần lượt các sinh viên có đầy đủ Mã sinh viên, Họ tên, Lớp, Ngày sinh (đã chuẩn hóa), điểm GPA (với đúng 2 số sau dấu phẩy) đã được sắp xếp theo điểm GPA giảm dần.

Mỗi sinh viên ghi trên 1 dòng, mỗi thông tin cách nhau 1 khoảng trống.

Ví dụ


Input

Output

2


ngUYen Van NaM


D20DCCN01-B


2/12/1994


2.17


Nguyen QuanG hAi


D20DCCN02-B


1/9/1994


3.0


	
B20DCCN002 Nguyen Quang Hai D20DCCN02-B 01/09/1994 3.00


B20DCCN001 Nguyen Van Nam D20DCCN01-B 02/12/1994 2.17

 */
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
        String code, name, cl, date;
        float gpa;
        
        public sv(int i, String n, String c, String d, Float s) {
            code = "B20DCCN" + String.format("%03d", i);
            name = stdName(n);
            cl = c;
            date = toD(d);
            gpa = s;
        }
        
        @Override
        public String toString() {
            return code + ' ' + name + cl + ' ' + date + ' ' + String.format("%.2f", gpa);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<sv> a = new ArrayList<>();
        int t = sc.nextInt();
        for (int i = 1; i <= t; i++) {
            sc.nextLine();
            a.add(new sv(i, sc.nextLine(), sc.next(), sc.next(), sc.nextFloat()));
        }
        Collections.sort(a, new Comparator<sv>() {
            @Override
            public int compare(sv x, sv y){
                if (y.gpa > x.gpa)
                    return 1;
                if (y.gpa < x.gpa)
                    return -1;
                return 0;
            }
        });
        a.forEach(e -> System.out.println(e));
    }
}