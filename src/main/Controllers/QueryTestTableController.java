import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    @FXML public TableView<Hotels> amenitiesTable;
    @FXML public TableColumn <Hotels, String> amenities;

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
    private ObservableList<Hotels> amenitieslist;
    String hotelname = "The Magnolia All Suites";

    public QueryTestTableController() throws ClassNotFoundException {
    }

    public void populateTableView() throws SQLException, ClassNotFoundException {
        System.out.println("populateTableView Called");
        //PreparedStatement ps = con.prepareStatement("call hotel.getListAvailHotels(?)");
        //CallableStatement callableStatement = con.prepareCall("{call hotel.getListAvailHotels}");
        ResultSet rs = dbExecuteCallQuery("call hotel.getListAvailHotels");

        ResultSet rs2 = getAmenitiesByHotelName(hotelname); //for Amenities TableView

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



        //loop through the resultSet , extract data and append it to our list
        while (rs.next() ) {

            //Create a hotels Object , add data to it and finally append it to list
            Hotels hotel = new Hotels();
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
            Hotels hotel = new Hotels();
            System.out.println(rs2.getString("Amenities_desc"));

            hotel.setAmenitiesdesc(rs2.getString("Amenities_desc"));

            amenitieslist.add(hotel);

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




//ON DOUBLE CLICK ANY ROW WILL GET THAT HOTEL NAME AND STORE IT INSIDE GLOBAL hotelname
        hotelTable.setRowFactory( tv -> {
            TableRow<Hotels> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Hotels rowData = row.getItem();
                    hotelname = rowData.getHotelname();
                    System.out.println(rowData.getHotelname());
                    list.clear();
                    amenitieslist.clear();
                    try {
                        populateTableView();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });



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
