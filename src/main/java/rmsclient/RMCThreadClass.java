package rmsclient;

import facade.MonitoringValue;
import remotesystemcontrol.RemoteSystemControl;

import java.io.*;
import java.net.*;

/***
 * Class dedicated to receiving and executing the remote commands
 */
public class RMCThreadClass implements Runnable{
    private BufferedReader br;
    private RemoteSystemControl rmc;
    private Socket sock;
    private PrintWriter pw;

    /***
     * Initializes required Classes
     * @param sock Reference to the same Socket
     * @throws IOException
     */
    public RMCThreadClass(Socket sock) throws IOException {
        this.sock=sock;
        this.br=new BufferedReader(new InputStreamReader(sock.getInputStream(),"UTF-16"));
        this.rmc=new RemoteSystemControl();
    }

    /***
     * Handles command requests and execute them
     * @param action Action name
     * @param br Buffer reader
     * @throws IOException
     */
    public void controllAction(String action,BufferedReader br) throws IOException {
        pw=new PrintWriter(new OutputStreamWriter(sock.getOutputStream(),"UTF-16"));
        if(action.contains("shutdown")){
            pw.println("result shutdown");
            pw.flush();
            sock.close();
            rmc.shutDown();
        }else if(action.contains("reboot")){
            pw.println("result reboot");
            pw.flush();
            sock.close();
            rmc.reboot();
        }else if(action.contains("killprocesswithname")){
            pw.println("result kill");
            pw.println(rmc.killProcessNameProcess(br.readLine())); //Ricodati che non deve mai avvenire l' abbort di java
            pw.flush();
        }else if(action.contains("killprocess")){
            pw.println("result kill");
            pw.println(rmc.killProcessPID(br.readLine()));
            pw.flush();
        }
    }

    /***
     *Thread waiting for incoming commands
     */
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
