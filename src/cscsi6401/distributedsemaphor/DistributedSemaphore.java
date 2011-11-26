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
import sun.nio.cs.ext.TIS_620;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author daniel
 */
public class DistributedSemaphore{// extends Thread{
//    Helper helper;
    
    String name;
    
    LogicalClock clock;
    
    PrintWriter helperWriter;
    BufferedReader helperReader;
    
   
    
    //create a socket to the helper
    
    /**
     * 
     * @param semaphorName The name for this semaphor
     * @param configuration A multi dimensional array of config info
     *                      column[0] == ipv4 address
     *                      column[1] == port number
     */
    public DistributedSemaphore(String semaphorName, final String[][] configuration, int helperPort){
        try {
            
            System.out.println(semaphorName);
            name = semaphorName;
            clock = new LogicalClock();
            ServerSocket socketForHelper = new ServerSocket(helperPort);
            
            final int numberOfNodes = configuration.length;
            
            final Socket[] inputs = new Socket[numberOfNodes];
            final Socket[] outputs = new Socket[numberOfNodes];
            
            final ServerSocket serverSocketForNodes = new ServerSocket(Integer.parseInt(configuration[0][1]));
            
            Socket[] nodes = new Socket[numberOfNodes];
            
            
            
            Thread input = new Thread(){
                public void run(){
                    int index = 0;
                    while(index < numberOfNodes){
                        try {
                            outputs[index] = serverSocketForNodes.accept();
                            System.out.println("Node is connected");
                            index++;
                        } catch (IOException ex) {
//                            System.out.println("Error " + ex.getMessage());
//                            Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
                            System.out.println("Connected to another node");
                            index++;
                        } catch (UnknownHostException ex) {
//                            System.out.println("Error " + ex.getMessage());
//                            Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
//                            System.out.println("Error " + ex.getMessage());
//                            Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            
            input.start();
            output.start();
            
            
            
            try {
                input.join();
                output.join();
            } catch (InterruptedException ex) {
                System.out.println("error " + ex.getMessage());
//                Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
            } 
            
            new Helper(this.name,inputs,outputs,helperPort).start();
            
            Socket helper = socketForHelper.accept();
            helperWriter = new PrintWriter(helper.getOutputStream(),true);
            helperReader = new BufferedReader(new InputStreamReader(helper.getInputStream()));
            
            System.out.println("Connected");
            
        } catch (IOException ex) {
            System.out.println("Error " + ex.getMessage());
//            Logger.getLogger(DistributedSemaphore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void V(){
        String message = name+","+clock.getTime()+"," + Message.REQ_V;
        System.out.println("time: " + clock.getTime());
        clock.tick();
        
        helperWriter.println(message);
        
    }
    
    public void P() throws IOException{
        String message = name+","+clock.getTime()+"," + Message.REQ_P;
        clock.tick();
        helperWriter.println(message);
        
        long t = Long.parseLong(helperReader.readLine());
        System.out.println("time: " + t);
        clock.compareAndSet(t);
        
    }
    
//    public void run(){
//        while(true);
//    }
}
