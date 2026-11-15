import java.util.*;
class  SinhVien{
    static int stt=1;
    String id="B20DCCN"+String.format("%03d",stt++),name="",birth="",clas="";
    double gpa=0;
    public SinhVien(){}

    public void in(Scanner sc){
        sc.nextLine();
        name=chuanhoaName(sc.nextLine());
        clas=sc.nextLine();
        birth=chuanhoaBirth(sc.nextLine());
        gpa=sc.nextDouble();
    }
    public void out(){
        System.out.println(id+" "+name+" "+clas+" "+birth+" "+String.format("%.2f",gpa));
    }
    public String chuanhoaName(String s){
        String a[]=s.trim().split("\\s+");
        String res="";
        for ( String x:a ){
            x=(x.charAt(0)+"").toUpperCase()+ x.substring(1).toLowerCase();
            res+=x+" ";
        }
        return res.trim();
    }
    public String chuanhoaBirth(String s){
        String a[]=s.split("/");
        if ( a[0].length()== 1 ) a[0]="0"+a[0];
        if ( a[1].length()== 1 ) a[1]="0"+a[1];
        return a[0]+"/"+a[1]+"/"+a[2];
    }
}
public class DanhSachDoiTuongSinhVien3 {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        int n=sc.nextInt();
        ArrayList<SinhVien> ds= new ArrayList<>();
        for ( int i=0;i<n;i++){
            SinhVien q= new SinhVien();
            q.in(sc);
            ds.add(q);
        }
        Collections.sort(ds,(sv1,sv2)-> Double.compare(sv2.gpa,sv1.gpa));
        for ( SinhVien s:ds ){
            s.out();
        }
    }
}
