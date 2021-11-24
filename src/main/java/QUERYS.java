

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;
import java.time.LocalDate;


public class QUERYS {

    private static String dbName = System.getenv("RDS_DB_NAME");
    private static String userName = System.getenv().get("RDS_USERNAME");
    private static String password = System.getenv().get("RDS_PASSWORD");
    private static String hostname = System.getenv().get("RDS_HOSTNAME");
    private static String port = System.getenv().get("RDS_PORT");
    // String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + "?user=" + userName + "&password=" + password;
    Connection dbCon = null;
    //String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" +"useSSL=false"+"&user=" + userName + "&password=" + password;

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password+"&useSSL=false";
    private static Connection conn = null;

   // Connection conn = getConnection();

    public QUERYS() throws ClassNotFoundException {
    }

    public static void dbConnect() throws SQLException, ClassNotFoundException {
        //Setting Oracle JDBC Driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
            throw e;
        }

        System.out.println("Oracle JDBC Driver Registered!");
        //Establish the Oracle Connection using Connection String
        try {
            conn = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }
    }


    public  void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
            throw e;
        }
    }

    public ResultSet dbExecuteCallQuery(String Call) throws SQLException, ClassNotFoundException {

        //Declare statement, resultSet and CachedResultSet as null
        //Statement stmt = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
           dbConnect();
            //Callable statement is an extension of prepared statement so it prevents against SQL INJECTION
            callableStatement = conn.prepareCall(Call);
            //callableStatement.setString(1, Call);

            //Execute select (query) operation
            resultSet = callableStatement.executeQuery();
            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (callableStatement != null) {
                //Close Statement
                callableStatement.close();
            }
            //Close connection
           dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }



//Use: to execute basic Select Queries only i.e ("Select * from table_name" , "Select hotel.hotel_name ...")
//recommended for testing only
    public ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            dbConnect();
            //Connect to DB (Establish Oracle Connection)
            //Connection conn = getConnection();
            System.out.println("Select statement: " + queryStmt + "\n");
            //Create statement
            stmt = conn.createStatement();
            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);
            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }


    public ResultSet getOverLappingDates(LocalDate checkin, LocalDate checkout, String roomType, int hotelID) throws SQLException, ClassNotFoundException {
     //CALL `hotel`.`getOverlapDateRanges`('2021-10-01', '2021-11-28', 'Queen' , 4);
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();

            //Callable statement is an extension of prepared statement so it prevents against SQL INJECTION
            callableStatement = conn.prepareCall("{CALL hotel.getOverlapDateRanges(?, ?, ? , ?)}");
            callableStatement.setDate(1, Date.valueOf(checkin));
            callableStatement.setDate(2, Date.valueOf(checkout));
            callableStatement.setString(3, roomType);
            callableStatement.setInt(4, hotelID);

            //Execute select (query) operation
            resultSet = callableStatement.executeQuery();
            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (callableStatement != null) {
                //Close Statement
                callableStatement.close();
            }
            //Close connection
            conn.close();
        }
        //Return CachedRowSet
        return crs;
    }




    //use: returns result set for total amenities for a given hotel name. i.e "The Magnolia All Suites"
    public ResultSet getCountOfAvailRoomsByDateRange(String RoomType, int hotelID, LocalDate checkin , LocalDate checkout) throws SQLException, ClassNotFoundException {
        // if(!hotelExist(hotelName)){
        //   System.out.println("Hotel DOES NOT EXIST!");
        // }
        //Declare statement, resultSet and CachedResultSet as null
        //Statement stmt = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            //Callable statement is an extension of prepared statement so it prevents against SQL INJECTION
            callableStatement = conn.prepareCall("{CALL getAmenitiesByHotel(?)}");
            callableStatement.setString(1, hotelName);

            //Execute select (query) operation
            resultSet = callableStatement.executeQuery();
            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (callableStatement != null) {
                //Close Statement
                callableStatement.close();
            }
            //Close connection
            conn.close();
        }
        //Return CachedRowSet
        return crs;
    }



    //use: returns result set for total amenities for a given hotel name. i.e "The Magnolia All Suites"
    public ResultSet getAmenitiesByHotelName(String hotelName) throws SQLException, ClassNotFoundException {
       // if(!hotelExist(hotelName)){
        //   System.out.println("Hotel DOES NOT EXIST!");
       // }
        //Declare statement, resultSet and CachedResultSet as null
        //Statement stmt = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            //Callable statement is an extension of prepared statement so it prevents against SQL INJECTION
            callableStatement = conn.prepareCall("{CALL getAmenitiesByHotel(?)}");
            callableStatement.setString(1, hotelName);

            //Execute select (query) operation
            resultSet = callableStatement.executeQuery();
            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (callableStatement != null) {
                //Close Statement
                callableStatement.close();
            }
            //Close connection
           conn.close();
        }
        //Return CachedRowSet
        return crs;
    }

    public ResultSet getAllAvailHotels() throws SQLException, ClassNotFoundException {
        // if(!hotelExist(hotelName)){
        //   System.out.println("Hotel DOES NOT EXIST!");
        // }
        //Declare statement, resultSet and CachedResultSet as null
        //Statement stmt = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            //Callable statement is an extension of prepared statement so it prevents against SQL INJECTION
            callableStatement = conn.prepareCall("{call hotel.getListAvailHotels}");
//            callableStatement.setString(1, hotelName);

            //Execute select (query) operation
            resultSet = callableStatement.executeQuery();
            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (callableStatement != null) {
                //Close Statement
                callableStatement.close();
            }
            //Close connection
           dbConnect();
        }
        //Return CachedRowSet
        return crs;
    }


//use: takes in a hotel name and returns true if exist inside hotel table or false if not
    public boolean hotelExist(String hotelName) throws SQLException, ClassNotFoundException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            dbConnect();
            //Connection conn = getConnection();
            callableStatement = conn.prepareCall("{CALL doesHotelExist(?)}");
            callableStatement.setString(1, hotelName);
            resultSet = callableStatement.executeQuery();
            if( resultSet.getInt(1)==0)
                return false;
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (callableStatement != null) {
                callableStatement.close();
            }
           dbDisconnect();
        }
        return true;
    }
    //use : takes in update statments i.e ("update hotel set column_name = "" where ...")
    //recommended for testing only
    public void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        PreparedStatement stmt = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnect();
            //Create Statement
            stmt = conn.prepareStatement(sqlStmt);
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
           dbDisconnect();
        }
    }














}
