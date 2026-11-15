import java.util.*;
public class bai1 {
    public static void main(String[] agrs){
        Scanner sc=  new Scanner(System.in);
        int n=-1;
        boolean check=false;
        while(sc.hasNextInt()){
            int choice=sc.nextInt();
            if ( choice == 0 ) {
                break;
            }
            switch (choice) {
                case 1:
                    if ( sc.hasNextInt()){
                        n=sc.nextInt();
                        check=true;
                    }
                    break;
                case 2:
                    if ( !check ){
                        System.out.println("Chọn 1");
                    }
                    else {
                        long sum=0;
                        for (int i=1;i<=n;i++){
                            sum+=i;
                        }
                        System.out.println(sum);
                    }
                    break;
                case 3:
                    if ( sc.hasNextInt()){
                        double d1=sc.nextDouble();
                        double d2=sc.nextDouble();
                        double gpa=(d1*2+d2)/3.0;
                        System.out.printf("%.1f\n",gpa);
                    }   
                    break;
                default:
                    System.out.println("Chọn 0,1,2,3");
                    break;
            }
        }
    }
}
