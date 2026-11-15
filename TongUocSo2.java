import java.util.*;
public class TongUocSo2 {
    
    public static void main(String[] agrs){
        Scanner sc=  new Scanner(System.in);
        int a= sc.nextInt(),b=sc.nextInt();
        int n[]= new int[b+1];
        for ( int i=1;i<=b;i++){
            for ( int j=i;j<=b;j+=i) n[j]+=i;
        }
        int count=0;
        for ( int i=a;i<=b;i++){
            if ( i < n[i]-i ) count++;
        }
        System.out.println(count);
    }
}
