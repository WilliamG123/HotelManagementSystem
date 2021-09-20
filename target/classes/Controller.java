import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Button BTN_insert;
    public Button BTN_update;
    public Button BTN_delete;
    public Button BTN_view;
    public TextArea TA_name;
    public TextArea TA_email;
    @FXML
    final DatePicker TA_dob = new DatePicker(LocalDate.now());

    CRUD instance ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBConnection DB = new DBConnection();
        instance = new CRUD();
        DatePickerSkin skin = new DatePickerSkin(new DatePicker());
        Node calendarControl = skin.getPopupContent();
        calendarControl.autosize();

        TA_dob.setOnAction(event -> {

            LocalDate date = TA_dob.getValue();
            System.out.println("Selected date: " + date);

        });




        BTN_insert.setOnAction(event ->{
            String name = TA_name.getText();
            String email = TA_email.getText();
            LocalDate dob = TA_dob.getValue();

            try {
                if(instance.insertconsumerdata(name, email, dob)){
                    System.out.println("insertion successful");
                }
                else{
                    System.out.println("insertion failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });



        BTN_delete.setOnAction(event ->{
            String name = TA_name.getText();
            String email = TA_email.getText();
            LocalDate dob = TA_dob.getValue();

            try {
                if(instance.deleteconsumer(email)){
                    System.out.println("insertion successful");
                }
                else{
                    System.out.println("insertion failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        BTN_view.setOnAction(event ->{
            String name = TA_name.getText();
            String email = TA_email.getText();
            LocalDate dob = TA_dob.getValue();

            try {
               ResultSet rs= instance.viewconsumer(email);
                if(rs != null){
                    System.out.println("insertion successful");
                }
                else{
                    System.out.println("insertion failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });









    }
}
