import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PropertyModifyController extends DBConnection {
    @FXML private ImageView hotelImage;
    @FXML private TextField hotelName;
    @FXML private TextField hotelAddress;
    @FXML private TextField hotelNumRooms;
    @FXML private TextField hotelNumAmenities;
    @FXML private TextArea hotelDesc;
    @FXML private ListView<String> roomTypesLV;
    @FXML private ListView<String> amenitiesLV;
    @FXML private Button backBtn;
    @FXML private Button modifyBtn;

    private final Property property;
    private Connection conn;
    private StringBuilder query;

    public PropertyModifyController(Property property){
        this.property = property;
        try {
            conn = getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleButtons(ActionEvent event){
        if(event.getSource() == modifyBtn){
            handleModify();
        }else if(event.getSource() == backBtn){
            AnchorPane newScene = null;
            try {
                newScene = FXMLLoader.load(getClass().getResource("StaffProperty.fxml"));
                Scene scene = new Scene(newScene);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void initialize(){
        query = new StringBuilder();
        File file = new File("Res/images/hotels/" + property.getPropertyName());
        Image image = new Image(file.toURI().toString());
        ImageView im = new ImageView();
        im.setImage(image);
        im.setPreserveRatio(true);
        im.setFitHeight(280);
        im.setFitWidth(280);
        hotelImage.setImage(im.getImage());
//        hotelImage = im;
        hotelName.setText(property.getPropertyName());
        hotelAddress.setText(property.getAddress());
        hotelDesc.setText(property.getDesc());
        hotelNumRooms.setText(Integer.toString(property.getNumberRooms()));
        hotelNumAmenities.setText(Integer.toString(property.getNumberAmenities()));
        try {
            initializeAmenities();
            initializeRooms();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void initializeAmenities() throws SQLException {
        CallableStatement callableStatement = conn.prepareCall("{call hotel.getAmenitiesByHotel(?)}");
        callableStatement.setString(1, property.getPropertyName());
        ResultSet rs = callableStatement.executeQuery();
        ObservableList<String> amenitiesList = FXCollections.observableArrayList();
        if(rs.next()){
            do{
                amenitiesList.add(rs.getString("Amenities_desc"));
            }while(rs.next());
            amenitiesLV.setItems(amenitiesList);
        }
    }
    private void initializeRooms() throws SQLException {
        CallableStatement callableStatement = conn.prepareCall("{call hotel.getAllRoomTypes(?)}");
        callableStatement.setInt(1, property.getPropertyId());
        ResultSet rs = callableStatement.executeQuery();
        ObservableList<String> roomsList = FXCollections.observableArrayList();
        if (rs.next()) {
            do{
                roomsList.add(rs.getString("type_name"));
            }while(rs.next());
            roomTypesLV.setItems(roomsList);
        }
    }

    private void handleModify(){
        if(modifiedCheck()){

        }
    }

    private boolean modifiedCheck(){
        boolean modified = false;
        String modifiedHotelName = hotelName.getText();
        String modifiedHotelAddress = hotelAddress.getText();
        String modifiedHotelDesc = hotelDesc.getText();

        if(!modifiedHotelName.equals("") && !modifiedHotelName.equals(property.getPropertyName())){
            modified = true;
        }
        if(!modifiedHotelAddress.equals("") && !modifiedHotelAddress.equals(property.getAddress())){
            modified = true;
        }
        if(!modifiedHotelDesc.equals("") && !modifiedHotelDesc.equals(property.getDesc())){
            modified = true;
        }

        return modified;
    }
}
