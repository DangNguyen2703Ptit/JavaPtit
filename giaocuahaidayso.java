import java.util.*;
public class giaocuahaidayso {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        Set<Integer>a= new TreeSet<>();
        for ( int i=1;i<=n;i++) {
            int x=sc.nextInt();
            a.add(x);
        }
        Set<Integer>b= new TreeSet<>();
        for ( int i=1;i<=m;i++){
            int x=sc.nextInt();
            b.add(x);
        }
        for ( int i:a ) {
            if ( b.contains(i) ) System.out.print(i+" ");
        }
    }
}
