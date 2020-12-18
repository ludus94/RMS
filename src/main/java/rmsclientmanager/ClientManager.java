package rmsclientmanager;

import rmsclientmanagerGUI.DataSet;
import rmsserver.StringObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/***
 * Client Manager main class.
 * Allows users to manage all devices linked to their account.
 */
public class ClientManager implements Runnable{

    private Socket sock;
    private int port=33333;
    private static final String address="ludovicorusso.ddns.net";
    private ArrayList<String> devicesList;
    private ArrayList<String> olddevicelist;
    private String username;
    private byte[] image;
    private Map<String,DataSet> devicetemperature;
    private Map<String,DataSet> devicecpuload;
    private Map<String,DataSet> devicecpuvoltage;
    private Map<String,DataSet> devicepower;
    private Map<String, StringObject>  outjtext;
    private static Logger log;
    public ClientManager(String username) throws IOException {
        this.sock=new Socket(address,port);
        this.devicesList = new ArrayList<>();
        this.olddevicelist=new ArrayList<>();
        this.username=username;
        this.devicetemperature=new TreeMap<>();
        this.devicecpuload=new TreeMap<>();
        this.devicecpuvoltage=new TreeMap<>();
        this.devicepower=new TreeMap<>();
        this.outjtext=new TreeMap<>();
        this.log=Logger.getLogger("global");
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
           prw.println(email);
           prw.flush();
           BufferedInputStream imgbin=new BufferedInputStream(sock.getInputStream());
           while(imgbin.available()==0);
           this.image=new byte[imgbin.available()];
           imgbin.read(image);
           this.retrieveDevices();
        }
        return returnValue;
    }
    public void logout(){
        try {
            PrintWriter prw= new PrintWriter(new OutputStreamWriter(sock.getOutputStream(),"UTF-16"));
            prw.println("logout manager");
            prw.println(username);
            prw.flush();
            sock.close();
        } catch (IOException e) {
        }

    }
    public Map<String, DataSet> getDevicetemperature() {
        return devicetemperature;
    }


    public Map<String, DataSet> getDevicecpuload() {
        return devicecpuload;
    }


    public Map<String, DataSet> getDevicecpuvoltage() {
        return devicecpuvoltage;
    }


    public Map<String, DataSet> getDevicepower() {
        return devicepower;
    }


    public ArrayList<String> getDevicesList() {
        return devicesList;
    }

    public Map<String, StringObject> getOutjtext() {
        return outjtext;
    }

    public byte[] getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getImageIcon() throws UnsupportedEncodingException,IOException {
        Image image2=new ImageIcon(image).getImage();
        Image image3=image2.getScaledInstance(75,75,75);
        return new ImageIcon(image3);
    }
    public void mapinit(){
        retrieveDevices();
        for(int i=0;i<devicesList.size();i++){
            devicetemperature.put(devicesList.get(i),new DataSet("chartline"));
            devicecpuload.put(devicesList.get(i),new DataSet("chartline"));
            devicecpuvoltage.put(devicesList.get(i),new DataSet("chartline"));
            devicepower.put(devicesList.get(i),new DataSet("chartline"));
            outjtext.put(devicesList.get(i),new StringObject());
        }
    }
    public void mapRefresh(){
        if(devicesList.size()> olddevicelist.size()) {
            for (int i = 0; i < devicesList.size(); i++) {
                if (!devicetemperature.containsKey(devicesList.get(i))) {
                    devicetemperature.put(devicesList.get(i), new DataSet("chartline"));
                    devicecpuload.put(devicesList.get(i), new DataSet("chartline"));
                    devicecpuvoltage.put(devicesList.get(i), new DataSet("chartline"));
                    devicepower.put(devicesList.get(i), new DataSet("chartline"));
                }
            }
        }else if(devicesList.size()<olddevicelist.size()){
            for(int i=0;i<devicesList.size();i++){
                String name=devicesList.get(i);
                if(!olddevicelist.get(i).contains(name)){
                    devicetemperature.remove(olddevicelist.get(i));
                    devicecpuload.remove(olddevicelist.get(i));
                    devicecpuvoltage.remove(olddevicelist.get(i));
                    devicepower.remove(olddevicelist.get(i));
                }
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
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
        byte[] imagebin=new byte[inputStream.available()];
        inputStream.read(imagebin);
        image=imagebin;
        PrintWriter prw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-16"));
        if(password.equals(password2)) {
            prw.println("signin");
            prw.println(email);
            prw.println(password);
            prw.println(name);
            prw.println(surname);
            prw.flush();
            BufferedReader br=new BufferedReader(new InputStreamReader(sock.getInputStream(),"UTF-16"));
            String image=br.readLine();
            if(image.contains("image")) {
                BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(sock.getOutputStream());
                bufferedOutputStream.write(imagebin);
                bufferedOutputStream.flush();
            }
            return Integer.parseInt(br.readLine());
        }
        return -1;
    }
    /***
     * Retrieve list of devices linked to
     * @return
     */
    public void retrieveDevices() {
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(this.sock.getOutputStream(), "UTF-16"));
            printWriter.println("retrieve device");
            printWriter.println(username);
            printWriter.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(this.sock.getInputStream(), "UTF-16"));
            if (br.readLine().contains("deviceavabile")) {
                ArrayList<String> out = new ArrayList<>();
                String input = null;
                while (!(input = br.readLine()).contains("stop")) {
                    out.add(input);
                }
                this.olddevicelist=this.devicesList;
                this.devicesList = out;
            }
        }catch(IOException ex){
        }
    }
    public void monitoringvalue(BufferedReader bufferedReader) throws IOException{
        String namedevice=bufferedReader.readLine();
        String ProcessActive="";
        while (!(ProcessActive=ProcessActive+bufferedReader.readLine()).contains("stop"));
        ProcessActive=ProcessActive+"\n";
        ProcessActive=ProcessActive.replace("stop", "");
        String cpuTotalLoad=bufferedReader.readLine();
        String cpuAvarageLoad= bufferedReader.readLine()+"\n";
        String cpuLoadPerCore= bufferedReader.readLine()+"\n";
        String CpuTemperature=bufferedReader.readLine();
        String Speed= bufferedReader.readLine()+"\n";
        String cpuVoltage= bufferedReader.readLine();
        String Power= bufferedReader.readLine();
        String time=bufferedReader.readLine();
        log.info(namedevice+time);
        log.info(ProcessActive);
        log.info(cpuTotalLoad);
        log.info(cpuAvarageLoad);
        log.info(cpuLoadPerCore);
        log.info(CpuTemperature);
        log.info(Speed);
        log.info(cpuVoltage);
        log.info(Power);
        log.info(time);
        devicetemperature.get(namedevice).setDataSetValue(Double.parseDouble(CpuTemperature),"°C",time);
        devicecpuload.get(namedevice).setDataSetValue(Double.parseDouble(cpuTotalLoad),"%",time);
        devicecpuvoltage.get(namedevice).setDataSetValue(Double.parseDouble(cpuVoltage),"mV",time);
        devicepower.get(namedevice).setDataSetValue(Double.parseDouble(Power),"mW",time);
        outjtext.get(namedevice).setOut("--- Measuring at "+time+"---",false);
        outjtext.get(namedevice).setOut(ProcessActive,false);
        outjtext.get(namedevice).setOut(cpuAvarageLoad,false);
        outjtext.get(namedevice).setOut(cpuLoadPerCore,false);
        outjtext.get(namedevice).setOut(Speed,false);

    }
    public void monitoringvaluestatic(BufferedReader bufferedReader) throws IOException{
        String namemachine=bufferedReader.readLine();
        String os=bufferedReader.readLine();
        String booted= bufferedReader.readLine();
        outjtext.get(namemachine).setOut("Operating System: "+os,false);
        outjtext.get(namemachine).setOut("Booted System: "+booted,false);
    }
    public String rmsmanage(String command,Integer index,String PID){
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-16"));
            if (command.contains("shutdown") && (PID.contains("") || PID == null)) {
                printWriter.println(command);
                printWriter.println(devicesList.get(index));
                printWriter.flush();
            } else if (command.contains("reboot") && (PID.contains("") || PID == null)) {
                printWriter.println(command);
                printWriter.println(devicesList.get(index));
                printWriter.flush();
            } else if (command.contains("killprocesswithname") && (!PID.contains("") || PID != null)) {
                printWriter.println(command);
                printWriter.println(devicesList.get(index));
                printWriter.println(PID);
                printWriter.flush();
            } else if (command.contains("killprocess") && (!PID.contains("") || PID != null)) {
                printWriter.println(command);
                printWriter.println(devicesList.get(index));
                printWriter.println(PID);
                printWriter.flush();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-16"));
            outjtext.get(devicesList.get(index)).setOut(bufferedReader.readLine(), false);
        }catch(IOException ex){

        }
        return null;
    }
    @Override
    public void run() {
        while(true) {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream(),"UTF-16"));
                if (input.readLine().contains("monitoringvalue")) {
                    monitoringvalue(input);
                }
                if (input.readLine().contains("monitoringvaluestatic")){
                    monitoringvalue(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
