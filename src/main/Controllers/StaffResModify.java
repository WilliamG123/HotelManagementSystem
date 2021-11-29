import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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
    @FXML private TextField YorN;
    private Connection conn;
    private Reservation reservation;
    private String accountType;
    private Hotels hotel;
    int userID;
    LocalDate today = LocalDate.now();

    @FXML private ObservableList<Room> roomTypes;

    public StaffResModify(Reservation reservation, String accountType) {
        this.reservation = reservation;
        this.accountType = accountType;
        System.out.println("Log: Reservation: \n" + reservation);
    }
    public void populateRoomTable() throws ClassNotFoundException, SQLException {

        Connection con = null;
        con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call hotel.getCountTypeAvailRooms2(?, ?, ?, ?)}");
        callableStatement.setDate(3, Date.valueOf(checkInDP.getValue()));
        callableStatement.setDate(4, Date.valueOf(checkOutDP.getValue()));
        roomTypes = getRoomTypes();
        ObservableList<String> typeStrings = FXCollections.observableArrayList();

        for(Room r : roomTypes) {
            System.out.println(r.getType() + " " + r.getAmountAvailable() + " " + r.getPrice());
            typeStrings.add(r.getType());
        }



        for(int i = 0; i < roomTypes.size(); i++) {
            callableStatement.setString(1, roomTypes.get(i).getType());
            callableStatement.setString(2, reservation.getHotelName());

            ResultSet rs = callableStatement.executeQuery();

            rs.next();
            roomTypes.get(i).setAmountAvailable(rs.getInt("count"));
            roomTypes.get(i).setPrice(rs.getDouble("room_rate"));
        }

    }
    public ObservableList<Room> getRoomTypes() throws ClassNotFoundException, SQLException {
        ObservableList<Room> roomsTypes = FXCollections.observableArrayList();
        Connection con = null;
        con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call hotel.getAllRoomTypes2(?)}");
        callableStatement.setString(1, reservation.getHotelName());
        ResultSet rs = callableStatement.executeQuery();

        //loop through the resultSet & add each room type to the ArrayList
        while (rs.next()) {
            roomsTypes.add(new Room(rs.getString("type_name")));
            System.out.println(rs.getString("room_type_desc"));
        }
        return roomsTypes;
    }
    public void restrictDatePicker(DatePicker datePicker) {
        datePicker.setDayCellFactory((DatePicker param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    //...
                    addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                } else {
                    //...
                    removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                }
                if (item.isBefore(today)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #FF4500;");
                }
            }
        });
    }
    EventHandler<MouseEvent> mouseClickedEventHandler = clickEvent -> {
        if (clickEvent.getButton() == MouseButton.PRIMARY) {

        }
        clickEvent.consume();
    };


    // modifies the dates based on what user puts in date picker
    @FXML private void modifyDates(MouseEvent event) throws ClassNotFoundException, SQLException {
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
        }else if(reservation.getCheckIn().isAfter(today)){
            Toast.makeText(stage, "Error: Sorry cannot change dates for current active reservation", 2000, 500, 500);
        }else if(reservation.getCheckIn().isBefore(today)){
            YorN.setText("YES");
            PreparedStatement ps = null;


            conn= getConnection();
            ps = conn.prepareStatement("UPDATE reservation set check_in = ? , check_out = ? where reservationId = ?");
            ps.setDate(1, Date.valueOf(checkInDP.getValue()));
            ps.setDate(1, Date.valueOf(checkOutDP.getValue()));
            ps.setInt(1, reservation.getResID());
            ps.execute();
            System.out.println("Dates Changed");
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
        checkInDP.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);


                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
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
        checkInDP.setValue(today);
        restrictDatePicker(checkInDP);
        restrictDatePicker(checkOutDP);
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