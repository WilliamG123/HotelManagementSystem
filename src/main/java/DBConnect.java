
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Narayan
 */

public class DBConnect {

   private static String dbName = System.getenv("RDS_DB_NAME");
   private static String userName = System.getenv().get("RDS_USERNAME");
    private static String password = System.getenv().get("RDS_PASSWORD");
    private static String hostname = System.getenv().get("RDS_HOSTNAME");
    private static String port = System.getenv().get("RDS_PORT");
    // String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + "?user=" + userName + "&password=" + password;
    private static Connection conn;
    //String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" +"useSSL=false"+"&user=" + userName + "&password=" + password;
    private static final String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password+"&useSSL=false";

    public static Connection connect() throws SQLException{
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(ClassNotFoundException cnfe){
            System.err.println("Error: "+cnfe.getMessage());
        }catch(InstantiationException ie){
            System.err.println("Error: "+ie.getMessage());
        }catch(IllegalAccessException iae){
            System.err.println("Error: "+iae.getMessage());
        }

        conn = DriverManager.getConnection(jdbcUrl);
        return conn;
    }
    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        if(conn !=null && !conn.isClosed())
            return conn;
        connect();
        return conn;

    }
}