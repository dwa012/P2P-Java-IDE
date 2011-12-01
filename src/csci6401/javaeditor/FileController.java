/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csci6401.javaeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author daniel
 */
public class FileController {
    String filePath;
    
    public FileController(){
        filePath = "";
    }
    
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
        
    public boolean fileOpen(){
        return !filePath.equals("");
    }
    
    public void writeToFile(String data) throws FileNotFoundException{
        PrintWriter writer = new PrintWriter(filePath);
        writer.print(data);
        writer.flush();
        writer.close();
    }
    
    public String readFile() throws FileNotFoundException{
        Scanner scanner = new Scanner(new File(filePath));
        StringBuilder result = new StringBuilder(2048);
        
        while(scanner.hasNextLine()){
            
            result.append(scanner.nextLine());
            
            if(scanner.hasNextLine())
                result.append("\n");
        }
        
        scanner.close();
        
        return result.toString();
    }
}
