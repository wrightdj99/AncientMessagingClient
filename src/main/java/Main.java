import javax.swing.JFrame;

public class Main {
    public static void main(String[] args){
        //You'd enter in your host addr here
        Client mainClient = new Client("127.0.0.1");
        mainClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainClient.startRunning();
    }
}
