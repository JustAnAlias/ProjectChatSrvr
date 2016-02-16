package echoclient;

import shared.ReceiveObserver;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;
import shared.ReceiveObserver;

public class EchoClient implements ReceiveObserver
{
    private String received = "";
    Runnable listener;
    ReceiveObserver observer;
  Socket socket;
  private String ip;
  private int port;
  private InetAddress serverAddress;
  private Scanner input;
  private PrintWriter output;

    public EchoClient() {

    }
    public EchoClient(ReceiveObserver obs)
    {   
        this.observer = obs;
        this.port = 9999;
        this.ip = "localhost";

    }
  
  public void connect(String address, int port) throws UnknownHostException, IOException
  {
      if (address !=null){
          this.ip = address;
          
      }
      else if (port>3000 && port<10000){
          
          this.port = 9999;
      }
      serverAddress = InetAddress.getByName(ip);
    
    
    socket = new Socket(serverAddress, port);
    input = new Scanner(socket.getInputStream());
    output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
    listener = new SocketListener(input, this);
    Thread t = new Thread(listener);
    t.start();
    
  }
  
  public void send(String msg)
  {
    output.println(msg);
  }
  
  public void stop() throws IOException{
    output.println(ProtocolStrings.STOP);
  }
  
//  public String receive()
//  {
//    String msg = input.nextLine();
//    observer.notify(msg);
//    if(msg.equals(ProtocolStrings.STOP)){
//      try {
//        socket.close();
//      } catch (IOException ex) {
//        Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
//      }
//    }
//    return msg;
//  }
  


    @Override
    public void notify(String received) {
        this.received = received;
        observer.notify(received);
    }
    
    public String getReceived(){
        return received;
    }
  
    class SocketListener implements Runnable{
        Scanner sc;
        ReceiveObserver observer;
        public SocketListener(Scanner sc, ReceiveObserver observer){
            System.out.println("listener started on client");
            this.sc = sc;
            this.observer = observer;
        }
        @Override
        public void run() {
            while(true){
//                System.out.println("listening....");
                if(sc.hasNextLine()){
                    System.out.println("message received:");
                    String message = sc.nextLine();
                    System.out.println(message);
                    observer.notify(message);
                }
            }
        }
    }

    
}