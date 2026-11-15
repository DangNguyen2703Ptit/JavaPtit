import java.io.*;
import java.util.Scanner;

public class DocFileVanBan {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File("DATA.in"));
        PrintWriter out = new PrintWriter(new File("DATA.out"));

        while (sc.hasNextLine()) {
            out.println(sc.nextLine()); // Ghi dữ liệu vào DATA.out
        }

        // BẮT BUỘC: Đóng file để dữ liệu xả hết từ bộ nhớ đệm ra file DATA.out
        sc.close();
        out.close(); 
    }
}