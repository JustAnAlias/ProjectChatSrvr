package echoserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;
import shared.ReceiveObserver;


public class EchoServer implements ReceiveObserver{

  private static boolean keepRunning = true;
  private static ServerSocket serverSocket;
  private String ip;
  private int port;
  private List<Client> clients = new ArrayList();
 

  public static void stopServer() {
    keepRunning = false;
  }

  private void handleClient(Socket socket) throws IOException {
      Client c = new Client(this, socket);
    clients.add(c);
    Thread t = new Thread(c);
    t.start();
  }
  
  private void runServer(String ip, int port)
  {
    this.port = port;
    this.ip = ip;
    
    System.out.println("Sever started. Listening on: "+port+", bound to: "+ip);
    try {
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(ip, port));
      do {
        Socket socket = serverSocket.accept(); //Important Blocking call
        System.out.println("Client connected" + socket.getRemoteSocketAddress());        
        handleClient(socket);
      } while (keepRunning);
    } 
    catch (IOException ex) {
      Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void main(String[] args) {
    String ip = "localhost";
    int port = 9999;
    if (args.length==2){
        ip = args[0];
        port = Integer.parseInt(args[1]);
    }
        
    new EchoServer().runServer(ip,port);
  }

    @Override
    public void notify(String received) {
        
        for (Client c : clients){
            c.send(received);
        }
    }
  
  class Client implements Runnable{
      ReceiveObserver observer;
      Socket socket;
      PrintWriter pw;
      Scanner sc;
      private boolean running = true;
      
      public Client(ReceiveObserver observer, Socket socket) throws IOException{
          this.observer = observer;
          this.socket = socket;
          this.pw = new PrintWriter(socket.getOutputStream(), true);
          sc = new Scanner(socket.getInputStream());
          pw.println("Welcome " + socket.getRemoteSocketAddress() + " to " + socket.getInetAddress());
          
      }
      
      public void send(String message){
          pw.println(message);
      }
      
        @Override
        public void run() {
            while(socket.isConnected() && running){
                if (sc.hasNext()){
                    String msg = sc.nextLine();
                    if(msg.equalsIgnoreCase("#exit")){
                        System.out.println("closing thread" + Thread.currentThread());
                        try{
                            running = false;
                            socket.close();
                        }
                        catch(Exception e){
                            Logger.getLogger(msg).log(Level.SEVERE, msg);
                        }
                        
                    }
//                    System.out.println("message received: " + msg);
                    
                    observer.notify(msg);
                }
            }
          try {
              socket.close();
          } catch (IOException ex) {
              Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      
  }
}