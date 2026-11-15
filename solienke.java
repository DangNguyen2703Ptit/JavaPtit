import java.util.*;
public class solienke {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- >0){
            long n=sc.nextLong();
            boolean check = true;
            Vector<Long>a= new Vector<>();
            while(n!=0){                                                                                
                a.add(n%10);
                n/=10;
            }
            for ( int i=0;i<a.size()-1;i++){
                if ( Math.abs(a.get(i)-a.get(i+1)) !=1 ) {
                    check=false;
                    break;
                }
            }
            if ( check ) System.out.println("YES");
            else System.out.println("NO");
        }
    }
}
