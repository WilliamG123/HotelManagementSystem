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
import java.util.ResourceBundle;

public class SharedBooking extends DBConnection implements Initializable {

    public static final String REMOVEFROMCART = "removefromcart";
    public static final String ADDTOCART = "addtocart";
    public static final int ERROR = -1;
    public static final int SUCCESS = 0;

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
    @FXML private TableView<Room> roomTV;
    @FXML private TableColumn<Room, String> styleColumn;
    @FXML private TableColumn<Room, String> availableColumn;
    @FXML private TableColumn<Room, String> priceColumn;
    // cart variables
    @FXML private TableView<Room> cartTV;
    @FXML private TableColumn<Room, String> style2Column;
    @FXML private TableColumn<Room, String> amount2Column;
    @FXML private TableColumn<Room, String> price2Column;

    @FXML private ListView<String> amenitiesLV;
    @FXML private ObservableList<Room> roomsList;
    @FXML private ObservableList<Room> roomTypes;
    @FXML private ObservableList<Room> cartList;
    @FXML private ChoiceBox<String> roomCB;
    private Hotels hotel;
    private String accountType;

// TODO: 11/17/2021 write handler method for the book button
    @FXML void book(MouseEvent event) { }

    @FXML void roomChange(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow(); // for displaying Toast error messages
        String choice;

        choice = roomCB.getValue();
        if(choice == null){
            Toast.makeText(stage, "Error: no room selected", 2000, 500, 500);
            return;
        }

        // find the room type in the list to be decremented
        for(int i = 0; i < roomTypes.size(); i++) {
            if(roomTypes.get(i).getType().equals(choice)) {
                if(event.getSource() == addRoomBtn) {
                    cartHandler(roomTypes.get(i), ADDTOCART, event);
                } else {
                    cartHandler(roomTypes.get(i), REMOVEFROMCART, event);
                }
            }
        }
    }

    private int roomsHandler(Room room, String action, ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        if(action.equals(ADDTOCART)){
            for(int i = 0; i < roomTypes.size(); i++){
                if(room.getType().equals(roomTypes.get(i).getType())) {
                    if(roomTypes.get(i).getAmountAvailable() == 0) {
                        Toast.makeText(stage, "Error: there are no more " + room.getType() + " rooms available", 2000, 500, 500);
                        return ERROR;
                    }
                    roomTypes.get(i).setAmountAvailable(roomTypes.get(i).getAmountAvailable() - 1);
                    roomTV.refresh();
                    return SUCCESS;
                }
            }
        } else {
            for (int i = 0; i < roomTypes.size(); i++) {
                if (room.getType().equals(roomTypes.get(i).getType())) {
                    roomTypes.get(i).setAmountAvailable(roomTypes.get(i).getAmountAvailable() + 1);
                    roomTV.refresh();
                    return SUCCESS;
                }
            }
        }
        return ERROR;
    }

    private void cartHandler(Room room, String action, ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        boolean exists = false;

        if(action.equals(REMOVEFROMCART)){
            for(int i = 0; i < cartList.size(); i++) {
                if(room.getType().equals(cartList.get(i).getType())) {
                    exists = true;
                    cartList.get(i).setAmountAvailable(cartList.get(i).getAmountAvailable() - 1);
                    if(cartList.get(i).getAmountAvailable() == 0) {
                        cartList.remove(cartList.get(i));
                    }
                }
            }
            if(!exists){
                Toast.makeText(stage, "Error: there is no " + room.getType() + " room in your cart", 2000, 500, 500);
                return;
            }
            roomsHandler(room, REMOVEFROMCART, event);
        } else {
            if(roomsHandler(room, ADDTOCART, event) == ERROR){
                return;
            } else {
                for(int i = 0; i < cartList.size(); i++) {
                    if(room.getType().equals(cartList.get(i).getType())) {
                        cartList.get(i).setAmountAvailable(cartList.get(i).getAmountAvailable() + 1);
                        exists = true;
                        break;
                    }
                }
                if(!exists) {
                    Room r = new Room(room.getPrice(), room.getType(), 1);
                    cartList.add(r);
                }
            }
        }
        cartTV.refresh();
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

        roomTypes = getRoomTypes();
        ObservableList<String> typeStrings = FXCollections.observableArrayList();

        for(Room r : roomTypes) {
            System.out.println(r.getType() + " " + r.getAmountAvailable() + " " + r.getPrice());
            typeStrings.add(r.getType());
        }

        roomCB.setItems(typeStrings); // set room types available for the choice box

        for(int i = 0; i < roomTypes.size(); i++) {
            callableStatement.setString(1, roomTypes.get(i).getType());
            callableStatement.setString(2, hotel.getHotelId());
            ResultSet rs = callableStatement.executeQuery();

            rs.next();
            roomTypes.get(i).setAmountAvailable(rs.getInt("count(*)"));
            roomTypes.get(i).setPrice(rs.getDouble("room_rate"));
        }
        roomTV.setItems(roomTypes);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cartList = FXCollections.observableArrayList();
        cartTV.setItems(cartList);

        amenitiesLV.setMouseTransparent(true);
        amenitiesLV.setFocusTraversable(false);
        cartTV.setMouseTransparent(true);
        cartTV.setFocusTraversable(false);

        hNameTF.setText(this.hotel.getHotelname());
        hAddressTF.setText(this.hotel.getHoteladdr());
        hRatingTF.setText(String.valueOf(this.hotel.getRating()) + "/10 Stars");
        descriptionTF.setText(hotel.getHoteldesc());

        // set the columns up for the rooms available TableView
        styleColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("amountAvailable"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // set the columns up for the Cart TableView
        style2Column.setCellValueFactory(new PropertyValueFactory<>("type"));
        amount2Column.setCellValueFactory(new PropertyValueFactory<>("amountAvailable"));
        price2Column.setCellValueFactory(new PropertyValueFactory<>("price"));

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
