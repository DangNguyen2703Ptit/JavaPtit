import java.util.*;
public class hopcuahaidayso{
    public static void main(String[] agrs){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        Set<Integer>kho= new TreeSet<>();
        for ( int i=1;i<=n;i++) {
            int x=sc.nextInt();
            kho.add(x);
        }
        for ( int i=1;i<=m;i++) {
            int x=sc.nextInt();
            kho.add(x);
        }
        for ( int i:kho ) System.out.print(i+" ");
    }
}