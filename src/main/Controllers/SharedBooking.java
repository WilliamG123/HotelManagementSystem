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
    @FXML private Button removeRoomBtn1;
    // rooms available table
    @FXML private TableView<Room> roomTB;
    @FXML private TableColumn<Room, String> styleColumn;
// TODO: 11/17/2021 make occupancy column
    @FXML private TableColumn<Room, String> occupancyColumn;
    @FXML private TableColumn<Room, String> availableColumn;
    @FXML private TableColumn<Room, String> priceColumn;
    // cart variables
    @FXML private TableView<Room> cartTV;
    @FXML private TableColumn<?, ?> style2Column;
    @FXML private TableColumn<?, ?> amount2Column;
    @FXML private TableColumn<?, ?> price2Column;

    @FXML private ListView<String> amenitiesLV;
    @FXML private ObservableList<Room> roomsList;
    @FXML private ChoiceBox<String> roomCB;

    private Hotels hotel;
    private String accountType;

// TODO: 11/17/2021 write handler method for the book button
    @FXML void book(MouseEvent event) { }

    @FXML void roomChange(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow(); // for displaying Toast error messages
        // TODO: 11/18/2021 write fucntionality for adding a room to the cart
        String choice;

        if(event.getSource() == addRoomBtn) {
            choice = roomCB.getValue();
            if(choice == null){
                Toast.makeText(stage, "Error: no room selected", 2000, 500, 500);
                return;
            }

        }

        // TODO: 11/18/2021 write functionality for removing a room from the cart
    }

    public SharedBooking(Hotels hotel, String accountType) {
        this.hotel = hotel;
        this.accountType = accountType;
    }

    public ObservableList<Room> getRoomTypes() throws ClassNotFoundException, SQLException {
        ObservableList<Room> roomsTypes = FXCollections.observableArrayList();
        Connection con = null;
        con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call hotel.getAllRoomTypes(?)}");
        callableStatement.setString(1, hotel.getHotelId());
        ResultSet rs = callableStatement.executeQuery();

        //loop through the resultSet & add each room type to the ArrayList
        while (rs.next()) {
            roomsTypes.add(new Room(rs.getString("type_name")));
        }
        return roomsTypes;
    }

    public void populateRoomTable() throws ClassNotFoundException, SQLException {
// TODO: 11/18/2021 write a way to dynamically query the info
        Connection con = null;
        con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call hotel.getRoomByType(?, ?)}");

        ObservableList<Room> roomTypes = getRoomTypes();
        ObservableList<String> typeStrings = FXCollections.observableArrayList();

        for(Room r : roomTypes) {
            System.out.println(r.getType() + " " + r.getAmountAvailable() + " " + r.getPrice());
            typeStrings.add(r.getType());
        }

        for(Room room : roomTypes) {
            callableStatement.setString(1, room.getType());
            callableStatement.setString(2, hotel.getHotelId());
            ResultSet rs = callableStatement.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
            //room.setPrice(rs.getDouble("room_rate"));
            //room.setAmountAvailable("");

        }

        roomCB.setItems(typeStrings);

        //loop through the resultSet & add each room type to the ArrayList
        //while (rs.next()) {
        //}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        amenitiesLV.setMouseTransparent(true);
        amenitiesLV.setFocusTraversable(false);
        cartTV.setMouseTransparent(true);
        cartTV.setFocusTraversable(false);

        hNameTF.setText(this.hotel.getHotelname());
        hAddressTF.setText(this.hotel.getHoteladdr());
        hRatingTF.setText(String.valueOf(this.hotel.getRating()) + "/10 Stars");
        descriptionTF.setText(hotel.getHoteldesc());

        try {
            populateAmenitiesList();
            populateRoomTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
