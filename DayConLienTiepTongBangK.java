import java.util.*;
public class DayConLienTiepTongBangK {

    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- >0){
            int n=sc.nextInt();
            long k=sc.nextLong();
            long a[]= new long[n];
            ArrayList<Long> list= new ArrayList<>();
            for ( int i=0;i<n;i++)    {
                a[i]=sc.nextLong();
                list.add(a[i]);
            }
            if ( k==0 ){
                if ( list.contains(0) ) {
                    System.out.println("YES");
                }
                else System.out.println("NO");
            }
            else{
                boolean check=false;
                int l=0,r=0;
                long sum=0;
                while ( r<n ){
                    sum+=a[r];
                    while( sum>k && l<=r ){
                        sum-=a[l];
                        l++;
                    }
                    if ( sum==k ) {
                        check=true;
                        break;
                    }
                    r++;
                }
                if ( check ) System.out.println("YES");
                else System.out.println("NO");
            }
        }
    }
}
