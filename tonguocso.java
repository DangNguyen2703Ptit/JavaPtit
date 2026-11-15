import java.util.Scanner;
public class tonguocso {
    static long prime[]=new long[2000001];
    public static void sang(){
        for ( int i=0;i<=2000000;i++) prime[i]=i;
        for ( int i=2;i*i<=2000000;i++){
            if ( prime[i]==i ){
                for ( int j=i*i;j<=2000000;j+=i) {
                    if ( prime[j]==j ) prime[j]=i;
                }
            }
        }
    }
    public static void main (String[] agrs){
        Scanner sc= new Scanner(System.in);
        long n=sc.nextLong();
        long sum=0;
        sang();
        while(n-->0){
            int x=sc.nextInt();
            while(x>1){
                sum+=prime[x];
                x/=prime[x];
            }
        }
        System.out.println(sum);

    }
}
