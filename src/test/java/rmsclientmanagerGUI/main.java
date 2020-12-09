
package rmsclientmanagerGUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author L.RUSSO62
 */
public class main {
    public static void main(String[] args){
        ManagerClient client=new ManagerClient();
        client.setVisible(true);
        int a=0;
       JFrame f=new JFrame();
        while(a==JOptionPane.YES_OPTION){
            String device=JOptionPane.showInputDialog(f,"Enter index device");
            String ora=JOptionPane.showInputDialog(f,"Ora"); 
            String temperature=JOptionPane.showInputDialog(f,"Enter Temperature"); 
            String Cpuload=JOptionPane.showInputDialog(f,"Enter CPuLoad"); 
            String Cpuvoltage=JOptionPane.showInputDialog(f,"Enter Cpu Voltage"); 
            String power=JOptionPane.showInputDialog(f,"Enter Power");
            client.getDeviceTemperature().get(Integer.parseInt(device))
                   .setDataSetVale(Double.parseDouble(temperature));
            client.getDeviceCpuLoad().get(Integer.parseInt(device))
                    .setDataSetValue(Double.valueOf(Cpuload),"CPU Load", ora);
            client.getDeviceCpuVoltage().get(Integer.parseInt(device))
                    .setDataSetVale(Double.parseDouble(Cpuvoltage));
            client.getDevicePower().get(Integer.parseInt(device))
                    .setDataSetVale(Double.parseDouble(power));
            a=JOptionPane.showConfirmDialog(f,"Do you want insert other date?"); 
        }
    }
}
