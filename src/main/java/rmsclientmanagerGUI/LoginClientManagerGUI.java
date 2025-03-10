package rmsclientmanagerGUI;

import rmsclientmanager.ClientManager;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Locale;

public class LoginClientManagerGUI extends JFrame {
    private JPanel mainPanel;
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signInButton;
    private JLabel image;
    private JButton exitButton;

    public LoginClientManagerGUI(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        Image icon = Toolkit.getDefaultToolkit().getImage("src/main/java/rmsclientmanagerGUI/logoapp.jpeg");
        this.setIconImage(icon);
        passwordField.setToolTipText("Password must contain at least 8 characters");

        emailTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                emailTextField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (emailTextField.getText().equals(""))
                    emailTextField.setText("insert your email");
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = String.valueOf(passwordField.getPassword());
                if ((email.equals("") || email.equals("insert your email")) && password.equals(""))
                    JOptionPane.showMessageDialog(null, "Incorrect login\nthere aren't email and password\nplease retype");
                else if (email.equals("") || email.equals("insert your email"))
                    JOptionPane.showMessageDialog(null, "Incorrect login\nthere isn't email\nplease retype");
                else if (password.equals(""))
                    JOptionPane.showMessageDialog(null, "Incorrect login\nthere isn't password\nplease retype");
                else {

                    //JFrame RMSFrame = new ManageRMSGUI(email,image);
                    //RMSFrame.setSize(500,500);
                    //RMSFrame.setVisible(true);
                    //RMSFrame.setResizable(true);
                    try {
                        ClientManager clientManager = new ClientManager(email);
                        int value = clientManager.login(email, password);
                        if (value == 0) {
                            dispose();
                            ManagerClientGUI manager = new ManagerClientGUI(clientManager);
                            manager.setUser(email);
                            manager.setVisible(true);
                            Thread t = new Thread(clientManager);
                            t.start();
                            clientManager.setTmonitoring(t);
                        } else if (value == 1) {
                            JFrame f = new JFrame();
                            JOptionPane.showMessageDialog(f, "Incorrect user or password.Retry", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }


                }
            }
        });
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame frame2 = new SignInGUI();
                frame2.setSize(500, 500);
                frame2.setVisible(true);
                frame2.setResizable(true);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });

    }

    public static void main(String[] args) {
        JFrame frame = new LoginClientManagerGUI("Login Client Manager RMS");
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setResizable(true);
    }

    private void createUIComponents() {
        //modifica con il buffered image
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/logoapp.jpeg").getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        image = new JLabel(imageIcon);
    }

}
