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
import java.util.List;
import java.util.ResourceBundle;

// controls user reservation creation scene
public class UserCreateController extends DBConnection implements Initializable {

    @FXML
    private Text mainmenuTV, logoutTV;
    @FXML
    private DatePicker check_in, check_out;
    @FXML
    private TableView<Hotels> tableHotels;
    @FXML
    private TableColumn<Hotels, String> Hotel, details, address,rating;
    @FXML
    private TableColumn<Hotels, Integer> Rooms, amentities;
    @FXML
    private TableColumn<Hotels, Double> Price;
    @FXML
    private TextField tf_HotelName;
    @FXML
    private Button btn_search;

    private ContextMenu entriesPopup;
    private Connection conn;
    @FXML private ObservableList<Hotels> list;

    ArrayList<String> possibleWords = new ArrayList<String>();

   LocalDate check_out_date;
    LocalDate check_in_date;

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == mainmenuTV)
                newScene = FXMLLoader.load(getClass().getResource("UserMainMenu.fxml"));
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


        TextFields.bindAutoCompletion(tf_HotelName, possibleWords);
        //run the method

        try {
            populateTableView();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        tf_HotelName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Type Ahead auto complete
                possibleWords.add(tf_HotelName.getText());
                TextFields.bindAutoCompletion(tf_HotelName, possibleWords);

                System.out.println(tf_HotelName.getText());

            }
        });



    }
    public void onClick_btn_search(ActionEvent e) throws SQLException, ClassNotFoundException {
        populateTableView();
    }
    //method to populate tableview
    public void populateTableView() throws SQLException, ClassNotFoundException {

        //instantiate list
        //both datepickers must have a value in order to run this query


        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("CALL getHotelsByDate(?,?)");

            if(check_in.getValue() != null){
                System.out.println("DATE PICKERS HAVE VALUES!");
                check_in_date = check_in.getValue();
                check_out_date = check_in.getValue();
                 ps.setDate(1, Date.valueOf(String.valueOf(check_in_date)));
                 ps.setDate(2, Date.valueOf(String.valueOf(check_out_date)));

            }else if(check_in.getValue() == null) {

                System.out.println("DATEPICKERS NULL USE DEFUALT INSTEAD");

                long millis = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);
                ps.setDate(1, date);
                ps.setDate(2, date);
            }
            ResultSet rs = ps.executeQuery();


        //ps.setInt(2, 1);


        //loop through the resultSet , extract data and append it to our list
        while(rs.next()) {
            //Create a hotels Object , add data to it and finally append it to list
            Hotels hotel =new Hotels();
            hotel.setHotelname(rs.getString("hotel_name"));
            hotel.setRooms(rs.getInt("hotel_availrms"));
            hotel.setAmentities(rs.getInt("hotel_numofamend"));
            hotel.setPrice(rs.getDouble("daily_rate"));
            hotel.setRating(rs.getInt("hotel_rating"));
            hotel.setHoteladdr(rs.getString("hotel_address"));
            hotel.setHoteldesc(rs.getString("hotel_desc"));

            list.add(hotel);

        }
        //Set property to tableview columns

        //NB. Use the same property that is in Object class

        //System.out.println( hotel.hotelnameProperty().getValue());
        Hotel.setCellValueFactory(new PropertyValueFactory<>("hotelname"));
        Rooms.setCellValueFactory(new PropertyValueFactory<>("rooms"));
        Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        amentities.setCellValueFactory(new PropertyValueFactory<>("amentities"));
        details.setCellValueFactory(new PropertyValueFactory<>("hoteldesc"));
        address.setCellValueFactory(new PropertyValueFactory<>("hoteladdr"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Hotel.setCellValueFactory(c-> new SimpleStringProperty(hotel.getHotelname()));

        //set data tp tableview
        tableHotels.setItems(list);

    }








}