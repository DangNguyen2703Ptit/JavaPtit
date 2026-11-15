import java.util.Scanner;
public class phantichthuasonguyento {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();int cnt=1;
        while(t-->0){
            int n= sc.nextInt();
            System.out.print("Test " + cnt +": ");
            cnt++;
            for ( int i=2;i*i<=n;i++){
                int dem=0;
                while(n%i==0){
                    dem++;
                    n/=i;
                }
                if ( dem!=0 ) System.out.print(i+"("+dem+") ");
            }
            if ( n>1 ) System.out.print(n+"(1)");
            System.out.println();
        }
    }
}
