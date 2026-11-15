import java.util.*;
public class sochinhphuong {
    public static void main(String[] aggrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-->0){
            int n=sc.nextInt();
            double k=Math.sqrt(n);
            if ( (int)k*k==n ) System.out.println("YES");
            else System.out.println("NO");
        }
    }
}
