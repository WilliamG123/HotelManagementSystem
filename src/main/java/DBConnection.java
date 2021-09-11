import java.sql.*;

public class DBConnection {


    public DBConnection() {}


        String CREDENTIALS_STRING = "jdbc:mysql:///hotel?cloudSqlInstance=hotel-325700:us-central1:datahaven&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=root&password=DBhotel123";
        // String username ="root";
        //String password = "DBhotel123";

        Connection dbCon = null;
        Statement stmt = null;
        ResultSet rs = null;

        public Connection getConnection(){

       // String query ="Select * from consumer";

        try {

            //getting database connection to MySQL server

            dbCon = DriverManager.getConnection(CREDENTIALS_STRING);
            System.out.println("Connected!");
            //getting PreparedStatment to execute query
            //stmt = dbCon.prepareStatement(query);

            //Resultset returned by query
           // rs = stmt.executeQuery(query);

           // while(rs.next()){
               // String result = rs.getString(2);
             //   System.out.println("guest name is : " + result);
           // }
        } catch (SQLException ex) {
            System.out.println(ex);
            ex.printStackTrace();
            ex.getCause();
        }

        return dbCon;
    }
}
