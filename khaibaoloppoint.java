import java.util.*;
class Point{
    double x;
    double y;
}
public class khaibaoloppoint {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- >0){
            Point a= new Point();
            Point b= new Point();
            a.x=sc.nextDouble();
            a.y=sc.nextDouble();
            b.x=sc.nextDouble();
            b.y=sc.nextDouble();
            double c=a.x-b.x;
            double d=a.y-b.y;
            double kc= Math.sqrt(c*c+d*d);
            System.out.printf("%.4f\n",kc);

        }
    }
}
