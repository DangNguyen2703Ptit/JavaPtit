import java.util.*;
public class uocsonguyentolonnhat {

    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-->0){
            Vector<Long> a = new Vector<>();
            long n=sc.nextLong();
            for (long i=2;i*i<=n;i++){
                while(n%i==0){
                    n/=i;
                }
                a.add(i);
            }
            if ( n>1 ) a.add(n);
            a.sort(null);
            System.out.println(a.get(a.size()-1));
        }
    }
}
