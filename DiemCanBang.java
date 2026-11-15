import java.util.*;
public class DiemCanBang {
    public static void main(String[] args) {
        Scanner sc = new Scanner( System.in);
        int t= sc.nextInt();
        while( t-- >0){
            int n= sc.nextInt();
            int a[]= new int[n];
            long sum=0;
            for ( int i=0;i<n;i++){
                a[i]=sc.nextInt();
                sum+=a[i];
            }
            boolean check=false;
            long sumleft=0,sumright=0;
            for ( int i=1;i<n-1;i++){
                sumleft+=a[i-1];
                sumright=sum-sumleft-a[i];
                if ( sumleft == sumright ){
                    System.out.println(i+1);
                    check=true;
                    break;
                }
            }
            if ( check == false) System.out.println(-1);

        }
    }
}
