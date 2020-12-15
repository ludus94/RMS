package rmsclient;
import facade.MonitoringValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

/***
 * Client main class.
 * Allows users to log in and sign in, thus connecting to the server and sending data
 */
public class Client {
    private Socket sock;
    private int port=33333;
    private static final String address="ludovicorusso.ddns.net";
    private MonitoringValue mv;

    /***
     * Initializes Socket and MonitoringValues
     * @throws IOException
     */
    public Client() {
        try {
            this.sock = new Socket(address, port);
            this.mv = new MonitoringValue();
        }catch (IOException ex){

        }
    }

    /***
     * Collect static values such as OS and hardware
     * @return Formatted String with info
     */
    public String staticValue(){
        String out="";
        out=out+mv.getSystemOP()+"\n";
        out=out+mv.getBootedSystem()+"\n";
        return out;
    }
    /***
     * Return the name of machine
     * @return
     */
    public String getNameMachine(){
        return System.getenv("COMPUTERNAME");
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
    public int login(String email,String password)  {
        try {
            PrintWriter prw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-16"));
            prw.println("login client");
            prw.println(getNameMachine());
            prw.println(email);
            prw.println(password);
            prw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-16"));
            int returnValue = Integer.parseInt(br.readLine());
            if (returnValue == 0) {
                prw.println("monitoringvaluestatic");
                prw.println(getNameMachine());
                prw.println(staticValue());
                prw.flush();
                Thread monitoring = new Thread(new MonitoringThreadClass(sock));
                Thread rmc = new Thread(new RMCThreadClass(sock));
                monitoring.start();
                rmc.start();
            }
            return returnValue;
        }catch(IOException ex){

        }
        return -1;
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
    public int sigin(String email, String password, String password2,String name, String surname, String path,String extension)  {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            ByteArrayOutputStream imagebin = new ByteArrayOutputStream();
            ImageIO.write(image, extension, imagebin);
            PrintWriter prw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-16"));
            if (password.equals(password2)) {
                prw.println("signin");
                prw.println(email);
                prw.println(password);
                prw.println(name);
                prw.println(surname);
                prw.println(imagebin.toByteArray());
                prw.flush();
            } else {
                return 1;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-16"));
            int value = Integer.parseInt(br.readLine());
            sock.close();
            return value;
        }catch (IOException ex){

        }
        return -1;
    }
}
