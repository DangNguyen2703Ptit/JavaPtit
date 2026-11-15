import java.util.*;
public class BoXungDaySo {
    
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n=sc.nextInt();
        int a[]=new int[n];
        for ( int i=0;i<n;i++) a[i]=sc.nextInt();
        int dem=1;
        boolean check=true;
        for ( int i=0;i<n;i++){
            while( dem < a[i] ) {
                check=false;
                System.out.println(dem);
                dem++;
            }
            dem++;
        }
        if ( check ) System.out.println("Excellent!");
    }
}
