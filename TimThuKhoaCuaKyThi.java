import java.util.*;
class SinhVien{
    static int stt=1;
    int id=stt++;
    double d1 = 0,d2 = 0, d3 = 0,sum=0;
    String name = "",birth = "";
    public SinhVien(){}

    public void inp(Scanner sc){
        sc.nextLine();
        name = sc.nextLine();
        birth = (sc.nextLine());
        d1 = sc.nextDouble();
        d2 = sc.nextDouble();
        d3 = sc.nextDouble();
        sum= d1+d2+d3;
    }
    public String fixdate(String s){
        String a[]= s.split("/");
        if ( a[0].length()==1 ) a[0] = "0" + a[0];
        if ( a[1].length()==1 ) a[1] = "0" + a[1];
        return a[0] + "/" + a[1] + "/" + a[2];
    }

    @Override
    public String toString() {
        return id + " " + name + " " + birth + " " + sum;
    }
}
public class TimThuKhoaCuaKyThi {
    public static void main(String[] arg){
        Scanner sc = new Scanner(System.in);
        int n= sc.nextInt();
        ArrayList<SinhVien> ds = new ArrayList<>();
        while( n-- >0 ){
            SinhVien q= new SinhVien();
            q.inp(sc);
            ds.add(q);
        }
        Collections.sort(ds,(sv1,sv2)->{
            if ( sv2.sum!=sv1.sum ){
                return Double.compare(sv2.sum,sv1.sum);
            }
            return Integer.compare(sv1.id,sv2.id);
        });
        double maxSum= ds.get(0).sum;
        for ( SinhVien i : ds ){
            if ( i.sum==maxSum ) System.out.println(i);
        }
        sc.close();
    }
}
