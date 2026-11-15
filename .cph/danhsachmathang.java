/*DLab
DLab
Nguyễn Mậu Đăng
Làm bài lớp: 04 · J05081
Đề bài: J05081 — DANH SÁCH MẶT HÀNG
2s · 146.5 MB
Bài toán quản lý danh sách mặt hàng trong đó mỗi mặt hàng sẽ có các thông tin:

Mã mặt hàng: tự động tăng, tính từ MH001
Tên mặt hàng: xâu ký tự độ dài không quá 100
Đơn vị tính: xâu ký tự độ dài không quá 10
Giá mua: số nguyên dương không quá 7 chữ số
Giá bán: số nguyên dương không quá 7 chữ số
Viết chương trình nhập danh sách mặt hàng, sắp xếp theo lợi nhuận (giá bán trừ đi giá mua) giảm dần. Nếu lợi nhuận bằng nhau thì in ra theo thứ tự mã tăng dần. 

Input

Dòng đầu ghi số M là số mặt hàng (không quá 40).

Tiếp theo là thông tin của M mặt hàng, mỗi mặt hàng ghi trên 4 dòng theo đúng thứ tự đã mô tả (không có mã)

Output

Ghi ra danh sách mặt hàng có đầy đủ thông tin ở trên và lợi nhuận tính được (mỗi thông tin cách nhau một khoảng trống)

Ví dụ

Input	Output
2
Ao phong tre em
Cai
25000
41000
Ao khoac nam
Cai
240000
515000

	MH002 Ao khoac nam Cai 240000 515000 275000
MH001 Ao phong tre em Cai 25000 41000 16000
 


Tải lên file mã nguồn hoặc .zip/.rar/.7z (project nhiều file) để nộp

Hỗ trợ .cpp, .py, .java… hoặc .zip / .rar / .7z (project).

Java
Lịch sử nộp của tôi — bài J05081
ID	Thời gian	Ngôn ngữ	Kết quả	Thời gian chạy	Bộ nhớ
Chưa có bài nộp
DLab */
import java.util.Scanner;

public class Main{
    static class goods{
        public goods(String n, String u, int b, int s,int i){
            name = n;
            unit = u;
            buy = b;
            sell = s;
            pro = s - b;
            String str = Integer.toString(i);
            code = i;
        }
        public String sCode(){
            String s = Integer.toString(code);
            while(s.length()<3) s = "0" + s;
            return "MH" + s;
        }
        public boolean small(goods b){
            if(pro == b.pro) return code > b.code;
            return pro < b.pro;
        }
        String name, unit;
        int buy,sell,pro,code;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        goods[] a = new goods[n];
        for(int i=0; i<n; i++){
            sc.nextLine();
            a[i] = new goods(sc.nextLine(), sc.nextLine(), sc.nextInt(), sc.nextInt(), i+1);
        }
        goods tmp;
        for(int i=0; i<n-1; i++){
            for(int j=i+1; j<n; j++){
                if(a[i].small(a[j])){
                    tmp = a[i];
                    a[i] = a[j];
                    a[j] = tmp;
                }
            }
        }
        for(int i=0; i<n; i++){
            System.out.printf("%s %s %s %d %d %d\n", a[i].sCode(), a[i].name,
            a[i].unit, a[i].buy, a[i].sell, a[i].pro);
        }
    }
}   
