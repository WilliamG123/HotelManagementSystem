import javafx.scene.control.Button;

import javax.swing.text.View;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class CRUD extends DBConnection {

        CRUD(){};
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

        PreparedStatement insertstmt;
        String query = "INSERT into consumer(name, email, dob) values( ?, ?, ?)";
        insertstmt =  DB.getConnection().prepareStatement(query);

        insertstmt.setString(2, email);


        int result = insertstmt.executeUpdate();

        if (result != 1){
            return true;
        }else
            return false;

    }




}
