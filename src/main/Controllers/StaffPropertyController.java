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
    @FXML private ObservableList<Property> propertiesList;
    @FXML private ListView<String> amenitiesLV;
    @FXML private ListView<String> roomsLV;
    @FXML private HashSet<String> amenitiesLVHS;
    @FXML private HashSet<String> roomsLVHS;
    @FXML private Button createBtn;
    @FXML private Button resetBtn;
    @FXML private Button searchBtn;
    @FXML private Button deleteBtn;

    private StringBuilder query;
    private Connection conn;
    private RoomInformation roomInfo;

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
        AnchorPane newScene = null;
        try {
            if (event.getSource() == createBtn) {
                newScene = FXMLLoader.load(getClass().getResource("PropertyCreate.fxml"));
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
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomInfo = new RoomInformation();
        propertiesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        propertiesList = FXCollections.observableArrayList();
        amenitiesLVHS = new HashSet<>();
        roomsLVHS = new HashSet<>();
        query = new StringBuilder();
        try{
            conn = getConnection();
            populateTable();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        populateAmenitiesList();
        populateRoomsList();
    }

    private void populateTable() throws SQLException, ClassNotFoundException {
        query.setLength(0);
        query.append("SELECT");
        query.append(" hotel.hotel_id,");
        query.append(" hotel.hotel_name,");
        query.append(" hotel.hotel_address,");
        query.append(" hotel.hotel_desc,");
        query.append(" hotel.hotel_total_rms,");
        query.append(" hotel.hotel_availrms,");
        query.append(" hotel.hotel_numofamend,");
        query.append(" hotel.hotel_rating,");
        query.append(" room.room_id,");
        query.append(" room.room_number,");
        query.append(" room.isOccupied,");
        query.append(" room_types.type_name,");
        query.append(" room_types.room_type_desc,");
        query.append(" room_types.room_rate,");
        query.append(" amenities.Amenities_desc,");
        query.append(" amenities.type,");
//        query.append(" amenities.facilities,");
        query.append(" reservation.*,");
        query.append(" users.email");
        query.append(" FROM hotel");
        query.append(" JOIN room");
        query.append(" ON room.hotel_id = hotel.hotel_id");
        query.append(" JOIN room_types");
        query.append(" ON room_types.room_type_id = room.room_id");
        query.append(" JOIN hotel_amenities");
        query.append(" ON hotel_amenities.hotel_id = hotel.hotel_id");
        query.append(" JOIN amenities");
        query.append(" ON amenities.amenitiesId = hotel_amenities.amenitiesId");
//        query.append(" JOIN amenities");
//        query.append(" ON amenities.hotel_id = hotel.hotel_id");
        query.append(" JOIN reservation");
        query.append(" ON reservation.hotel_id = hotel.hotel_id");
        query.append(" JOIN customer");
        query.append(" ON customer.custId = reservation.custId");
        query.append(" JOIN users");
        query.append(" ON users.userId = customer.custId;");

        ResultSet rs = conn.createStatement().executeQuery(query.toString());
        if(rs.next())
            addProperties(rs);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("propertyName"));
        roomsColumn.setCellValueFactory(new PropertyValueFactory<>("numberAvailableRooms"));
        amenitiesColumn.setCellValueFactory(new PropertyValueFactory<>("amenitiesString"));
        propertiesTable.setItems(propertiesList);
        for(Property prop : propertiesList){
            System.out.println(prop.getAmenitiesString());
        }

    }

    private void addProperties(ResultSet rs) throws SQLException, ClassNotFoundException {
        HashMap<String, Property> propertyHashMap = new HashMap<>();
        ArrayList<Room> rooms;
        ArrayList<Reservation> reservations;
        ArrayList<String> amenities;
        QUERYS query = new QUERYS();
        do{
            Property property = propertyHashMap.getOrDefault(rs.getString("hotel_name"), new Property());
            rooms = property.getRooms();
            reservations = property.getReservations();
            amenities = property.getAmenitiesAL();

            Room room = new Room();
            Reservation reservation = new Reservation();

            property.setPropertyId(rs.getInt("hotel_id"));
            property.setPropertyName(rs.getString("hotel_name"));
            property.setAddress(rs.getString("hotel_address"));
            property.setDesc(rs.getString("hotel_desc"));
            property.setRating(rs.getInt("hotel_rating"));
            property.setNumberAvailableRooms(rs.getInt("hotel_availrms"));
            property.setNumberRooms(rs.getInt("hotel_total_rms"));

            room.setRoomID(rs.getInt("room_id"));
            room.setRoomNum(rs.getInt("room_number"));
            room.setIsOccupied(rs.getInt("isOccupied"));
            room.setPrice(rs.getDouble("room_rate"));
            room.setType(rs.getString("type_name"));
            room.setDesc(rs.getString("room_type_desc"));
//            room.setAmenities(rs.getString("roomAmenities").split(","));
            rooms.add(room);

            reservation.setResID(rs.getInt("reservationId"));
            reservation.setCost(rs.getDouble("total_price"));
            reservation.setCheckIn(rs.getDate("check_in").toLocalDate());
            reservation.setCheckOut(rs.getDate("check_out").toLocalDate());
            reservation.setAdults(rs.getInt("adults"));
            reservation.setChildren(rs.getInt("children"));
            reservation.setHotelName(rs.getString("hotel_name"));
            reservation.setCustId(rs.getString("email"));
            reservations.add(reservation);

//            ResultSet amenitiesSet = query.getAmenitiesByHotelName(rs.getString("hotel_name"));
//            if(amenitiesSet.next()){
//                do{
//                    amenities.add(amenitiesSet.getString("Amenities_desc"));
//                }while(amenitiesSet.next());
//            }
            //TODO: uncomment after requested db change
//            amenities.addAll(Arrays.asList(rs.getString("propertyAmenities")));
            amenities.add(rs.getString("Amenities_desc"));
//            amenities.add(rs.getString("facilities"));

            property.setRooms(rooms);
            property.setReservations(reservations);
            property.setAmenitiesAL(amenities);
            propertyHashMap.put(rs.getString("hotel_name"), property);
        }while(rs.next());

        propertiesList.addAll(propertyHashMap.values());
    }

    private void deleteProperty() throws SQLException {
        Property property = propertiesTable.getSelectionModel().getSelectedItem();
        String query = "DELETE FROM hotel WHERE hotel_id=?;";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, property.getPropertyId());
        ps.executeUpdate();
    }

    private void handleSearch(){
        String propName = hotelTF.getText();
        boolean searchName = !propName.equals("");
        boolean added = false;
        ObservableList<Property> toSearch = propertiesTable.getItems();
        ObservableList<Property> found = FXCollections.observableArrayList();
        ObservableList<String> amenitiesSearch = amenitiesLV.getSelectionModel().getSelectedItems();
        ObservableList<String> roomsSearch = roomsLV.getSelectionModel().getSelectedItems();
        for(Property prop : toSearch){
            if(searchName && prop.getPropertyName().equalsIgnoreCase(propName)) {
                if(!found.contains(prop)) {
                    found.add(prop);
                    added = true;
                }
            }

            for(Room room : prop.getRooms()){
                if(roomsSearch.contains(room.getType())){
                    if(!found.contains(prop)){
                        found.add(prop);
                        added = true;
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

    private void populateRoomsList(){
        for(Property prop : propertiesList){
            for(Room room : prop.getRooms()){
                roomsLVHS.add(room.getType());
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
