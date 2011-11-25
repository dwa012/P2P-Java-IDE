/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cscsi6401.distributedsemaphor;

/**
 *
 * @author daniel
 */
class Message {
    
    public static final String ACK = "ack";
    public static final String VOP = "vop";
    public static final String POP = "pop";
    public static final String REQ_P = "reqP";
    public static final String REQ_V = "reqV";
    
    
    private int numberOfACKs;
    
    private long timeStamp;
    private String message;
    private String sender;

    
    public Message(String sender, String message, long timeStamp) {
        this.numberOfACKs = 0;
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
    
    public void ack(){
        this.numberOfACKs++;
    }

    public int getNumberOfACKs() {
        return numberOfACKs;
    }    
    
}
