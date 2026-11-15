import java.util.*;
public class ThuGonDaySo {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        ArrayList<Integer> a = new ArrayList<>();
        long n= sc.nextInt();
        for ( int i=0;i<n;i++){
            int x= sc.nextInt();
            if ( !a.isEmpty() && (a.get(a.size()-1)+x)%2==0 ) {
                a.remove(a.size()-1);
            }
            else a.add(x);
        }
        System.out.println(a.size());
    }
}
