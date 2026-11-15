import java.util.Scanner;

public class tinhTongNSoNguyenDuong {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- >0){
            int n=sc.nextInt();
            System.out.println((long)n*(n+1)/2);
        }
    }
}
