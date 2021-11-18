import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SharedBooking extends DBConnection implements Initializable {

    @FXML private Text hNameTF;
    @FXML private Text hAddressTF;
    @FXML private Text hRatingTF;
    @FXML private Text checkInTF;
    @FXML private Text checkOutTF;
    @FXML private Text returnTF;
    @FXML private TextArea descriptionTF;
    @FXML private TextField adultsTF;
    @FXML private TextField childrenTF;
    @FXML private DatePicker checkInDP;
    @FXML private DatePicker checkOutDP;
    @FXML private Button addRoomBtn;
    @FXML private Button bookBtn;
    @FXML private TableView<Room> roomTB;
    @FXML private TableColumn<Room, String> styleColumn;
    // TODO: 11/17/2021 make occupancy column
    @FXML private TableColumn<Room, String> occupancyColumn;
    @FXML private TableColumn<Room, String> availableColumn;
    @FXML private TableColumn<Room, String> priceColumn;
    @FXML private ListView<String> amenitiesLV;
    @FXML private ObservableList<Room> roomsList;

    private Hotels hotel;
    private String accountType;

    // TODO: 11/17/2021 write handler method for the book button
    @FXML void book(MouseEvent event) { }

    // TODO: 11/17/2021 write method for adding a room
    @FXML void addRoom(ActionEvent event) { }


    public SharedBooking(Hotels hotel, String accountType) {
        this.hotel = hotel;
        this.accountType = accountType;
    }

    public List<String> getRoomTypes() throws ClassNotFoundException, SQLException {
        List<String> roomsTypes = new ArrayList<>();
        Connection con = null;
        con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call hotel.getAllRoomTypes(?)}");
        callableStatement.setString(1, hotel.getHotelId());
        ResultSet rs = callableStatement.executeQuery();

        //loop through the resultSet & add each room type to the ArrayList
        while (rs.next()) {
            roomsTypes.add(rs.getString("type_name"));
        }
        return roomsTypes;
    }

    public void populateRoomTable() throws ClassNotFoundException, SQLException {
        // TODO: 11/18/2021 write a way to dynamically query the info

        Connection con = null;
        con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call hotel.getRoomByType(?, ?)}");
        callableStatement.setString(1, hotel.getHotelId());
        ResultSet rs = callableStatement.executeQuery();

        //loop through the resultSet & add each room type to the ArrayList
        while (rs.next()) {
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            populateAmenitiesList();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        hNameTF.setText(this.hotel.getHotelname());
        hAddressTF.setText(this.hotel.getHoteladdr());
        hRatingTF.setText(String.valueOf(this.hotel.getRating()) + "/10 Stars");
        descriptionTF.setText(hotel.getHoteldesc());

        // TODO: 11/17/2021 delete/move this stuff to a populate TableView method
        roomsList = FXCollections.observableArrayList();
        roomsList.add(new Room(106, 300,"King", 1, 33.00));
        roomsList.add(new Room(129, 250,"Queen", 1, 33.00));
        String[] amenities = {"pool", "balcony", "breakfast"};
        ArrayList<Reservation> reservations = new ArrayList<>();
        //Property property = new Property("La Quinta", "nice hotel on the beach", "1 UTSA circle San Antonio", amenities, rooms, reservations, 8, 99);

        styleColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        //occupancyColumn.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        //availableColumn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        roomTB.setItems(roomsList);
    }

    public void populateAmenitiesList() throws ClassNotFoundException, SQLException {
        Connection con = null;
        con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call getAmenitiesByHotel(?)}");
        callableStatement.setString(1, hotel.getHotelname());
        ResultSet rs = callableStatement.executeQuery();

        //loop through the resultSet & add each amenity to the ListView
        while(rs.next()) {
            amenitiesLV.getItems().add(rs.getString("Amenities_desc"));
        }

    }

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;
        try{
            if(event.getSource() == returnTF){
                newScene = FXMLLoader.load(getClass().getResource("UserCreate.fxml"));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
