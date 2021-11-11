

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;


public class QUERYS extends DBConnection {

    private static Connection conn = null;





    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
            throw e;
        }
    }





    public ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            Connection conn = getConnection();
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
            getConnection().close();
        }
        //Return CachedRowSet
        return crs;
    }


    public ResultSet getAmenitiesByHotelName(String hotelName) throws SQLException, ClassNotFoundException {
        if(!hotelExist(hotelName)){
           System.out.println("Hotel DOES NOT EXIST!");
        }
        //Declare statement, resultSet and CachedResultSet as null
        //Statement stmt = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            Connection conn = getConnection();
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
            getConnection().close();
        }
        //Return CachedRowSet
        return crs;
    }



    public boolean hotelExist(String hotelName) throws SQLException, ClassNotFoundException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            Connection conn = getConnection();
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
            getConnection().close();
        }
        return true;
    }

    public void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            Connection conn = getConnection();
            //Connect to DB (Establish Oracle Connection)

            //Create Statement
            stmt = conn.createStatement();
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
            getConnection().close();
        }
    }














}
