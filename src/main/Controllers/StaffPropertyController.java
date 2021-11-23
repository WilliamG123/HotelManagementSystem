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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class StaffPropertyController extends DBConnection implements Initializable {
    @FXML private Text mainmenuTV;
    @FXML private Text logoutTV;
    @FXML private TextField hotelTF;
    @FXML private TableView<Property> propertiesTable;
    @FXML private TableColumn<Property, String> nameColumn;
    @FXML private TableColumn<Property, String> roomsColumn;
    @FXML private TableColumn<Property, String> amenitiesColumn;
    @FXML private TableColumn<Property, String> imageColumn;
    @FXML private ObservableList<Property> propertiesList;
    @FXML private ListView<String> amenitiesLV;
    @FXML private ListView<String> roomsLV;
    @FXML private HashSet<String> amenitiesLVHS;
    @FXML private HashSet<String> roomsLVHS;
    @FXML private Button createBtn;
    @FXML private Button resetBtn;
    @FXML private Button searchBtn;
    @FXML private Button deleteBtn;

    private Connection conn;

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == mainmenuTV)
            newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            else if(event.getSource() == logoutTV){
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("test");

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML private void handleButtons(ActionEvent event){
        try {
            if (event.getSource() == createBtn) {
                AnchorPane newScene = FXMLLoader.load(getClass().getResource("PropertyCreate.fxml"));
                Scene scene = new Scene(newScene);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }else if(event.getSource() == deleteBtn){
                deleteProperty();
                return;
            }else if(event.getSource() == searchBtn){
                handleSearch();
                resetFilters();
                return;
            }else if(event.getSource() == resetBtn){
                propertiesList.clear();
                populateTable();
                return;
            }
        }catch(IOException | SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        propertiesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        propertiesList = FXCollections.observableArrayList();
        amenitiesLVHS = new HashSet<>();
        roomsLVHS = new HashSet<>();
        try{
            conn = getConnection();
            populateTable();
            populateRoomsList();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        populateAmenitiesList();
    }

    private void populateTable() throws SQLException, ClassNotFoundException {
        addProperties();

        imageColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("propertyName"));
        roomsColumn.setCellValueFactory(new PropertyValueFactory<>("numberAvailableRooms"));
        amenitiesColumn.setCellValueFactory(new PropertyValueFactory<>("amenitiesString"));
        propertiesTable.setItems(propertiesList);


    }

    private void addProperties() throws SQLException {
        HashMap<String, Property> propertyHashMap = new HashMap<>();
        ArrayList<String> amenities;
        CallableStatement callableStatement = conn.prepareCall("{call hotel.getAllHotelsAndAmenities()}");
        ResultSet rs = callableStatement.executeQuery();

        if(rs.next()) {
            do {
                Property property = propertyHashMap.getOrDefault(rs.getString("hotel_name"), new Property());
                amenities = property.getAmenitiesAL();

                if (property.getPhoto() == null) {
                    File file = new File("Res/images/hotels/" + rs.getString("hotel_image"));
                    Image image = new Image(file.toURI().toString());
                    ImageView im = new ImageView();
                    im.setImage(image);
                    im.setPreserveRatio(true);
                    im.setFitHeight(150);
                    im.setFitWidth(200);
                    property.setPhoto(im);
                }
                property.setPropertyId(rs.getInt("hotel_id"));
                property.setPropertyName(rs.getString("hotel_name"));
                property.setAddress(rs.getString("hotel_address"));
                property.setDesc(rs.getString("hotel_desc"));
                property.setNumberAvailableRooms(rs.getInt("hotel_availrms"));


                amenities.addAll(Arrays.asList(rs.getString("group_concat(DISTINCT Amenities_desc)").split(",")));

                property.setAmenitiesAL(amenities);
                propertyHashMap.put(rs.getString("hotel_name"), property);
            } while (rs.next());
            propertiesList.addAll(propertyHashMap.values());
        }
    }

    private void deleteProperty() throws SQLException {
        Property property = propertiesTable.getSelectionModel().getSelectedItem();
        if(property != null) {
            String query = "DELETE FROM hotel WHERE hotel_id=?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, property.getPropertyId());
            ps.executeUpdate();
        }
    }

    private void handleSearch() throws SQLException {
        String propName = hotelTF.getText();
        boolean searchName = !propName.equals("");
        boolean added = false;
        propertiesList.clear();
        addProperties();
        ObservableList<Property> toSearch = propertiesList;
        ObservableList<Property> found = FXCollections.observableArrayList();
        ObservableList<String> amenitiesSearch = amenitiesLV.getSelectionModel().getSelectedItems();
        ObservableList<String> roomsSearch = roomsLV.getSelectionModel().getSelectedItems();
        CallableStatement callableStatement = conn.prepareCall("{call hotel.getAllPropertiesByRoomType(?)}");
        for(Property prop : toSearch){
            if(searchName && prop.getPropertyName().equalsIgnoreCase(propName)) {
                if(!found.contains(prop)) {
                    found.add(prop);
                    added = true;
                }
            }

            for(String type : roomsSearch) {
                if(!found.contains(prop)) {
                    callableStatement.setString(1, type);
                    ResultSet propByType = callableStatement.executeQuery();
                    if(propByType.next()){
                        do {
                            if (propByType.getString("hotel_name").equals(prop.getPropertyName())) {
                                found.add(prop);
                                added = true;
                            }
                        }while(propByType.next());
                    }
                }
            }

            for(String amenity : prop.getAmenitiesAL()){
                if(amenitiesSearch.contains(amenity)){
                    if(!found.contains(prop)){
                        found.add(prop);
                        added = true;
                    }
                }
            }
        }
        if(added){
            propertiesList.clear();
            propertiesList.addAll(found);
            propertiesTable.setItems(propertiesList);
        }
    }

    private void resetFilters(){
        hotelTF.setText("");
        roomsLV.getSelectionModel().clearSelection();
        amenitiesLV.getSelectionModel().clearSelection();

    }

    private void populateAmenitiesList(){
        for(Property prop : propertiesList){
            amenitiesLVHS.addAll(prop.getAmenitiesAL());
        }
        initializeListView(amenitiesLV, amenitiesLVHS);
    }

    private void populateRoomsList() throws SQLException {
        CallableStatement callableStatement = conn.prepareCall("{call hotel.getAllRoomTypes(?)}");
        for(Property prop : propertiesList){
            callableStatement.setString(1, Integer.toString(prop.getPropertyId()));
            ResultSet roomType = callableStatement.executeQuery();
            if(roomType.next()){
                do{
                    roomsLVHS.add(roomType.getString("type_name"));
                }while(roomType.next());
            }
        }

        initializeListView(roomsLV, roomsLVHS);
    }

    private void initializeListView(ListView<String> listView, HashSet<String> hashSet) {
        listView.setItems(FXCollections.observableArrayList(hashSet));
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            Node node = evt.getPickResult().getIntersectedNode();

            // go up from the target node until a list cell is found or it's clear
            // it was not a cell that was clicked
            while (node != null && node != listView && !(node instanceof ListCell)) {
                node = node.getParent();
            }

            // if is part of a cell or the cell,
            // handle event instead of using standard handling
            if (node instanceof ListCell) {
                // prevent further handling
                evt.consume();

                ListCell cell = (ListCell) node;
                ListView lv = cell.getListView();

                // focus the listview
                lv.requestFocus();

                if (!cell.isEmpty()) {
                    // handle selection for non-empty cells
                    int index = cell.getIndex();
                    if (cell.isSelected()) {
                        lv.getSelectionModel().clearSelection(index);
                    } else {
                        lv.getSelectionModel().select(index);
                    }
                }
            }
        });
    }
}
