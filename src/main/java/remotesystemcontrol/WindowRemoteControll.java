package remotesystemcontrol;

import java.io.IOException;

public class WindowRemoteControll {

    public WindowRemoteControll(){

    }
    public void shutDown() {
        try {
            Runtime.getRuntime().exec("shutdown.exe -s -t 0");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void reboot() {
        try {
            Runtime.getRuntime().exec("shutdown.exe -r -t 0");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void killProcessNameProcess(String nameprocess){
        try {
            if (nameprocess.contains(".exe"))
                Runtime.getRuntime().exec("Taskkill /F /IM " + nameprocess );
            else
                Runtime.getRuntime().exec("Taskkill /F /IM " + nameprocess + ".exe");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void killProcessPID(String pid){
        try {
            Runtime.getRuntime().exec("Taskkill /PID "+pid+" /F");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
