import java.util.*;
public class sapxepchon {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int n= sc.nextInt();
        ArrayList<Integer>a= new ArrayList<>();
        for ( int i=0;i<n;i++){
            int x=sc.nextInt();
            a.add(x);
        }
        for ( int i=0;i<n-1;i++){
            int min=i;
            for ( int j=i+1;j<n;j++){
                if ( a.get(j) < a.get(min) ) min=j;
            }
            Collections.swap(a,i,min);
            System.out.print("Buoc " + (i+1)+ ": ");
            for ( int s:a ) System.out.print(s+" ");
            System.out.println();
        }
    }
}
