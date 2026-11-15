import java.util.*;
public class DayConCoKPhanTuTangDan {
    static int n,k;
    static int a[] = new int[16];
    static int x[];
    static void back(int i){
        for ( int j=a[i-1]+1;j<=n-k+i;j++){
            a[i]=j;
            if ( i==k ) {
                for  ( int z=1;z<=k;z++) {
                    System.out.print(x[a[z]] + " ");
                }
                System.out.println();
            }
            else back(i+1);
        }
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int t=sc.nextInt();
        while(t-->0){
            n=sc.nextInt();
            k=sc.nextInt();
            x=new int[n+1];
            for ( int i=1;i<=n;i++) x[i]= sc.nextInt();
            Arrays.sort(x,1,n+1);
            back(1);
            System.out.println();
        }
    }
}
