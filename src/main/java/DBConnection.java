import com.mysql.jdbc.log.Log;

import java.sql.*;

public class DBConnection {


    public DBConnection() {}
    //jdbc:driver://hostname:port/dbName?user=userName&password=password
    Log logger;
    String dbName = System.getenv("RDS_DB_NAME");
    String userName = System.getenv().get("RDS_USERNAME");
    String password = System.getenv().get("RDS_PASSWORD");
    String hostname = System.getenv().get("RDS_HOSTNAME");
    String port = System.getenv().get("RDS_PORT");
   // String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + "?user=" + userName + "&password=" + password;
    Connection dbCon = null;
    //String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" +"useSSL=false"+"&user=" + userName + "&password=" + password;
    String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password+"&useSSL=false";



    public Connection getConnection() throws ClassNotFoundException {
        System.out.println(jdbcUrl);
        try {
            System.out.println("Loading driver...");
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        System.out.println("Driver loaded!");
        try {

            //getting database connection to MySQL server

            dbCon = DriverManager.getConnection(jdbcUrl);
            System.out.println("Connected!");

        } catch (SQLException ex) {
            System.out.println(ex);
            ex.printStackTrace();
            ex.getCause();
        }

        return dbCon;
    }
}
