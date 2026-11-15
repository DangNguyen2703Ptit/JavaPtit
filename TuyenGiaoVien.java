import java.util.*;
class GiaoVien{
    static int stt=1;
    String id= String.format("GV%02d",stt++),name="",ma="";
    double diemTin=0,diemChuyen=0,sum=0;
    public GiaoVien(){}

    public String getMonhoc(){
        char x= ma.charAt(0);
        if ( x=='A' ) return "TOAN";
        else if ( x=='B' ) return "LY";
        return "HOA";
    }
    public double getDiemuutien(){
        char x= ma.charAt(1);
        if ( x== '1' ) return 2.0;
        else if ( x=='2' ) return 1.5;
        else if ( x=='3' ) return 1.0;
        return 0.0;
    }
    public String getKq(){
        if ( sum>= 18.0 ) return "TRUNG TUYEN";
        return "LOAI";
    }

    public void inp(Scanner sc){
        sc.nextLine();
        name= sc.nextLine();
        ma= sc.nextLine();
        diemTin= sc.nextDouble();
        diemChuyen= sc.nextDouble();
        sum=diemTin*2+diemChuyen+getDiemuutien();
    }

    @Override
    public String toString() {
        return id+" "+name+" "+getMonhoc()+" "+String.format("%.1f",sum)+" "+getKq();
    }
    
}
public class TuyenGiaoVien {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n= sc.nextInt();
        ArrayList<GiaoVien> ds= new ArrayList<>();
        while( n-->0){
            GiaoVien q= new GiaoVien();
            q.inp(sc);
            ds.add(q);
        }
        Collections.sort(ds,(gv1,gv2)->Double.compare(gv2.sum,gv1.sum));
        for ( GiaoVien i:ds )  System.out.println(i);
        sc.close();
    }
}
