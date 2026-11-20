import java.util.*;
class SinhVien{
    String id="",name="",clas="",email="";
    public SinhVien(){}

    public void nhap(Scanner sc){
        id=sc.nextLine().trim();
        name=sc.nextLine().trim();
        clas=sc.nextLine().trim();
        email=sc.nextLine().trim();
    }

    @Override
    public String toString() {
        return id+" "+name+" "+clas+" "+email;
    }
    
}
public class SapXepTheoMaSinhVien {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        ArrayList<SinhVien> ds= new ArrayList<>();
        while( sc.hasNext() ){
            SinhVien x= new SinhVien();
            x.nhap(sc);
            ds.add(x);
        }
        Collections.sort(ds,(sv1,sv2)->sv1.id.compareTo(sv2.id));
        for ( SinhVien i : ds ) System.out.println(i);
        sc.close();
    }
}
