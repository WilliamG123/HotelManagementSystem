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
import java.util.ArrayList;

public class PropertyCreateController {
    @FXML private Text mainmenuTV;
    @FXML private Text logoutTV;
    @FXML private TextField propertyNameField;
    @FXML private TextField propertyAddressField;
    @FXML private TextField basePriceField;
    @FXML private TextField weekendRateField;
    @FXML private TextArea propertyDescArea;
    @FXML private TextArea roomTypeArea;
    @FXML private TextArea amenitiesArea;
    @FXML private Spinner<Integer> numberOfRoomsSelector;
    @FXML private Button addBtn;
    @FXML private Button resetBtn;
    @FXML private Button backBtn;
    @FXML private Label errorMessageLabel;

    private final int initialValue = 1;

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
        roomTypeArea.setPromptText("Room Type,Count,Starting Room #");
        amenitiesArea.setPromptText("Amenity (one per line)");
        numberOfRoomsSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, initialValue));
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
        if(amenitiesText.equals("") || amenitiesText.equals(",")){
            error = true;
            errorString.append("\nInvalid amenities value");
        }

        String roomTypesText = roomTypeArea.getText();
        if(roomTypesText.equals("") || roomTypesText.equals(",")){
            error = true;
            errorString.append("\nInvalid room type value");
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
            errorMessageLabel.setText(errorString.toString());
            errorMessageLabel.setTextFill(Color.web("#FF0000"));
            return;
        }

        int numberOfRooms = numberOfRoomsSelector.getValue();
        int runningCount = 0;
        String[] roomTypes = roomTypesText.split("\n");
        String roomType;
        int roomCount;
        int startingRoomNumber;
        ArrayList<Room> rooms = new ArrayList<>();
        for(String roomLine : roomTypes){
            String[] roomComponents = roomLine.split(",");
            roomType = roomComponents[0];
            try{
                roomCount = Integer.parseInt(roomComponents[1]);
            }catch(NumberFormatException e){
                errorString.append("\nInvalid room count value");
                errorMessageLabel.setText(errorString.toString());
                errorMessageLabel.setTextFill(Color.web("#FF0000"));
                return;
            }
            try{
                startingRoomNumber = Integer.parseInt(roomComponents[2]);
            }catch(NumberFormatException e){
                errorString.append("\nInvalid starting room number value");
                errorMessageLabel.setTextFill(Color.web("#FF0000"));
                errorMessageLabel.setText(errorString.toString());
                return;
            }
            for(int i = 0; i < roomCount; i++){
                Room room = new Room(startingRoomNumber++, price, roomType, 0, rate);
                runningCount++;
                if(runningCount > numberOfRooms){
                    errorString.append("\nNumber of rooms exceeded the total number of rooms. Nothing added");
                    errorMessageLabel.setTextFill(Color.web("#FF0000"));
                    errorMessageLabel.setText(errorString.toString());
                    return;
                }
                rooms.add(room);
            }
        }

        String[] amenities = amenitiesText.split("\n");
        Property property = new Property(propertyNameText, propertyDescText, propertyAddressText, amenities, rooms, new ArrayList<>(), 0, numberOfRooms);
        System.out.println(property);
    }
}
