import java.util.*;
public class sokhonglienke{

    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- >0){
            long n=sc.nextLong();
            Vector<Long>a=new Vector<>();
            boolean ktr=true;
            long sum=0;
            while(n!=0){
                a.add(n%10);
                sum+=n%10;
                n/=10;
            }
            for ( int i=0;i<a.size()-1;i++){
                if ( Math.abs(a.get(i)-a.get(i+1)) !=2 ) {
                    ktr=false;
                    break;
                }
            }
            if ( ktr && sum%10==0 ) System.out.println("YES");
            else System.out.println("NO");
        }
    }
}