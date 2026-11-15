import java.util.*;
class ThiSinh{
    String name="",donvi="",time="";
    public ThiSinh(){}

    public String getMa(){
        String  res="";
        String a[]=donvi.split(" ");
        for ( String i:a ) res+= i.charAt(0);
        String b[]= name.split(" ");
        for ( String i:b ) res+= i.charAt(0);
        return res;
    }
    public double getVantoc(){
        String a[]= time.split(":");
        double gio= Integer.parseInt(a[0])*60+ Integer.parseInt(a[1])-6*60;
        return 120/(gio/60);
    }
    public void in(Scanner sc){
        name= sc.nextLine();
        donvi= sc.nextLine();
        time= sc.nextLine();
    }
    @Override
    public String toString() {
        return getMa()+" "+name+" "+donvi+" "+String.format("%.0f",getVantoc())+" Km/h";
    }
    
}
public class DuaXeDap {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n= sc.nextInt();
        sc.nextLine();
        ArrayList<ThiSinh> ds= new ArrayList<>();
        while( n-->0 ){
            ThiSinh q= new ThiSinh();
            q.in(sc);
            ds.add(q);
        }
        Collections.sort(ds,(ts1,ts2)->Double.compare(ts2.getVantoc(),ts1.getVantoc()));
        for ( ThiSinh i : ds ) System.out.println(i);
        sc.close();
    }
}
