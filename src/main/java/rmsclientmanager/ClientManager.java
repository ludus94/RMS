package rmsclientmanager;

import rmsclient.MonitoringThreadClass;
import rmsclient.RMCThreadClass;
import rmsclientmanagerGUI.DataSet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

/***
 * Client Manager main class.
 * Allows users to manage all devices linked to their account.
 */
public class ClientManager{

    private Socket sock;
    private int port=33333;
    private static final String address="ludovicorusso.ddns.net";
    private ArrayList<String> devicesList;
    private ArrayList<String> olddevicelist;
    private String username;
    private String image;
    private Map<String,DataSet> devicetemperature;
    private Map<String,DataSet> devicecpuload;
    private Map<String,DataSet> devicecpuvoltage;
    private Map<String,DataSet> devicepower;

    public ClientManager(String username) throws IOException {
        this.sock=new Socket(address,port);
        this.devicesList = new ArrayList<>();
        this.olddevicelist=new ArrayList<>();
        this.username=username;
    }

    public void setOlddevicelist(ArrayList<String> olddevicelist) {
        this.olddevicelist = olddevicelist;
    }

    public ArrayList<String> getDevicesList() {
        return devicesList;
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getImageIcon() throws UnsupportedEncodingException,IOException {
        ByteArrayInputStream imagebin = new ByteArrayInputStream(image.getBytes("UTF-16"));
        BufferedImage bufferedImage = ImageIO.read(imagebin);
        ImageIcon imageIconUser = new ImageIcon(new ImageIcon(bufferedImage).getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
        return imageIconUser;
    }
        /***Log in with an existing user
     *
     * @param email User's email
     * @param password User's password
     * @return Error code:
     * code 0: Success
     * code 1: Incorrect parameters
     * @throws IOException
     */
    public int login(String email,String password) throws IOException {
        PrintWriter prw=new PrintWriter(new OutputStreamWriter(sock.getOutputStream(),"UTF-16"));
        prw.println("login manager");
        prw.println(email);
        prw.println(password);
        prw.flush();
        BufferedReader br=new BufferedReader(new InputStreamReader(sock.getInputStream(),"UTF-16"));
        int returnValue=Integer.parseInt(br.readLine());
        if(returnValue==0) {
           prw.println("image");
           this.image= br.readLine();
           this.retrieveDevices();
           this.controll();
        }
        return returnValue;
    }

    public Map<String, DataSet> getDevicetemperature() {
        return devicetemperature;
    }

    public void setDevicetemperature(Map<String, DataSet> devicetemperature) {
        this.devicetemperature = devicetemperature;
    }

    public Map<String, DataSet> getDevicecpuload() {
        return devicecpuload;
    }

    public void setDevicecpuload(Map<String, DataSet> devicecpuload) {
        this.devicecpuload = devicecpuload;
    }

    public Map<String, DataSet> getDevicecpuvoltage() {
        return devicecpuvoltage;
    }

    public void setDevicecpuvoltage(Map<String, DataSet> devicecpuvoltage) {
        this.devicecpuvoltage = devicecpuvoltage;
    }

    public Map<String, DataSet> getDevicepower() {
        return devicepower;
    }

    public void setDevicepower(Map<String, DataSet> devicepower) {
        this.devicepower = devicepower;
    }
    public void mapRefresh(){
        //Dove aggiungere l' aggiornameto delle mappe.
        for(int i=0;i<devicesList.size();i++){
            if(!devicetemperature.containsKey(devicesList.get(i))){
                devicetemperature.put(devicesList.get(i),new DataSet("chartline"));
                devicecpuload.put(devicesList.get(i),new DataSet("chartline"));
                devicecpuvoltage.put(devicesList.get(i),new DataSet("chartline"));
                devicepower.put(devicesList.get(i),new DataSet("chartline"));
            }
        }
    }
    /***Sign in a new user into the system
     *
     * @param email User's email
     * @param password User's password
     * @param password2 Second password used to prevent an unintended password choice
     * @param name User's name
     * @param surname User's surname
     * @param path Image's path
     * @param extension Image extension (.jpg, .png)
     * @return Error code:
     * code 0: Success
     * code 1: Password is not equal to password 2
     * code 2: User already exists in system
     * code 3: Generic error
     * @throws IOException
     */
    public int sigin(String email, String password, String password2,String name, String surname, String path,String extension) throws IOException {
        BufferedImage image=ImageIO.read(new File(path));
        ByteArrayOutputStream imagebin=new ByteArrayOutputStream();
        ImageIO.write(image,extension,imagebin);
        PrintWriter prw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-16"));
        if(password.equals(password2)) {
            prw.println("signin");
            prw.println(email);
            prw.println(password);
            prw.println(name);
            prw.println(surname);
            prw.println(imagebin.toByteArray());
            prw.flush();
        }else{
            return 1;
        }
        BufferedReader br=new BufferedReader(new InputStreamReader(sock.getInputStream(),"UTF-16"));
        return Integer.parseInt(br.readLine());
    }
    /***
     * Retrieve list of devices linked to
     * @return
     */
    public void retrieveDevices() {
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(this.sock.getOutputStream(), "UTF-16"));
            printWriter.println("retrieve device");
            printWriter.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(this.sock.getInputStream(), "UTF-16"));
            if (br.readLine().contains("deviceavabile")) {
                ArrayList<String> out = new ArrayList<>();
                String input = null;
                while ((input = br.readLine()) != null) {
                    out.add(input);
                }
                this.devicesList = out;
            }
        }catch(IOException ex){
        }
    }
    public void controll(){
        while(true) {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                if (input.readLine().contains("monitoringvalue")) {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
