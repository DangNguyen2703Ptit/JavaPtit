import java.util.Scanner;
public class ktrfibo {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        long cnt[]= new long[93];
        cnt[0]=0;cnt[1]=1;
        for ( int i=2;i<=92;i++) cnt[i]=cnt[i-1]+cnt[i-2];
        int t= sc.nextInt();
        while(t-->0){
            long n=sc.nextLong();
            boolean check=false;
            for ( int i=0;i<=92;i++){
                if ( cnt[i]==n ){
                    check=true;
                    break;
                }
            }
            if ( check ) System.out.println("YES");
            else System.out.println("NO");
        }
    }
}
