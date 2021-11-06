import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StaffPropertyController extends DBConnection implements Initializable {
    @FXML private Text mainmenuTV;
    @FXML private Text logoutTV;
    @FXML private TextField hotelTF;
    @FXML private TextField roomTF;
    @FXML private TextField amenitiesTF;
    @FXML private TableView<Property> propertiesTable;
    @FXML private TableColumn<Property, String> nameColumn;
    @FXML private TableColumn<Property, String> roomsColumn;
    @FXML private TableColumn<Property, String> amenitiesColumn;
    @FXML private ObservableList<Property> propertiesList;
    @FXML private Button createBtn;
    @FXML private Button resetBtn;
    @FXML private Button searchBtn;
    @FXML private Button deleteBtn;

    private StringBuilder query;
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
        AnchorPane newScene = null;
        try {
            if (event.getSource() == createBtn) {
                newScene = FXMLLoader.load(getClass().getResource("PropertyCreate.fxml"));
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        propertiesList = FXCollections.observableArrayList();
        query = new StringBuilder();
        try{
            conn = getConnection();
            populateTable();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFilter(ActionEvent event) throws SQLException {
        if(event.getSource() == resetBtn){
            propertiesList.clear();
            populateTable();
        }else{
            HashMap<Integer, Object> statementValues = new HashMap<>();
            query.setLength(0);
            query.append("SELECT * FROM users");

            if(buildQuery(statementValues)){
                PreparedStatement ps = conn.prepareStatement(query.toString());
                for(Integer key : statementValues.keySet()){
                    Object obj = statementValues.get(key);
                    if(obj instanceof String) {
                        ps.setString(key, (String)obj);
                    }
                    else if(obj instanceof LocalDate) {
                        ps.setDate(key, Date.valueOf((LocalDate) obj));
                    }
                }
//                System.out.println(ps);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    propertiesList.clear();
                    addProperties(rs);
                }
//                System.out.println(rs);
            }else{
                System.out.println("no query built");
            }
        }
        resetFilter();
    }

    private void resetFilter(){
        hotelTF.setText("");
        roomTF.setText("");
        amenitiesTF.setText("");

    }

    private boolean buildQuery(HashMap<Integer, Object> statementValues){
        int pos = 1;
        boolean appended = false;
        query.append(" WHERE");
        String hotel = hotelTF.getText();
        String room = roomTF.getText();
        String amenities = amenitiesTF.getText();


        if(!hotel.equals("")){
            if(appended){
                query.append(" AND");
            }
            query.append(" hotel=?");
            appended=true;
            statementValues.put(pos, hotel);
            pos++;
        }
        if(!room.equals("")){
            if(appended){
                query.append(" AND");
            }
            query.append(" room=?");
            appended = true;
            statementValues.put(pos, room);
            pos++;
        }
        if(!amenities.equals("")){
            if(appended){
                query.append(" AND");
            }
            query.append(" amenities=?");
            appended = true;
            statementValues.put(pos, amenities);
            pos++;
        }
        query.append(";");

        return appended;
    }

    private void populateTable() throws SQLException {
        query.setLength(0);
        query.append("SELECT");
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
        query.append(" room_rates.daily_rate,");
        query.append(" amenities.roomAmenities,");
        query.append(" amenities.services,");
        query.append(" amenities.facilities,");
        query.append(" reservation.*,");
        query.append(" users.email");
        query.append(" FROM hotel");
        query.append(" JOIN room");
        query.append(" ON room.hotel_id = hotel.hotel_id");
        query.append(" JOIN room_types");
        query.append(" ON room_types.room_type_id = room.room_type_id");
        query.append(" JOIN room_rates");
        query.append(" ON room_rates.rrId = room_types.room_type_id");
        query.append(" JOIN amenities");
        query.append(" ON amenities.hotel_id = hotel.hotel_id");
        query.append(" JOIN reservation");
        query.append(" ON reservation.hotel_id = hotel.hotel_id");
        query.append(" JOIN customer");
        query.append(" ON customer.custId = reservation.custId");
        query.append(" JOIN users");
        query.append(" ON users.userId = customer.custId;");

        ResultSet rs = conn.createStatement().executeQuery(query.toString());
        if(rs.next())
            addProperties(rs);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roomsColumn.setCellValueFactory(new PropertyValueFactory<>("rooms"));
        amenitiesColumn.setCellValueFactory(new PropertyValueFactory<>("amenities"));
        propertiesTable.setItems(propertiesList);

    }

    private void addProperties(ResultSet rs) throws SQLException {
        HashMap<String, Property> propertyHashMap = new HashMap<>();
        ArrayList<Room> rooms;
        ArrayList<Reservation> reservations;
        do{
            Property property = propertyHashMap.getOrDefault(rs.getString("hotel_name"), new Property());
            rooms = property.getRooms();
            reservations = property.getReservations();

            Room room = new Room();
            Reservation reservation = new Reservation();

            property.setPropertyName(rs.getString("hotel_name"));
            property.setAddress(rs.getString("hotel_address"));
            property.setDesc(rs.getString("hotel_desc"));
            property.setRating(rs.getInt("hotel_rating"));
            property.setNumberAvailableRooms(rs.getInt("hotel_availrms"));
            property.setNumberRooms(rs.getInt("hotel_total_rms"));

            room.setRoomID(rs.getInt("room_id"));
            room.setRoomNum(rs.getInt("room_num"));
            room.setIsOccupied(rs.getInt("isOccupied"));
            room.setPrice(rs.getDouble("daily_rate"));
            room.setType(rs.getString("type_name"));
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

            property.setRooms(rooms);
            property.setReservations(reservations);
            propertyHashMap.put(rs.getString("hotel_name"), property);
        }while(rs.next());

        propertiesList.addAll(propertyHashMap.values());
    }
}
