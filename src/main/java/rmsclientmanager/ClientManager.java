package rmsclientmanager;

import rmsclientmanagerGUI.DataSet;
import rmsclientmanagerGUI.JTextUpgrade;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
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
    private static final String address="151.73.96.214";
    private ArrayList<String> devicesList;
    private ArrayList<String> olddevicelist;
    private String username;
    private byte[] image;
    private Map<String,DataSet> devicetemperature;
    private Map<String,DataSet> devicecpuload;
    private Map<String,DataSet> devicecpuvoltage;
    private Map<String,DataSet> devicepower;
    private Map<String, StringObject>  outjtext;
    private Map<String, String> monitoringvalue;
    private static Logger log;
    private JTextUpgrade JTextUpgrade;
    private Thread t;
    private Thread tmonitoring;
    public ClientManager(String username) throws IOException {
        this.sock=new Socket(address,port);
        this.devicesList = new ArrayList<>();
        this.olddevicelist=new ArrayList<>();
        this.monitoringvalue=new TreeMap<>();
        this.username=username;
        this.devicetemperature=new TreeMap<>();
        this.devicecpuload=new TreeMap<>();
        this.devicecpuvoltage=new TreeMap<>();
        this.devicepower=new TreeMap<>();
        this.outjtext=new TreeMap<>();
        this.log=Logger.getLogger("global");
    }

    public void setTmonitoring(Thread tmonitoring) {
        this.tmonitoring = tmonitoring;
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

    /***
     * Method logout of client
     */
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

    /***
     * Get method DeviceTemperature
     * @return reference of map
     */
    public Map<String, DataSet> getDevicetemperature() {
        return devicetemperature;
    }

    /***
     * Get method DeviceCPUTemperature
     * @return reference of map
     */
    public Map<String, DataSet> getDevicecpuload() {
        return devicecpuload;
    }

    /***
     * Get method DeviceCPUVoltage
     * @return reference of map
     */
    public Map<String, DataSet> getDevicecpuvoltage() {
        return devicecpuvoltage;
    }

    /***
     * Get method DevicePower
     * @return reference of map
     */
    public Map<String, DataSet> getDevicepower() {
        return devicepower;
    }

    public void setDevicesList(ArrayList<String> devicesList) {
        this.devicesList = devicesList;
    }

    public void setDevicetemperature(Map<String, DataSet> devicetemperature) {
        this.devicetemperature = devicetemperature;
    }

    public void setDevicecpuload(Map<String, DataSet> devicecpuload) {
        this.devicecpuload = devicecpuload;
    }

    public void setDevicecpuvoltage(Map<String, DataSet> devicecpuvoltage) {
        this.devicecpuvoltage = devicecpuvoltage;
    }

    public void setDevicepower(Map<String, DataSet> devicepower) {
        this.devicepower = devicepower;
    }

    public void setOutjtext(Map<String, StringObject> outjtext) {
        this.outjtext = outjtext;
    }

    public void setMonitoringvalue(Map<String, String> monitoringvalue) {
        this.monitoringvalue = monitoringvalue;
    }

    /***
     * Set method of thread updated for JTextArea
     * @param JTextUpgrade
     */
    public void setThreadJtextUpgrade(JTextUpgrade JTextUpgrade) {
        this.JTextUpgrade = JTextUpgrade;
    }
    /***
     * Get method DeviceCPUVoltage
     *
     */
    public ArrayList<String> getDevicesList() {
        return devicesList;
    }
    /***
     * Get method Output JTextArea
     * @return reference of map
     */
    public Map<String, StringObject> getOutjtext() {
        return outjtext;
    }
    /***
     * Get method for MonitoringValue
     * @return reference of map
     */
    public Map<String,String > getMonitoringValue(){
        return monitoringvalue;
    }

    /***
     * Get method for Image byte
     * @return image byte
     */
    public byte[] getImage() {
        return image;
    }

    /***
     * Get method for Username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /***
     * Get method to obtain a ImageIcon User
     * @return ImageIcon object
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public ImageIcon getImageIcon() throws UnsupportedEncodingException,IOException {
        Image image2=new ImageIcon(image).getImage();
        Image image3=image2.getScaledInstance(75,75,75);
        return new ImageIcon(image3);
    }

    /***
     * Initialization of map to use a dataset in the graphics
     */
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

    /***
     * Using to refresh a graphics
     */
    public void mapRefresh(){
        if(devicesList.size()> olddevicelist.size()) {
            for (int i = 0; i < devicesList.size(); i++) {
                if (!devicetemperature.containsKey(devicesList.get(i))) {
                    devicetemperature.put(devicesList.get(i), new DataSet("chartline"));
                    devicecpuload.put(devicesList.get(i), new DataSet("chartline"));
                    devicecpuvoltage.put(devicesList.get(i), new DataSet("chartline"));
                    devicepower.put(devicesList.get(i), new DataSet("chartline"));
                    outjtext.put(devicesList.get(i),new StringObject());
                }
            }
        }else if(devicesList.size()<olddevicelist.size()){
            for(int i=0;i<devicesList.size();i++){
                if(!devicetemperature.containsKey(devicesList.get(i))){
                    devicetemperature.remove(olddevicelist.get(i));
                    devicecpuload.remove(olddevicelist.get(i));
                    devicecpuvoltage.remove(olddevicelist.get(i));
                    devicepower.remove(olddevicelist.get(i));
                    outjtext.remove(devicesList.get(i));
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
     *
     */
    public void retrieveDevices() {
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(this.sock.getOutputStream(), "UTF-16"));
            printWriter.println("retrieve device");
            printWriter.println(username);
            printWriter.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(this.sock.getInputStream(), "UTF-16"));
            if(br.readLine().contains("monitoringstatic")){
                while(!br.readLine().equals("stop")){
                    monitoringvalue.put(br.readLine(), br.readLine());
                }
            }
            if (br.readLine().contains("deviceavabile")) {
                ArrayList<String> out = new ArrayList<>();
                String input = null;
                while (!(input = br.readLine()).contains("stop")) {
                    out.add(input);
                }
                if(out.contains("empty")){
                    this.olddevicelist=this.devicesList;
                    this.devicesList = new ArrayList<>();
                }
                else {
                    this.olddevicelist = this.devicesList;
                    this.devicesList = out;
                }
            }
        }catch(IOException ex){
        }
    }

    /***
     * Read a monitoring dynamic value on the buffer and display value on the GUI
     * @param bufferedReader
     * @throws IOException
     */
    public void monitoringvalue(BufferedReader bufferedReader) throws IOException{
        String namedevice=bufferedReader.readLine();
        String ProcessActive="";
        String out="";
        while (!(ProcessActive=ProcessActive+bufferedReader.readLine()+"\n").contains("stop"));
        int index=ProcessActive.indexOf("stop");
        ProcessActive=ProcessActive.replace("stop\n", "");
        String cpuTotalLoad=bufferedReader.readLine();
        String cpuAvarageLoad="CPU Avarage Load:"+bufferedReader.readLine();
        String cpuLoadPerCore="CPU Load Per Core:"+bufferedReader.readLine();
        String CpuTemperature=bufferedReader.readLine();
        String Speed="Speed Fan:"+ bufferedReader.readLine();
        String cpuVoltage= bufferedReader.readLine();
        String Power= bufferedReader.readLine();
        String time=bufferedReader.readLine();

        devicetemperature.get(namedevice).setDataSetValue(Double.parseDouble(CpuTemperature),"°C",time);
        devicecpuload.get(namedevice).setDataSetValue(Double.parseDouble(cpuTotalLoad),"%",time);
        devicecpuvoltage.get(namedevice).setDataSetValue(Double.parseDouble(cpuVoltage),"mV",time);
        devicepower.get(namedevice).setDataSetValue(Double.parseDouble(Power),"mW",time);
        outjtext.get(namedevice).setCount(outjtext.get(namedevice).getCount()+index);
        outjtext.get(namedevice).setOut("--- Measuring at "+time+"---\n",false);
        outjtext.get(namedevice).setOut(ProcessActive+"\n",false);
        outjtext.get(namedevice).setOut(cpuAvarageLoad+"\n",false);
        outjtext.get(namedevice).setOut(cpuLoadPerCore+"\n",false);
        outjtext.get(namedevice).setOut(Speed+"\n",false);
        JTextUpgrade.setDeviceSelected(namedevice);
        JTextUpgrade.setOutputjtext(outjtext);
        if (t==null){
            t=new Thread(this.JTextUpgrade);
            t.start();
        }


    }
    /***
     * Read a monitoring value static on the buffer and display value on the GUI
     * @param bufferedReader
     * @throws IOException
     */
    public void monitoringvaluestatic(BufferedReader bufferedReader) throws IOException{
        String namemachine=bufferedReader.readLine();
        String os=bufferedReader.readLine();
        String booted= bufferedReader.readLine();
        outjtext.get(namemachine).setOut("Operating System: "+os,false);
        outjtext.get(namemachine).setOut("Booted System: "+booted,false);
        JTextUpgrade.setDeviceSelected(namemachine);
        JTextUpgrade.setOutputjtext(outjtext);

    }

    /***
     * Send at server the command catch at GUI for shutdown,reboot and killprocess
     * @param command
     * @param deviceSelected
     * @param PID
     * @return String to printed in interface
     */
    public String rmsmanage(String command,String deviceSelected,String PID){
        try {
            System.out.println(deviceSelected);
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-16"));
            if (command.contains("shutdown") && (PID.contains("") || PID == null)) {
                printWriter.println("rms");
                printWriter.println(command);
                printWriter.println(deviceSelected);
                printWriter.flush();
            } else if (command.contains("reboot") && (PID.contains("") || PID == null)) {
                printWriter.println("rms");
                printWriter.println(command);
                printWriter.println(deviceSelected);
                printWriter.flush();
            } else if (command.contains("killprocesswithname") && (!PID.contains("") || PID != null)) {
                printWriter.println("rms");
                printWriter.println(command);
                printWriter.println(deviceSelected);
                printWriter.println(PID);
                printWriter.flush();
            } else if (command.contains("killprocess") && (!PID.contains("") || PID != null)) {
                printWriter.println("rms");
                printWriter.println(command);
                printWriter.println(deviceSelected);
                printWriter.println(PID);
                printWriter.flush();
            }
            if(command.contains("killprocess")) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-16"));
                outjtext.get(deviceSelected).setOut(bufferedReader.readLine(), false);
                JTextUpgrade.setOutputjtext(outjtext);
            }
        }catch(IOException ex){

        }
        return null;
    }

    /***
     * Thread to manage the monitoring value sends
     */
    @Override
    public void run() {
        while(true) {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream(),"UTF-16"));
                String action=input.readLine();
                if(action.equals("deviceupdate")){
                    System.out.println("Retrive");
                    retrieveDevices();
                    Iterator<String> device=devicesList.iterator();
                    System.out.println("---Device List---");
                    while(device.hasNext()){
                        System.out.println(device.next());
                    }
                    mapRefresh();
                    JTextUpgrade.setJdeviceListText(devicesList);
                }
                if (action.equals("monitoringvalue")) {
                    monitoringvalue(input);
                }
                if (action.equals("monitoringvaluestatic")) {
                    monitoringvalue(input);
                }
                //if (action.equals("shutdown")|| action.equals("reboot")){
                   // break;
                //}

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
