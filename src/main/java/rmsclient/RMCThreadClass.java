package rmsclient;

import remotesystemcontrol.RemoteSystemControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class RMCThreadClass implements Runnable{
    private BufferedReader br;
    private RemoteSystemControl rmc;
    private Socket sock;

    public RMCThreadClass(Socket sock) throws IOException {
        this.sock=sock;
        this.br=new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.rmc=new RemoteSystemControl();
    }
    public void controllAction(String action,BufferedReader br) throws IOException {
        if(action.contains("shutdown")){
            sock.close();
            rmc.shutDown();
        }else if(action.contains("reboot")){
            sock.close();
            rmc.reboot();
        }else if(action.contains("kill process with name")){
            rmc.killProcessNameProcess(br.readLine()); //Ricodati che non deve mai avvenire l' abbord di java
        }else if(action.contains("kill process")){
            rmc.killProcessPID(br.readLine());
        }
    }
    @Override
    public void run() {
        while (true){
            try {
                String action= br.readLine();
                controllAction(action,br);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
