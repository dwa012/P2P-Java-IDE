/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csci6401.javaeditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author daniel
 */
public class SystemCalls {
    
    public static String compileJavaFile(String filePath) throws IOException{
        /*
         * code adopted from:
         * http://www.devdaily.com/java/edu/pj/pj010016
         */
        String result = "";
//        String stdIn = "";
        String stdErr = "";
        String s;
        
        String directory = filePath.substring(0, filePath.lastIndexOf(File.separator));
        
        Process p = Runtime.getRuntime().exec("javac -Xlint -d " + directory + " " + filePath);
            
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        // read the output from the command
//        System.out.println("Here is the standard output of the command:\n");
//        while ((s = stdInput.readLine()) != null){
//            stdIn += s + "\n";
//            
//        }
                
        // read any errors from the attempted command
//        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            stdErr += s + "\n";
        }
        
        if(stdErr.length() > 0)
            result = stdErr;
        else
            result = "Compiled Successfully";
            
        
        return result;
    }
    
}
