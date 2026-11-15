import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.*;
class KhachHang{
    static int stt=1;
    String id=String.format("KH%02d",stt++),name="",begin="",end="";
    long number=0,dichvu=0;
    public KhachHang(){}

    public long getNgay(){
        DateTimeFormatter f= DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate d1= LocalDate.parse(begin,f);
        LocalDate d2= LocalDate.parse(end,f);
        long cnt= ChronoUnit.DAYS.between(d1, d2);
        return cnt+1;
    }
    public long getGia(){
        long x= number/100; 
        if ( x==1 ) return 25;
        else if ( x==2 ) return 34;
        else if ( x==3 ) return 50;
        return 80;
    }
    public long getBill(){
        return getNgay()*getGia()+dichvu;
    }
    public void in(Scanner sc){
        sc.nextLine();
        name= sc.nextLine().trim();
        number= sc.nextLong();
        sc.nextLine();
        begin= sc.nextLine().trim();
        end= sc.nextLine().trim();
        dichvu= sc.nextLong();
    }
    @Override
    public String toString() {
        return id+" "+name+" "+number+" "+getNgay()+" "+getBill();
    }
}
public class HoaDonKhachSan {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n= sc.nextInt();
        ArrayList<KhachHang> ds= new ArrayList<>();
        while( n-- >0){
            KhachHang q= new KhachHang();
            q.in(sc);
            ds.add(q);
        }
        Collections.sort(ds,(kh1,kh2)->Long.compare(kh2.getBill(),kh1.getBill()));
        for ( KhachHang i:ds ) System.out.println(i);
        sc.close();
    }
}
