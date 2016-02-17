/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static shared.ProtocolStrings.MSG;
import static shared.ProtocolStrings.STOP;
import static shared.ProtocolStrings.USER;
import shared.ReceiveObserver;

/**
 *
 * @author Michael
 */
public class Client implements Runnable {
    Server server;
    UserNameObserver uObs;
    ReceiveObserver observer;
    Socket socket;
    PrintWriter pw;
    private PrintWriter out;
    Scanner sc;
    String userName = null;
    private boolean running = true;

    public Client(Socket socket, ReceiveObserver rObs, UserNameObserver uObs) {
        this.uObs = uObs;
        this.observer = observer;
        this.socket = socket;
        try {
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            sc = new Scanner(socket.getInputStream());
            setUserName(sc.nextLine());
            pw.println("Welcome " + userName + "@" + socket.getRemoteSocketAddress());
            String message = "";
            while (!message.equals(STOP)) {
            message = sc.nextLine();
                    handleMsg(message);            
            }      
        } catch (Exception e) {
            running = false;
        }
    }

    public void send(String message) {
        pw.println(message);
    }
    public PrintWriter getWriter(){
        return out;
    }
    @Override
    public void run() {
        while (socket.isConnected() && running) {
            if (sc.hasNext()) {
                String msg = sc.nextLine();
                if (msg.equalsIgnoreCase("#exit")) {
                    System.out.println("closing thread" + Thread.currentThread());
                    try {
                        running = false;
                        socket.close();
                    } catch (Exception e) {
                        Logger.getLogger(msg).log(Level.SEVERE, msg);
                    }

                }
                else{
                    String[] forward = msg.split("#");
                    if (forward[0].equals("SEND")){
                        forward[2] = userName + "says: " + forward[2];
                    }
                    observer.notify(msg);
                }
//                    System.out.println("message received: " + msg);

                
            }
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUserName(String first) throws IOException {
        String[] temp;
        String res = "";
        String command = "";
        if (first.contains("#")) {
            temp = first.split("#");
            if (temp[0].equals("USER") && temp[1].length() > 2) {
                userName = temp[1];
                uObs.addUser(userName, this);
            }
        } else {
            pw.println("Error: Bad username, or username not set");
            socket.close();
            running = false;
        }

    }
    public String getUserName(){
        return userName;
    }

    private void handleMsg(String message) {
        String[] splitted = message.split("#");
        String protocol = splitted[0];
        switch(protocol){
            case USER:
                userName = splitted[1];
                server.addClient(userName, this);
                server.userList(); // Send userlist
                break;
            case MSG:
                String receivers = splitted[1];
                String msg = splitted[2];
                server.sendMsg(userName, receivers, msg);
                break;
            case STOP:
                break;
            default:
        }
    }
    
}
