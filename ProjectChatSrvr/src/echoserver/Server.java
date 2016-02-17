package echoserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ClientHandler2;
import static shared.ProtocolStrings.USERLIST;
import shared.ReceiveObserver;

public class Server implements ReceiveObserver, UserNameObserver{

    // load config file
    private static final Properties properties = Utils.initProperties("server.properties");

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
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
        System.out.println("Sever started. Listening on: " + port + ", bound to: " + ip);
        users = new HashMap<>();
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                System.out.println("Client connected" + socket.getRemoteSocketAddress());
                handleClient(socket);
            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeClient(String userName){
        users.remove(userName);
    }
        
    public static void main(String[] args) {
        String ip = properties.getProperty("serverIp");
        int port = Integer.parseInt(properties.getProperty("port"));
        if (args.length == 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }

        new Server().runServer(ip, port);
    }

    @Override
    public void notify(String received) {
        
        
        
    }

    public void addClient(String userName, ClientHandler2 aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void userList() {
        ArrayList<String> userList = new ArrayList<String>();
        for (String userName : users.keySet()) userList.add(userName); // Get all keysets from map
        for (Client c : users.values())  // get the list of values
        {
            PrintWriter out = c.getWriter();
            String userNames = "";
            for (String userName : userList) {
                if (!userName.equals(c.getUserName())) {
                    userNames += userName + ","; // Adding a comma in the end of the string
                }
            }
            if (userNames.length() > 0) {
                userNames = userNames.substring(0, userNames.length() - 1); //removing the last comma from the string
            }
            out.println(USERLIST + "#" + userNames);
        }
    }

    public void sendMsg(String userName, String receivers, String msg) {
        
    }

    @Override
    public void addUser(String userName, Client client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
