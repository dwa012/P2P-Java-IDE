/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cscsi6401.distributedsemaphor;

import java.util.Comparator;

/**
 *
 * @author daniel
 */
class Message implements Comparator<Message> {
    
    public static final String ACK = "ack";
    public static final String VOP = "vop";
    public static final String POP = "pop";
    public static final String REQ_P = "reqP";
    public static final String REQ_V = "reqV";
    
    private long timeStamp;
    private String message;
    private String sender;

    public Message(String sender, String message, long timeStamp) {
        this.sender = sender;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int compare(Message o1, Message o2) {
        int result = 0;
        if (o1.getTimeStamp() < o2.getTimeStamp()) {
            result = -1;
        }
        if (o1.getTimeStamp() < o2.getTimeStamp()) {
            result = 1;
        }
        return result;
    }
    
}
