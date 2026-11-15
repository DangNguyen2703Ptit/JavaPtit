
import java.util.Scanner;
 
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        QLPT ql = new QLPT(sc);
        int chon;
 
        do {
            System.out.println("\n===== QUẢN LÝ PHƯƠNG TIỆN GIAO THÔNG =====");
            System.out.println("1. Nhập danh sách phương tiện");
            System.out.println("2. Hiển thị danh sách");
            System.out.println("3. Thêm một phương tiện");
            System.out.println("4. Sắp xếp phương tiện (theo giá tăng dần)");
            System.out.println("5. Tìm phương tiện theo tên");
            System.out.println("0. Thoát");
            chon = PhuongTien.docInt(sc, "Chọn chức năng: ");
 
            switch (chon) {
                case 1: ql.nhap(sc); break;
                case 2: ql.hienThi(); break;
                case 3: ql.them(); break;
                case 4: ql.sapxep(); break;
                case 5: ql.timTheoTen(); break;
                case 0: System.out.println("Tạm biệt!"); break;
                default: System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (chon != 0);
 
        sc.close();
    }
}
 
