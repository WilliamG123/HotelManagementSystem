

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;


public class QUERYS extends DBConnection {

  //  private static Connection conn = null;

    Connection conn = getConnection();

    public QUERYS() throws ClassNotFoundException {
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
       // if(!hotelExist(Call)){
         //   System.out.println("Hotel DOES NOT EXIST!");
       // }


        //Declare statement, resultSet and CachedResultSet as null
        //Statement stmt = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            Connection conn = getConnection();
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
           conn.close();
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
            getConnection().close();
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
            conn = getConnection();
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


//use: takes in a hotel name and returns true if exist inside hotel table or false if not
    public boolean hotelExist(String hotelName) throws SQLException, ClassNotFoundException {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
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
           conn.close();
        }
        return true;
    }
//use : takes in update statments i.e ("update hotel set column_name = "" where ...")
    //recommended for testing only
    public void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        PreparedStatement stmt = null;
        try {
           // Connection conn = getConnection();
            //Connect to DB (Establish Oracle Connection)

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
            conn.close();
        }
    }














}
