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
public class LietKeSinhVienTheoLop {
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);
        int n= Integer.parseInt(sc.nextLine().trim());
        ArrayList<SinhVien> ds= new ArrayList<>();
        while( n-- >0 ){
            SinhVien x= new SinhVien();
            x.nhap(sc);
            ds.add(x);
        }
        int k=Integer.parseInt(sc.nextLine());
        while( k-- >0 ){
            String search= sc.nextLine();
            System.out.println("DANH SACH SINH VIEN LOP "+search+":");
            for ( SinhVien i : ds ){
                if ( search.equals(i.clas) ) System.out.println(i);
            }
        }
        sc.close();
    }
}
