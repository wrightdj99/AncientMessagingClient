import javax.swing.JFrame;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        String addr;
        //You'd enter in your host addr here
        System.out.println("Find out the IPv4 Address of your server machine and enter it here:\n");
        addr = scan.nextLine();
        Client mainClient = new Client(addr);
        mainClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainClient.startRunning();
    }
}
