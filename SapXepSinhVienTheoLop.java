import java.util.*;
class SinhVien{
    String id="",name="",clas="",email="";
    public SinhVien(){}

    public void in(Scanner sc){
        id=sc.nextLine().trim();
        name= sc.nextLine().trim();
        clas = sc.nextLine().trim();
        email=sc.nextLine().trim();
    }

    @Override
    public String toString() {
        return id + " " + name +" "+ clas + " " + email;
    }
    
}
public class SapXepSinhVienTheoLop {
    public static void main(String[] agrs){
        Scanner sc= new Scanner(System.in);
        int n=Integer.parseInt(sc.nextLine().trim());
        ArrayList <SinhVien> ds= new ArrayList<>();
        while( n-->0 ){
            SinhVien x= new SinhVien();
            x.in(sc);
            ds.add(x);
        }
        Collections.sort(ds,(sv1,sv2)->{
            if ( sv1.clas.equals(sv2.clas) ) return sv1.clas.compareTo(sv2.clas);
            return sv1.clas.compareTo(sv2.clas);
        });
        for ( SinhVien i : ds ) System.out.println(i);
        sc.close();
    }
}
