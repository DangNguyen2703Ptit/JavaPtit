import java.util.*;
class Tram{
    static int stt=1;
    String id=String.format("T%02d",stt++);
    String start="",end="";
    double luongmua=0,thoigian=0;
    public Tram(){}

    public double getTime(){
        double x= Double.parseDouble(start.substring(0,2))*60 + Double.parseDouble(start.substring(3,5));
        double y= Double.parseDouble(end.substring(0,2))*60 + Double.parseDouble(end.substring(3,5));
        return y-x;
    }
    

}
public class LuongMuaTrungBinh {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        sc.close();
    }
}
