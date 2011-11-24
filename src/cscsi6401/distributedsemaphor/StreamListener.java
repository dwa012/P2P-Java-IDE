/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package cscsi6401.distributedsemaphor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author daniel
 */
public class StreamListener extends Thread {

    private BufferedReader reader;
    private Helper parent;

    public StreamListener(Socket socket, Helper parent) throws IOException {
        this.parent = parent;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run() {
        while (true) {

        }
    }
}
