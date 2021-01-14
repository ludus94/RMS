package rmsclientGUI;

import rmsclient.Client;

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
    private JTextField jsurnametextField;
    private Client client;
    private String extension;
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
                String email=emailTextField.getText();
                String name=nameTextField.getText();
                String surname=jsurnametextField.getText();
                String password1=String.valueOf(passwordField1.getPassword());
                String password2=String.valueOf(passwordField2.getPassword());
                String path=photoTextField.getText();
                if(password1.length()<=8 && password2.length()<=8){
                    client=new Client();
                    int value=client.sigin(email,password1,password2,name,surname,path,extension);
                    if(value==0){
                        JFrame frameoption=new JFrame();
                        JOptionPane.showMessageDialog(frameoption,"User registered with success");
                        dispose();
                        JFrame frame = new LoginClientGUI("Login ARSM");
                        frame.setSize(500, 500);
                        frame.setVisible(true);
                        frame.setResizable(true);
                    }
                    if(value==2){
                        JFrame frameoption=new JFrame();
                        JOptionPane.showMessageDialog(frameoption,"User all ready existis in system");
                        dispose();
                        JFrame frame = new LoginClientGUI("Login ARSM");
                        frame.setSize(500, 500);
                        frame.setVisible(true);
                        frame.setResizable(true);
                    }
                }

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
                       extension=returnFile.substring(returnFile.length()-4,returnFile.length());
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
                JFrame frame = new LoginClientGUI("Login ARSM");
                frame.setSize(500, 500);
                frame.setVisible(true);
                frame.setResizable(true);
            }
        });
    }

}
