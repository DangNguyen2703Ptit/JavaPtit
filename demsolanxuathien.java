import java.util.*;
public class demsolanxuathien {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        int x=1;
        while(t-->0){
            System.out.println("Test " + x +":");
            x++;
            int n=sc.nextInt();
            int a[]= new int[n];
            int dem[]=new int[(int)1e6];
            boolean check[] = new boolean[(int)1e6];
            Arrays.fill(dem,0);
            for ( int i=0;i<n;i++){
                a[i]=sc.nextInt();
                dem[a[i]]++;
            }
            for ( int i=0;i<n;i++){
                if ( !check[a[i]] ) {
                    System.out.println(a[i]+ " xuat hien " + dem[a[i]] + " lan");
                    check[a[i]]=true;
                }
            }
        }
    }
}
