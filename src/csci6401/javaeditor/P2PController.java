/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csci6401.javaeditor;

import cscsi6401.distributedsemaphor.DistributedSemaphore;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;

/**
 *
 * @author daniel
 */
public class P2PController{
    
    private DistributedSemaphore semaphore;
    private String[][] configuration;
    private ProgressDialog progressDialog;
    private GUI parentView;
    
    private ServerSocket serverSocketForNodes;
    
    private BufferedReader[] inputs;
    private PrintWriter[] outputs;
    
    public P2PController(GUI parentView, final String[][] configuration) throws IOException {

        this.parentView = parentView;
        this.configuration = configuration;

        inputs = new BufferedReader[configuration.length-1];
        outputs = new PrintWriter[configuration.length-1];
        
        progressDialog = new ProgressDialog(parentView, "Connecting to other editors", null, true);

        new Thread() {

            public void run() {
                try {
                    P2PController.this.semaphore = new DistributedSemaphore(new Random(System.currentTimeMillis()).nextInt(4000)+"", configuration);
                    
                    P2PController.this.serverSocketForNodes = semaphore.getServerSocket();
                    
                    Thread one = createInputStreamsToPeers();
                    Thread two = createOutputStreamsToPeers();
                    
                    one.start();
                    two.start();
                    
                    one.join();
                    two.join();
                    
                    P2PController.this.progressDialog.dispose();
                } catch (InterruptedException ex) {
                    Logger.getLogger(P2PController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(P2PController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();

        progressDialog.setVisible(true);
    }
    
    public void requestP(){
            
        progressDialog = new ProgressDialog(parentView, "Requesting permission", null, true);
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    P2PController.this.semaphore.P();
                    P2PController.this.progressDialog.dispose();
                } catch (IOException ex) {
                   System.err.println("error reading the request from the semaphore");
                }
            }
        };
        
        t.start();

        progressDialog.setVisible(true);
    }
    
    public void requestV(){        
        semaphore.V();
    }
    
    public void sendData(String data){
        String[] s = data.split("\n");
        for (PrintWriter writer : outputs) {   
            writer.println("\0");
            for (int i = 0; i < s.length; i++) {
                
                if(i > 0)
                    writer.println("");
                
                if(s[i].length() > 0)
                    writer.println(s[i]);
                else
                    writer.println("");
            }
            
        }
    }
    
    private GUI getGUIParent(){
        return parentView;
    }
    
    private Thread createOutputStreamsToPeers(){
        Thread result = new Thread(){
            public void run(){
                int index = 1;
                while (index < configuration.length) {
                    try {
                        outputs[index-1] = new PrintWriter(serverSocketForNodes.accept().getOutputStream(),true);
                        index++;
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                    }
                }//end while
            }
        };
        
        return result;
    }
    
    private Thread createInputStreamsToPeers() {
        Thread result = new Thread() {

            public void run() {
                int index = 1;
                while (index < configuration.length) {
                    try {
                        String address = configuration[index][0];
                        int port = Integer.parseInt(configuration[index][1]);
                        Socket socket = new Socket(address,port);
                        new InputStreamListener(socket).start();
                        index++;
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                    }
                }//end while
            }
        };

        return result;
    }
        
    private class InputStreamListener extends Thread{
        
        private BufferedReader input;
        
        public InputStreamListener(Socket socket) throws IOException{
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        
        public void run(){
            while(true){
                try {
                    
//                    StringBuilder message = new StringBuilder(1024);
//                    char[] buffer = new char[1024];
//                    int read = 0;
//                    String s = "";
                    
//                    while ((s = input.readLine()) != null) {                        
//                        message.append(s);
//                        message.append("\n");
//                        System.out.println(message.toString());
//                                
//                    }
                    
//                    do { 
//                        read = input.read(buffer, 0, buffer.length);
//
//                        for (int i = 0; i < read; i++) {
//                            message.append(buffer[i]);
//                        }
//                        
//                    } while (read != -1);
//                    
//                   
//                    P2PController.this.getGUIParent().getEditorPane().setText(message.toString());
                    P2PController.this.getGUIParent().getEditorPane().setCaretPosition(P2PController.this.getGUIParent().getEditorPane().getDocument().getLength()); 
                    
                    String i = input.readLine();
                    
                    if (i.equals("\0")) {
                        P2PController.this.getGUIParent().getEditorPane().setText("");
                        P2PController.this.getGUIParent().getEditorPane().setCaretPosition(P2PController.this.getGUIParent().getEditorPane().getDocument().getLength());
                    } else {


                        if (i.length() == 0) {
                            i = "\n";
                        }

                        P2PController.this.getGUIParent().getEditorPane().getDocument().insertString(P2PController.this.getGUIParent().getEditorPane().getCaretPosition(), i, null);
                        P2PController.this.getGUIParent().getEditorPane().setCaretPosition(P2PController.this.getGUIParent().getEditorPane().getDocument().getLength());
                    }
                    //setText(input.readLine());
                } catch (BadLocationException ex) {
                    Logger.getLogger(P2PController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    System.err.println("ERROR: " + ex.getMessage());
                }
                
            }
        }
        
    }
}
