/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael
 */
public class MyMessage {
    List<PrintWriter> writers = new ArrayList();
    String message = "";

    public List<PrintWriter> getWriters() {
        return writers;
    }

    public void addWriter(PrintWriter writer) {
        writers.add(writer);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    

}
