package cscsi6401.distributedsemaphor;

/**
 * This class models a messages that are sent by a distributed semaphore.
 * 
 * @author Daniel Ward
 * @date November 25, 2011
 */
class Message {
    
    /**
     * Some message constants to represent  type of messages sent
     */
    public static final String ACK = "ack";
    public static final String VOP = "vop";
    public static final String POP = "pop";
    public static final String REQ_P = "reqP";
    public static final String REQ_V = "reqV";
    
    
    private int numberOfACKs; //the number of acknowledgements for this Message
    
    private long timeStamp; //the timestamp associated with this Message
    private String message; //t
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
    
    public String toString(){
        return "{time: " + timeStamp + ", message: " + message + ", sender: " + sender + "}";
    }
}
