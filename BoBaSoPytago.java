import java.util.*;
public class BoBaSoPytago {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t= sc.nextInt();
        while(t-->0){
            int n= sc.nextInt();
            long a[]= new long[n];
            for ( int i=0;i<n;i++) {
                long x= sc.nextLong();
                a[i]=x*x;
            }
            Arrays.sort(a);
            boolean check=false;
            for ( int i=n-1;i>=2;i--){
                int l=0,r=n-1;
                while(l<r){
                    if ( a[l]+a[r] == a[i] ) {
                        check=true;
                        break;
                    }
                    else if ( a[l]+a[r] < a[i] ) l++;
                    else if ( a[l]+a[r] > a[i] ) r--;
                }
                if ( check ) break;
            }
            if ( check ) System.out.println("YES");
            else System.out.println("NO");
        }
    }
}
