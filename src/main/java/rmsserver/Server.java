package rmsserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.Buffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

//implements runnable
public class Server implements Runnable{
    private Socket socket;
    private DbRms dbrms;
    private static Map<String,ManageUser> user=new TreeMap<>();
    private static final int port=33333;
    private static Logger log;

    public Server(Socket sock) {
        this.dbrms=new DbRms("DB_RMS","postgres" +
                "","dp20202021");
        this.socket=sock;
        this.log=Logger.getLogger("global");
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(port);
        while(true){
            Socket socket= serverSocket.accept();
            Server server=new Server(socket);
            log.info("--- Connection Accept ---");
            Thread t=new Thread(server);
            t.start();
        }
    }

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
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            } catch (NullPointerException ex3) {
                ex3.printStackTrace();
            }
        }
    }

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
                manageUser.addClient(namemachine, this.socket);
            }
            dbrms.loginmachine(namemachine,email);
            log.info("Client "+namemachine+ " user "+ email+" is added");
            printWriter.println(value);
            printWriter.flush();
        }else{
            log.info("User not registerd");
            printWriter.println(1);
            printWriter.flush();
        }
    }
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
    public void logout(BufferedReader bufferedReader) throws IOException{
        String email=bufferedReader.readLine();
        ManageUser manager=user.get(email);
        ArrayList<Socket> listManager=manager.getSocketManagers();
        listManager.remove(socket);
        socket.close();
    }
    public void moinitoringvalue(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException {
        String namemachine=bufferedReader.readLine();
        String ProcessActive="";
        while (!(ProcessActive=ProcessActive+bufferedReader.readLine()).contains("stop"));
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
                prv.println(ProcessActive);
                prv.println(cpuTotalLoad);
                prv.println(cpuAvarageLoad);
                prv.println(cpuLoadPerCore);
                prv.println(CpuTemperature);
                prv.println(Speed);
                prv.println(cpuVoltage);
                prv.println(Power);
                prv.println(time);
                log.info("monitoring of "+namemachine);
                log.info(ProcessActive);
                log.info(cpuTotalLoad);
                log.info(cpuAvarageLoad);
                log.info(cpuLoadPerCore);
                log.info(CpuTemperature);
                log.info(Speed);
                log.info(cpuVoltage);
                log.info(Power);
                log.info(time);
                prv.flush();
                log.info("Send monitoring message at user "+email);
            }
        }else{
            log.info("No one client manager on line to user "+email);
        }
    }
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
                printWriter.flush();
                log.info("Send monitoring static message at user "+email);
            }
        }else{
            log.info("No one client manager on line to user "+email);
        }
    }
    public void image(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException{
        String email=bufferedReader.readLine();
        byte[] image= dbrms.imageManagerQuery(email);
        BufferedOutputStream imagebin=new BufferedOutputStream(socket.getOutputStream());
        imagebin.write(image);
        imagebin.flush();
    }
    public void rms(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException {
        String namemachine=bufferedReader.readLine();
        String command=bufferedReader.readLine();
        String email=dbrms.Machine(namemachine);
        ManageUser manager=user.get(email);
        Socket client=manager.getSocketMachine(namemachine);
        PrintWriter printWriterClient=new PrintWriter(new OutputStreamWriter(client.getOutputStream(),"UTF-16"));
        if(command.contains("shutdown") || command.contains("reboot")) {
            printWriterClient.println(command);
            printWriter.flush();
            log.info("Send "+command+"message at machine "+namemachine+" at user "+email);
        }else{
            String pid=bufferedReader.readLine();
            printWriterClient.println(command);
            printWriterClient.println(pid);
            printWriter.flush();
            log.info("Send"+command+" "+pid+" message at machine "+namemachine+" at user"+email);
        }
        if(command.contains("killprocess")) {
            BufferedReader bufferedReaderClient = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-16"));
            String returned = bufferedReaderClient.readLine();
            Iterator<Socket> itrlistmanager = manager.getSocketManagers().iterator();
            if (itrlistmanager != null) {
                while (itrlistmanager.hasNext()) {
                    Socket socketManager = itrlistmanager.next();
                    PrintWriter prv = new PrintWriter(new OutputStreamWriter(socketManager.getOutputStream(), "UTF-16"));
                    prv.println(namemachine);
                    prv.println(returned);
                    printWriter.flush();
                    log.info("Send " + returned + " at machine " + namemachine + " at user " + email);
                }
            } else {
                log.info("No one client manager on line to user " + email);
            }
        }
    }
    public void retriveDevice(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException {
        String email=bufferedReader.readLine();
        String out= dbrms.retriveDevice(email);
        log.info(out);
        printWriter.println("deviceavabile");
        printWriter.println(out);
        printWriter.println("stop");
        printWriter.flush();
    }

}
