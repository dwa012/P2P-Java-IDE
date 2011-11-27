package cscsi6401.distributedsemaphor.helper;

import csci6401.utils.LogicalClock;
import csci6401.utils.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This class provides a Thread subclass that allows a Helper class to listen
 * to a InputStream from a Socket. This provides the means to concurrently
 * listen to a Socket InputStream.
 * 
 * @author Daniel Ward
 * @date November 25, 2011
 */
class HelperStreamListener extends Thread {

    private LogicalClock clock; //a reference to the parent Helper's clock
    private BufferedReader reader; //the Reader class used to read the stram
    private Helper parent; //a reference to the parent helper

    /**
     * Creates a new HelperStreamListener to listen to the given Socket's 
     * InputStream.
     * 
     * @param socket The Socket to extract the InputStream from
     * @param parent The parent Helper that spawned this Listener
     * @param clock A reference to the parent Helper's logical clock
     * @throws IOException Thrown if the stream cannot be opened for reading
     */
    public HelperStreamListener(Socket socket, Helper parent, LogicalClock clock) throws IOException {
        this.clock = clock;
        this.parent = parent;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * The overriden method form Thread.
     * 
     * This runs in an indefinite while loop, which reads from the stream, and
     * calls upon the appropriate method of the parent Helper.
     */
    @Override
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
                
                //update the clock with the recieved timestamp
                clock.compareAndSet(Long.valueOf(message[1]));                
                
                //see what kind of message was recieved
                if(message[2].equals(Message.ACK)){
                    parent.ack(message);
                }
                
                else if(message[2].equals(Message.VOP)){
                    parent.op(message);
                }
                
                else if(message[2].equals(Message.POP)){
                    parent.op(message);
                }
                
                else if(message[2].equals(Message.REQ_P)){
                    parent.requestP();
                }
                
                else if(message[2].equals(Message.REQ_V)){
                    parent.requestV();
                }
                //end message check                
                
            } catch (IOException ex) {
//                Logger.getLogger(HelperStreamListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//end while
    }//end run
}
