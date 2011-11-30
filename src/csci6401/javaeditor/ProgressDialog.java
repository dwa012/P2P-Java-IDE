/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csci6401.javaeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author daniel
 */
public class ProgressDialog extends JDialog{
    
    final JDialog dlg = new JDialog(this, "Progress Dialog", true);
                JProgressBar dpb = new JProgressBar();
                
                  
    public ProgressDialog(JFrame frame, String title, String message, boolean indeterminate) {
        super(frame, title, true);

        dpb.setIndeterminate(true);
        
//        this.add(BorderLayout.SOUTH, button);
        this.add(BorderLayout.CENTER, dpb);
        this.add(BorderLayout.NORTH, new JLabel(message));
//        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
//        this.setModalityType(ModalityType.DOCUMENT_MODAL);
        this.setPreferredSize(new Dimension(350,30));
        this.setResizable(false);
        super.setLocationRelativeTo(frame);
        this.toFront();
        
        
        
        this.pack();
    }
}
