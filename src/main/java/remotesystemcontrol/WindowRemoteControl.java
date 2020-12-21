package remotesystemcontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/***
 * Class for Remote Control on Windows systems
 */
public class WindowRemoteControl {

    public WindowRemoteControl(){

    }

    /***
     * Activates shutdown procedure
     */
    public void shutDown() {
        try {
            Runtime.getRuntime().exec("shutdown.exe -s -t 0");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /***
     * Activates reboot procedure
     */
    public void reboot() {
        try {
            Runtime.getRuntime().exec("shutdown.exe -r -t 0");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /***
     * Activates kill process with name procedure
     * @param nameprocess Process name
     * @return OS response
     */
    public String killProcessNameProcess(String nameprocess){
        String out="";
        String app="";
        Process process;
        try {
            if (nameprocess.contains(".exe")) {
                process = Runtime.getRuntime().exec("Taskkill /F /IM " + nameprocess);
            }else {
                process = Runtime.getRuntime().exec("Taskkill /F /IM " + nameprocess + ".exe");
            }
            BufferedReader buffer=new BufferedReader(new InputStreamReader(process.getInputStream()));
            while((app= buffer.readLine())!=null){
                out=out+app+"\n";
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return out;
    }

    /***
     * Activates the kill with PID procedure
     * @param pid Process PID
     * @return OS response
     */
    public String killProcessPID(String pid){
        String out="";
        String app="";
        Process process;
        try {
            process=Runtime.getRuntime().exec("Taskkill /PID "+pid+" /F");
            BufferedReader buffer=new BufferedReader(new InputStreamReader(process.getInputStream()));
            while((app= buffer.readLine())!=null){
                out=out+app+"\n";
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return out;
    }
}
