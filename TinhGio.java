import java.util.*;
class Game{
    String id = "", name = "", begin = "", end = "";
    int sum = 0;
    public Game(){}

    public int fix(String begin,String end){
        int vao = Integer.parseInt(begin.substring(0,2))*60+Integer.parseInt(begin.substring(3,5));
        int ra = Integer.parseInt(end.substring(0,2))*60+Integer.parseInt(end.substring(3,5));
        return ra - vao;
    }
    public void in(Scanner sc){
        id = sc.nextLine();
        name = sc.nextLine();
        begin = sc.nextLine();
        end = sc.nextLine();
        sum = fix(begin,end);
    }

    @Override
    public String toString() {
        return id+"  "+name+" "+sum/60+" gio "+sum%60+" phut";
    }
    
}
public class TinhGio {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<Game> ds = new ArrayList<>();
        sc.nextLine();
        while( n-->0){
            Game q= new Game();
            q.in(sc);
            ds.add(q);
        }
        Collections.sort(ds,(sv1,sv2)->Integer.compare(sv2.sum,sv1.sum));
        for ( Game i: ds ){
            System.out.println(i);;
        }
        sc.close();
    }
}
