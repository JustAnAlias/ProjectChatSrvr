package echoserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ReceiveObserver;

public class EchoServer implements ReceiveObserver, UserNameObserver{

    private static final Properties properties = Utils.initProperties("server.properties");

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private String ip;
    private int port;
    private List<Client> clients = new ArrayList();
    private HashMap<String, Client> users = new HashMap();

    public static void stopServer() {
        keepRunning = false;
    }
    
    
    
    private void handleClient(Socket socket) throws IOException {
        Client c = new Client(socket, this, this);
//        clients.add(c);
        Thread t = new Thread(c);
        t.start();
    }

    private void runServer(String ip, int port) {
        this.port = port;
        this.ip = ip;

        System.out.println("Sever started. Listening on: " + port + ", bound to: " + ip);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                System.out.println("Client connected" + socket.getRemoteSocketAddress());
                handleClient(socket);
            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        String ip = properties.getProperty("serverIp");
        int port = Integer.parseInt(properties.getProperty("port"));
        if (args.length == 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }

        new EchoServer().runServer(ip, port);
    }

    @Override
    public void notify(String received) {
        
        
        
    }

    @Override
    public void addUser(String userName, Client client) {
        users.put(userName, client);
    }



}
