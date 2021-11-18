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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SharedBooking implements Initializable {

    @FXML private Text hNameTF;
    @FXML private Text hAddressTF;
    @FXML private Text hRatingTF;
    @FXML private Text checkInTF;
    @FXML private Text checkOutTF;
    @FXML private Text returnTF;
    @FXML private TextArea descriptionTF;
    @FXML private TextField adultsTF;
    @FXML private TextField childrenTF;
    @FXML private DatePicker checkInDP;
    @FXML private DatePicker checkOutDP;
    @FXML private Button addRoomBtn;
    @FXML private Button bookBtn;
    @FXML private TableView<Room> roomTB;
    @FXML private TableColumn<Room, String> styleColumn;
    // TODO: 11/17/2021 make occupancy column
    @FXML private TableColumn<Room, String> occupancyColumn;
    @FXML private TableColumn<Room, String> availableColumn;
    @FXML private TableColumn<Room, String> priceColumn;
    @FXML private ListView<String> amenitiesLV;
    @FXML private ObservableList<Room> roomsList;

    private Property property;
    private String accountType;

    // TODO: 11/17/2021 write handler method for the book button
    @FXML void book(MouseEvent event) { }

    // TODO: 11/17/2021 write method for adding a room
    @FXML void addRoom(ActionEvent event) { }


    /*public SharedBooking(Property property, String accountType) {
        this.property = property;
        this.accountType = accountType;
    }*/

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;
        try{
            if(event.getSource() == returnTF){
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // TODO: 11/17/2021 delete/move this stuff to a populate TableView method
        roomsList = FXCollections.observableArrayList();
        roomsList.add(new Room(106, 300,"King", 1, 33.00));
        roomsList.add(new Room(129, 250,"Queen", 1, 33.00));
        String[] amenities = {"pool", "balcony", "breakfast"};
        ArrayList<Reservation> reservations = new ArrayList<>();
        //Property property = new Property("La Quinta", "nice hotel on the beach", "1 UTSA circle San Antonio", amenities, rooms, reservations, 8, 99);

        styleColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        //occupancyColumn.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        //availableColumn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        roomTB.setItems(roomsList);

    }
}
