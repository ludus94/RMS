package remotesystemcontrol;

import java.io.*;
import java.io.IOException;

public class UnixRemoteControll {

    public UnixRemoteControll(){

    }
    public void shutDown(String password) {
        try {
            Process process=Runtime.getRuntime().exec("sudo -S shutdown -h now");
            PrintWriter pw = new PrintWriter(process.getOutputStream(), true);    // true=autoflush
            pw.println(password);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void reboot(String password) {
        try {
            Process process=Runtime.getRuntime().exec("sudo -S shutdown -r now");
            PrintWriter pw = new PrintWriter(process.getOutputStream(), true);
            pw.println(password);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void killProcessPID(String pid,String password){
        try {
            Process process=Runtime.getRuntime().exec("sudo -S kill "+pid);
            PrintWriter pw = new PrintWriter(process.getOutputStream(), true);
            pw.println(password);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
