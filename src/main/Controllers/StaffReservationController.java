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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

/*****************************************************************
 *                     StaffReservationController Class
 * - populates a ListView of all reservations in the DB
 * - clicking a reservation triggers a detailed view pop up window or scene
 * - can filter through reservations by various search criteria
 *****************************************************************/

public class StaffReservationController extends DBConnection implements Initializable {

    @FXML private TableView<Reservation> resTable;
    @FXML private Text mainmenuTV;
    @FXML private Text logoutTV;
    @FXML private TableColumn<Reservation, String> userIDColumn;
    @FXML private TableColumn<Reservation, String> hotelColumn;
    @FXML private TableColumn<Reservation, String> checkInColumn;
    @FXML private TableColumn<Reservation, String> checkOutColumn;
    @FXML private TableColumn<Reservation, String> costColumn;
    @FXML private TableColumn<Reservation, String> resNumColumn;
    @FXML private TextField hotelTF;
    @FXML private TextField resNumTF;
    @FXML private TextField nameTF;
    @FXML private DatePicker checkInDP;
    @FXML private DatePicker checkOutDP;
    @FXML private Button applyBtn;
    @FXML private Button resetBtn;
    @FXML private Text modifyTF;
    @FXML private Text deleteTF;
    @FXML private ObservableList<Reservation> resList;
    private Connection conn;
    private StringBuilder query;

    /*****************************************************************
     *                     sceneChange Function
     * @param event
     * - Handles all button pressing input to various scenes
     *****************************************************************/
    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow(); // for displaying Toast error messages

