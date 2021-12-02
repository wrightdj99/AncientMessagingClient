import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;

    public Client(String host){
        super("Instant Messaging Client");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(600, 600);
        setVisible(true);
    }

    //connect to server
    public void startRunning(){
        try{
            connectToServer();
            setStreams();
            whileChatting();
        }
        catch(EOFException eofException){
            showMessage("\n Client terminated connection \n");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            closeAll();
        }
    }

    private void connectToServer() throws IOException{
        showMessage("Attempting connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP), 6788);
        showMessage("You are connected to: " + connection.getInetAddress().getHostName());
    }

    private void setStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nStreams are now set up between users\n");
    }

    private void whileChatting() throws IOException{
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }
            catch(ClassNotFoundException classNotFoundException){
                showMessage("\nUnknown object type\n");
            }
        }
        while(!message.equals("CLIENT - END"));
    }

    private void closeAll(){
        showMessage("\nClosing program down...\n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void sendMessage(String message){
        try{
            output.writeObject("\nCLIENT - " + message);
            output.flush();
            showMessage("\nCLIENT - " + message);
        }
        catch(IOException exception){
            chatWindow.append("\nSomething went wrong with sending your message\n");
        }
    }

    private void showMessage(final String message){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(message);
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
        );
    }

    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                       userText.setEditable(tof);
                    }
                }
        );
    }
}