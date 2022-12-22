package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    // store the connection object from the sql connection
    static Connection connection = null;
    public  static  Connection getConnection(){
        if(connection!=null){
            return connection;
        }
        // to create a connection these values required
        String db ="searchaccio";
        String user = "root";
        String pwd = "amitkushwaha@1998";
        return  getConnection(db,user,pwd);
    }
    // now override this object
    private  static  Connection getConnection(String db, String user, String pwd){
        // throw a exception
        try{
            //add the library jdbc
            Class.forName("com.mysql.cj.jdbc.Driver");
            //using the driver manager class in the sql going to get connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?user="+user+"&password="+pwd);

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return connection;
    }
}
