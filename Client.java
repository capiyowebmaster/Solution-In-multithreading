import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.HashMap;
import java.util.Random;

public class Client extends JFrame {

    Socket socket;

    BufferedReader br;   //Reading
    PrintWriter out;     //Writing
    public FileWriter fileWriter;
    Random destination;
    public int dataDest;
    public int clientAdress = 0;


    //Declare components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {
        try {
            System.out.println("Sending request to Server..");
            socket = new Socket("127.0.0.1", 7778);
            System.out.println("Connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            ////startWriting();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("key released :"+e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    //System.out.println("you have presses enter button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }
        });
    }

    private void createGUI() {
        //gui code..

        this.setTitle("Client Messager[END]");
        this.setSize(200, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        //coding for component

        JLabel label= new JLabel("Time taken is: "+
                String.valueOf(Main.endTime)+"milliseconds");
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon("S:\\JAVA\\ChatApp\\src\\image5.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        // adding Layout to Frame
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);
        this.add(label);
    }

    public void startReading() {

        Runnable r1 = () -> {

            System.out.println("reader started..");


            try {
                while (clientAdress < Main.clientNumbers) {
                    String fileExt = String.valueOf(clientAdress);
                    //creating  input files
                    String path = "C:" + File.separator + "Users" + File.separator
                            + "Capiyo" + File.separator + "IdeaProjects" +
                            File.separator + "Simulation" + File.separator + "NodesFolder" + File.separator +
                            "Inputs";
                    String fileName = path + File.separator + "node" + fileExt + ".txt";
                    File f = new File(path);
                    File f1 = new File(fileName);
                    f.mkdir();
                    try {
                        f1.createNewFile();
                        //int dataDest=destination.nextInt(20);
                        // open file and write destination and data to be sent
                        destination = new Random();
                        dataDest = destination.nextInt(50);
                        fileWriter = new FileWriter(fileName);
                        fileWriter.write(clientAdress + ". " + dataDest);
                        fileWriter.close();
                        sendToTheServer(String.valueOf(clientAdress), String.valueOf(dataDest));
                        getDataFromServer();


                    } catch (IOException e) {
                        System.out.println("error occured");
                    }

                    clientAdress = clientAdress + 1;


                }
            } finally {

            }
        };


        new Thread(r1).start();
    }

    private void getDataFromServer() throws IOException {
        Runnable r1 = () -> {
            while (clientAdress <= Main.clientNumbers) {
                InputStreamReader inputStreamReader = null;
                try {
                    inputStreamReader = new
                            InputStreamReader(socket.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                br = new BufferedReader(inputStreamReader);
                String message = null;
                try {
                    message = br.readLine();
                } catch (IOException e) {
                    System.out.println("checking connection");
                }
                // creating a new file and insert data from server
                String fileExt = String.valueOf(clientAdress);
                //creating  input files
                String path = "C:" + File.separator + "Users" + File.separator
                        + "Capiyo" + File.separator + "IdeaProjects" +
                        File.separator + "Simulation" + File.separator + "NodesFolder" + File.separator +
                        "Outputs";
                String fileName = path + File.separator + "node" + fileExt + ".txt";
                File f = new File(path);
                File f1 = new File(fileName);
                f.mkdir();
                try {
                    try {
                        f1.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //int dataDest=destination.nextInt(20);
                    // open file and write destination and data to be sent
                    destination = new Random();
                    dataDest = destination.nextInt(50);
                    try {
                        fileWriter = new FileWriter(fileName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        fileWriter.write("This is message from server" + message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // System.out.println("This is from client" + message);
                } finally {

                }
                clientAdress = clientAdress + 1;
            }
        };
        new  Thread(r1).start();
    }


    public void sendToTheServer(String myIp, String friendsIp) {

        System.out.println("Writer started..");
        int i = 0;
        try {
            while (!socket.isClosed() && i <= Main.clientNumbers)  //true && !socket.isClosed()
            {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println("my ip adress" + myIp + "  " + friendsIp);
                printWriter.flush();
                System.out.println("Writen sucessssss");
                i = i + 1;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}



