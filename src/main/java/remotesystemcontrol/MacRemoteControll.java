package remotesystemcontrol;

import java.io.*;
import java.io.IOException;

public class MacRemoteControll {

    public MacRemoteControll(){

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

    public String killProcessPID(String pid,String password){
        String app="";
        String out="";
        try {
            Process process=Runtime.getRuntime().exec("sudo -S kill "+pid);
            PrintWriter pw = new PrintWriter(process.getOutputStream(), true);
            pw.println(password);
            BufferedReader buffer=new BufferedReader(new InputStreamReader(process.getInputStream()));
            while((app= buffer.readLine())!=null){
                out=out+app+"\n";
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return app;
    }
}
