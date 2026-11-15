import java.util.*;
public class LietKeToHop1 {
    static int n,k;
    static int count=0;
    static int a[]= new int[15];
    static void back(int i){
        for ( int j=a[i-1]+1;j<=n-k+i;j++){
            a[i]=j;
            if ( i==k ){
                for ( int x=1;x<=k;x++) {
                    System.out.print(a[x]+" ");
                }
                System.out.println();
                count++;
            }
            else back(i+1);
        }
    }
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        n= sc.nextInt();
        k=sc.nextInt();
        back(1);
        System.out.println("Tong cong co " + count + " to hop");
    }
}
