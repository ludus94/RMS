package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageRMSGUI extends JFrame {

    private JButton shutDownButton;
    private JButton restartButton;
    private JTextField userField;
    private JTextField devicesField;
    private JLabel image;
    private JPanel graphicPanel;
    private JPanel panel2;
    private JButton backButton;
    private JComboBox comboBox1;

    public ManageRMSGUI(String email,JLabel icon){
        super("Manage RMS");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel2);
        this.pack();

        userField.setEnabled(false);
        userField.setText(email);
        image.setEnabled(false);
        //image.setIcon((Icon) icon);
        devicesField.setEnabled(false);
        devicesField.setText("Available Devices, click one button for display graphics");

        JFrame graphicsFrame=new graphicGUI();
        graphicsFrame.setVisible(true);
        graphicsFrame.setResizable(true);
        graphicPanel.add(graphicsFrame);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        shutDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
