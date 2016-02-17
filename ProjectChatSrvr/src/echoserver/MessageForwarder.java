/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.util.Queue;

/**
 *
 * @author Michael
 */
public class MessageForwarder implements Runnable{
    Queue q;
    Boolean running = true;
    public MessageForwarder(Queue queue){
        this.q = queue;
    }

    @Override
    public void run() {
        while (running){
            String
        }
    }
            
            
    
}
