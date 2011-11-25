/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cscsi6401.distributedsemaphor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author daniel
 */
class StreamListener extends Thread {

    private LogicalClock clock;
    private BufferedReader reader;
    private Helper parent;

    public StreamListener(Socket socket, Helper parent, LogicalClock clock) throws IOException {
        this.clock = clock;
        this.parent = parent;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run() {
        while (true) {
            try {
                
                //read the message form the stream
                String read = reader.readLine();
                
                /*
                 * message[0] = sender
                 * message[1] = timestamp
                 * message[2] = kind
                 * if message[2] == ack
                 *  message[3] = kind of ack
                 *  message[4] = sender of original message
                 */
                String[] message = read.split(",");
                
                //update the clock
                clock.compareAndSet(Long.valueOf(message[1]));                
                
                //see what kind of message was recieved
                if(message[2].equals(Message.ACK)){
                    parent.ack(message);
                }
                
                if(message[2].equals(Message.VOP)){
                    parent.op(message);
                }
                
                if(message[2].equals(Message.POP)){
                    parent.op(message);
                }
                
                if(message[2].equals(Message.REQ_P)){
                    parent.requestP();
                }
                
                if(message[2].equals(Message.REQ_V)){
                    parent.requestV();
                }
                
                
            } catch (IOException ex) {
//                Logger.getLogger(StreamListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//end while
    }//end run
}
