import java.util.*;
class HocSinh{
    static int stt=1;
    String id= String.format("HS%02d",stt++);
    String name="",loai="";
    double a[]= new double[10],tb=0;
    HocSinh(){}
    public double setTB(){
        tb+=a[0]*2+a[1]*2;
        for ( int i=2;i<=9;i++) tb+=a[i];
        return tb/12;
    }
    public String setLoai(){
        if ( tb>=9 ) return "XUAT SAC";
        else if ( tb>=8 ) return "GIOI";
        else if ( tb>=7 ) return "KHA";
        else if ( tb>=5 ) return "TB";
        return "YEU";
    }
    public void in(Scanner sc){
        name= sc.nextLine();
        for ( int i=0;i<=9;i++) a[i]=sc.nextDouble();
        if (sc.hasNextLine()) sc.nextLine();
        tb=setTB();
    }
    @Override
    public String toString() {
        return id+" "+name+" "+String.format("%.1f",tb+1e-9)+" "+setLoai();
    }
    
}
public class bangdiemhocsinh{
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n=Integer.parseInt(sc.nextLine());
        ArrayList<HocSinh> ds= new ArrayList<>();
        for ( int i=1;i<=n;i++){
            HocSinh q= new HocSinh();
            q.in(sc);
            ds.add(q);
        }
        Collections.sort(ds,(hs1,hs2)->{
            if ( hs1.tb== hs2.tb ) return hs1.id.compareTo(hs2.id);
            return Double.compare(hs2.tb,hs1.tb);
        });
        for ( HocSinh i : ds ){
            System.out.println(i);
        }
        sc.close();
    }
}