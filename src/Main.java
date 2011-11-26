
import cscsi6401.distributedsemaphor.DistributedSemaphore;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * configuration data.
 * the first entry will be my own self
 * the port is the listening port
 * @author daniel
 */
public class Main {
    public static void main(String[] args){
        String[][] config = {{"localhost","5001"},{"localhost","5002"}};
        DistributedSemaphore sem = new DistributedSemaphore(""+new Random().nextInt(1000), config,4800);

        
         try {
            Thread.sleep(1500);


            System.out.println("Getting P");

            sem.P();
            
            System.out.println("Got P");
            
            Thread.sleep(3000);
            
            System.out.println("Releasing V");
            
            sem.V();

        } catch (InterruptedException ex) {
        } catch (IOException ex) {
            Logger.getLogger(Main1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
