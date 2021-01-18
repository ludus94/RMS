package rmsserver;

/***
 * Parametric queries for retrieving data in a database
 */
public class Query {
    public static String insertUser(){
        return "insert into users " +
            "values(?,?,?,?,crypt(?,gen_salt('bf')))";
    }

    public static String deleteUser() {
        return "delete from machine " +
                "where email=?;" +
                "delete from users " +
                "where email=?";
    }
    public static String deleteMachine() {
        return "delete from machine " +
                "where name=? and email=?";
    }
    public static String insertMachine(){
        return "insert into machine " +
                "values(?,?,?)";
    }
    public static String selectMachine(){
        return "select * " +
                "from machine " +
                "where email=? and name=?";
    }
    public static String selectEmailUser(){
        return "select * " +
                "from machine " +
                "where name=?";
    }
    public static String selectMachines(){
        return  "select * " +
                "from machine " +
                "where email=?";
    }
    public static String selectNameMachines(){
        return  "select name " +
                "from machine " +
                "where email=?";
    }
    public static String selectUser(){
        return "select email,name,surname,image " +
                "from users "+
                "where email=?";
    }
    public static String selectImageUser(){
        return "select image"+
                " from users " +
                "where email=?";
    }
    public static String selectIP(){
        return "select IP " +
                "from machine " +
                "where email=?";
    }
    public static String selectPassword(){ return "select password " +
            "from users " +
            "where email=?";
    }
    public static String matchingPassword(){
        return "select email,name,surname,image " +
            "from users " +
            "where users.email=? and users.password=crypt(?,password)";
    }
}
