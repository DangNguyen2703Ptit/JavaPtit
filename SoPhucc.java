import java.util.*;
class SoPhuc{
    int ao,thuc;
    public SoPhuc(int a,int b){
        thuc=a;
        ao=b;
    }
    public SoPhuc add(SoPhuc a){
        SoPhuc c= new SoPhuc(0, 0);
        c.ao=this.ao+a.ao;
        c.thuc=this.thuc+a.thuc;
        return c;
    }
    public SoPhuc nhan(SoPhuc a){
        SoPhuc c= new SoPhuc(0,0);
        c.thuc=this.thuc*a.thuc - this.ao*a.ao;
        c.ao= this.thuc*a.ao + this.ao*a.thuc;
        return c;
    }
    @Override
    public String toString() {
        if ( ao<0 ) return thuc + " - " + Math.abs(ao) + "i";
        else return thuc + " + "  + ao + "i";
    }
    
}
public class SoPhucc {
    public static void main(String[] args){
       Scanner sc= new Scanner(System.in);
        int t=sc.nextInt();
        while(t-- >0){
            int a=sc.nextInt();
            int b=sc.nextInt();
            int c=sc.nextInt();
            int d=sc.nextInt();
            SoPhuc x= new SoPhuc(a,b);
            SoPhuc y= new SoPhuc(c,d);
            SoPhuc z=x.add(y);
            SoPhuc C=z.nhan(x);
            SoPhuc D=z.nhan(z);
            System.out.println(C + ", " + D);
        }
    }
}
