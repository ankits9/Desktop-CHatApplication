import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.*;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Font;

 public class Client extends JFrame {
      
    Socket socket;

    BufferedReader br;
    PrintWriter out;

   private JLabel heading=new JLabel("Client Space");
   private JTextArea messArea=new JTextArea();
   private JTextField messInput=new JTextField();
   private  Font font=new Font("Roboto",Font.PLAIN,20);

    public Client(){
        try {
            System.out.println("Sending request to server");
            socket= new Socket("127.0.0.1",2706);
            System.out.println("Connection done");
            br= new BufferedReader(new InputStreamReader(socket.getInputStream()) );
            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvent();


            startReading();
            //startWriting();
        } catch (Exception e) {
            // e.printStackTrace();
        }
             
        
    }


    private void handleEvent() {

        messInput.addKeyListener(new KeyListener(){
            

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
            //System.out.println("Key Released "+ e.getKeyCode());
            if(e.getKeyCode()==10){
            String contentToSend=messInput.getText();
            messArea.append("Me :" +contentToSend+ "\n");
            out.println(contentToSend);
            out.flush();
            messInput.setText(" ");
            messInput.requestFocus();

            }               
        }

        });
    }


    private void createGUI() {

        this.setTitle("ClientMessanger[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        heading.setFont(font);
        messArea.setFont(font);
        messInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messArea.setEditable(false);
        messInput.setHorizontalAlignment(SwingConstants.CENTER);


        this.setLayout(new BorderLayout());
        
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messInput, BorderLayout.SOUTH);



        this.setVisible(true);
    }


    public  void startWriting() {
        Runnable r2=()->{
         System.out.println("Start writing");

         try {
            while(!socket.isClosed()){
            
                BufferedReader br2= new BufferedReader(new InputStreamReader(System.in));
                String content=br2.readLine();
                out.println(content);
                out.flush();

                if(content.equals("quit")){
                    socket.close();
                    break;
                }
            
            }

            System.out.println("Connection off");

        }
                catch (Exception e) {
                e.printStackTrace();
            }
         
        };
        new Thread(r2).start();
    }


    public void startReading() {
        Runnable r1=()->{
           System.out.println("Start reading");
         try {
           
         while(true){

            String msg=br.readLine();
            if(msg.equals("quit")){
                System.out.println("Server terminated this chat");
                JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                messInput.setEnabled(false);
                socket.close();
                break;
            }
            //System.out.println();
            messArea.append("Server: " +msg + "\n");
            
        }
    } 
            catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Connection off");
            }
          
        };
        new Thread(r1).start();
    }


    public static void main(String[] args) {
        System.out.println("This is Client class");
        new Client();
    }
}