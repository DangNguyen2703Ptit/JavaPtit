import java.util.*;
import java.math.BigInteger;
public class boichungnhonhat{
    public static void main(String[] agrs){
        Scanner sc=new Scanner(System.in);
        int t= sc.nextInt();
        while(t-- >0){
            BigInteger a= new BigInteger(sc.next());
            BigInteger b= sc.nextBigInteger();
            BigInteger gcd=a.gcd(b);
            BigInteger lcm=a.divide(gcd).multiply(b);
            System.out.println(lcm);
        }
    }
}