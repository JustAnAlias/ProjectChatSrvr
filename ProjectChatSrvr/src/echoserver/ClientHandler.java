/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RL^
 */
public class ClientHandler {
    
    private static List<ClientHandler> clients = new ArrayList();
    
    private String userName;
    private Socket socket;
    private PrintWriter pw;
    
    public static List<ClientHandler> getClients() {
        return clients;
    }
    
    
    public Socket getSocket() {
        return socket;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public ClientHandler(Socket socket, String userName) {
        this.userName = userName;
        this.socket = socket;
        try {
            pw = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void send(String message) {
        pw.println(message);
    }
    
    
}    
