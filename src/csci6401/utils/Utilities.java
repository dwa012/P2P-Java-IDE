/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package csci6401.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author daniel
 */
public class Utilities {
    public static String[][] readConfiguratoinFile() throws FileNotFoundException{
        File file = null;
        
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(new JFrame("Select the configuration file"));
        
        if(returnVal == JFileChooser.APPROVE_OPTION){
            file = chooser.getSelectedFile();
        }
        
        Scanner scanner = new Scanner(file);
        
        int counter = 0;
        
        while(scanner.hasNextLine()){
            counter ++;
            scanner.nextLine();
        }
        
        String[][] config = new String[counter][2];
        
        scanner.reset();
        
        counter = 0;
        
        while(scanner.hasNextLine()){
            config[counter][0] = scanner.next();
            config[counter][1] = scanner.next();
            scanner.nextLine();
            counter++;
        }
        
        return config;
    }
}
