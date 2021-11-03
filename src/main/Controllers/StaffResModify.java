import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/*****************************************************************
 *                     StaffResModify Class
 * - displays a detailed view of a reservation
 * - allows employee to modify the dates or delete reservation
 *****************************************************************/

public class StaffResModify implements Initializable {

    @FXML private Text hNameTF;
    @FXML private Text hAddressTF;
    @FXML private Text hRatingTF;
    @FXML private TextArea descriptionTA;
    @FXML private Text resIdTF;
    @FXML private DatePicker checkInDP;
    @FXML private DatePicker checkOutDP;
    @FXML private Text daysTF;
    @FXML private Text priceTF;
    @FXML private Text adultsTF;
    @FXML private Text childrenTF;
    @FXML private Text returnTF;
    @FXML private Text modDatesTF;
    @FXML private Text deleteTF;
    @FXML private TextArea roomsTA;

    private Reservation reservation;

    public StaffResModify(Reservation reservation){
        this.reservation = reservation;
        System.out.println("Log: Reservation: \n" + reservation);
    }

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        // try block attempts to load a new scene
        try {
            if (event.getSource() == returnTF) {
                newScene = FXMLLoader.load(getClass().getResource("StaffReservation.fxml"));
                System.out.println("Log: StaffResModify -> returnBtn");
            } else if(event.getSource() == modDatesTF) {
                System.out.println("Log: StaffResModify -> modifyBtn");
                modifyDates(event);
            } else if(event.getSource() == deleteTF){
                System.out.println("Log: StaffResModify -> deleteBtn");
                deleteRes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // modifies the dates based on what user puts in date picker
    private void modifyDates(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        // TODO: 11/3/2021 write a query to see if dates are available & then modify the request if so
        LocalDate checkin = checkInDP.getValue();
        LocalDate checkout = checkOutDP.getValue();

        if(checkin.isAfter(checkout)){
            Toast.makeText(stage, "Error: Check in date cannot be before checkout", 5000, 1000, 1000);
        }
    }

    // deletes a reservation in the database
    private void deleteRes() {
        // TODO: 11/3/2021 write a query to delete a reservation from the DB
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //System.out.println("Log: StaffResModify Initialized");

        // TODO: 11/3/2021 uncomment and fix to initialize all TextViews with the appropriate reservation user data
        resIdTF.setText("#" + reservation.getResID());
        hNameTF.setText(reservation.getHotelName());
        //hAddressTF.setText();
        //hRatingTF.setText();
        //descriptionTA.setText();
        checkInDP.setValue(reservation.getCheckIn());
        checkOutDP.setValue(reservation.getCheckOut());
        adultsTF.setText("Adults: " + reservation.getAdults());
        childrenTF.setText("Children: " + reservation.getChildren());
        //daysTF.setText("Total days: " + reservation.);
        priceTF.setText("Total price: " + reservation.getCost());
        //roomsTA.setText();
    }
}