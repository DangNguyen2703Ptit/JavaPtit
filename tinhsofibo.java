import java.util.Scanner;
public class tinhsofibo {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        long cnt[]=new long[93];
        cnt[1]=cnt[2]=1;
        for ( int i=3;i<=92;i++) cnt[i]=cnt[i-1]+cnt[i-2];
        int t=sc.nextInt();
        while(t-->0){
            int n=sc.nextInt();
            System.out.println(cnt[n]);
        }
    }
}
