package rmsserver;

import javax.xml.transform.Result;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

/***
 * Class to manage a connection on database
 */
public class DbRms {
    private Connection connection;
    private String dbname;
    private String username;
    private String passworddb;

    /***
     * Contractor of class DbRMS
     * @param dbname name of database
     * @param username username of database
     * @param passworddb password of database
     */
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

    /***
     * Method used to login an user. The query is compiled ar runtime
     * @param email email of user
     * @param password password of user
     * @return 0 if success
     * @return 1 if user is not registered
     * @throws SQLException
     */
    public synchronized int loginQuery(String email, String password) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.matchingPassword());
        pst.setString(1,email);
        pst.setString(2,password);
        ResultSet resultSet=pst.executeQuery();
        if(resultSet.next()==false){
            return 1;
        }else{
            return 0;
        }
    }

    /***
     * Retrive image on the database with email of user
     * @param email email user
     * @return byte image
     * @throws SQLException
     */
    public synchronized byte[] imageManagerQuery(String email) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.selectImageUser());
        pst.setString(1,email);
        ResultSet resultSet=pst.executeQuery();
        while(resultSet.next()) {
            return resultSet.getBytes("image");

        }
        return null;
    }

    /***
     * Return a device registered at user
     * @param email of user
     * @return string of machine
     * @throws SQLException
     */
    public synchronized String retriveDevice(String email) throws SQLException{
        PreparedStatement pst=connection.prepareStatement(Query.selectNameMachines());
        pst.setString(1,email);
        ResultSet resultSet=pst.executeQuery();
        Boolean entry=false;
        String out = "";
            while(resultSet.next()){
                entry=true;
                out=out+resultSet.getString("name")+"\n";
            }
        if (entry==true) {
               return out;
        }
        else{
            return "Client not connect";
        }
    }

    /***
     * Retrive a name of machine
     * @param email of user
     * @return An array list contains a name of machine
     * @throws SQLException
     */
    public synchronized ArrayList<String> retriveNameMachine(String email) throws SQLException{
        PreparedStatement pst=connection.prepareStatement(Query.selectNameMachines());
        pst.setString(1,email);
        ResultSet resultSet=pst.executeQuery();
        Boolean entry=false;
        ArrayList<String> device=new ArrayList<>();
        while(resultSet.next()){
            entry=true;
            device.add(resultSet.getString("name"));
        }
        if (entry==true) {
            return device;
        }
        else{
            device.add("Client not connect");
            return device;
        }
    }

    /***
     * delete a Machine
     * @param name of machine
     * @param email of user
     * @throws SQLException
     */
    public synchronized void delateMachine(String name,String email) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.deleteMachine());
        pst.setString(1,name);
        pst.setString(2,email);
        pst.executeQuery();
    }

    /***
     * Method used to compile a runtime query for user's signing
     * @param name of user
     * @param surname of user
     * @param email of user
     * @param password of user
     * @param image of user
     * @return
     * @throws SQLException
     */
    public synchronized int signin(String name,String surname,String email,String password,byte[] image) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.selectUser());
        pst.setString(1,email);
        ResultSet resultSet=pst.executeQuery();
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(image);
        if (resultSet.next()==false){
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

    /***
     * Method to compile a query for a login user's machine
     * @param name of machine user
     * @param email of user
     * @throws SQLException
     */
    public synchronized  void loginmachine(String name,String email) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.selectMachine());
        pst.setString(1,email);
        pst.setString(2,name);
        ResultSet resultSet=pst.executeQuery();
        if (resultSet.next()==false){
            PreparedStatement pst2=connection.prepareStatement(Query.insertMachine());
            String id=name+email;
            pst2.setString(1, String.valueOf(id.hashCode()));
            pst2.setString(2,email);
            pst2.setString(3,name);
            pst2.executeUpdate();
        }
    }

    /***
     * Return a email's user associated a machine
     * @param name machine
     * @return
     * @throws SQLException
     */
    public synchronized  String Machine(String name) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.selectEmailUser());
        pst.setString(1,name);
        ResultSet resultSet=pst.executeQuery();
        while (resultSet.next())
               return resultSet.getString("email");
        return null;
    }

    /***
     * Return a list of machine
     * @param email of user
     * @return
     * @throws SQLException
     */
    public synchronized ArrayList<String> retriveListDevice(String email) throws  SQLException{
        PreparedStatement preparedStatement= connection.prepareStatement(Query.selectMachines());
        preparedStatement.setString(1,email);
        ResultSet result=preparedStatement.executeQuery();
        ArrayList<String> out=new ArrayList<>();
        while(result.next()){
            out.add(result.getString("name"));
        }
        return out;
    }
}
