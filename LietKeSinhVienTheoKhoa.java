import java.util.*;
class SinhVien{
    String id="", name="", clas="",email="";
    int key=0;
    public SinhVien(){}
    public void in(Scanner sc){
        id=sc.nextLine().trim();
        name=sc.nextLine().trim();
        clas=sc.nextLine().trim();
        email=sc.nextLine().trim();
        key=Integer.parseInt("20"+clas.substring(1,3));
    }


}
public class LietKeSinhVienTheoKhoa {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n= Integer.parseInt(sc.nextLine());
        
        while(n-- >0){

        }
        sc.close();
    }
}
