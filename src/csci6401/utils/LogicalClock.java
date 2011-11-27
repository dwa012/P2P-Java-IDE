package csci6401.utils;

/**
 * This class models a logical clock
 * 
 * @author Daniel Ward
 * @date November 15, 2011
 */
public class LogicalClock {
    
    private long counter;//counter for the clock
    
    /**
     * Creates a new logical clock that starts at zero
     */
    public LogicalClock(){
        counter = 0;
    }
    
    /**
     * Get the current "time" of the clock
     * 
     * @return The current time of this clock
     */
    public synchronized long getTime(){
        return this.counter;
    }
    
    /**
     * Increment this clock
     */
    public synchronized void tick(){
        counter++;
    }
    
    /**
     * Will compare the given time to the current time.
     * If the current time is less that the given time,
     * then the clock will be set to the given time, then incremented.
     * 
     * @param timeToCompareAndSetWith Time to set and compare with
     */
    public synchronized  void compareAndSet(long timeToCompareAndSetWith){
        counter = Math.max(counter, timeToCompareAndSetWith + 1);
        counter = counter + 1;
    }
}
