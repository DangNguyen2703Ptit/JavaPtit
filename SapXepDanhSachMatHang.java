import java.lang.reflect.Array;
import java.util.*;
class MatHang{
    static int stt=1;
    int id= stt++;
    String name="", group= "";
    double buy=0,sell=0,profit=0;
    public MatHang(){}

    public void inp(Scanner sc){
        sc.nextLine();
        name = sc.nextLine();
        group = sc.nextLine();
        buy = sc.nextDouble();
        sell = sc.nextDouble();
        profit = sell-buy;
    }
    @Override
    public String toString() {
        return id + " " + name + " " + group +" " + String.format("%.2f",profit);
    }
}
public class SapXepDanhSachMatHang {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n= sc.nextInt();
        ArrayList<MatHang> ds= new ArrayList<>();
        while ( n-- >0){
            MatHang q= new MatHang();
            q.inp(sc);
            ds.add(q);
        }
        Collections.sort(ds,(mh1,mh2)->Double.compare(mh2.profit,mh1.profit));
        for ( MatHang i : ds ){
            System.out.println(i);
        }
        sc.close();
    }
}
