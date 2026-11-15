import java.util.*;
public class DayConCoTongLe {
    static int n;
    static int a[]=new int[16];
    static ArrayList<Integer> x=new ArrayList<>();

    static void back(int i){
        for ( int j=0;j<=1;j++){
            a[i]=j;
            if ( i==n ) {
                if ( check(x,a) ){
                    for ( int q=1;q<=n;q++){
                        if ( a[q] ==1 ) System.out.print(x.get(q-1) + " ");
                    }
                    System.out.println();
                }
            }
            else back(i+1);
        }
    }
    static boolean check(ArrayList<Integer> x,int a[]){
        int sum=0;
        for ( int i=1;i<=n;i++){
            sum+=a[i]*x.get(i-1);
        }
        return sum%2!=0;

    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int t=sc.nextInt();
        while( t-->0){
            n=sc.nextInt();
            x.clear();
            for ( int i=1;i<=n;i++) {
                int z=sc.nextInt();
                x.add(z);
            }
            Collections.sort(x);
            Collections.reverse(x);
            back(1);
            System.out.println();

        }

    }
}
