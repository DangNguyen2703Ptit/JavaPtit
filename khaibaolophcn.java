import java.util.*;
class hcn{
    double cd;
    double cr;
    String color;
}

public class khaibaolophcn {
    static double chuvi(double a,double b){
        return ((a+b)*2);
    }
    static double dientich(double a,double b){
        return (a*b);
    }
    static String check(double n){
        if ( n==(long)n ) return String.valueOf((long)n);
        else return String.valueOf((double)n);
    }
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
            hcn x= new hcn();
            x.cd= sc.nextDouble();
            x.cr=sc.nextDouble();
            x.color = sc.next();
            x.color=x.color.toLowerCase();
            x.color=x.color.substring(0,1).toUpperCase()+ x.color.substring(1);
            if ( x.cd >0 && x.cr>0 ){
                System.out.println( check(chuvi(x.cd,x.cr)) + " " + check(dientich(x.cd,x.cr)) + " " + x.color);
            }
            else {
                System.out.println("INVALID");
            }
    }
}
