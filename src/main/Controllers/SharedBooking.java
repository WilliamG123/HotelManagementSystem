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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SharedBooking extends DBConnection implements Initializable {

    public static final String REMOVEFROMCART = "removefromcart"; // key to let method know removing from cart
    public static final String ADDTOCART = "addtocart"; // key to let method know adding to cart
    public static final int ERROR = -1; // used for returning error
    public static final int SUCCESS = 0; // used for returning success

    @FXML private Text hNameTF;
    @FXML private Text hAddressTF;
    @FXML private Text hRatingTF;
    @FXML private Text checkInTF;
    @FXML private Text checkOutTF;
    @FXML private TextArea descriptionTF;
    @FXML private TextField adultsTF;
    @FXML private TextField childrenTF;
    @FXML private DatePicker checkInDP;
    @FXML private DatePicker checkOutDP;
    @FXML private Button addRoomBtn;
    @FXML private Button bookBtn;
    @FXML private Button removeRoomBtn1;
    @FXML private Button returnBtn;
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

    @FXML void book(ActionEvent event) {
// TODO: 11/17/2021 write handler method for the book button
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow(); // for displaying Toast error messages

        // retrieve dates from date pickers
        LocalDate checkin = checkInDP.getValue();
        LocalDate checkout = checkOutDP.getValue();

        // date picker input validation
        if(checkin == null || checkout == null) {
            Toast.makeText(stage, "Error: please select dates before trying to book", 1500, 250, 250);
            return;
        } else if(checkin != null && checkout != null) {
            if(checkin.isAfter(checkout)) {
                Toast.makeText(stage, "Error: check in date cannot be after checkout", 1500, 250, 250);
                return;
            }
        }

        // call scene change to return to the createResScene
        sceneChange(event);
    }

    private void queryTotal() throws ClassNotFoundException, SQLException {
// TODO: 11/18/2021 set up query for getting total cost
        // this code could be helpful to manually calculate it
        // https://stackoverflow.com/questions/4600034/calculate-number-of-weekdays-between-two-dates-in-java
        Connection con = null;
        con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call hotel.CalculateTotalByDate(?,?,?,?)}");
        callableStatement.setString(1, hotel.getHotelname());
        callableStatement.setString(1, hotel.getHotelname());
        callableStatement.setString(1, hotel.getHotelname());
        callableStatement.setString(1, hotel.getHotelname());
        ResultSet rs = callableStatement.executeQuery();

        //loop through the resultSet & add each amenity to the ListView
        while(rs.next()) {
            amenitiesLV.getItems().add(rs.getString("Amenities_desc"));
        }
    }

    // constructor that takes in hotel to populate hotel data and account type for login
    public SharedBooking(Hotels hotel) {
        this.hotel = hotel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initializes the cart TableView and observableArrayList
        cartList = FXCollections.observableArrayList();
        cartTV.setItems(cartList);
        Label label = new Label("Your cart is empty");
        label.setFont(new Font("Arial", 20));
        cartTV.setPlaceholder(label);

        // makes it to where the ListView and TableView items are not clickable (front end)
        amenitiesLV.setMouseTransparent(true);
        amenitiesLV.setFocusTraversable(false);
        cartTV.setMouseTransparent(true);
        cartTV.setFocusTraversable(false);
        roomTV.setMouseTransparent(true);
        roomTV.setFocusTraversable(false);

        // using Hotel data sent in through constructor we set text on hotel data elements
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

        // call the methods to initialize the amenities ListView & the Rooms TableView
        try {
            populateAmenitiesList();
            populateRoomTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML void sceneChange(ActionEvent event) {
        AnchorPane newScene = null;
        try{
            if(event.getSource() == returnBtn){
                newScene = FXMLLoader.load(getClass().getResource("UserCreate.fxml"));
            } else if(event.getSource() == bookBtn) {
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

    // queries DB for hotel amenities using hotel Id and adds each one to a ListView
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

    // calls getRoomTypes helper method to return a list of Rooms with only their string types being stored/known
    // queries each room type to get the price and amount of each type of room that is available
    public void populateRoomTable() throws ClassNotFoundException, SQLException {
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
            callableStatement.setInt(2, hotel.getHotelId());
            ResultSet rs = callableStatement.executeQuery();

            rs.next();
            roomTypes.get(i).setAmountAvailable(rs.getInt("count(*)"));
            roomTypes.get(i).setPrice(rs.getDouble("room_rate"));
        }
        roomTV.setItems(roomTypes);
    }

    // helper method to populate room table. queries DB for a all the different room types using a hotel Id
    // returns a ObservableList<Room>
    public ObservableList<Room> getRoomTypes() throws ClassNotFoundException, SQLException {
        ObservableList<Room> roomsTypes = FXCollections.observableArrayList();
        Connection con = null;
        con = getConnection();
        CallableStatement callableStatement = con.prepareCall("{call hotel.getAllRoomTypes(?)}");
        callableStatement.setInt(1, hotel.getHotelId());
        ResultSet rs = callableStatement.executeQuery();

        //loop through the resultSet & add each room type to the ArrayList
        while (rs.next()) {
            roomsTypes.add(new Room(rs.getString("type_name")));
        }
        return roomsTypes;
    }

    /****************************************************************************************************************
     *                                      roomChange method
     * @param event - used to see whether remove or add button was pressed
     * - checks which button was pressed, finds the room in the roomTypes list and then calls cartHandler()
     *   to handle the add or remove from cart depending on the second parameter being ADDTOCART or REMOVEFROMCART
     ****************************************************************************************************************/
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

    /****************************************************************************************************************
     *                                          cartHandler method
     * @param room - room that user selected used to identify room type in cart
     * @param action - key used to determine whether user adding or removing from cart
     * @param event - used to initialize stage variable to use Toast messages
     * - if user adding to cart this method adds a new room or increments it if it already exists in cart table
     * - else the user is removing a room from the cart and the method decrements that room in the cart table
     ****************************************************************************************************************/
    private void cartHandler(Room room, String action, ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        boolean exists = false;

        if(action.equals(REMOVEFROMCART)){
            for(int i = 0; i < cartList.size(); i++) {
                if(room.getType().equals(cartList.get(i).getType())) {
                    exists = true;
                    cartList.get(i).setAmountAvailable(cartList.get(i).getAmountAvailable() - 1);
                    cartList.get(i).setPrice(cartList.get(i).getPrice() - room.getPrice());
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
                        cartList.get(i).setPrice(cartList.get(i).getPrice() + room.getPrice());
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

    /****************************************************************************************************************
     *                                      roomsHandler method
     * @param room - room that user selected used to identify room type in rooms table
     * @param action - key used to determine whether user adding or removing from cart
     * @param event - used to initialize stage variable to use Toast messages
     * - if user adding to cart this method decrements the availability of the specified room type from the rooms table
     * - else the user is removing a room from the cart and the method increments that room in the rooms table
     ****************************************************************************************************************/
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
}
