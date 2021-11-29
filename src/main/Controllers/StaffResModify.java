import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.time.temporal.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

/*****************************************************************
 *                     StaffResModify Class
 * - displays a detailed view of a reservation
 * - allows employee to modify the dates or delete reservation
 *****************************************************************/

public class StaffResModify extends DBConnection implements Initializable {

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
    @FXML private Text logoutTV;
    @FXML private Text mainmenuTV;
    @FXML private TextArea roomsTA;
    @FXML private ImageView hotelIV;
    private Connection conn;
    private Reservation reservation;
    private String accountType;

    public StaffResModify(Reservation reservation, String accountType) {
        this.reservation = reservation;
        this.accountType = accountType;
        System.out.println("Log: Reservation: \n" + reservation);
    }

    // modifies the dates based on what user puts in date picker
    @FXML private void modifyDates(MouseEvent event) {
        System.out.println("Log: StaffResModify -> modifyBtn");
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        LocalDate checkin = checkInDP.getValue();
        LocalDate checkout = checkOutDP.getValue();

        // check if dates are empty and if checkin is not after checkout
        if(checkin == null || checkout == null){
            Toast.makeText(stage, "Error: One or more dates missing", 2000, 500, 500);
        } else if(checkin.isAfter(checkout)){
            Toast.makeText(stage, "Error: Check in date cannot be after checkout", 2000, 500, 500);
        } else if(checkin.equals(reservation.getCheckIn()) && checkout.equals(reservation.getCheckOut())){
            Toast.makeText(stage, "Error: Dates not changed", 2000, 500, 500);
        }

        // TODO: 11/3/2021 ask prof if users can modify dates within 5 days or delete
        /*else if(ChronoUnit.DAYS.between(checkin, checkout) <= 5){
            Toast.makeText(stage, "Error: cannot ", 2000, 500, 500);
        }*/

        // TODO: 11/3/2021 query the database to see if a room is available

        // TODO: 11/3/2021 if room is available then update dates for the reservation in DB

        // TODO: 11/3/2021 if room is not available then send a toast message saying so
    }

    // pops up alert dialog window to confirm deletion of reservation and then queries DB to do so
    @FXML private void deleteRes() throws ClassNotFoundException, SQLException {
        System.out.println("Log: StaffResModify -> deleteBtn");
        ButtonType deleteBtn = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.NONE,"Please confirm reservation deletion", deleteBtn, cancelBtn);
        alert.setTitle("Deletion Confirmation");
        //alert.setContentText("Please confirm reservation deletion");
        Optional<ButtonType> result = alert.showAndWait();

        // if user confirmed reservation deletion
        if(result.orElse(cancelBtn) == deleteBtn){
            PreparedStatement ps = null;
            System.out.println("Delete");
            conn= getConnection();
            ps = conn.prepareStatement("DELETE FROM reservation where reservationId = ?");
            ps.setInt(1, reservation.getResID());
            ps.execute();
            System.out.println("Deleted");
// TODO: 11/3/2021 write a query to delete a reservation from the DB
        }
    }

    String getDays() throws SQLException, ClassNotFoundException {
        String Days = null;
        Connection con = DBConnect.getConnection();
        CallableStatement callableStatement = con.prepareCall("{call hotel.getNumDays(?,?)}");
        callableStatement.setDate(1, Date.valueOf(checkInDP.getValue()));
        callableStatement.setDate(2, Date.valueOf(checkOutDP.getValue()));
        ResultSet rs = callableStatement.executeQuery();
        while(rs.next()){
            Days = rs.getString("days");
        }
        return Days;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            conn = getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //System.out.println("Log: StaffResModify Initialized");
        resIdTF.setText("Reservation: #" + reservation.getResID());
        hNameTF.setText(reservation.getHotelName());
        //hAddressTF.setText();
        //hRatingTF.setText();
        //descriptionTA.setText();
        checkInDP.setValue(reservation.getCheckIn());
        checkOutDP.setValue(reservation.getCheckOut());
        adultsTF.setText("Adults: " + reservation.getAdults());
        childrenTF.setText("Children: " + reservation.getChildren());
        try {
            daysTF.setText("Total days: " + getDays());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        priceTF.setText("Total price: " + reservation.getCost());

        hotelIV.setImage(reservation.getPhoto().getImage());
        //roomsTA.setText();
    }

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == mainmenuTV) {
                if (LoadedUser.getInstance().getUser().getType().equals("CUST"))
                    newScene = FXMLLoader.load(getClass().getResource("UserMainMenu.fxml"));
                else if (LoadedUser.getInstance().getUser().getType().equals("EMP"))
                    newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            } else if(event.getSource() == logoutTV) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                LoginController controller = new LoginController();
                loader.setController(controller);
                newScene = loader.load();
            } else if(event.getSource() == returnTF) {
                if (LoadedUser.getInstance().getUser().getType().equals("CUST"))
                    newScene = FXMLLoader.load(getClass().getResource("UserManage.fxml"));
                else if (LoadedUser.getInstance().getUser().getType().equals("EMP"))
                    newScene = FXMLLoader.load(getClass().getResource("StaffReservation.fxml"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}