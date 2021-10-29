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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

    @FXML private TableColumn<Reservation, String> datesColumn;

    @FXML private TableColumn<Reservation, String> roomsColumn;

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
        query.append("SELECT * FROM reservation");
        // query the database
        ResultSet rs = conn.createStatement().executeQuery(query.toString());
        ResultSetMetaData rsmd = rs.getMetaData();
        System.out.println("querying SELECT * FROM reservation");
        int columnsNumber = rsmd.getColumnCount();

        // while loop prints out all result set data
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }

        /*while (rs.next()) {
            Reservation res = new Reservation();
            res.setResID(rs.getString("reservationid"));
            res.setTotalCost(rs.getDouble("total_price"));
            resList.add(res);
        }*/

        //resTable.setItems(resList);
    }
}