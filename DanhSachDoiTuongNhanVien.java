import java.util.*;
class NhanVien{
    static int stt=1;
    String id=String.format("%05d",stt++),name="",gioitinh="",birth="",diachi="",ki="";
    String thue="";

    public NhanVien(){}
    public String ddmmyy(String s){
        String a[]=s.split("/");
        if ( a[0].length()==1 ) a[0]="0"+a[0];
        if ( a[1].length()==1 ) a[1]="0"+a[1];
        return a[0]+"/"+a[1]+"/"+a[2];
    }
    public void in(Scanner sc){
        name=sc.nextLine();
        gioitinh=sc.nextLine();
        birth=ddmmyy(sc.nextLine());
        diachi=sc.nextLine();
        thue= sc.nextLine();
        ki=ddmmyy(sc.nextLine());
    }
}
public class DanhSachDoiTuongNhanVien {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        ArrayList<NhanVien> ds = new ArrayList<>(); 
        int n=sc.nextInt();
        sc.nextLine();
        while(n-->0){
            NhanVien q= new NhanVien();
            q.in(sc);
            ds.add(q);
        }
        for ( NhanVien i:ds ){
            System.out.println(i.id + " "+ i.name + " "+ i.gioitinh + " " + i.birth + " " + i.diachi + " "+ i.thue+" "+i.ki);;
        }
    }
}
