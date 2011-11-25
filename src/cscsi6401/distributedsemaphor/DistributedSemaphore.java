package cscsi6401.distributedsemaphor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.Configuration;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author daniel
 */
public class DistributedSemaphore extends Thread{
//    Helper helper;
    
    String name;
    
    LogicalClock clock;
    
    PrintWriter helperWriter;
    BufferedReader helperReader;
    
    Socket[] inputs;
    Socket[] outputs;
    
    //create a socket to the helper
    
    /**
     * 
     * @param semaphorName The name for this semaphor
     * @param configuration A multi dimensional array of config info
     *                      column[0] == ipv4 address
     *                      column[1] == port number
     */
    public DistributedSemaphore(String semaphorName, final String[][] configuration){
        try {
            name = semaphorName;
            clock = new LogicalClock();
            
            final int numberOfNodes = configuration.length;
            Socket[] nodes = new Socket[numberOfNodes];
            inputs = new Socket[numberOfNodes];
            outputs = new Socket[numberOfNodes];
            
            Thread input = new Thread(){
                public void run(){
                    int index = 0;
                    while(index < numberOfNodes){
                        try {
                            int port = Integer.parseInt(configuration[index][1]);
                            outputs[index] = new ServerSocket(port).accept();
                            index++;
                        } catch (IOException ex) {
                            Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    ServerSocket s;
                    try {
                        s = new ServerSocket(32000);
                        helperReader = new BufferedReader(new InputStreamReader(s.accept().getInputStream()));
                    } catch (IOException ex) {
                        Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            };
            
            Thread output = new Thread(){
                public void run(){
                    int index = 0;
                    while(index < numberOfNodes){
                        try {
                            String address = configuration[index][0];
                            int port = Integer.parseInt(configuration[index][1]);
                            inputs[index] = new Socket(address,port);
                            index++;
                        } catch (UnknownHostException ex) {
//                            Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
//                            Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            
            try {
                input.join();
                output.join();
            } catch (InterruptedException ex) {
//                Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
            }                     
            
            
            new Helper(inputs,outputs).start();
            
            ServerSocket s = new ServerSocket(32000);
            Socket helper = s.accept();
            helperWriter = new PrintWriter(helper.getOutputStream());
            helperReader = new BufferedReader(new InputStreamReader(helper.getInputStream()));
            
        } catch (IOException ex) {
            Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void V(){
        String message = name+","+clock.getTime()+"," + Message.REQ_V;
        clock.tick();
        helperWriter.println(message);
        
    }
    
    public void P() throws IOException{
        String message = name+","+clock.getTime()+"," + Message.REQ_P;
        clock.tick();
        helperWriter.println(message);
        clock.compareAndSet(Long.parseLong(helperReader.readLine()));
        
    }
    
    public void run(){
        while(true);
    }
}
