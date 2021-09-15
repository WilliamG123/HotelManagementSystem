import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.text.TabableView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    //lets declare some variables

    private Connection conn;
    private ObservableList<Hotels> list;

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == mainmenuTV)
                newScene = FXMLLoader.load(getClass().getResource("UserMainMenu.fxml"));
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

    //run the method
        try {
            populateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    //method to populate tableview
    private void populateTableView() throws SQLException {

        //instantiate list
    list= FXCollections.observableArrayList();
        //select query string
     String query="SELECT * FROM hotel";
        //Run Query and put results in resultset
     conn=getConnection();
     ResultSet rs = conn.createStatement().executeQuery(query);
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
