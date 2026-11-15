import java.util.Scanner;
public class bscusc {
    public static int ucln(int a,int b){
        while(b!=0){
            int c=a%b;
            a=b;b=c;
        }   return a;
    }
    public static void main(String[] agrs){
        Scanner sc=  new Scanner(System.in);
        int t=sc.nextInt();
        while(t-->0){
            int a= sc.nextInt();
            int b= sc.nextInt();
            if ( a<b ){
                int tmp=a;
                a=b;
                b=tmp;
            }
            int uoc=ucln(a,b);
            long boi=(long)(a/uoc)*b;
            System.out.println(boi + " " + uoc);
        }
    }
}
