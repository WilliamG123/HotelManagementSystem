import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QueryTestTableController extends QUERYS implements Initializable {
    @FXML public TableView amenitiesTable;
    @FXML public TableColumn <Hotels, String> amenities;
   // public TableColumn amentitiesTotal;
    @FXML
    private TableView<Hotels> hotelTable;
    @FXML private TableColumn<Hotels, String> Hotel, details, address,rating;
    @FXML private TableColumn<Hotels, Integer> Rooms, amenitiesTotal;
    @FXML private TableColumn<Hotels, Double> Price;


    @FXML
    public Text bookBtn;
    @FXML
    public Button resetBtn;
    @FXML
    public Button btn_search;
    @FXML
    public TextField cityTF;
    @FXML
    private DatePicker checkinDP;
    @FXML
    private DatePicker checkoutDP;

    @FXML
    private ObservableList<Hotels> list;
    private ObservableList<String> amenitieslist;


    public void populateTableView() throws SQLException, ClassNotFoundException {

        //instantiate list
        //both datepickers must have a value in order to run this query


        Connection con = getConnection();
        //PreparedStatement ps = con.prepareStatement("call hotel.getListAvailHotels(?)");
        //CallableStatement callableStatement = con.prepareCall("{call hotel.getListAvailHotels}");
        ResultSet rs = dbExecuteCallQuery("call hotel.getListAvailHotels");
        ResultSet rs2 = getAmenitiesByHotelName("The Magnolia All Suites");
        if (checkinDP.getValue() != null) {
            System.out.println("DATE PICKERS HAVE VALUES!");
            // check_in_date = checkinDP.getValue();
            // check_out_date = checkinDP.getValue();
            // ps.setDate(1, Date.valueOf(String.valueOf(check_in_date)));
            // ps.setDate(2, Date.valueOf(String.valueOf(check_out_date)));

        } else if (checkinDP.getValue() == null) {

            System.out.println("DATEPICKERS NULL USE DEFUALT INSTEAD");

            // long millis = System.currentTimeMillis();
            // java.sql.Date date = new java.sql.Date(millis);
            //ps.setDate(1, date);
            // ps.setDate(2, date);
        }

        Hotels hotel = new Hotels();

        //loop through the resultSet , extract data and append it to our list
        while (rs.next()) {

            //Create a hotels Object , add data to it and finally append it to list

            hotel.setHotelname(rs.getString("hotel_name"));
            hotel.setRooms(rs.getInt("hotel_availrms"));
            hotel.setAmenities(rs.getInt("hotel_numofamend"));
            hotel.setPrice(rs.getDouble("room_rate"));
            hotel.setRating(rs.getInt("hotel_rating"));
            hotel.setHoteladdr(rs.getString("hotel_address"));
            hotel.setHoteldesc(rs.getString("hotel_desc"));


            list.add(hotel);

        }
        while(rs2.next()){

            System.out.println(rs2.getString("Amenities_desc"));

            hotel.setAmenitiesdesc("Amenities_desc");

            amenitieslist.add(String.valueOf(hotel));

        }
        amenities.setCellValueFactory(new PropertyValueFactory<>("amenitiesdesc"));
        Hotel.setCellValueFactory(new PropertyValueFactory<>("hotelname"));
        Rooms.setCellValueFactory(new PropertyValueFactory<>("rooms"));
        Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        amenitiesTotal.setCellValueFactory(new PropertyValueFactory<>("amenities"));
        details.setCellValueFactory(new PropertyValueFactory<>("hoteldesc"));
        address.setCellValueFactory(new PropertyValueFactory<>("hoteladdr"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Hotel.setCellValueFactory(c-> new SimpleStringProperty(hotel.getHotelname()));

        //set data tp tableview
        hotelTable.setItems(list);
        amenitiesTable.setItems(amenitieslist);



    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXCollections.observableArrayList();
        amenitieslist = FXCollections.observableArrayList();
        try {
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
