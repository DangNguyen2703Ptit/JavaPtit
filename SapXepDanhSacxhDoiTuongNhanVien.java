import java.util.*;
class NhanVien{
    static int stt=1;
    String id = String.format("%05d",stt++);
    String name = "", gioitinh = "", birth = "", address = "", idThue = "", date = "";
    int birthNumber=0;
    public NhanVien(){}

    public void inp(Scanner sc){
        name = sc.nextLine();
        gioitinh = sc.nextLine();
        birth = fixdate(sc.nextLine());
        address = sc.nextLine();
        idThue = sc.nextLine();
        date = fixdate(sc.nextLine());
        birthNumber= fixBirth(birth);
    }
    public String fixdate(String s){
        String a[]=s.split("/");
        if ( a[0].length()==1 ) a[0]= "0" + a[0];
        if ( a[1].length()==1 ) a[1]= "0" + a[1];
        return a[0] + "/" + a[1] + "/" +a[2];
    } 
    public int fixBirth(String s){
        String a[]=s.split("/");
        if ( a[0].length()==1 ) a[0]= "0" + a[0];
        if ( a[1].length()==1 ) a[1]= "0" + a[1];
        return Integer.parseInt(a[2] + a[1] + a[0]);
    }
    @Override
    public String toString() {
        return  id + " " + name + " " + gioitinh + " " + birth + " " + address + " " + idThue + " " + date;
    }
}
public class SapXepDanhSacxhDoiTuongNhanVien {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n= sc.nextInt();
        sc.nextLine();
        ArrayList<NhanVien> ds= new ArrayList<>();
        while( n-- >0 ){
            NhanVien q= new NhanVien();
            q.inp(sc);
            ds.add(q);
        }
        Collections.sort(ds,(nv1,nv2)-> Integer.compare(nv1.birthNumber, nv2.birthNumber));
        for ( NhanVien i:ds ){
            System.out.println(i);
        }
        sc.close();
    }
}
