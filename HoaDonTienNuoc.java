import java.util.*;
class KhachHang{
    static int stt=1;
    String id=String.format("KH%02d",stt++);
    String name="";
    long begin=0,end=0;
    long bill=0;

    public KhachHang(){}
    public long setBill(){
        long x=end-begin;
        double sum=0;
        if ( x > 100 ) sum= (50*100+50*150+(x-100)*200)*1.05;
        else if ( x>=51 ) sum= (50*100+(x-50)*150)*1.03;
        else sum= x*100*1.02;
        return Math.round(sum);
    }
    public void in(Scanner sc){
        name=sc.nextLine().trim();
        begin=Long.parseLong(sc.nextLine().trim());
        end=Long.parseLong(sc.nextLine().trim());
    }
    @Override
    public String toString() {
        return id+" "+name+" "+setBill();
    }
}
public class HoaDonTienNuoc {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n=Integer.parseInt(sc.nextLine().trim());
        ArrayList<KhachHang> ds= new ArrayList<>();
        while( n-- >0 ){
            KhachHang q= new KhachHang();
            q.in(sc);
            ds.add(q);
        }
        Collections.sort(ds,(kh1,kh2)->Long.compare(kh2.setBill(),kh1.setBill()));
        for ( KhachHang i : ds )    System.out.println(i);
        sc.close();
    }
}
