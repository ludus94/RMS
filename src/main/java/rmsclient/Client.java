package rmsclient;
import facade.MonitoringValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

public class Client {
    private Socket sock;
    private int port=33333;
    private static final String address="ludovicorusso.ddns.net";
    private MonitoringValue mv;

    public Client() throws IOException {
        this.sock=new Socket(address,port);
        this.mv=new MonitoringValue();
    }

    public String monitoringSendValueStatic(){
        String out="";
        out=mv.getSystemOP()+"\n";
        out=out+mv.getBootedSystem()+"\n";
        return out;
    }

    public int login(String email,String password) throws IOException {
        PrintWriter prw=new PrintWriter(new OutputStreamWriter(sock.getOutputStream(),"UTF-16"));
        prw.println("login");
        prw.println(email);
        prw.println(password);
        prw.flush();
        BufferedReader br=new BufferedReader(new InputStreamReader(sock.getInputStream(),"UTF-16"));
        return Integer.parseInt(br.readLine());
    }

    /***
     *
     * @param email
     * @param password
     * @param password2
     * @param name
     * @param surname
     * @param path
     * @param extension
     * @return code
     * code 0: success
     * code 1: password is not equal to password 2
     * code 2: user exists in system
     * code 3: generical error
     * @throws IOException
     */
    public int sigin(String email, String password, String password2,String name, String surname, String path,String extension) throws IOException {
            BufferedImage image=ImageIO.read(new File(path));
            ByteArrayOutputStream imagebin=new ByteArrayOutputStream();
            ImageIO.write(image,extension,imagebin);
            PrintWriter prw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-16"));
            if(password.equals(password2)) {
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
}
