package rmsserver;

import javax.xml.transform.Result;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

public class DbRms {
    private Connection connection;
    private String dbname;
    private String username;
    private String passworddb;

    public DbRms(String dbname,String username,String passworddb) {
        try {
            this.dbname=dbname;
            this.username=username;
            this.passworddb=passworddb;
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname,username,passworddb);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public ResultSet loginQuery(String email, String password) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.matchingPassword());
        pst.setString(1,email);
        pst.setString(2,password);
        ResultSet resultSet=pst.executeQuery();
        return resultSet;
    }
    public int signin(String name,String surname,String email,String password,byte[] image) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.selectUser());
        pst.setString(1,email);
        ResultSet resultSet=pst.executeQuery();
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(image);
        if (resultSet==null){
            pst=connection.prepareStatement(Query.insertUser());
            pst.setString(1,email);
            pst.setString(2,name);
            pst.setString(3,surname);
            pst.setBinaryStream(4,byteArrayInputStream);
            pst.setString(5,password);
            pst.executeUpdate();
            return 0;
        }
        return 2;
    }
    public void loginmachine(String machine,String email){

    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/DB_RMS","postgres","Fabrizio1995");
        String name="Fabrizio";
        String surname="Lettieri";
        String email="fab.lettieri@hotmail.com";
        String password ="sas";
        File file = new File("src/logoapp.jpeg");
        FileInputStream fis = new FileInputStream(file);
        /*PreparedStatement pst = conn.prepareStatement(Query.selectUser());
        pst.setString(1,email);
        ResultSet rst = pst.executeQuery();*/
        //if(rst==null){
          PreparedStatement pst = conn.prepareStatement(Query.insertUser());
           pst.setString(1,email);
           pst.setString(2,name);
           pst.setString(3,surname);
           pst.setBinaryStream(4, fis, file.length());
           pst.setString(5,password);
           pst.executeUpdate();
        /*}else{
            System.out.println("l'user è già presente");
        }*/

        /*pst = conn.prepareStatement(Query.selectMachine());
        pst.setString(1,"1987yh");
        rst = pst.executeQuery();
        if(rst==null) {
            pst = conn.prepareStatement(Query.insertMachine());
            pst.setString(1, "1987yh");
            pst.setString(2, "fab.lettieri@hotmail.com");
            pst.setString(3, "Windows Machine");
            pst.setString(4, "199.234.1.1");
            pst.setInt(5, 80);
            pst.executeUpdate();
        }else{
            System.out.println("macchina già presente");
        }
        pst.close();
        fis.close();
        conn.close();

        String email="fab.lettieri@hotmail.com";
        PreparedStatement pst2 = conn.prepareStatement(Query.deleteUser());
        pst2.setString(1,email);
        pst2.setString(2,email);
        pst2.executeUpdate();
        pst2.close();
        conn.close();*/
    }
}
