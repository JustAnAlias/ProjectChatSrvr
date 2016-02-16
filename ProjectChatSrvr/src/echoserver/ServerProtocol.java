/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

/**
 *
 * @author RL^
 */
public class ServerProtocol implements Runnable{

    private boolean addClient(ClientHandler ch) {

        for (ClientHandler c : ClientHandler.getClients()) {
            if (c.getUserName().equals(ch.getUserName())) {
                return false;
            }
        }
        ClientHandler.getClients().add(ch);
        return true;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
