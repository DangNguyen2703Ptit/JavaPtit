import java.util.*;
public class boisonhonhatcuansodau{

    static long gcd(long a,long b){
        while(b!=0){
            long c=a%b;
            a=b;
            b=c;
        }
        return a;
    }
    static long lcm(long a,long b){
        return a/gcd(a,b)*b;
    }

    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- >0){
            int n=sc.nextInt();
            long bcnn=1;
            for ( int i=1;i<=n;i++) bcnn=lcm(bcnn,i);
            System.out.println(bcnn);
        }
    }
}