
import java.util.*;
class  NhanVien{
    static int stt=1;
    String id= String.format("TS%02d",stt++),name="",xeploai="";
    double diemLT=0,diemTH=0,diemTB=0;

    public NhanVien(){}
    public double setdiemTH(double diemTH){
        if ( diemTH > 10.0 ) return  diemTH/10.0;
        return diemTH;
    }
    public double setdiemLT(double diemLT){
        if ( diemLT > 10.0 ) return  diemLT/10.0;
        return diemLT;
    }
    public String setxeploai(double x){
        if ( x>9.5 ) return "XUAT SAC";
        else if ( x>=8 ) return "DAT";
        else if ( x>=5 ) return "CAN NHAC";
        return "TRUOT";
    } 
    public void in(Scanner sc){
        sc.nextLine();
        name = sc.nextLine();
        diemLT = sc.nextDouble();
        diemLT=setdiemLT(diemLT);
        diemTH = sc.nextDouble();
        diemTH=setdiemTH(diemTH);
        diemTB = (diemLT+diemTH)/2.0;
        xeploai = setxeploai(diemTB);
    }
    @Override
    public String toString() {
        return id+" "+name+" "+String.format("%.2f",diemTB)+" "+xeploai;
    }
    
}
public class TuyenDung {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n= sc.nextInt();
        ArrayList<NhanVien> ds = new ArrayList<>();
        while( n-- >0 ){
            NhanVien q= new NhanVien();
            q.in(sc);
            ds.add(q);
        }
        Collections.sort(ds,(nv1,nv2)-> Double.compare(nv2.diemTB,nv1.diemTB));
        for ( NhanVien i:ds )   System.out.println(i);
        sc.close();
    }
}
