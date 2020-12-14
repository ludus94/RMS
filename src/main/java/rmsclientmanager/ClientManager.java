package rmsclientmanager;

import rmsclient.MonitoringThreadClass;
import rmsclient.RMCThreadClass;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    private String image;

    public ClientManager(String username) throws IOException {
        this.sock=new Socket(address,port);
        this.devicesList = new ArrayList<>();
        this.username=username;
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
           this.retrieveDevices(br);
           this.controll();
        }
        return returnValue;
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
    public void retrieveDevices(BufferedReader br) throws IOException {
        PrintWriter printWriter=new PrintWriter(new OutputStreamWriter(this.sock.getOutputStream(),"UTF-16"));
        printWriter.println("retrieve device");
        printWriter.flush();
        ArrayList<String> out=new ArrayList<>();
        String input=null;
        while ((input = br.readLine()) != null) {
                out.add(input);
        }
        this.devicesList=out;
    }
    public void controll(){
        while(true) {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                if (input.readLine().contains("deviceavabile")) {
                    retrieveDevices(input);
                }
                if (input.readLine().contains("monitoringvalue")) {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
