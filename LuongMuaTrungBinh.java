import java.util.*;
class Tram{
    static int stt=1;
    String id=String.format("T%02d",stt++);
    String name="",start="",end="";
    double luongmua=0,thoigian=0,sum=0;
    public Tram(){}

    public double getTime(){
        double x= Double.parseDouble(start.substring(0,2))*60 + Double.parseDouble(start.substring(3,5));
        double y= Double.parseDouble(end.substring(0,2))*60 + Double.parseDouble(end.substring(3,5));
        return y-x;
    }
    public double getTB(){
        return sum/(thoigian/60);
    }
    public void nhap(Scanner sc){
        name= sc.nextLine();
        start= sc.nextLine();
        end= sc.nextLine();
        luongmua= Double.parseDouble(sc.nextLine());
    }
    @Override
    public String toString() {
        return id+" "+name+" "+getTB();
    }

}
public class LuongMuaTrungBinh {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n= Integer.parseInt(sc.nextLine());
        ArrayList<Tram> ds= new ArrayList<>();
        while( n-- >0){
            Tram x= new Tram();
            
        }
        sc.close();
    }
}
