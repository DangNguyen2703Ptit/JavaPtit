import java.util.*;
public class SoXaCach {
    static int used[]= new int[15];
    static int n;
    static int a[]= new int[15];
    static boolean check(int a[]){
        for ( int i=1;i<n;i++){
            if ( Math.abs(a[i]-a[i+1]) ==1 ) return false;
        }
        return true;
    }
    static void back(int i){
        for ( int j=1;j<=n;j++){
            if( used[j]== 0 ){
                a[i]=j;
                used[j]=1;
                if ( i==n ) {
                    if ( check(a) ) {
                        for ( int x=1;x<=n;x++ ){
                            System.out.print(a[x]);
                        }
                        System.out.println();
                    }
                }
                else back(i+1);
                used[j]=0;
            }
        }
    }

    public static void main (String[] args){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- >0){
            n=sc.nextInt();
            Arrays.fill(used,0);
            back(1);
            System.out.println();
        }
    }

}
