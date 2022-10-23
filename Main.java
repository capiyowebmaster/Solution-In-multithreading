import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {
    static String name= "Capiyo the genious";
    static  Long  startTime=System.nanoTime();
    public static  long endTime=10;
    public static int clientNumbers=0;

    public static void main(String[] args) {

        System.out.println("Sever has been created please enter number of clients you want me to create");
        //Enter the number of clients you want
        Scanner scanner= new Scanner(System.in);
        clientNumbers= scanner.nextInt();

        //instantiating array of nodes between 2 to 255
        Runnable allnodes=()->{
            int[] nodes= new int[clientNumbers];
            for (int i = 0; i < nodes.length; i++) {
                try {

                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Client c= new Client();
                endTime=(System.nanoTime()-startTime)/10000000;


            }


        };
        new Thread(allnodes).start();
        Server s = new Server();




    }


}
