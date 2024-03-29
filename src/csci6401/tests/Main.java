package csci6401.tests;


import csci6401.distributedsemaphor.DistributedSemaphore;
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

    public static void main(String[] args) {
        try {
            String[][] config = {{"localhost", "5001"}, {"localhost", "5002"}};
            DistributedSemaphore sem = new DistributedSemaphore("" + new Random().nextInt(1000), config);

            Thread.sleep(1500);


            System.out.println("Getting P");

            sem.P();

            System.out.println("Got P");

            Thread.sleep(8000);

            System.out.println("Releasing V");

            sem.V();

            System.out.println("Getting P");
Thread.sleep(2000);
            
            sem.P();

            System.out.println("Got P");

            System.out.println("Releaseing V");

            sem.V();

        } catch (InterruptedException ex) {
        } catch (IOException ex) {
            Logger.getLogger(Main1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