        // try block attempts to load a new scene
        try {
            if (event.getSource() == mainmenuTV) {
                newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
                System.out.println("Log: StaffRes -> MainMenuBtn");
            } else if(event.getSource() == logoutTV) {
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
                System.out.println("Log: StaffRes -> LoginBtn");
            } else if(event.getSource() == modifyTF) {
                if(resTable.getSelectionModel().getSelectedItem() == null) {
                    Toast.makeText(stage, "Error: no reservation selected", 2000, 500, 500);
                    return;
                } else {
                    System.out.println("Log: StaffRes -> ModifyBtn");
                    Reservation reservation = resTable.getSelectionModel().getSelectedItem();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("StaffResModify.fxml"));
                    StaffResModify controller = new StaffResModify(reservation, "staff");
                    loader.setController(controller);
                    newScene = loader.load();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /*****************************************************************
     *                     initialize Function
     * - initializes global variables
     * - sets up SQL DB connection
     * - calls populateList() to populate ListView
     *****************************************************************/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // get list of reservations
        resList = FXCollections.observableArrayList();
        query = new StringBuilder();
        try{
            // get connection
            conn = getConnection();
            populateListView();
        } catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /*****************************************************************
     *                     populateListView Function
     * - populates the list view with reservation data
     * - makes a query to DB for all reservations for a specific user
     *****************************************************************/
    public void populateListView() throws SQLException{
        // set up query for all reservations
        query.setLength(0);
        //getQuery();
        query.append("SELECT * FROM reservation;");
        // query the database
        ResultSet rs = conn.createStatement().executeQuery(query.toString());
        System.out.println("Log: querying SELECT * FROM reservation");

        if(rs.next()){
            addReservations(rs);
            userIDColumn.setCellValueFactory(new PropertyValueFactory<>("custId"));
            hotelColumn.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
            checkInColumn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
            checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
            resNumColumn.setCellValueFactory(new PropertyValueFactory<>("resID"));
        }

        resTable.setItems(resList);
    }

    /*****************************************************************
     *                     handleFilter Method
     * @param event - holds data on the UI element clicked either resetBtn or applyBtn
     * - if resetBtn then clear the ListView and repopulate it will all reservation data
     * - else it is applyBtn so repopulate listview with filters applied
     *****************************************************************/
    @FXML private void handleFilter(ActionEvent event) throws SQLException {
        if(event.getSource() == resetBtn) {
            resList.clear();
            populateListView();
        } else {
            HashMap<Integer, Object> statementValues = new HashMap<>();
            query.setLength(0);
            query.append("SELECT * FROM reservation");

            if(buildQuery(statementValues, event)) {
                PreparedStatement ps = conn.prepareStatement(query.toString());
                for(Integer key : statementValues.keySet()) {
                    Object obj = statementValues.get(key);
                    if(obj instanceof String) {
                        ps.setString(key, (String)obj);
                    }
                    else if(obj instanceof LocalDate) {
                        ps.setDate(key, Date.valueOf((LocalDate) obj));
                    }
                }
                System.out.println(ps);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    resList.clear();
                    addReservations(rs);
                } else {
                    System.out.println("no query built");
                }
            }
            resetSearchFields();
        }
    }

    /*****************************************************************
     *                     buildQuery Method
     * @param statementValues - HashMap holds the position of each query filter element and its corresponding SQL key
     * - stores the filter TextField data and builds a query string based off of what is entered
     *****************************************************************/
    private boolean buildQuery(HashMap<Integer, Object> statementValues, ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow(); // for displaying Toast error messages
        int pos = 1; // tracks the position of elements in the query linking query questions marks to the corresponding hashmap value
        boolean appended = false; // tracks whether anything is added or appended
        query.append(" WHERE");

        // stores filter TextField data
        LocalDate checkin = checkInDP.getValue();
        LocalDate checkout = checkOutDP.getValue();
        String hotel = hotelTF.getText().toString();
        String name = nameTF.getText().toString();

        if(hotel.equals("") && name.equals("") && checkin == null && checkout == null) {
            Toast.makeText(stage, "Error: no filter information given", 2000, 500, 500);
        } else if(checkin != null && checkout != null) {
            if(checkin.isAfter(checkout)){
                Toast.makeText(stage, "Error: Check in date cannot be after checkout", 2000, 500, 500);
            }
        }

        // TODO: 10/30/2021 need to find out proper query to get hotel name & guest name
        /*if(!hotel.equals("")){
            query.append(" hotelName=?");
            appended = true;
            statementValues.put(pos, phoneNumber);
            pos++;
        }
        if(!name.equals("")){
            if (appended) {
                query.append(" AND");
            }
            query.append(" check_in=?");
            appended = true;
            statementValues.put(pos, checkin);
            pos++;
        }*/
        if(checkin != null){
            if (appended) {
                query.append(" AND");
            }
            query.append(" check_in=?");
            appended = true;
            statementValues.put(pos, checkin);
            pos++;
        }
        if(checkout != null){
            if (appended) {
                query.append(" AND");
            }
            query.append(" check_out=?");
            appended = true;
            statementValues.put(pos, checkout);
            pos++;
        }
        query.append(";");

        return appended;
    }
    // TODO: 11/3/2021 make sure u add a query append for the customer Id

    // method clears text all filter TextFields & Date Pickers
    private void resetSearchFields(){
        hotelTF.clear();
        checkInDP.setValue(null);
        checkOutDP.setValue(null);
    }

    /*****************************************************************
     *                     addReservations Method
     * @param rs - holds reservations data from DB
     * - uses a ResultSet iterable to parse the res data into an ObservableList
     *****************************************************************/
    private void addReservations(ResultSet rs) throws SQLException {
        do{
            Reservation res = new Reservation();
            res.setResID(rs.getInt("reservationId"));
            res.setCost(rs.getDouble("total_price"));
            res.setCheckIn(rs.getDate("check_in").toLocalDate());
            res.setCheckOut(rs.getDate("check_out").toLocalDate());
            res.setAdults(rs.getInt("adults"));
            res.setChildren(rs.getInt("children"));
            res.setHotelName("FixLater");
            res.setCustId("Brandon");
            resList.add(res);
            System.out.println(res);
        } while(rs.next());

        // TODO: 11/3/2021 delete once the DB is populated with data
        // hard coded data since not a lot of DB data
        LocalDate checkin1 = LocalDate.now();
        LocalDate checkout1 = checkin1.plusDays(4);
        LocalDate checkin2 = checkout1.plusDays(10);
        LocalDate checkout2 = checkin2.plusDays(4);
        LocalDate checkin3 = checkout2.plusDays(10);
        LocalDate checkout3 = checkin3.plusDays(4);
        Reservation w = new Reservation("William", "La Quinta", checkin1, checkout1, 230.50, 123647861);
        Reservation f = new Reservation("Filberto", "HolidayInn", checkin2, checkout2, 360.29, 978546312);
        Reservation e = new Reservation("Edgar", "Hilton", checkin3, checkout3, 590.13, 123456789);
        resList.add(w);
        resList.add(f);
        resList.add(e);

        for(int i = 0; i < 30; i++){
            resList.add(new Reservation("Edgar", "Hilton", checkin3, checkout3, 590.13, 123456789));
        }
    }

    // pops up alert dialog window to confirm deletion of reservation and then queries DB to do so
    @FXML private void deleteRes() throws SQLException {
        System.out.println("Log: StaffResManagey -> deleteBtn");
        ButtonType deleteBtn = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.NONE,"Please confirm reservation deletion", deleteBtn, cancelBtn);
        alert.setTitle("Deletion Confirmation");
        //alert.setContentText("Please confirm reservation deletion");
        Optional<ButtonType> result = alert.showAndWait();

        // if user confirmed reservation deletion
        if(result.orElse(cancelBtn) == deleteBtn){
            System.out.println("Delete");
            // TODO: 11/3/2021 write a query to delete a reservation from the DB
        }

        resList.clear();
        populateListView();
    }
}