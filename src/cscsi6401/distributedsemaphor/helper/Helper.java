package cscsi6401.distributedsemaphor.helper;

import csci6401.utils.LogicalClock;
import csci6401.utils.Message;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class models a Helper process for a DistributedSemaphore object.
 * This class will talk to other Helper objects in a peer-to-peer manner.
 * 
 * @author Daniel Ward
 * @date November 25, 2011
 */
public class Helper extends Thread {

    private LogicalClock clock; //the logocal clock for this helper
    private int semaphore; //the semaphore value 
    
    private PrintWriter[] outputStreams; //streams to write to all the connected nodes
    private PrintWriter parentWriter; //a writer to send messages to the parent process
    
    ArrayList<Message> queue; //a message queue, will be useded as a priority queue.
                              //the queue will have the lowest timestamp value at
                              //the head
    String parentName; //the name of the parent object that created this helper

    /**
     * Will create a new Helper object with the given parameters.
     * 
     * During construction of this Helper, the connection to the parent Object
     * and the other nodes.
     * 
     * @param parentName The unique name of the parent DistributedSemaphore object
     * @param inputs The array of Sockets to be used as InputStreams
     * @param outputs The array of Sockets to be used as OutputStreams
     * @param parentPort The port of the parent DistributedSemaphore that this
     *                   object will talk to on
     */
    public Helper(String parentName, Socket[] inputs, Socket[] outputs, int parentPort) {

        semaphore = 1;
        clock = new LogicalClock();
        this.parentName = parentName;
        
        queue = new ArrayList<Message>();
        outputStreams = new PrintWriter[outputs.length];

        this.connectToParent(parentPort);

        this.createStreamsToNodes(inputs, outputs);
    }

    /**
     * Will broadcast the given message to all the other connected nodes.
     * 
     * @param message The message array be formatted as follows:
     *                  message[0] = timestamp
     *                  message[1] = message kind (POP,VOP,ACK)
     *                  if message[1] == Message.ACK
     *                       message[2] = POP || VOP
     *                       message[3] = sender of POP || VOP
     */
    synchronized void broadcast(String... message) {
        String toSend = "";

        //build the message string to send
        for (String string : message) {
            toSend += string + ",";
        }

        //send the message string to the other nodes
        for (PrintWriter writer : outputStreams) {
            writer.println(toSend);
        }
    }

    /**
     * The handler for ACK messages.
     * 
     * @param message   message[0] = sender 
     *                  message[1] = timestamp 
     *                  message[2] = Message.ACK
     *                  message[3] = kind of acknowledged 
     *                  message[4] = sender of original message
     */
    synchronized void ack(String[] message) {

        //record the ACK message
        recordAck(message[3], message[4]);
        
        //check for fully acknowledged VOP messages
        checkFullyAckVOP();
        
        //check for fully acknowledged POP messages
        checkFullyAckPOP();
    }
    
    /**
     * The handler for POP messages
     */
    synchronized void requestP() {
        broadcast(parentName, clock.getTime() + "", Message.POP);
        clock.tick();
    }

    /**
     * The handler for VOP messages
     */
    synchronized void requestV() {
        broadcast(parentName, clock.getTime() + "", Message.VOP);
        clock.tick();
    }

    /**
     * The handler for VOP and POP messages.
     * 
     * @param message The message array be formatted as follows:
     *                  message[0] = sender
     *                  message[1] = timestamp
     *                  message[2] = message kind POP || VOP
     */
    synchronized void op(String... message) {
        //add the recieved messagte to the queue
        queue.add(new Message(message[0], message[2], Long.valueOf(message[1])));

        //resort the list by the timestamp of the messages
        Collections.sort(queue, new Comparator<Message>() {

            @Override
            public int compare(Message o1, Message o2) {
                int result = 0;

                if (o1.getTimeStamp() < o2.getTimeStamp()) {
                    result = 1;
                }
                if (o1.getTimeStamp() < o2.getTimeStamp()) {
                    result = -1;
                }

                return result;
            }
        });

        //broadcast the acknowledged message
        broadcast(parentName, clock.getTime() + "", Message.ACK, message[2], message[0]);
        clock.tick();
    }

    /**
     * The overriden method from Thread.
     * 
     * While cause the Helper to run indefinitly.
     */
    @Override
    public void run() {
        while (true);
    }
       
    /**
     * Check the queue for fully acknowledged VOP messages 
     */
    private void checkFullyAckVOP(){
        
        //look for fully acknowledged Message.VOP messages
        for (int i = 0; i < queue.size(); i++) {
            Message mess = queue.get(i);

            //if the message is a VOp message, then check the number of 
            //acknowledgments
            if (mess.getMessage().equals(Message.VOP)) {
                //if it has been fully acknowledged, then remove it from the queue
                if (mess.getNumberOfACKs() == outputStreams.length) {
                    queue.remove(i);
                    semaphore++;
                }//end if
            }//end if
        }//end if
    }
    
    /**
     * Check for fully acknowledged POP messages
     */
    private void checkFullyAckPOP(){
        //look for fully acknowledged Message.POP messages
        for (int i = 0; i < queue.size(); i++) {
            Message mess = queue.get(i);
            
            //if the message is a POP messages, then check the number of
            //acknowledgements
            if (mess.getMessage().equals(Message.POP)) {
                //if the message has been fully acknowledged and the semaphore
                //value is > 0, remove it from the queue.
                if (mess.getNumberOfACKs() == outputStreams.length && semaphore > 0) {
                    
                    //if the request was from my parent object, then notify
                    //them that they may proceed
                    if (mess.getSender().equals(parentName)) {
                        parentWriter.println(clock.getTime() + "");
                        clock.tick();
                    }

                    queue.remove(i);
                    semaphore--;
                }//end  number of acks and s > 0
            }//end message == Message.POP
        }//end for
    }
    
    /**
     * Create the streams to the other nodes.
     * 
     * @param inputs The Socket array to be used to create the InputStreams 
     *               from the other nodes.
     * @param outputs The Socket array to be used to create the OutputStreams
     *                to the other nodes.
     */
    private void createStreamsToNodes(Socket[] inputs, Socket[] outputs) {
        for (int i = 0; i < inputs.length; i++) {
            try {
                //create a new stream listener to listen to message from the
                //other nodes
                new HelperStreamListener(inputs[i], this, clock).start();
                //create the OutputStream to the other nodes
                outputStreams[i] = new PrintWriter(outputs[i].getOutputStream(), true);
            } catch (IOException ex) {
            }
        }
    }
    
    /**
     * Create a connection to the parent DistributedSemaphore object
     * 
     * @param parentPort The port that the parent object is listening on
     */
    private void connectToParent(int parentPort) {
        Socket parent = null;
        //keep trying to establish a connection to the parent object
        do {
            try {
                parent = new Socket("localhost", parentPort);
                new HelperStreamListener(parent, this, clock).start();
                parentWriter = new PrintWriter(parent.getOutputStream(), true);
            } catch (UnknownHostException ex) {
            } catch (IOException ex) {
            }
        } while (parent == null);
    }
    
    /**
     * Record a message as acknowledged.
     */
    private void recordAck(String messageKind, String sender){
        //record the acknowledgement of the message
        for (int i = 0; i < queue.size(); i++) {
            Message mess = queue.get(i);
            
            //if the message matches the given parameters then mark it as
            //acknowledged
            if (mess.getSender().equals(sender) && mess.getMessage().equals(messageKind)) {
                
                mess.acknowledged();//tell the message that it has an ACK                
                
                i = queue.size(); //end loop early
            }
        }//end loop
    }
}