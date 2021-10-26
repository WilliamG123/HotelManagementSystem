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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// controls user reservation creation scene
public class UserCreateController extends DBConnection implements Initializable {

    @FXML
    private Text mainmenuTV;

    @FXML
    private Text logoutTV;
    @FXML
    private TableView<Hotels> tableHotels;
    @FXML
    private TableColumn<Hotels, String> Hotel;
    @FXML
    private TableColumn<Hotels, Integer> Rooms;
    @FXML
    private TableColumn<Hotels, Double> Price;
    @FXML
    private TableColumn<Hotels, Integer> amentities;
    @FXML
    private TextField tf_HotelName;
    //lets declare some variables
    private ContextMenu entriesPopup;
    private Connection conn;
    private ObservableList<Hotels> list;

    ArrayList<String> possibleWords = new ArrayList<String>();



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



    @Override
    public void initialize(URL location, ResourceBundle resources) {
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


    //method to populate tableview
    private void populateTableView() throws SQLException, ClassNotFoundException {

        //instantiate list
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("Select * From hotel");
       // ps.setString(1, "supplier_id");
        //ps.setInt(2, 1);
        ResultSet rs = ps.executeQuery();
        //loop through the resultSet , extract data and append it to our list
        while(rs.next()) {
            //Create a hotels Object , add data to it and finally append it to list
            Hotels hotel =new Hotels();
            hotel.setHotelname(rs.getString("hotel_name"));
            hotel.setRooms(rs.getInt("hotel_availrms"));
            hotel.setAmentities(rs.getInt("hotel_numofamend"));
            hotel.setPrice(rs.getDouble("hotel_price"));
            list.add(hotel);

        }
        //Set property to tableview columns

        //NB. Use the same property that is in Object class

        //System.out.println( hotel.hotelnameProperty().getValue());
        Hotel.setCellValueFactory(new PropertyValueFactory<>("hotelname"));
        Rooms.setCellValueFactory(new PropertyValueFactory<>("rooms"));
        Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        amentities.setCellValueFactory(new PropertyValueFactory<>("amentities"));
        // Hotel.setCellValueFactory(c-> new SimpleStringProperty(hotel.getHotelname()));

        //set data tp tableview
        tableHotels.setItems(list);

    }








}