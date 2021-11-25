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
import java.util.Optional;
import java.util.ResourceBundle;

public class SharedBooking extends DBConnection implements Initializable {

    public static final String REMOVEFROMCART = "removefromcart"; // key to let method know removing from cart
    public static final String ADDTOCART = "addtocart"; // key to let method know adding to cart
    public static final String RESTORECART = "restorecart"; // key to let method know removing from cart
    public static final int ERROR = -1; // used for returning error
    public static final int SUCCESS = 0; // used for returning success

    // data retrieval keys
    public static final int ROOMS_KEY = 333;
    public static final int  CHECKIN_KEY = 666;
    public static final int CHECKOUT_KEY = 999;
    public static final int LOGIN_KEY = 100;

    @FXML private Text hNameTF;
    @FXML private Text hAddressTF;
    @FXML private Text hRatingTF;
    @FXML private Text checkInTF;
    @FXML private Text checkOutTF;
    @FXML private Text infoT;
    @FXML private TextArea descriptionTF;
    @FXML private TextField adultsTF;
    @FXML private TextField childrenTF;
    @FXML private TextField nameTF;
    @FXML private TextField emailTF;
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
    private Reservation resData; // stores data received from login scene
    private boolean recievedInfo; // boolean check if we received data from login

    private Reservation getUserInput() {
        Reservation reservation = new Reservation();
        LocalDate checkin = checkInDP.getValue();
        LocalDate checkout = checkOutDP.getValue();
// TODO: 11/24/2021 finish this method
        //int adults =
        //int children =

        if(checkin != null) {
            reservation.setCheckIn(checkin);
            reservation.addToCurrent(CHECKIN_KEY);
        }
        if(checkout != null) {
            reservation.setCheckOut(checkout);
            reservation.addToCurrent(CHECKOUT_KEY);
        }
        return reservation;
    }

    @FXML void book(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow(); // for displaying Toast error messages
        //Check to see if user is logged in
        AnchorPane newScene = null;

        if(LoadedUser.getInstance().getUser() == null) {

            ButtonType loginAlertBtn = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelAlertBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.NONE,"You are currently not logged in please do so to complete your reservation", loginAlertBtn, cancelAlertBtn);
            alert.setTitle("Booking Check");
            //alert.setContentText("Please confirm reservation deletion");
            Optional<ButtonType> result = alert.showAndWait();

            // if user confirmed login go login
            if(result.orElse(cancelAlertBtn) == loginAlertBtn){
                Reservation reservation = getUserInput();
                System.out.println("SharedBooking Scene -> Login Scene");
                System.out.println("NO USER LOGGED IN");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                LoginController controller = new LoginController(hotel, reservation, LOGIN_KEY);
                loader.setController(controller);
                newScene = loader.load();
                Scene scene = new Scene(newScene);
                Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();

                window.setScene(scene);
                window.show();
            }
        } else {
            System.out.println("USER FIRSTNAME IS " + LoadedUser.getInstance().getUser().getFirstName());

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
// TODO: 11/24/2021 check if user is an employee and if so set up input validation for the customer information



// TODO: 11/17/2021 needs a query to actually write a reservation to the DB

            // call scene change to return to the createResScene
            sceneChange(event);
        }
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
        this.resData = null;
        this.recievedInfo = false;
    }

    // constructor for when the scene comes back after logging in to remember user data
    public SharedBooking(Hotels hotel, Reservation resData) {
        this.hotel = hotel;
        this.resData = resData;
        this.recievedInfo = true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

        if(recievedInfo) {
            if(resData.dyanmicData.contains(CHECKIN_KEY))
                checkInDP.setValue(resData.getCheckIn());
            if(resData.dyanmicData.contains(CHECKOUT_KEY))
                checkOutDP.setValue(resData.getCheckOut());
        } else {
            // initializes the cart TableView and observableArrayList
            cartList = FXCollections.observableArrayList();
            cartTV.setItems(cartList);
            Label label = new Label("Your cart is empty");
            label.setFont(new Font("Arial", 20));
            cartTV.setPlaceholder(label);
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
            System.out.println(rs.getString("room_type_desc"));
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
        } else if(action.equals(ADDTOCART)) {
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
        } else if(action.equals(RESTORECART)) {
            
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
