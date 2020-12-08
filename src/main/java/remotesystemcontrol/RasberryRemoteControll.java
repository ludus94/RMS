package remotesystemcontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class RasberryRemoteControll {

    public RasberryRemoteControll(){

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
