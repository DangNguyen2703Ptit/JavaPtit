import java.util.*;
class SinhVien{
    static int stt=1;
    String id="B20DCCN"+ String.format("%03d",stt++),name="",clas="",birth="";
    double gpa=0;
    public SinhVien(){}

    public void nhap(Scanner sc){
        sc.nextLine();
        name=sc.nextLine();
        name=chuanhoaName(name);
        clas=sc.nextLine();
        birth=sc.nextLine();
        birth=chuanhoaBirth(birth);
        gpa=sc.nextDouble();
    }
    public void in(){
        System.out.println(id+" " + name + " "+ clas+ " " + birth +" " + String.format("%.2f",gpa));
    }
    public String chuanhoaBirth(String s){
        String a[] = s.split("/");
        if ( a[0].length()== 1 ) a[0]="0"+a[0];
        if ( a[1].length()== 1 ) a[1]="0"+a[1];
        return a[0]+"/"+a[1]+"/"+a[2];
    }
    public String chuanhoaName(String s){
        String a[]=s.trim().split("\\s+");
        String res="";
        for ( String x : a ){
            x=(x.charAt(0)+"").toUpperCase()+x.substring(1).toLowerCase();
            res+=x+" ";
        }
        
        return res.trim();
    }
}
public class DanhSachDoiTuongSinhVien2 {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        while(n-- >0){
            SinhVien x=new SinhVien();
            x.nhap(sc);
            x.in();
        }
    }
}
