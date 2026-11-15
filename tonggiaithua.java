import java.util.Scanner;
public class tonggiaithua {
    public static long gt(int n){
        long sum=1;
        for ( int i=2;i<=n;i++) sum*=i;
        return sum;
    }
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int n= sc.nextInt();
        long sum=0;
        for ( int i=1;i<=n;i++) sum+=gt(i);
        System.out.println(sum);

    }
}
