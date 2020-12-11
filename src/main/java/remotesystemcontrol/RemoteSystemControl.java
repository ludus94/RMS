package remotesystemcontrol;

import javax.swing.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class RemoteSystemControl {
    private static String os = System.getProperty("os.name").toLowerCase();
    private String sudopassword;
    private WindowRemoteControll wrc;
    private MacRemoteControll mrc;
    private RasberryRemoteControll urm;
    public RemoteSystemControl(){
        if(isWindows())
            wrc=new WindowRemoteControll();
        else if (isMac() || isUbuntu()){
            mrc =new MacRemoteControll();
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Enter a password:");
            JPasswordField pass = new JPasswordField(20);
            panel.add(label);
            panel.add(pass);
            String[] options = new String[]{"OK", "Cancel"};
            int option = JOptionPane.showOptionDialog(null, panel, "Enter super user password",
                    JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
            if(option == 0) // pressing OK button
            {
                char[] password = pass.getPassword();
                sudopassword=new String(password);
            }
        }else{
            urm=new RasberryRemoteControll();
        }
    }
    public void shutDown(){
        if(isWindows()){
            wrc.shutDown();
        }else if(isMac() || isUbuntu()){
            mrc.shutDown(this.sudopassword);
        }else{
            urm.shutDown();
        }
    }
    public void reboot(){
        if(isWindows()){
            wrc.reboot();
        }else if(isMac() || isUbuntu()){
            mrc.reboot(this.sudopassword);
        }else{
            urm.shutDown();
        }
    }
    public String killProcessNameProcess(String nameprocess){
        if (isWindows()){
            return wrc.killProcessNameProcess(nameprocess);
        }
        return "Action not available";
    }
    public String killProcessPID(String pid){
        if (isWindows()){
            return wrc.killProcessPID(pid);
        }
        else if (isMac() || isUbuntu()){
            return mrc.killProcessPID(pid,this.sudopassword);
        }else{
            return urm.killProcessPID(pid);
        }
    }
    public static boolean isWindows() {

        return (os.indexOf("win") >= 0);

    }
    public static boolean isMac() {

        return (os.indexOf("mac") >= 0);

    }
    public static boolean isUbuntu(){

        return (os.indexOf("ubuntu")>=0);
    }
    public String getOs(){
        return os;
    }

}
