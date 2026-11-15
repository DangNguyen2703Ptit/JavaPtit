import java.lang.reflect.Array;
import java.util.*;
class HangHoa{
    String id="", name="";
    long sl=0,gia=0,chietkhau=0,bill=0;
    public HangHoa(){}

    public void in(Scanner sc){
        sc.nextLine();
        id=sc.nextLine();
        name=sc.nextLine();
        sl=sc.nextLong();
        gia=sc.nextLong();
        chietkhau=sc.nextLong();
        bill=gia*sl-chietkhau;
    }

    @Override
    public String toString() {
        return id+" "+name+" "+sl+" "+gia+" "+chietkhau+" "+bill;
    }
    
}
public class TinhTien {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<HangHoa> ds = new ArrayList<>();
        while( n-->0 ){
            HangHoa q= new HangHoa();
            q.in(sc);
            ds.add(q);
        }
        Collections.sort(ds,(h1,h2)->Long.compare(h2.bill, h1.bill));
        for ( HangHoa i : ds ){
            System.out.println(i);
        }
        sc.close();
    }
}
