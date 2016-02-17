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
import shared.ReceiveObserver;

/**
 *
 * @author Michael
 */
public class Client implements Runnable {
    UserNameObserver uObs;
    ReceiveObserver observer;
    Socket socket;
    PrintWriter pw;
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
        } catch (Exception e) {
            running = false;
        }
    }

    public void send(String message) {
        pw.println(message);
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
                    if (forward[0].equals("SEND"))
                    observer.notify(msg);
                }
//                    System.out.println("message received: " + msg);

                
            }
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
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
}
