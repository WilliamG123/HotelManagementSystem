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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class PropertyModifyController extends DBConnection {
    @FXML private ImageView hotelImage;
    @FXML private TextField hotelName;
    @FXML private TextField hotelAddress;
    @FXML private TextField hotelNumRooms;
    @FXML private TextField hotelNumAmenities;
    @FXML private TextArea hotelDesc;
    @FXML private Text mainmenuTV;
    @FXML private Text logoutTV;
    @FXML private ListView<String> roomTypesLV;
    @FXML private ListView<String> amenitiesLV;
    @FXML private Button backBtn;
    @FXML private Button modifyBtn;
    @FXML private AnchorPane anchor;

    private final Property property;
    private Connection conn;

    public PropertyModifyController(Property property){
        this.property = property;
        try {
            conn = getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sceneChange(MouseEvent event){
        AnchorPane newScene = null;

        try {
            if (event.getSource() == mainmenuTV)
                newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            else if(event.getSource() == logoutTV){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                LoginController controller = new LoginController();
                loader.setController(controller);
                newScene = loader.load();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleButtons(ActionEvent event){
        try {
            if (event.getSource() == modifyBtn) {
                handleModify();
            } else if (event.getSource() == backBtn) {
                AnchorPane newScene = null;
                newScene = FXMLLoader.load(getClass().getResource("StaffProperty.fxml"));
                Scene scene = new Scene(newScene);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();

            }
        }catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    public void initialize(){
        File file = new File("Res/images/hotels/" + property.getPropertyName());
        Image image = new Image(file.toURI().toString());
        ImageView im = new ImageView();
        im.setImage(image);
        im.setPreserveRatio(true);
        im.setFitHeight(280);
        im.setFitWidth(280);
        hotelImage.setImage(im.getImage());
        try {
            initializeAmenities();
            initializeRooms();
        }catch(SQLException e){
            e.printStackTrace();
        }
//        hotelImage = im;
        hotelName.setText(property.getPropertyName());
        hotelAddress.setText(property.getAddress());
        hotelDesc.setText(property.getDesc());
        hotelNumRooms.setText(Integer.toString(property.getNumberAvailableRooms()));
        hotelNumAmenities.setText(Integer.toString(property.getNumberAmenities()));
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

    private void handleModify() throws SQLException {
        if(modifiedCheck()){
            Stage stage = (Stage) anchor.getScene().getWindow();
            String modifiedHotelName = hotelName.getText();
            String modifiedHotelAddress = hotelAddress.getText();
            String modifiedHotelDesc = hotelDesc.getText();
            String query = "UPDATE hotel SET hotel_name=?, hotel_address=?, hotel_desc=? WHERE hotel_id=?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, modifiedHotelName);
            ps.setString(2, modifiedHotelAddress);
            ps.setString(3, modifiedHotelDesc);
            ps.setInt(4, property.getPropertyId());
            ps.executeUpdate();
            Toast.makeText(stage, "Modification successful!", 2000, 500, 500);
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
