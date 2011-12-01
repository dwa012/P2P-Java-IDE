package csci6401.tests;


import csci6401.javaeditor.SystemCalls;
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
public class Systest {
    public static void main(String[] args){
        try {
            String s = SystemCalls.compileJavaFile("/home/daniel/Desktop/Box.java");
            System.out.println(s);
        } catch (IOException ex) {
            Logger.getLogger(Systest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
