import java.util.*;
class SinhVien{
    String id="",name="",clas="",email="";
    public SinhVien(){}

    public void in(Scanner sc){
        id=sc.nextLine();
        name= sc.nextLine();
        clas = sc.nextLine();
        email=sc.nextLine();
    }

    @Override
    public String toString() {
        return id + " " + name +" "+ clas + " " + email;
    }
    
}
public class SapXepSinhVienTheoLop {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int n=Integer.parseInt(sc.nextLine());
        ArrayList <SinhVien> ds= new ArrayList<>();
        while( n-->0 ){
            SinhVien x= new SinhVien();
            x.in(sc);
            ds.add(x);
        }
        Collections.sort(ds,(sv1,sv2)->{
            if ( sv1.clas==sv2.clas ) return sv1.clas.compareTo(sv2.clas);
            return sv1.clas.compareTo(sv2.clas);
        });
        for ( SinhVien i : ds ) System.out.println(i);
        sc.close();
    }
}
