package rmsserver;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.Buffer;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

/***
 * Class implement a server
 */
//implements runnable
public class Server implements Runnable{
    private Socket socket;
    private DbRms dbrms;
    private Map<String,ManageUser> user;
    private ManageUser manageUser;
    private static final int port=33333;
    private static Logger log;

    /***
     * Construct a object server
     * @param sock socket connection
     * @param manageUser managuser
     * @param usermap usermap
     *
     */
    public Server(Socket sock,ManageUser manageUser,Map<String,ManageUser> usermap) {
        this.dbrms=new DbRms("DB_RMS","postgres" +
                "","dp20202021");
        this.socket=sock;
        this.log=Logger.getLogger("global");
        this.manageUser=manageUser;
        this.user=usermap;
    }

    /***
     * Main of class
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(port);
        ManageUser manageUser=new ManageUser();
        Map<String,ManageUser> manageUserMap=new TreeMap<>();
        while(true){
            Socket socket= serverSocket.accept();
            Server server=new Server(socket,manageUser,manageUserMap);
            log.info("--- Connection Accept ---");
            Thread t=new Thread(server);
            t.start();
        }
    }

    /***
     * Thread for multiple connection
     */
    @Override
    public void run() {
        while(true) {
            try {
                control(socket);
            } catch (SocketException ex4){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            } catch (NullPointerException ex3) {
                try {
                    socket.close();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * Method of control action message
     * @param socket
     * @throws IOException
     * @throws SQLException
     */
    public void control(Socket socket) throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-16"));
        PrintWriter printWriter=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-16"));
        String action=bufferedReader.readLine();
        if(action.contains("signin")){
            signin(bufferedReader,printWriter);
        }
        else if(action.contains("login client")){
            loginclient(bufferedReader,printWriter);
        }else if(action.contains("login manager")){
            loginmanager(bufferedReader,printWriter);
        }else if(action.contains("logout manager")){
            logout(bufferedReader);
        }
        else if(action.contains("monitoringvalue")){
            if(action.equals("monitoringvaluestatic"))
                moinitoringValueStatic(bufferedReader,printWriter);
            else {
                moinitoringvalue(bufferedReader, printWriter);
            }
        }else if(action.contains("rms")){
            rms(bufferedReader,printWriter);
        }else if(action.contains("image")){
            image(bufferedReader,printWriter);
        }else if(action.contains("retrieve device")){
            retriveDevice(bufferedReader,printWriter);
        }
    }

    /***
     * Register a new user
     * @param bufferedReader
     * @param printWriter
     * @throws IOException
     * @throws SQLException
     */
    public void signin(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException{
        String email=bufferedReader.readLine();
        String password=bufferedReader.readLine();
        String name=bufferedReader.readLine();
        String surname=bufferedReader.readLine();
        printWriter.println("image");
        printWriter.flush();
        BufferedInputStream bufferedInputStream=new BufferedInputStream(socket.getInputStream());
        while(bufferedInputStream.available()==0);
        byte[] img=new byte[bufferedInputStream.available()];
        bufferedInputStream.read(img);
        int value=dbrms.signin(name,surname,email,password,img);
        if(value==0){
            log.info("User "+email+" register with success");
        }else if(value==2){
            log.info("User "+email+" already exist");
        }else if(value==3){
            log.info("User "+email+" generic error");
        }
        printWriter.println(value);
        printWriter.flush();
    }

    /***
     * method to login client
     * @param bufferedReader
     * @param printWriter
     * @throws IOException
     * @throws SQLException
     */
    public void loginclient(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException {
        String namemachine=bufferedReader.readLine();
        String email=bufferedReader.readLine();
        String password=bufferedReader.readLine();
        int value=dbrms.loginQuery(email,password);
        if(value==0){
            if (!user.containsKey(email)) {
                ManageUser manageUser = new ManageUser();
                manageUser.addClient(namemachine, this.socket);
                user.put(email, manageUser);
            }else{
                ManageUser manageUser=user.get(email);
                manageUser.addClient(namemachine,this.socket);
                user.remove(email);
                user.put(email,manageUser);
            }
            dbrms.loginmachine(namemachine,email);
            log.info("Client "+namemachine+ " user "+ email+" is added");
            printWriter.println(value);
            printWriter.flush();
            Iterator<Socket> managerconnect=user.get(email).getSocketManagers().iterator();
            while(managerconnect.hasNext()){
                Socket manager=managerconnect.next();
                PrintWriter printWriterm=new PrintWriter(new OutputStreamWriter(manager.getOutputStream(),"UTF-16"));
                printWriterm.println("deviceupdate");
                printWriterm.flush();
            }
        }else{
            log.info("User not registerd");
            printWriter.println(1);
            printWriter.flush();
        }
    }

    /***
     * Login of manager client
     * @param bufferedReader
     * @param printWriter
     * @throws IOException
     * @throws SQLException
     */
    public void loginmanager(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException, SQLException {
        String email=bufferedReader.readLine();
        String password=bufferedReader.readLine();
        int value=dbrms.loginQuery(email,password);
        if(value==0){
            if (!user.containsKey(email)) {
                ManageUser manageUser = new ManageUser();
                manageUser.addClientManager(this.socket);
                user.put(email, manageUser);
            }else{
                ManageUser manageUser=user.get(email);
                manageUser.addClientManager(this.socket);
                user.remove(email);
                user.put(email, manageUser);
            }
            log.info("Client Manager user "+ email+" is added");
            printWriter.println(value);
            printWriter.flush();
        }else{
            log.info("User not registerd");
            printWriter.println(1);
            printWriter.flush();
        }
    }

    /***
     * Method log out
     * @param bufferedReader
     * @throws IOException
     */
    public void logout(BufferedReader bufferedReader) throws IOException{
        String email=bufferedReader.readLine();
        ManageUser manager=user.get(email);
        ArrayList<Socket> listManager=manager.getSocketManagers();
        listManager.remove(socket);
        socket.close();
    }

    /***
     * Method monitoringvalue is used for proxy to client and client manager
     * @param bufferedReader
     * @param printWriter
     * @throws IOException
     * @throws SQLException
     */
    public void moinitoringvalue(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException {
        String namemachine=bufferedReader.readLine();
        String ProcessActive="";
        while (!(ProcessActive=ProcessActive+bufferedReader.readLine()+"\n").contains("stop"));
        String cpuTotalLoad=bufferedReader.readLine();
        String cpuAvarageLoad= bufferedReader.readLine();
        String cpuLoadPerCore= bufferedReader.readLine();
        String CpuTemperature=bufferedReader.readLine();
        String Speed= bufferedReader.readLine();
        String cpuVoltage= bufferedReader.readLine();
        String Power= bufferedReader.readLine();
        String time= bufferedReader.readLine();
        String email=dbrms.Machine(namemachine);
        ManageUser manager=user.get(email);
        Iterator<Socket> itrlistmanager=manager.getSocketManagers().iterator();
        if(itrlistmanager!=null){
            while (itrlistmanager.hasNext()){
                Socket socketManager=itrlistmanager.next();
                PrintWriter prv=new PrintWriter(new OutputStreamWriter(socketManager.getOutputStream(),"UTF-16"));
                prv.println("monitoringvalue");
                prv.println(namemachine);
                prv.print(ProcessActive);
                prv.println(cpuTotalLoad);
                prv.println(cpuAvarageLoad);
                prv.println(cpuLoadPerCore);
                prv.println(CpuTemperature);
                prv.println(Speed);
                prv.println(cpuVoltage);
                prv.println(Power);
                prv.println(time);
                log.info("One massage of client ");
                System.out.print("---Monitoring of machine "+namemachine+" user "+email+" at "+ time
                                    +ProcessActive+"\n"
                                    +"CPU Total: "+cpuTotalLoad+"\n"
                                    +"CPU Avarage Load: "+cpuAvarageLoad+"\n"
                                    +"CPU Load Per Core: "+cpuLoadPerCore+"\n"
                                    +"CPU Temperature: "+CpuTemperature+"\n"
                                    +"Speed: "+Speed+"\n"
                                    +"CPU Voltage: "+cpuVoltage+"\n"
                                    +"Power:"+Power+"\n");
                prv.flush();
                log.info("Send monitoring message at user "+email);
            }
        }else{
            log.info("No one client manager on line to user "+email);
        }
    }

    /***
     * Monitoring static value proxy method
     *
     * @param bufferedReader
     * @param printWriter
     * @throws IOException
     * @throws SQLException
     */
    public void moinitoringValueStatic(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException {
        String namemachine=bufferedReader.readLine();
        String os=bufferedReader.readLine();
        String booted= bufferedReader.readLine();
        String email=dbrms.Machine(namemachine);
        ManageUser manager = user.get(email);
        Iterator<Socket> itrlistmanager=manager.getSocketManagers().iterator();
        if(itrlistmanager!=null){
            while (itrlistmanager.hasNext()){
                Socket socketManager=itrlistmanager.next();
                PrintWriter prv=new PrintWriter(new OutputStreamWriter(socketManager.getOutputStream(),"UTF-16"));
                prv.println("monitoringvaluestatic");
                prv.println(namemachine);
                prv.println(os);
                prv.println(booted);
                prv.flush();
                printWriter.flush();
                log.info("Send monitoring static message at user "+email);
            }
        }else{
            log.info("No one client manager on line to user "+email);
            user.get(email).addMonitoringStatic(namemachine, os+"\n"+booted+"\n");
        }
    }

    /***
     * Retrieve a byte image and send it
     * @param bufferedReader
     * @param printWriter
     * @throws IOException
     * @throws SQLException
     */
    public void image(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException{
        String email=bufferedReader.readLine();
        byte[] image= dbrms.imageManagerQuery(email);
        BufferedOutputStream imagebin=new BufferedOutputStream(socket.getOutputStream());
        imagebin.write(image);
        imagebin.flush();
    }

    /***
     * Used to manage a shutdown,reboot,killprocess proxy method
     * @param bufferedReader
     * @param printWriter
     * @throws IOException
     * @throws SQLException
     */
    public void rms(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException {
        String command=bufferedReader.readLine();
        String namemachine=bufferedReader.readLine();
        String email=dbrms.Machine(namemachine);
        ManageUser manager=new ManageUser();
        manager=user.get(email);
        Socket client=manager.getSocketMachine(namemachine);
        PrintWriter printWriterClient=new PrintWriter(new OutputStreamWriter(client.getOutputStream(),"UTF-16"));
        if(command.contains("shutdown") || command.contains("reboot")) {
            printWriterClient.println(command);
            printWriterClient.flush();
            printWriter.println(command);
            printWriter.flush();
            log.info("Send "+command+"message at machine "+namemachine+" at user "+email);
            manager.removeClientUser(namemachine);
            user.remove(email);
            user.put(email, manager);
            printWriter.println("deviceupdate");
            printWriter.flush();
            System.out.print("Device Update");
        }else{
            String pid=bufferedReader.readLine();
            printWriterClient.println(command);
            printWriterClient.println(pid);
            printWriterClient.flush();
            printWriter.println(command);
            printWriter.flush();
            log.info("Send"+command+" "+pid+" message at machine "+namemachine+" at user"+email);
        }
        if(command.contains("killprocess")) {
            BufferedReader bufferedReaderClient = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-16"));
            String returned = bufferedReaderClient.readLine();
            Iterator<Socket> itrlistmanager = manager.getSocketManagers().iterator();
            if (itrlistmanager != null) {
                while (itrlistmanager.hasNext()) {
                    Socket socketManager = itrlistmanager.next();
                    PrintWriter prv = new PrintWriter(new OutputStreamWriter(socketManager.getOutputStream(), "UTF-16"));
                    prv.println(namemachine);
                    prv.println(returned);
                    prv.flush();
                    log.info("Send " + returned + " at machine " + namemachine + " at user " + email);
                }
            } else {
                log.info("No one client manager on line to user " + email);
            }
        }
    }

    /***
     * message retrive device
     * @param bufferedReader
     * @param printWriter
     * @throws IOException
     * @throws SQLException
     */
    public void retriveDevice(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException {
        String email=bufferedReader.readLine();
        String out=new String(); //dbrms.retriveDevice(email);
        log.info("User Connect");
        ArrayList<String> userconnect=user.get(email).getNameMachines();
        Iterator<String> connect=userconnect.iterator();
        Map<String,String> monitoringstatic=user.get(email).getMonitoringStatic();
        if(monitoringstatic.size()>0) {
            printWriter.println("monitoringstatic");
            ArrayList<String> device = dbrms.retriveNameMachine(email);
            for (int i = 0; i < device.size(); i++) {
                printWriter.println(device.get(i));
                printWriter.print(user.get(email).getMonitoringStatic(device.get(i)));
            }
            printWriter.println("stop");
        }else{
            printWriter.println("stop");
        }
        printWriter.println("deviceavabile");
        if(!userconnect.isEmpty()) {
            while (connect.hasNext()) {
                String conn = connect.next();
                printWriter.println(conn);
                out = out + conn + "\n";
            }
        }else{
            printWriter.println("empty");
        }
        log.info(out);
        printWriter.println("stop");
        printWriter.flush();
    }

}
