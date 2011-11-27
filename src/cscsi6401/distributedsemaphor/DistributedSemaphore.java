package cscsi6401.distributedsemaphor;

import csci6401.utils.LogicalClock;
import csci6401.utils.Message;
import cscsi6401.distributedsemaphor.helper.Helper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * This class models a distributed semaphore.
 * 
 * @author Daniel Ward
 * @date November 25, 2011
 */
public class DistributedSemaphore{
    
    String name; //the "unique" name of this object
    
    LogicalClock clock; //the  clock for this object
    
    PrintWriter helperWriter; //the writer to send to the Helper object
    BufferedReader helperReader; //the reader to read from the Helper object
    
    Socket[] inputs; //the array of input Sockets from the other nodes
    Socket[] outputs; //the array of ouput Sockets to the other nodes
            
    /**
     * Creates a new DistributedSemaphor.
     * 
     * This object will create the connection to the other nodes.
     * This will cause the object o wait for all the other nodes to
     * connect to this.
     * 
     * The nodes are listed in the given array. The first row of the array
     * should be the information for this node. The address of this node 
     * can be left as "localhost".
     * 
     * The first column is the address, and the second column is the port number
     * that the node will be listening on.
     * 
     * @param semaphorName The name for this semaphore
     * @param configuration A multi dimensional array of configuration info
     *                      column[0] == ipv4 address
     *                      column[1] == port number
     * @throws IOException Will be thrown if the connections to the other nodes
     *                     cannot be established.
     */
    public DistributedSemaphore(String semaphorName, final String[][] configuration) throws IOException{
            name = semaphorName;
            clock = new LogicalClock();
            
            //creating connections to nodes
            createConnectionToNodes(configuration);
            
            //create a Helper and the connection to the helper
            createHelper(); 
    }
    
    /**
     * Provides the semaphore P operation.
     * 
     * This method will block until the requested P is granted.
     * 
     * @throws IOException Will be thrown if this object cannot read the
     *                     message to grant permission to proceed.
     */
    public void P() throws IOException{
        //format the message to send to the Helper
        String message = name+","+clock.getTime()+"," + Message.REQ_P;
        clock.tick();
        
        //send the message to the Helper
        helperWriter.println(message);
        
        //get the timestamp and the permission to proceed from the helper
        long t = Long.parseLong(helperReader.readLine());
        clock.compareAndSet(t);        
    }
    
    /**
     * Provides the semaphore V operation.
     * 
     * Allows the caller to release the semaphor
     */
    public void V(){
        //format the message to send to the Helper
        String message = name+","+clock.getTime()+"," + Message.REQ_V;
        clock.tick();
        
        //send the message to the Helper
        helperWriter.println(message);        
    }
    
    /**
     * Creates a connection to the Helper object used by this object.
     * 
     * @throws IOException Will be thrown if the connection to the Helper
     *                     cannot be created.
     */
    private void createHelper() throws IOException {
        int portForHelper = 0; //port to use with the Helper
        ServerSocket socketForHelper = null; //ServerSocket to connect to the Helper

        //try to pick a available port to use for communication to the Helper
        do {
            //use this try to catch an error if the port is not available
            try {
                
                //try to pick a port between 5000-8000
                do {
                    portForHelper = new Random(System.currentTimeMillis()).nextInt(8000);
                } while (portForHelper < 5000);

                //create a new ServerSocket with the selected port
                socketForHelper = new ServerSocket(portForHelper);
            } catch (IOException ex) {
            }
        } while (socketForHelper == null);

        //create the Helper object with the appropriate information
        new Helper(this.name, inputs, outputs, portForHelper).start();

        //connect to the Helper
        Socket helper = socketForHelper.accept();
        
        //create the streams to talk to the Helper
        helperWriter = new PrintWriter(helper.getOutputStream(), true);
        helperReader = new BufferedReader(new InputStreamReader(helper.getInputStream()));
    }//end createHelper
    
    /**
     * Creates the connections to the other nodes.
     * 
     * @param configuration The configuration data used to create the connections
     *                      to the other nodes
     * @throws IOException Will be thrown if the connections to the nodes cannot
     *                     be established.
     */
    private void createConnectionToNodes(final String[][] configuration) throws IOException {

        //get the numebr of nodes
        final int numberOfNodes = configuration.length;

        inputs = new Socket[numberOfNodes];
        outputs = new Socket[numberOfNodes];
        
        //create the ServerSocket to accept incoming connections from the other nodes
        final ServerSocket serverSocketForNodes = new ServerSocket(Integer.parseInt(configuration[0][1]));

        //create a thread to accept incoming connections from the other nodes
        Thread input = new Thread() {

            public void run() {
                int index = 0;
                while (index < numberOfNodes) {
                    try {
                        outputs[index] = serverSocketForNodes.accept();
                        index++;
                    } catch (IOException ex) {
                    }
                }//end while
            }//end run
        };//end input Thread

        //create a therad to create outbound connections to the other nodes
        Thread output = new Thread() {

            public void run() {
                int index = 0;
                while (index < numberOfNodes) {
                    try {
                        String address = configuration[index][0];
                        int port = Integer.parseInt(configuration[index][1]);
                        inputs[index] = new Socket(address, port);
                        index++;
                    } catch (UnknownHostException ex) {
                    } catch (IOException ex) {
                    }
                }//end while
            }//end run
        };//end ouput Thread

        //start the thread
        input.start();
        output.start();

        //wait for the two created threads to finish
        //This causes the blocking action of this object
        try {
            input.join();
            output.join();
        } catch (InterruptedException ex) {
        }
    }//end createConnectionsToNodes
}
