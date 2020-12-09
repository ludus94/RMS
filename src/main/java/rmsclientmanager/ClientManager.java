package rmsclientmanager;

import rmsclient.MonitoringThreadClass;
import rmsclient.RMCThreadClass;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/***
 * Client Manager main class.
 * Allows users to manage all devices linked to their account.
 */
public class ClientManager{

    private Socket sock;
    private int port=33333;
    private static final String address="ludovicorusso.ddns.net";
    private ArrayList<String> devicesList;
    private String username;

    public ClientManager(String username) throws IOException {
        this.sock=new Socket(address,port);
        this.devicesList = new ArrayList<>();
        this.username=username;
    }

    public ArrayList<String> getDevicesList() {
        return devicesList;
    }

    /***
     * Retrieve list of devices linked to
     * @return
     */
    public ArrayList<String> retrieveDevices(BufferedReader br) throws IOException {
        ArrayList<String> out=new ArrayList<>();
        String input=null;
        while ((input = br.readLine()) != null) {
                out.add(input);
        }
            return out;
    }
    public void Control(){
        try {
            BufferedReader input=new BufferedReader(new InputStreamReader(sock.getInputStream()));
            if(input.readLine().contains("deviceavabile")) {
                devicesList=retrieveDevices(input);
            }if(input.readLine().contains("")){

            }
            } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
