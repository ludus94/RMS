package remotesystemcontrol;

import java.io.IOException;
import java.io.PrintWriter;

public class UnixRemoteControll {

    public UnixRemoteControll(){

    }
    public void shutDown() {
        try {
            Runtime.getRuntime().exec("shutdown -h now");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void reboot() {
        try {
            Runtime.getRuntime().exec("shutdown -r now");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void killProcessPID(String pid){
        try {
            Runtime.getRuntime().exec("kill "+pid);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
