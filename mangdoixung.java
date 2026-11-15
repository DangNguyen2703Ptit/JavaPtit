import java.util.*;
public class mangdoixung {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- >0){
            int n=sc.nextInt();
            int a[]= new int[n+1];
            for ( int i=1;i<=n;i++)  a[i]=sc.nextInt();
            boolean check=true;
            for ( int i=1;i<=n/2;i++){
                if  ( a[i]!=a[n-i+1] ) check=false;
            }
            if ( check ) System.out.println("YES");
            else System.out.println("NO");
        }
    }
}
