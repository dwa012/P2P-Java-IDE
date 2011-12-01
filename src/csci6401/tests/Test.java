package csci6401.tests;


import csci6401.distributedsemaphor.DistributedSemaphore;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author daniel
 */
public class Test {

    static DistributedSemaphore sem1;
    static DistributedSemaphore sem2;
    static DistributedSemaphore sem3;
    static DistributedSemaphore sem4;

    public static void main(String[] args) {
        try {
//            testSingleNode();
//            testOneNodeActive();
//            testTwoNodesActive();
//            testTwoActiveNodes2();
//            testThreeActiveNodes();
            testFourActiveNodes();
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testSingleNode() throws IOException {

        String[][] config1 = {{"localhost", "5000"}};
        DistributedSemaphore sem1 = new DistributedSemaphore("test1", config1);

        //testing single Node with multiple P/V calls
        System.out.println("acquire 1");
        sem1.P();
        sem1.V();
        System.out.println("release");

        System.out.println("acquire 2");
        sem1.P();
        sem1.V();
        System.out.println("release");

        System.out.println("acquire 3");
        sem1.P();
        sem1.V();
        System.out.println("release");

        System.out.println("acquire 4");
        sem1.P();
        sem1.V();
        System.out.println("release");

        System.out.println("acquire 5");
        sem1.P();
        sem1.V();
        System.out.println("release");

        System.out.println("acquire 6");
        sem1.P();
        sem1.V();
        System.out.println("release");

        System.out.println("acquire 7");
        sem1.P();
        sem1.V();
        System.out.println("release");

        System.out.println("acquire 8");
        sem1.P();
        sem1.V();
        System.out.println("release");


        sem1.close();

        System.out.println("END OF SINGLE NODE TEST");
        System.out.println("------------------------------------------");
        System.out.println();

    }

    public static void testOneNodeActive() throws IOException {
        try {
            Thread one = new Thread() {

                public void run() {
                    try {
                        String[][] config1 = {{"localhost", "5001"}, {"localhost", "5002"}};
                        sem1 = new DistributedSemaphore("test1", config1);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            Thread two = new Thread() {

                public void run() {
                    try {
                        String[][] config2 = {{"localhost", "5002"}, {"localhost", "5001"}};
                        sem2 = new DistributedSemaphore("test2", config2);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            one.start();
            two.start();

            one.join();
            two.join();

            System.out.println("acquire 1");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 2");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 3");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 4");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 5");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 6");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 7");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 8");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            sem1.close();
            sem2.close();

            System.out.println("END OF TWO NODE ONE NODE ACTIVE TEST");
        } catch (InterruptedException ex) {
            System.err.println("ERROR: " + ex.getMessage());
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void testTwoNodesActive() throws IOException {
        try {
            Thread one = new Thread() {

                public void run() {
                    try {
                        String[][] config1 = {{"localhost", "5001"}, {"localhost", "5002"}};
                        sem1 = new DistributedSemaphore("test1", config1);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            Thread two = new Thread() {

                public void run() {
                    try {
                        String[][] config2 = {{"localhost", "5002"}, {"localhost", "5001"}};
                        sem2 = new DistributedSemaphore("test2", config2);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            one.start();
            two.start();

            one.join();
            two.join();

            System.out.println("acquire 1");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 2");
            sem2.P();
            sem2.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 3");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 4");
            sem2.P();
            sem2.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 5");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 6");
            sem2.P();
            sem2.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 7");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 8");
            sem2.P();
            sem2.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            sem1.close();
            sem2.close();

            System.out.println("END OF TWO NODE TEST");
        } catch (InterruptedException ex) {
            System.err.println("ERROR: " + ex.getMessage());
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void testTwoActiveNodes2() throws IOException {
        try {
            Thread one = new Thread() {

                public void run() {
                    try {
                        String[][] config1 = {{"localhost", "5001"}, {"localhost", "5002"}};
                        sem1 = new DistributedSemaphore("test1", config1);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            Thread two = new Thread() {

                public void run() {
                    try {
                        String[][] config2 = {{"localhost", "5002"}, {"localhost", "5001"}};
                        sem2 = new DistributedSemaphore("test2", config2);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            one.start();
            two.start();

            one.join();
            two.join();

            System.out.println("acquire 1");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 2");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 3");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 4");
            sem2.P();
            sem2.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 5");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 6");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 7");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 8");
            sem2.P();
            sem2.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            sem1.close();
            sem2.close();

            System.out.println("END OF TWO NODE  2 TEST");
            System.out.println("------------------------------------------");
            System.out.println();
        } catch (InterruptedException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void testThreeActiveNodes() throws IOException {
        try {
            Thread one = new Thread() {

                public void run() {
                    try {
                        String[][] config1 = {{"localhost", "5001"}, {"localhost", "5002"}, {"localhost", "5003"}};
                        sem1 = new DistributedSemaphore("test1", config1);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            Thread two = new Thread() {

                public void run() {
                    try {
                        String[][] config2 = {{"localhost", "5002"}, {"localhost", "5001"}, {"localhost", "5003"}};
                        sem2 = new DistributedSemaphore("test2", config2);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            Thread three = new Thread() {

                public void run() {
                    try {
                        String[][] config2 = {{"localhost", "5003"}, {"localhost", "5001"}, {"localhost", "5002"}};
                        sem3 = new DistributedSemaphore("test2", config2);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            one.start();
            two.start();
            three.start();

            one.join();
            two.join();
            three.join();

            System.out.println("acquire 1");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 2");
            sem3.P();
            sem3.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 3");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 4");
            sem2.P();
            sem2.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 5");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 6");
            sem3.P();
            sem3.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 7");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 8");
            sem2.P();
            sem2.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            sem1.close();
            sem2.close();
            sem3.close();

            System.out.println("END OF THREE NODE TEST");
            System.out.println("------------------------------------------");
            System.out.println();
        } catch (InterruptedException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void testFourActiveNodes() throws IOException {
        try {
            Thread one = new Thread() {

                public void run() {
                    try {
                        String[][] config1 = {{"localhost", "5001"}, {"localhost", "5002"}, {"localhost", "5003"}, {"localhost", "5004"}};
                        sem1 = new DistributedSemaphore("test1", config1);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            Thread two = new Thread() {

                public void run() {
                    try {
                        String[][] config2 = {{"localhost", "5002"}, {"localhost", "5001"}, {"localhost", "5003"}, {"localhost", "5004"}};
                        sem2 = new DistributedSemaphore("test2", config2);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            Thread three = new Thread() {

                public void run() {
                    try {
                        String[][] config2 = {{"localhost", "5003"}, {"localhost", "5001"}, {"localhost", "5002"}, {"localhost", "5004"}};
                        sem3 = new DistributedSemaphore("test2", config2);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            
            Thread four = new Thread() {

                public void run() {
                    try {
                        String[][] config2 = {{"localhost", "5004"}, {"localhost", "5001"}, {"localhost", "5002"}, {"localhost", "5003"}};
                        sem4 = new DistributedSemaphore("test2", config2);
                    } catch (IOException ex) {
                        System.err.println("ERROR: " + ex.getMessage());
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            one.start();
            two.start();
            three.start();
            four.start();

            one.join();
            two.join();
            three.join();
            four.join();

            System.out.println("acquire 1");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 2");
            sem3.P();
            sem3.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 3");
            sem4.P();
            sem4.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 4");
            sem2.P();
            sem2.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 5");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 6");
            sem3.P();
            sem3.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 7");
            sem4.P();
            sem4.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            System.out.println("acquire 8");
            sem2.P();
            sem2.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);
            
            System.out.println("acquire 9");
            sem2.P();
            sem2.V();
            System.out.println("release");
            
            Thread.currentThread().sleep(1000);
            
            System.out.println("acquire 10");
            sem1.P();
            sem1.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);
            
            System.out.println("acquire 11");
            sem3.P();
            sem3.V();
            System.out.println("release");
            
            Thread.currentThread().sleep(1000);
            
            System.out.println("acquire 12");
            sem4.P();
            sem4.V();
            System.out.println("release");

            Thread.currentThread().sleep(1000);

            

            sem1.close();
            sem2.close();
            sem3.close();
            sem4.close();
            

            System.out.println("END OF FOUR NODE TEST");
            System.out.println("------------------------------------------");
            System.out.println();
        } catch (InterruptedException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
