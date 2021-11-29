import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;


public class PropertyCreateController extends DBConnection implements Initializable {
    public Button removeRoomBtn1;
    public Button addRoomBtn;
    public TableColumn style2Column;
    public TableColumn amount2Column;
    public TableView cartTV;
    public ChoiceBox roomCB;
    public TableColumn styleColumn;
    public TableView roomTV;
    public ListView amenitiesLV;
    @FXML private Text mainmenuTV;
    @FXML private Text logoutTV;
    @FXML private TextField propertyNameField;
    @FXML private TextField propertyAddressField;
    @FXML private TextField basePriceField;
    @FXML private TextField weekendRateField;
    @FXML private TextArea propertyDescArea;
    @FXML private TextArea roomTypeArea;
    @FXML private TextArea amenitiesArea;
    @FXML private TextArea typeDescArea;
    @FXML private Spinner<Integer> numberOfRoomsSelector;
    @FXML private Button addBtn;
    @FXML private Button resetBtn;
    @FXML private Button backBtn;
    @FXML private AnchorPane anchor;
    @FXML private ObservableList<String> cartList;

    private final int initialValue = 1;
    Connection conn;
    ObservableList<String> typeStrings = FXCollections.observableArrayList();

    HashMap<String, Object> RType =
            new HashMap<String, Object>();

    private TableView<ObservableList<StringProperty>> table = new TableView<>();
    private ArrayList<String> myList = new ArrayList<>();
    ObservableList<StringProperty> roomTypes = FXCollections.observableArrayList();







    @FXML private void sceneChange(MouseEvent event){
        AnchorPane newScene = null;
        try{
            if(event.getSource() == mainmenuTV){
                newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            }else if(event.getSource() == logoutTV){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                LoginController controller = new LoginController();
                loader.setController(controller);
                newScene = loader.load();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void populateRoomTypesTable() throws ClassNotFoundException, SQLException {
        Connection con = null;
        con = getConnection();

        CallableStatement callableStatement = con.prepareCall("{call hotel.getAllRoomTypesThatExist()}");
        ResultSet rs = callableStatement.executeQuery();

        while(rs.next()){

            typeStrings.add(rs.getString("roomtypes"));
            amenitiesLV.getItems().add(rs.getString("roomtypes"));

        }


        roomCB.setItems(typeStrings);




    }


    @FXML private void handleButtons(ActionEvent event){
        if(event.getSource() == addBtn){
            handleAddProperty();
        }else if(event.getSource() == resetBtn){
            resetFields();
        }else if(event.getSource() == backBtn){
            AnchorPane newScene;
            try{
                newScene = FXMLLoader.load(getClass().getResource("StaffProperty.fxml"));
                Scene scene = new Scene(newScene);
                Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    @FXML void roomChange(ActionEvent event) {
        Stage stage = (Stage) anchor.getScene().getWindow(); // for displaying Toast error messages
        String choice;

    }


    private void resetFields(){
        propertyAddressField.setText("");
        propertyDescArea.setText("");
        propertyNameField.setText("");
        amenitiesArea.setText("");
        basePriceField.setText("");
        weekendRateField.setText("");

    }

    private void handleAddProperty(){
        boolean error = false;
        Stage stage = (Stage) anchor.getScene().getWindow();
        StringBuilder errorString = new StringBuilder();
        String priceText = basePriceField.getText();
        double price = 0.0;
        try {
            price = Double.parseDouble(priceText);
        }catch(NumberFormatException e){
            error = true;
            errorString.append("Invalid base price");
        }
        String rateText = weekendRateField.getText();
        double rate = 0.0;
        try{
            rate = Double.parseDouble(rateText);
        }catch(NumberFormatException e){
            error = true;
            errorString.append("\nInvalid weekend rate value");
        }

        String amenitiesText = amenitiesArea.getText();
        if(amenitiesText.equals("") || amenitiesText.equals(",") || amenitiesText.equals("\n")){
            error = true;
            errorString.append("\nInvalid amenities value");
        }



        String typeDescText = typeDescArea.getText();
        if(typeDescText.equals("") || typeDescText.equals(",") || typeDescText.equals("\n")){
            error = true;
            errorString.append("\nInvalid room type description value");
        }

        String propertyNameText = propertyNameField.getText();
        String propertyAddressText = propertyAddressField.getText();
        String propertyDescText = propertyDescArea.getText();
        if(propertyNameText.equals("")){
            error = true;
            errorString.append("\nInvalid property name value");
        }
        if(propertyAddressText.equals("")){
            error = true;
            errorString.append("\nInvalid property address value");
        }
        if(propertyDescText.equals("")){
            error = true;
            errorString.append("\nInvalid property description value");
        }
        if(error){
            Toast.makeText(stage, errorString.toString(), 2000, 500, 500);
            errorString.setLength(0);
            return;
        }


/**

        String[] amenities = amenitiesText.split("\n");
        Property property = new Property(propertyNameText, propertyDescText, propertyAddressText, amenities, rooms, new ArrayList<>(), 0, numberOfRooms);
        try {

            CallableStatement callableStatement = conn.prepareCall("{call hotel.CreateNewHotel(?,?,?,?,?,?,?)}");
            callableStatement.setString(1, property.getPropertyName());
            callableStatement.setString(2, property.getAddress());
            callableStatement.setString(3, property.getDesc());
            callableStatement.setString(4, Integer.toString(property.getNumberRooms()));
            callableStatement.setInt(5, property.getNumberAmenities());
            callableStatement.setInt(6, property.getRating());
            callableStatement.setDouble(7, price);
            callableStatement.executeQuery();

            callableStatement = conn.prepareCall("{call hotel.updateBulkRoomTypes(?,?,?,?)}");

                callableStatement.setString(1, key);
                callableStatement.setString(2, roomsDesc.get(key));
                callableStatement.setDouble(3, price);
                callableStatement.setInt(4, typesCount.get(key));
                callableStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 **/
}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            populateRoomTypesTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cartList = FXCollections.observableArrayList();
        cartTV.setItems(cartList);



        System.out.println("PROPERTY CREATE");
        try {
            conn = getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }
}
