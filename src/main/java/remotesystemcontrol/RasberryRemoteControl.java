package remotesystemcontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/***
 * Class for Remote Control on Raspbian systems
 */
public class RasberryRemoteControl {

    public RasberryRemoteControl(){

    }

    /***
     * Activates shutdown procedure
     */
    public void shutDown() {
        try {
            Runtime.getRuntime().exec("shutdown -h now");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /***
     * Activates reboot procedure
     */
    public void reboot() {
        try {
            Runtime.getRuntime().exec("shutdown -r now");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /***
     * Kills the process specified by pid
     * @param pid PID of process target
     * @return OS response
     */
    public String killProcessPID(String pid){
        String app="";
        String out="";
        try {
            Process process=Runtime.getRuntime().exec("kill "+pid);
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
