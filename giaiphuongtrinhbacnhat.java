import java.util.Scanner;

public class giaiphuongtrinhbacnhat {
    public static boolean snt(long n){
        for ( int i=2;i*i<=n;i++){
            if ( n%i==0 ) return false;
        }
        return n>1;
    }
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        double a=sc.nextDouble();
        double b=sc.nextDouble();
        if ( a==0 && b!=0 ) System.out.println("VN");
        else if ( a==0 && b==0 ) System.out.println("VSN");
        else  System.out.printf("%.2f",-b/a);
    }
}
