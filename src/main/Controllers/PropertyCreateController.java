import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PropertyCreateController extends DBConnection {
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

    private final int initialValue = 1;
    Connection conn;

    @FXML private void sceneChange(MouseEvent event){
        AnchorPane newScene = null;
        try{
            if(event.getSource() == mainmenuTV){
                newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            }else if(event.getSource() == logoutTV){
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
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

    @FXML private void initialize(){
        numberOfRoomsSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, initialValue));
        try {
            conn = getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void resetFields(){
        propertyAddressField.setText("");
        propertyDescArea.setText("");
        propertyNameField.setText("");
        amenitiesArea.setText("");
        basePriceField.setText("");
        weekendRateField.setText("");
        numberOfRoomsSelector.getValueFactory().setValue(initialValue);
        roomTypeArea.setText("");
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

        String roomTypesText = roomTypeArea.getText();
        if(roomTypesText.equals("") || roomTypesText.equals(",") || roomTypesText.equals("\n")){
            error = true;
            errorString.append("\nInvalid room type value");
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

        int numberOfRooms = numberOfRoomsSelector.getValue();
        int runningCount = 0;
        String[] roomTypes = roomTypesText.split("\n");
        String roomType;
        int roomCount;
        int startingRoomNumber;
        ArrayList<Room> rooms = new ArrayList<>();
        HashMap<String, Integer> typesCount = new HashMap<>();
        for(String roomLine : roomTypes){
            String[] roomComponents = roomLine.split(",");
            roomType = roomComponents[0];
            try{
                roomCount = Integer.parseInt(roomComponents[1]);
            }catch(NumberFormatException e){
                Toast.makeText(stage, "Invalid room count value", 2000, 500, 500);
                return;
            }
            typesCount.put(roomComponents[0], roomCount);
            try{
                startingRoomNumber = Integer.parseInt(roomComponents[2]);
            }catch(NumberFormatException e){
                errorString.append("Invalid starting room number value");
                Toast.makeText(stage, "Invalid starting room number value", 2000, 500, 500);
                return;
            }
            for(int i = 0; i < roomCount; i++){
                Room room = new Room(startingRoomNumber++, price, roomType, 0, rate);
                runningCount++;
                if(runningCount > numberOfRooms){
                    Toast.makeText(stage, "Number of rooms exceeded the total number of rooms. Nothing added", 2000, 500, 500);
                    return;
                }
                rooms.add(room);
            }
        }

        HashMap<String, String> roomsDesc = new HashMap<>();
        for(String lines : typeDescText.split("\n")){
            String[] components = lines.split(",");
            if(components.length > 2){
                Toast.makeText(stage, "Invalid type description value, too many commas\nNothing added",2000, 500, 500);
                return;
            }
            roomsDesc.put(components[0], components[1]);
        }

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
            for(String key : roomsDesc.keySet()){
                callableStatement.setString(1, key);
                callableStatement.setString(2, roomsDesc.get(key));
                callableStatement.setDouble(3, price);
                callableStatement.setInt(4, typesCount.get(key));
                callableStatement.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
