package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInGUI extends JFrame {

    private JPanel panel1;
    private JTextField emailTextField;
    private JTextField nameTextField;
    private JButton chooseFileButton;
    private JButton registerButton;
    private JButton backButton;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JTextField photoTextField;

    public SignInGUI(){
        super("Sign In ARSM");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel1);
        this.pack();

        passwordField1.setToolTipText("Password must contain at least 8 characters");
        passwordField2.setToolTipText("Password must be identical");
        photoTextField.setFocusable(false);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        chooseFileButton.addActionListener(new ActionListener() {
            String path;
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd=new java.awt.FileDialog((java.awt.Frame) null);
                fd.setVisible(true);
                String returnDir = fd.getDirectory();
                String returnFile = fd.getFile();

                Pattern pat=Pattern.compile(".jpg|.jpeg|.png");
                if(returnFile!=null){
                    Matcher matcher=pat.matcher(returnFile);
                    if(matcher.find()) {
                       path=returnDir+returnFile;
                       photoTextField.setText(path);
                   }else{
                       JOptionPane optionPane = new JOptionPane("File isn't an image", JOptionPane.ERROR_MESSAGE);
                       JDialog dialog = optionPane.createDialog("Failure");
                       dialog.setAlwaysOnTop(true);
                       dialog.setVisible(true);
                    }
                }else{
                    JOptionPane optionPane = new JOptionPane("File musn't be void", JOptionPane.ERROR_MESSAGE);
                    JDialog dialog = optionPane.createDialog("Failure");
                    dialog.setAlwaysOnTop(true);
                    dialog.setVisible(true);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame frame = new ApplicationGUI("Login ARSM");
                frame.setSize(500, 500);
                frame.setVisible(true);
                frame.setResizable(true);
            }
        });
    }
}
