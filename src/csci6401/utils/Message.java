package csci6401.utils;

/**
 * This class models a messages that are sent by a distributed semaphore.
 * 
 * @author Daniel Ward
 * @date November 25, 2011
 */
public class Message {
    
    /**
     * Some message constants to represent  type of messages sent
     */
    public static final String ACK = "a";
    public static final String VOP = "v";
    public static final String POP = "p";
    public static final String REQ_P = "rp";
    public static final String REQ_V = "rv";
    public static final String CLOSE = "c";
    
    
    private int numberOfACKs; //the number of acknowledgements for this Message
    private long timeStamp; //the timestamp associated with this Message
    private String message; //the type of this Message
    private String sender; //the sender of this Message

    /**
     * Creates a new Message with the given components. A Message is immutable
     * after creation.
     * 
     * @param sender The sender of this Message
     * @param message The type of Message, must be one of the provided constants
     * @param timeStamp The time the message was sent
     */
    public Message(String sender, String message, long timeStamp) {
        this.numberOfACKs = 0;
        this.sender = sender;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    /**
     * Get the kind of message that this Message contains
     * 
     * @return The kind of this message, will be one of the provided constants
     *         of Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the sender of this Message.
     * 
     * @return The String representation of this sender. 
     */
    public String getSender() {
        return sender;
    }

    /**
     * Get the time that this Message was sent.
     * 
     * @return The time that this Message was sent.
     */
    public long getTimeStamp() {
        return timeStamp;
    }
    
    /**
     * Mark this Message as acknowledged.
     */
    public void acknowledged(){
        this.numberOfACKs++;
    }

    /**
     * Get the number of times this message was acknowledged
     * 
     * @return The number of acknowledgments of this Message, result >= 0 
     */
    public int getNumberOfACKs() {
        return numberOfACKs;
    }    
    
    /**
     * Get a String representation of this Message
     * 
     * @return Will return a String in the format of:
     *         {time: this.getTimeStamp(), message: this.getMessage(), sender: this.getSender()}
     */
    public String toString(){
        return "{time: " + timeStamp + ", message: " + message + ", sender: " + sender + ", acks: " + numberOfACKs +"}";
    }
}
