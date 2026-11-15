import java.util.*;
class QLMA{
    String ma,ten,kk,ho,last;
    double hsl;
    int gio;
    static int dem=0;
    public QLMA(String ten,double hsl,String kk,int gio){
        this.ma=String.format("PM%03d",++dem);
        this.ten=ten;
        this.hsl=hsl;
        this.kk=kk;
        this.gio=gio;
        String a[]=ten.trim().split("\\s+");
        this.ho=a[0];
        this.last=a[a.length-1];
    }
    
    public double getKK(){
        if ( kk.equals("A")) return 1.5;
        if ( kk.equals("B")) return 1.2;
        return 1.0;
    }
    public long getLuong(){
        double luong=hsl* getKK()*3000;
        if ( gio<=100 ) luong*=0.85;
        else if ( gio <= 350 ) luong*=0.90;
        else if ( gio < 500 ) luong*=0.95;
        return Math.round(luong);
    }
    @Override 
    public String toString(){
        return ma+" " + ten + " " + hsl+" "+kk+" " + gio+" " + getLuong();
    }
}
public class bai3 {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        int n=Integer.parseInt(sc.nextLine());
        ArrayList<QLMA> ds= new ArrayList<>();
        for ( int i=0;i<n;i++){
            String ten=sc.nextLine();
            double hsl=sc.nextDouble();
            sc.nextLine();
            String kk=sc.nextLine();
            int gio=Integer.parseInt(sc.nextLine());
            QLMA q= new QLMA(ten,hsl,kk,gio);
            ds.add(q);
        }
        ds.sort((a,b)->{int cmp=a.last.compareToIgnoreCase(b.last);
        if ( cmp!=0 ) return cmp;
        return
        a.ho.compareToIgnoreCase(b.ho);});
        for ( QLMA q:ds){
            System.out.println(q);
        }
    }
}
