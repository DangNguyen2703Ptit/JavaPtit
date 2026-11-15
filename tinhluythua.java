import java.util.*;
public class tinhluythua {
    static long mod=(long)1e9+7;
    public static long POW(long a,long n){
        if ( n==0 ) return 1;
        long k=POW(a,n/2);
        if (n%2==0) return ( (k%mod)*(k%mod) )%mod;
        else return ((a%mod)*(( (k%mod)*(k%mod) )%mod))%mod;
    }
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        while(true){
            long a=sc.nextLong();
            long n=sc.nextLong();
            if ( a==0 && n==0) break;
            System.out.println(POW(a,n));
        }
    }
}
