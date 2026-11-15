import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// 1. Lớp SinhVien
class SinhVien {
    private String ma, hoten;
    private Date ngaySinh;
    private boolean gtinh; // true: Nam, false: Nữ
    private double gpa;
    private static int sMa = 1;

    public SinhVien() {
        this.ma = String.format("SV%03d", sMa++);
    }

    public SinhVien(String hoten, Date ngaySinh, boolean gtinh, double gpa) {
        this.ma = String.format("SV%03d", sMa++);
        this.hoten = hoten;
        this.ngaySinh = ngaySinh;
        this.gtinh = gtinh;
        this.gpa = gpa;
    }

    public String getMa() { return ma; }
    public String getHoten() { return hoten; }
    public Date getNgaySinh() { return ngaySinh; }
    public boolean isGtinh() { return gtinh; }
    public double getGpa() { return gpa; }

    public void setHoten(String hoten) { this.hoten = hoten; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }
    public void setGtinh(boolean gtinh) { this.gtinh = gtinh; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public void nhap(Scanner in) {
        System.out.print("Nhập họ tên: ");
        this.hoten = in.nextLine();

        System.out.print("Nhập ngày sinh (dd/MM/yyyy): ");
        String dateStr = in.nextLine();
        try {
            this.ngaySinh = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
        } catch (ParseException e) {
            this.ngaySinh = new Date();
            System.out.println("Định dạng ngày không hợp lệ, lấy ngày hiện tại làm mặc định.");
        }

        System.out.print("Nhập giới tính (1-Nam / 0-Nữ): ");
        this.gtinh = in.nextInt() == 1;
        System.out.print("Nhập điểm GPA: ");
        this.gpa = in.nextDouble();
        in.nextLine(); // Đọc bỏ dòng thừa sau nextDouble()
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ngayStr = (ngaySinh != null) ? sdf.format(ngaySinh) : "";
        return String.format("Mã: %s | Họ tên: %s | Ngày sinh: %s | Giới tính: %s | GPA: %.2f",
                ma, hoten, ngayStr, (gtinh ? "Nam" : "Nữ"), gpa);
    }
}

// 2. Interface ChucNang
interface ChucNang {
    void them(Scanner in);
    void hienthi();
    void xoa(String ma);
    void xoaNhieu(String ten);
    void tim(String ma);
    void timNhieu(String ten);
    void timTheoNam(int nam);
    void timTheoDiem(double tu, double den);
    void sua(Scanner in, String ma);
}

// 3. Lớp QLSV
class QLSV implements ChucNang {
    private List<SinhVien> ds = new ArrayList<>();

    @Override
    public void them(Scanner in) {
        SinhVien sv = new SinhVien();
        sv.nhap(in);
        ds.add(sv);
        System.out.println("Đã thêm sinh viên thành công!");
    }

    @Override
    public void hienthi() {
        if (ds.isEmpty()) {
            System.out.println("Danh sách sinh viên trống!");
            return;
        }
        System.out.println("--- DANH SÁCH SINH VIÊN ---");
        for (SinhVien sv : ds) {
            System.out.println(sv);
        }
    }

    @Override
    public void xoa(String ma) {
        boolean removed = ds.removeIf(sv -> sv.getMa().equalsIgnoreCase(ma));
        if (removed) {
            System.out.println("Đã xóa sinh viên có mã: " + ma);
        } else {
            System.out.println("Không tìm thấy sinh viên có mã: " + ma);
        }
    }

    @Override
    public void xoaNhieu(String ten) {
        boolean removed = ds.removeIf(sv -> sv.getHoten().toLowerCase().contains(ten.toLowerCase()));
        if (removed) {
            System.out.println("Da xoa cac sinh vien co ten chua: " + ten);
        } else {
            System.out.println("Khong tim thay sinh vien nao phu hop de xoa.");
        }
    }

    @Override
    public void tim(String ma) {
        boolean found = false;
        for (SinhVien sv : ds) {
            if (sv.getMa().equalsIgnoreCase(ma)) {
                System.out.println(sv);
                found = true;
            }
        }
        if (!found) System.out.println("Khong tim thay sinh vien ma: " + ma);
    }

