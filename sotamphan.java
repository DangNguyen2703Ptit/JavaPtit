import java.util.*;
public class sotamphan {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- >0){
            int n=sc.nextInt();
            Vector<Integer>a= new Vector<>();
            while(n!=0){
                a.add(n%10);
                n/=10;
            }
            boolean check=true;
            for ( int i:a ) {
                if (i!=0 && i!=1 && i!=2 ) {
                    check=false;
                    break;
                }
            }
            if ( check ) System.out.println("YES");
            else System.out.println("NO");
        }
    }
}
