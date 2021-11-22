import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;



import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;



import javax.swing.text.TabableView;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/*****************************************************************
 *                     UserCreateController Class
 * - populates a TableView of all hotels in the DB that users can select to make a reservation
 * - clicking hotel and then clicking book takes the user to a new detailed scene to book
 * - can filter through hotels via hotel name, checkin date, and checkout date
 *****************************************************************/
public class UserCreateController extends DBConnection implements Initializable {

    @FXML private Text mainmenuTV;
    @FXML private Text logoutTV;
    @FXML private DatePicker checkinDP;
    @FXML private DatePicker checkoutDP;
    @FXML private TextField hotelTF;
    @FXML private TableView<Hotels> hotelTable;
    @FXML private TableColumn<Hotels, String> Hotel, details, address, rating;
    @FXML private TableColumn<Hotels, Integer> Rooms, amenities;
    @FXML private TableColumn<Hotels, Double> Price;
    @FXML private Button applyBtn;
    @FXML private Button resetBtn;
    @FXML private Button bookBtn;
    @FXML private TextField cityTF;
    @FXML private ObservableList<Hotels> list;

    private ContextMenu entriesPopup;
    private Connection conn;
    private StringBuilder query;
    ArrayList<String> possibleWords = new ArrayList<String>();
    LocalDate check_out_date;
    LocalDate check_in_date;

    // checks to see if the user is logged in because they must be to make a reservation
    public void loginCheck() {
        // TODO: 11/17/2021 make code to check if the user is logged in before proceeding to book a hotel
    }

    // method clears the filters and reloads the TableView
    public void resetFilter(ActionEvent event) throws SQLException, ClassNotFoundException {
        checkinDP.setValue(null);
        checkoutDP.setValue(null);
        hotelTF.clear();
        populateTableView();
    }

    // method will handle the filter apply button
    @FXML public void handleFilter(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(event.getSource() == resetBtn) {
            resetFilter(event);
        } else {
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow(); // for displaying Toast error messages

            LocalDate checkin = checkinDP.getValue();
            LocalDate checkout = checkoutDP.getValue();
            String hotel = hotelTF.getText().toString();

            if(hotel.equals("") && checkin == null && checkout == null) {
                Toast.makeText(stage, "Error: no filter information given", 2000, 500, 500);
            } else if(checkin != null && checkout != null) {
                if(checkin.isAfter(checkout)) {
                    Toast.makeText(stage, "Error: Check in date cannot be after checkout", 2000, 500, 500);
                }
            }

// TODO: 11/19/2021 write the query and code for using filters

        }
    }

    public static final LocalDate LOCAL_DATE (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXCollections.observableArrayList();
        //adding all 10 required hotels for type ahead in search by hotel name
        possibleWords.add("The Magnolia All Suites");
        possibleWords.add("The Lofts at Town Centre");
        possibleWords.add("Park North Hotel");
        possibleWords.add("The Courtyard Suites");
        possibleWords.add("The Regency Rooms");
        possibleWords.add("Town Inn Budget Rooms");
        possibleWords.add("The Comfy Motel Place");
        possibleWords.add("Sun Palace Inn");
        possibleWords.add("HomeAway Inn");
        possibleWords.add("Rio Inn");

        TextFields.bindAutoCompletion(hotelTF, possibleWords);
        //run the method

        try {
            populateTableView();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        hotelTF.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Type Ahead auto complete
                possibleWords.add(hotelTF.getText());
                TextFields.bindAutoCompletion(hotelTF, possibleWords);

                System.out.println(hotelTF.getText());

            }
        });
    }

    /*****************************************************************
     *                     populateListView method
     * - populates the list view with reservation data
     * - makes a query to DB for all reservations for a specific user
     *****************************************************************/
    public void populateTableView() throws SQLException, ClassNotFoundException {

        //instantiate list
        //both datepickers must have a value in order to run this query

        Connection con = getConnection();
        //PreparedStatement ps = con.prepareStatement("call hotel.getListAvailHotels(?)");
        CallableStatement callableStatement = con.prepareCall("{call hotel.getListAvailHotels}");
        ResultSet rs = callableStatement.executeQuery();
        if(checkinDP.getValue() != null){
            System.out.println("DATE PICKERS HAVE VALUES!");
            check_in_date = checkinDP.getValue();
            check_out_date = checkinDP.getValue();
            // ps.setDate(1, Date.valueOf(String.valueOf(check_in_date)));
            // ps.setDate(2, Date.valueOf(String.valueOf(check_out_date)));

        } else if(checkinDP.getValue() == null) {

            System.out.println("DATEPICKERS NULL USE DEFUALT INSTEAD");

            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            //ps.setDate(1, date);
            // ps.setDate(2, date);
        }
        // ResultSet rs = ps.executeQuery();

        //ps.setInt(2, 1);

        //loop through the resultSet , extract data and append it to our list
        while(rs.next()) {
            //Create a hotels Object , add data to it and finally append it to list
            Hotels hotel = new Hotels();
            hotel.setHotelname(rs.getString("hotel_name"));
            hotel.setRooms(rs.getInt("hotel_availrms"));
            hotel.setAmenities(rs.getInt("hotel_numofamend"));
            hotel.setPrice(rs.getDouble("room_rate"));
            hotel.setRating(rs.getInt("hotel_rating"));
            hotel.setHoteladdr(rs.getString("hotel_address"));
            hotel.setHoteldesc(rs.getString("hotel_desc"));
            hotel.setHotelId(rs.getInt("hotel_id"));
            list.add(hotel);
        }

        hotelTable.setRowFactory( tv -> {
            TableRow<Hotels> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Hotels rowData = row.getItem();
                    System.out.println(rowData);
                }
            });
            return row ;
        });

        //System.out.println( hotel.hotelnameProperty().getValue());
        Hotel.setCellValueFactory(new PropertyValueFactory<>("hotelname"));
        Rooms.setCellValueFactory(new PropertyValueFactory<>("rooms"));
        Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        amenities.setCellValueFactory(new PropertyValueFactory<>("amenities"));
        details.setCellValueFactory(new PropertyValueFactory<>("hoteldesc"));
        address.setCellValueFactory(new PropertyValueFactory<>("hoteladdr"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Hotel.setCellValueFactory(c-> new SimpleStringProperty(hotel.getHotelname()));

        //set data tp tableview
        hotelTable.setItems(list);

    }

    @FXML void sceneChange(ActionEvent event) {
        AnchorPane newScene = null;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // for displaying Toast error messages

        try {
            if (event.getSource() == mainmenuTV)
                newScene = FXMLLoader.load(getClass().getResource("UserMainMenu.fxml"));
            else if (event.getSource() == logoutTV) {
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
            } else if (event.getSource() == bookBtn) {
                if (hotelTable.getSelectionModel().getSelectedItem() == null) {
                    Toast.makeText(stage, "Error: no hotel selected", 2000, 500, 500);
                    return;
                } else {
                    System.out.println("Log: ResCreateController -> SharedBooking");
                    Hotels hotel = hotelTable.getSelectionModel().getSelectedItem();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("SharedBooking.fxml"));
                    SharedBooking controller = new SharedBooking(hotel);
                    loader.setController(controller);
                    newScene = loader.load();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}