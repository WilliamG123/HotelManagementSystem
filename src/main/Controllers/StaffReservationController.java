import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
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

    @FXML private TextField useridTF;

    @FXML private TextField hotelTF;

    @FXML private TextField resNumTF;

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

        // try block attempts to load a new scene
        try {
            if (event.getSource() == mainmenuTV)
                newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            else if(event.getSource() == logoutTV){
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
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
    // TODO: 10/29/2021  Need to read in data and put into listview and then set up the detailed view
    public void populateListView() throws SQLException{
        // set up query for all reservations
        query.setLength(0);
        query.append("SELECT * FROM reservation;");
        // query the database
        ResultSet rs = conn.createStatement().executeQuery(query.toString());
        ResultSetMetaData rsmd = rs.getMetaData();
        System.out.println("querying SELECT * FROM reservation");
        int columnsNumber = rsmd.getColumnCount();

        if(rs.next()){
            addReservations(rs);
            userIDColumn.setCellValueFactory(new PropertyValueFactory<>("custId"));
            hotelColumn.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
            checkInColumn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
            checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
            resNumColumn.setCellValueFactory(new PropertyValueFactory<>("resID"));
        }
        // while loop prints out all result set data
        /*while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }*/

        resTable.setItems(resList);
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
    }
}