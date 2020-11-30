package remotesystemcontrol;

import javax.swing.*;

public class RemoteSystemControl {
    private static String os = System.getProperty("os.name").toLowerCase();
    private String sudopassword;
    private WindowRemoteControll wrc;
    private UnixRemoteControll  urc;
    public RemoteSystemControl(){
        if(isWindows())
            wrc=new WindowRemoteControll();
        else{
            urc=new UnixRemoteControll();
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
        }
    }
    public void shutDown(){
        if(isWindows()){
            wrc.shutDown();
        }else{
            urc.shutDown(this.sudopassword);
        }
    }
    public void reboot(){
        if(isWindows()){
            wrc.reboot();
        }else{
            urc.reboot(this.sudopassword);
        }
    }
    public void killProcessNameProcess(String nameprocess){
        if (isWindows()){
            wrc.killProcessNameProcess(nameprocess);
        }
    }
    public void killProcessPID(String pid){
        if (isWindows()){
            wrc.killProcessPID(pid);
        }
        else{
            urc.killProcessPID(pid,this.sudopassword);
        }
    }
    public static boolean isWindows() {

        return (os.indexOf("win") >= 0);

    }
}
