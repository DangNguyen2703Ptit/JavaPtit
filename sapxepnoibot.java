import java.util.*;
public class sapxepnoibot {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int n= sc.nextInt();
        ArrayList<Integer>a= new ArrayList<>();
        for ( int i=0;i<n;i++){
            int x=sc.nextInt();
            a.add(x);
        }
        int buoc=0;
        for ( int i=0;i<n-1;i++){
            boolean check=false;
            for ( int j=0;j<n-1-i;j++ ){
                if ( a.get(j)>a.get(j+1) ) {
                    Collections.swap(a,j,j+1);
                    check=true;
                }
            }
            if ( check ){
                buoc++;
                System.out.print("Buoc " + buoc + ": ");
                for ( int s:a ) System.out.print(s + " ");
                System.out.println();
            }
        }
    }
}
