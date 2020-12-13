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
        this.br=new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.pw=new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
        this.rmc=new RemoteSystemControl();
    }

    /***
     * Handles command requests and execute them
     * @param action Action name
     * @param br Buffer reader
     * @throws IOException
     */
    public void controllAction(String action,BufferedReader br) throws IOException {
        if(action.contains("shutdown")){
            pw.println("shutdown");
            pw.flush();
            sock.close();
            rmc.shutDown();
        }else if(action.contains("reboot")){
            pw.println("reboot");
            pw.flush();
            sock.close();
            rmc.reboot();
        }else if(action.contains("kill process with name")){
            pw.println("result kill");
            pw.println(rmc.killProcessNameProcess(br.readLine())); //Ricodati che non deve mai avvenire l' abbort di java
            pw.flush();
        }else if(action.contains("kill process")){
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
