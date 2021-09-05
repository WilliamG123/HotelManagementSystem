import javafx.scene.control.Button;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CRUD extends DBConnection {

        CRUD(){}
    Button insert, update, delete, view;

    DBConnection DB = new DBConnection();

        public boolean insertconsumerdata(String name, String email, LocalDate dob) throws SQLException {

           PreparedStatement insertstmt;
           String query = "INSERT into consumer(name, email, dob) values( ?, ?, ?)";
            insertstmt =  DB.getConnection().prepareStatement(query);
            insertstmt.setString(1, name);
            insertstmt.setString(2, email);
            insertstmt.setDate(3, Date.valueOf(dob));

            int result = insertstmt.executeUpdate();

            if (result == 1){
                return true;
            }else
                return false;

        }



    public boolean deleteconsumer(String email) throws SQLException {

        PreparedStatement deletestmt;
        String query = "DELETE from consumer WHERE email=?";
        deletestmt =  DB.getConnection().prepareStatement(query);

        deletestmt.setString(1, email);


        int result = deletestmt.executeUpdate();

        if (result== 1){
            return true;
        }else
            return false;

    }

    public ResultSet viewconsumer(String email) throws SQLException {
        String query = "Select * from consumer WHERE email=?";
        PreparedStatement viewstmt =
                getConnection().prepareStatement(query);

        viewstmt.setString(1, email);


        ResultSet rs = viewstmt.executeQuery();
        while(rs.next()){
            int id = rs.getInt("cons_id");
            String name = rs.getString("name");
            String email2 = rs.getString("email");
            Date dob = rs.getDate("dob");
            System.out.println("consumer info : \n");
            System.out.println(id+" "+name+" "+email+" "+dob);

        }
        return rs;

    }



}
