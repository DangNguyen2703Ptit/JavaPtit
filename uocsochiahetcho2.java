import java.util.Scanner;
public class uocsochiahetcho2{
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-->0){
            int n=sc.nextInt();
            int cnt=0;
            for (int i=1;i*i<=n;i++){
                if ( n%i==0 ) {
                    if ( i%2==0 ) cnt++;
                    if ( (n/i)!=i && (n/i)%2==0 ) cnt++;
                }
            }
            System.out.println(cnt);
        }
    }
}