    @Override
    public void timNhieu(String ten) {
        boolean found = false;
        for (SinhVien sv : ds) {
            if (sv.getHoten().toLowerCase().contains(ten.toLowerCase())) {
                System.out.println(sv);
                found = true;
            }
        }
        if (!found) System.out.println("Khong tim thay sinh vien co ten chua: " + ten);
    }

    @Override
    public void timTheoNam(int nam) {
        boolean found = false;
        Calendar cal = Calendar.getInstance();
        for (SinhVien sv : ds) {
            if (sv.getNgaySinh() != null) {
                cal.setTime(sv.getNgaySinh());
                if (cal.get(Calendar.YEAR) == nam) {
                    System.out.println(sv);
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("Khong tim thay sinh vien sinh nam: " + nam);
        }
    }

    @Override
    public void timTheoDiem(double tu, double den) {
        boolean found = false;
        for (SinhVien sv : ds) {
            if (sv.getGpa() >= tu && sv.getGpa() <= den) {
                System.out.println(sv);
                found = true;
            }
        }
        if (!found) System.out.println("Khong tim thay sinh vien trong khoang diem nay.");
    }

    @Override
    public void sua(Scanner in, String ma) {
        for (SinhVien sv : ds) {
            if (sv.getMa().equalsIgnoreCase(ma)) {
                System.out.print("Nhap ho ten moi: ");
                sv.setHoten(in.nextLine());
                System.out.print("Nhap GPA moi: ");
                sv.setGpa(in.nextDouble());
                in.nextLine(); // Dọn bộ đệm
                System.out.println("Cap nhat thanh cong!");
                return;
            }
        }
        System.out.println("Khong tim thay thong tin sinh vien can sua: " + ma);
    }
}

// 4. Lớp Main
public class baitap1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QLSV qlsv = new QLSV();
        int chon;

        do {
            System.out.println("\n--- CHUONG TRINH QUAN LI SINH VIEN ---");
            System.out.println("1. Them sinh vien");
            System.out.println("2. Hien thi danh sach");
            System.out.println("3. Xoa sinh vien theo ma");
            System.out.println("4. Xoa sinh vien theo ten");
            System.out.println("5. Tim kiem sinh vien theo ma");
            System.out.println("6. Tim kiem sinh vien theo ten");
            System.out.println("7. Tim kiem theo khoang cach GPA");
            System.out.println("8. Tim kiem theo nam sinh");
            System.out.println("9. Sua thong tinh sinh vien theo ma");
            System.out.println("0. Thoat");
            System.out.print("Chon chuc nang: ");
            chon = scanner.nextInt();
            scanner.nextLine(); // Đọc bỏ ký tự xuống dòng sau nextInt()

            switch (chon) {
                case 1:
                    qlsv.them(scanner);
                    break;
                case 2:
                    qlsv.hienthi();
                    break;
                case 3:
                    System.out.print("Nhap ma sinh vien can xoa: ");
                    qlsv.xoa(scanner.nextLine());
                    break;
                case 4:
                    System.out.print("Nhap ten sinh vien can xoa: ");
                    qlsv.xoaNhieu(scanner.nextLine());
                    break;
                case 5:
                    System.out.print("Nhap ma sinh vien can tim: ");
                    qlsv.tim(scanner.nextLine());
                    break;
                case 6:
                    System.out.print("Nhap ten sinh vien can tim: ");
                    qlsv.timNhieu(scanner.nextLine());
                    break;
                case 7:
                    System.out.print("Nhap diem tu: ");
                    double tu = scanner.nextDouble();
                    System.out.print("Nhap diem den: ");
                    double den = scanner.nextDouble();
                    scanner.nextLine();
                    qlsv.timTheoDiem(tu, den);
                    break;
                case 8:
                    System.out.print("Nhap nam sinh can tim: ");
                    int nam = scanner.nextInt();
                    scanner.nextLine();
                    qlsv.timTheoNam(nam);
                    break;
                case 9:
                    System.out.print("Nhap ma sinh vien can sua: ");
                    String maSua = scanner.nextLine();
                    qlsv.sua(scanner, maSua);
                    break;
                case 0:
                    System.out.println("Thoat chuong trinh!");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } while (chon != 0);

        scanner.close();
    }
}

