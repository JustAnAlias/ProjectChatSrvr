/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael
 */
public class MyMessage {
    List<Socket> sockets = new ArrayList();
    String message = "";

    public List<Socket> getSockets() {
        return sockets;
    }

    public void addSocket(Socket sock) {
        sockets.add(sock);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    

}
