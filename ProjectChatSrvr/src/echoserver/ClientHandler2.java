/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import echoserver.Server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;
import static shared.ProtocolStrings.MSG;
import static shared.ProtocolStrings.STOP;
import static shared.ProtocolStrings.USER;

/**
 *
 * @author RL
 */
public class ClientHandler2 extends ProtocolStrings {
    
    private Server server;
    private Socket socket;
    private PrintWriter out;
    private String userName;
    
    public ClientHandler2(Server server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    
    
        
    public void run() {
        try {
            handleClient();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void handleClient() throws IOException {
        Scanner input = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        
        String message = "";
        while (!message.equals(STOP)) {
        message = input.nextLine();
                handleMsg(message);            
        }      
        if (message.equals(STOP)) {
            out.println(STOP);
        }
        socket.close();
    }
    
    
    private void handleMsg(String message) throws IOException{
        String[] splitted = message.split("#");
        String protocol = splitted[0];
        switch(protocol){
            case USER:
                userName = splitted[1];
                server.addClient(userName, this);
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
    
    
    public PrintWriter getWriter(){
        return out;
    }
    
    public String getUserName(){
        return userName;
    }
    
}
