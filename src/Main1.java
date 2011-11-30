
import cscsi6401.distributedsemaphor.DistributedSemaphore;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author daniel
 */
public class Main1 {
    public static void main(String[] args) {
        try {
            String[][] config = {{"localhost", "5002"}, {"localhost", "5001"}};
            DistributedSemaphore sem = new DistributedSemaphore("" + new Random().nextInt(100), config);
//        sem.start();
//        System.out.println("Connected");


            Thread.sleep(3000);


            System.out.println("Getting P");

            sem.P();

            System.out.println("Got P");

            System.out.println("Releaseing V");

            sem.V();

            System.out.println("Getting P");

            sem.P();
            Thread.sleep(3000);

            System.out.println("Got P");

            System.out.println("Releaseing V");

            sem.V();

        } catch (InterruptedException ex) {
        } catch (IOException ex) {
//            Logger.getLogger(Main1.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true);
    }
}
