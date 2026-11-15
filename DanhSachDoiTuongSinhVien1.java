import java.util.*;
class SinhVien{
    String id,name,clas,birth;
    double gpa;
    static int stt=1;

    public SinhVien(){
        id="B20DCCN" + String.format("%03d",stt++);
        name="";
        clas="";
        birth="";
        gpa=0;
    }
    public void nhap(Scanner sc){
        sc.nextLine();
        name=sc.nextLine();
        clas=sc.nextLine();
        birth=sc.nextLine();
        birth=chuanhoa(birth);
        gpa=sc.nextDouble();
    }
    public void in(){
        System.out.println(id+" "+name+" "+clas+" "+birth+" "+String.format("%.2f",gpa));
    }
    public String chuanhoa(String s){
        String a[]=s.split("/");
        if ( a[0].length()==1 ) a[0]="0"+a[0];
        if ( a[1].length()==1 ) a[1]="0"+a[1];
        return a[0]+"/"+a[1]+"/"+a[2];
    }
}
public class DanhSachDoiTuongSinhVien1 {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n=sc.nextInt();
        for ( int i=0;i<n;i++){
            SinhVien a=new SinhVien();
            a.nhap(sc);
            a.in();
        }
    }
}
