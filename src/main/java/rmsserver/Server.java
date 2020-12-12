package rmsserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
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
             ex.printStackTrace();
         }catch (SQLException ex2){
             ex2.printStackTrace();
         }
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
                manageUser.addClientUser(namemachine, this.socket);
                user.put(email, manageUser);
            }else{
                ManageUser manageUser=user.get(email);
                manageUser.addClientUser(namemachine, this.socket);
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
        }
        //Gestire i messaggi rms e monitoring
    }
}
