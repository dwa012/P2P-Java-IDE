package cscsi6401.distributedsemaphor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author daniel
 */
public class Helper extends Thread {
    
    

    private StreamListener[] listeners;
    private PrintWriter[] outputStreams;
    PriorityQueue<Message> messages;
    
    String hostname;

    public Helper(Socket[] sockets) {
        
        for (int i = 0; i < sockets.length; i++) {
            try {
                listeners[i] = new StreamListener(sockets[i], this);
                outputStreams[i] = new PrintWriter(sockets[i].getOutputStream());
            } catch (IOException ex) {
//                Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            hostname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }

        messages = new PriorityQueue<Message>();
    }
    
    public synchronized  void broadcast(String sender){
        
    }
    
    public synchronized void ack(){
        
    }
    
    public synchronized void requestP(){
        
    }
    
    public synchronized void requestV(){
        
    }

    @Override
    public void run() {
    }
}
