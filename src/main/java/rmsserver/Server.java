package rmsserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
    private Map<String,ManageUser> user;
    private static final int port=33333;
    private static Logger log;

    public Server(Socket sock) {
        this.user=new TreeMap<>();
        this.dbrms=new DbRms("DB_RMS","postgres","dp20202021");
        this.socket=sock;
        this.log=Logger.getLogger("global");
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(port);
        while(true){
            log.info("--- Server run ---");
            Socket socket= serverSocket.accept();
            Server server=new Server(socket);
            Thread t=new Thread(server);
            t.start();
        }
    }

    @Override
    public void run() {
        while(true) {
         try {
            control(socket);
         }catch (IOException ex){

         }catch (SQLException ex2){

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
            moinitoringvalue(bufferedReader,printWriter);
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
        String image= bufferedReader.readLine();
        printWriter.println(dbrms.signin(name,surname,email,password,image.getBytes("UTF-16")));
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
        String ProcessActive= bufferedReader.readLine();
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
                printWriter.flush();
                log.info("Send monitoring message at user "+email);
            }
        }else{
            log.info("No one client manager on line to user "+email);
        }
    }
    public void image(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException{
        String email=bufferedReader.readLine();
        String image= dbrms.imageManagerQuery(email);
        printWriter.println(image);
        printWriter.flush();
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
        BufferedReader bufferedReaderClient=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-16"));
        String returned=bufferedReaderClient.readLine();
        Iterator<Socket> itrlistmanager=manager.getSocketManagers().iterator();
        if(itrlistmanager!=null){
            while (itrlistmanager.hasNext()){
                Socket socketManager=itrlistmanager.next();
                PrintWriter prv=new PrintWriter(new OutputStreamWriter(socketManager.getOutputStream(),"UTF-16"));
                prv.println(namemachine);
                prv.println(returned);
                printWriter.flush();
                log.info("Send "+returned+" at machine "+namemachine+" at user "+email);
            }
        }else{
            log.info("No one client manager on line to user "+email);
        }
    }
    public void retriveDevice(BufferedReader bufferedReader,PrintWriter printWriter) throws IOException,SQLException {
        String email=bufferedReader.readLine();
        String out= dbrms.retriveDevice(email);
        log.info(out);
        printWriter.println("deviceavabile");
        printWriter.println(out);
        printWriter.flush();
    }

}
