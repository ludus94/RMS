package rmsserver;

import javax.xml.transform.Result;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

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
    public synchronized byte[] imageManagerQuery(String email) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.selectImageUser());
        pst.setString(1,email);
        ResultSet resultSet=pst.executeQuery();
        while(resultSet.next()) {
            return resultSet.getBytes("image");

        }
        return null;
    }
    public synchronized String retriveDevice(String email) throws SQLException{
        PreparedStatement pst=connection.prepareStatement(Query.selectNameMachines());
        pst.setString(1,email);
        ResultSet resultSet=pst.executeQuery();
        if(resultSet.next()==true){
            String out = "";
            while(resultSet.next()){
                out=out+resultSet.getString("name")+"\n";
            }
            return out;
        }else{
            return "Client not connect";
        }
    }
    public synchronized void delateMachine(String name,String email) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.deleteMachine());
        pst.setString(1,name);
        pst.setString(2,email);
        pst.executeQuery();
    }
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
    public synchronized  String Machine(String name) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(Query.selectEmailUser());
        pst.setString(1,name);
        ResultSet resultSet=pst.executeQuery();
        while (resultSet.next())
               return resultSet.getString("email");
        return null;
    }
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
