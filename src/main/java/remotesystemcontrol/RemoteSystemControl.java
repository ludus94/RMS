package remotesystemcontrol;

import javax.swing.*;

/***
 * Manages calls for Remote Controls and redirects the requests to the appropriate class
 */
public class RemoteSystemControl {
    private static String os = System.getProperty("os.name").toLowerCase();
    private String sudopassword;
    private WindowRemoteControl wrc;
    private MacRemoteControl mrc;
    private RasberryRemoteControl urm;

    /***
     * Creates the RemoteSystemControl object and creates the RemoteControl object for the appropriate OS
     */
    public RemoteSystemControl(){
        if(isWindows())
            wrc=new WindowRemoteControl();
        else if (isMac() || isUbuntu()){
            mrc =new MacRemoteControl();
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
            urm=new RasberryRemoteControl();
        }
    }

    /***
     * Catches the shutdown request and redirects to the appropriate Controller
     */
    public void shutDown(){
        if(isWindows()){
            wrc.shutDown();
        }else if(isMac() || isUbuntu()){
            mrc.shutDown(this.sudopassword);
        }else{
            urm.shutDown();
        }
    }

    /***
     * Catches the reboot request and redirects to the appropriate Controller
     */
    public void reboot(){
        if(isWindows()){
            wrc.reboot();
        }else if(isMac() || isUbuntu()){
            mrc.reboot(this.sudopassword);
        }else{
            urm.shutDown();
        }
    }

    /***
     * Catches the kill process with name request and redirects to the appropriate Controller
     * @param nameprocess Process Name
     * @return OS response
     */
    public String killProcessNameProcess(String nameprocess){
        if (isWindows()){
            return wrc.killProcessNameProcess(nameprocess);
        }
        return "Action not available";
    }

    /***
     * Catches the kill process with pid request and redirects to the appropriate Controller
     * @param pid Process PID
     * @return OS response
     */
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

    /***
     * Check if the OS belongs to the Windows OS family
     * @return True if the machine runs on a Windows OS, False otherwise
     */
    public static boolean isWindows() {

        return (os.indexOf("win") >= 0);

    }

    /***
     * Check if the OS belongs to the MacOS family
     * @return True if the machine runs on MacOS, False otherwise
     */
    public static boolean isMac() {

        return (os.indexOf("mac") >= 0);

    }

    /***
     * Check if the OS belongs to the Ubuntu OS family
     * @return True if the machine runs on a Ubuntu OS, False otherwise
     */
    public static boolean isUbuntu(){

        return (os.indexOf("ubuntu")>=0);
    }

    /***
     * Get method for the OS
     * @return OS name
     */
    public String getOs(){
        return os;
    }

}
