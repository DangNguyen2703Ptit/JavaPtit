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
        return id + " " + name +clas + " " + email;
    }
    
}
public class SapXepSinhVienTheoLop {
    
}
