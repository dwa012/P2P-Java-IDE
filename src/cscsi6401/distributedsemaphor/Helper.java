package cscsi6401.distributedsemaphor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
class Helper extends Thread {

    private LogicalClock clock;
    private int s;
    private StreamListener[] listeners;
    private PrintWriter[] outputStreams;
    private StreamListener parentListener;
    private PrintWriter parentWriter;
    ArrayList<Message> queue;
    String parentName;

    public Helper(String parentName, Socket[] inputs, Socket[] outputs, int parentPort) {

        s = 1;
        clock = new LogicalClock();
        this.parentName = parentName;
        queue = new ArrayList<Message>();
        outputStreams = new PrintWriter[inputs.length];

        this.connectToParent(parentPort);

        this.createStreamsToNodes(inputs, outputs);
    }

    private void connectToParent(int parentPort) {
        Socket parent = null;
        do {
            try {
                parent = new Socket("localhost", parentPort);
                new StreamListener(parent, this, clock).start();
                //parentListener.start();
                parentWriter = new PrintWriter(parent.getOutputStream(), true);
            } catch (UnknownHostException ex) {
//                System.out.println("UHost_Error " + ex.getMessage());
//                Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
//                System.out.println( "IOError " + ex.getMessage());
//                Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (parent == null);
    }

    private void createStreamsToNodes(Socket[] inputs, Socket[] outputs) {
        for (int i = 0; i < inputs.length; i++) {
            try {
                //listeners[i] = 
                new StreamListener(inputs[i], this, clock).start();
                //listeners[i].start();
                outputStreams[i] = new PrintWriter(outputs[i].getOutputStream(), true);
            } catch (IOException ex) {
//                System.out.println("Error " + ex.getMessage());
//                Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @require The message array be formatted as follows:
     *           message[0] = timestamp
     *           message[1] = message kind (POP,VOP,ACK)
     *          [message[2] = POP || VOP
     *           message[3] = sender of POP || VOP]
     * 
     * @param message 
     */
    public synchronized void broadcast(String... message) {
        String toSend = "";



        for (String string : message) {
            toSend += string + ",";
        }

//        System.out.println("Broadcast: " + toSend);

        for (PrintWriter writer : outputStreams) {
            writer.println(toSend);
        }

        clock.tick();
    }

    public synchronized void ack(String[] message) {
        /*
         * message[0] = sender message[1] = timestamp message[2] = kind if
         * message[2] == ack message[3] = kind of ack message[4] = sender of
         * original message
         */

        //record the ack
        for (int i = 0; i < queue.size(); i++) {
            Message mess = queue.get(i);
//            System.out.println(message[4]);
//            System.out.println(message[3]);
            if (mess.getSender().equals(message[4])
                    && mess.getMessage().equals(message[3])) {
                mess.ack();
//                System.out.println("acknowledge " + mess);
                //end loop early
                i = queue.size();
            }
        }

        //look for fully acknowledged acks
        for (int i = 0; i < queue.size(); i++) {
            Message mess = queue.get(i);

            if (mess.getMessage().equals(Message.VOP)) {
                if (mess.getNumberOfACKs() == outputStreams.length) {
//                   System.out.println("acknowledge");
                    queue.remove(i);
                    s++;
                }
            }
        }
        
        for (int i = 0; i < queue.size(); i++) {
            Message mess = queue.get(i);
            if (mess.getMessage().equals(Message.POP)) {
                if (mess.getNumberOfACKs() == outputStreams.length && s > 0) {
//                    System.out.println("acknowledge");
                    if (mess.getSender().equals(parentName)) {
                        parentWriter.println(clock.getTime() + "");
                        clock.tick();
                    }

                    queue.remove(i);
                    s--;
                }
            }


        }

//        System.out.println("s value: " + s);

        //find the messages with the proper number of acks and
        //remove them

        //adjust the s value
    }

    public synchronized void requestP() {
        broadcast(parentName, clock.getTime() + "", Message.POP);
        clock.tick();
    }

    public synchronized void requestV() {
        broadcast(parentName, clock.getTime() + "", Message.VOP);
        clock.tick();
    }

    /**
     * @require The message array be formatted as follows:
     *           message[0] = sender
     *           message[1] = timestamp
     *           message[2] = message kind POP || VOP
     * @param message 
     */
    public synchronized void op(String... message) {
        //add to queue
        queue.add(new Message(message[0], message[2], Long.valueOf(message[1])));

        //resort the list
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

//        System.out.println(queue);

        //broadcast the ack
        broadcast(parentName, clock.getTime() + "", Message.ACK, message[2], message[0]);
    }

    @Override
    public void run() {
        while (true);
    }
}
