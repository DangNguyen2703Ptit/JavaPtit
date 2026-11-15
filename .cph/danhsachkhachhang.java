/*DLab
DLab
Nguyễn Mậu Đăng
Làm bài lớp: 04 · J05082
Đề bài: J05082 — DANH SÁCH KHÁCH HÀNG
2s · 146.5 MB
Bài toán quản lý danh sách khách hàng, trong đó mỗi Khách hàng có các thông tin:

Mã khách hàng: tự động tăng, tính từ KH001
Tên khách hàng: xâu ký tự độ dài không quá 50, cần đưa về dạng chuẩn
Giới tính: Nam hoặc Nu
Ngày sinh: cần đưa về dạng chuẩn dd/mm/yyyy
Địa chỉ: xâu ký tự độ dài không quá 100
Viết chương trình nhập danh sách khách hàng, sắp xếp theo ngày sinh từ già nhất đến trẻ nhất. Không có hai khách hàng nào cùng ngày sinh. 

Input

Dòng đầu ghi số N là số khách hàng (không quá 20).

Tiếp theo là thông tin của N khách hàng, mỗi khách hàng ghi trên 4 dòng theo đúng thứ tự đã mô tả (không có mã)

Output

Danh sách khách hàng với đầy đủ các thông tin liệt kê ở trên, trong đó tên và ngày sinh đã được chuẩn hóa. 

Ví dụ

Input	Output
2
 nGuyen VAN     nAm
Nam
12/12/1997
Mo Lao-Ha Dong-Ha Noi
 TRan    vAn     biNh
Nam
14/11/1995
Phung Khoang-Nam Tu Liem-Ha Noi
 	KH002 Tran Van Binh Nam Phung Khoang-Nam Tu Liem-Ha Noi 14/11/1995
KH001 Nguyen Van Nam Nam Mo Lao-Ha Dong-Ha Noi 12/12/1997
 
 


Tải lên file mã nguồn hoặc .zip/.rar/.7z (project nhiều file) để nộp

Hỗ trợ .cpp, .py, .java… hoặc .zip / .rar / .7z (project).

Java
Lịch sử nộp của tôi — bài J05082
ID	Thời gian	Ngôn ngữ	Kết quả	Thời gian chạy	Bộ nhớ
Chưa có bài nộp
DLab */
import java.util.*;
import java.text.*;

public class Main {  

    static String cleanName(String name){
        String[] a = name.trim().split("\\s+");
        for(int i=0;i<a.length;i++){
            a[i] = Character.toString(a[i].charAt(0)).toUpperCase() + a[i].substring(1).toLowerCase();
        }
        return String.join(" ", a);
    }

    static String cleanDate(String date){
        String a[] = date.split("/");
        a[0] = String.format("%02d", Integer.parseInt(a[0]));
        a[1] = String.format("%02d", Integer.parseInt(a[1])); 
        return String.join("/", a);
    }

    static class ob {
        String code, name, sex, strdate, address;
        Long date;

        public ob(int i, String name, String sex, String date, String address){
            this.code = "KH" + String.format("%03d", i);
            this.name = cleanName(name);
            this.sex = sex;
            this.address = address;
            this.strdate = cleanDate(date);
            SimpleDateFormat f = new SimpleDateFormat("dd/mm/yyyy");
            try{
                this.date = f.parse(this.strdate).getTime();
            }catch(ParseException e){

            }
        }

        @Override 
        public String toString(){
            return code + " " + name + " " + sex + " " + address + " " + strdate;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<ob> a = new ArrayList<>();
        int n = sc.nextInt();
        sc.nextLine();
        for(int i=0;i<n;i++){
            a.add(new ob(i+1, sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine()));
        }
        a.sort((ob x, ob y) ->{
            if(x.date.compareTo(y.date)==0){
                return x.code.compareTo(y.code);
            }
            return x.date.compareTo(y.date);
        });
        a.forEach(e -> System.out.println(e));
    }
}
