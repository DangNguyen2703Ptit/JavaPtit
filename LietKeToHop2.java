import java.util.*;
public class LietKeToHop2 {

    static int n,k,count=0;
    static int a[]=new int[15];

    static void back(int i){
        for ( int j=a[i-1]+1;j<=n-k+i;j++){
            a[i]=j;
            if ( i==k ) {
                for ( int s=1;s<=k;s++) {
                    System.out.print(a[s]);
                }
                System.out.print(" ");
                count++;
            }
            else back(i+1);
        }
    }
    public static void main (String[] args){
        Scanner sc= new Scanner(System.in);
        n=sc.nextInt();
        k=sc.nextInt();
        back(1);
        System.out.println("\nTong cong co "+ count + " to hop");

    }
}
