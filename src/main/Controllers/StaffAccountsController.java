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

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StaffAccountsController extends DBConnection implements Initializable {

    @FXML private AnchorPane accountsScreen;
    @FXML private Text mainmenuTV;
    @FXML private Text logoutTV;
    @FXML private Text modifyTV;
    @FXML private Text createTV;
    @FXML private Text deleteTV;
    @FXML private TextField firstNameSearchField;
    @FXML private TextField lastNameSearchField;
    @FXML private TextField emailSearchField;
    @FXML private TextField phoneNumberSearchField;
    @FXML private DatePicker dobSearchPicker;
    @FXML private ComboBox<String> typeSearchPicker;
    @FXML private Button searchBtn;
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> firstNameColumn;
    @FXML private TableColumn<User, String> lastNameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> phoneColumn;
    @FXML private TableColumn<User, String> dobColumn;
    @FXML private TableColumn<User, String> typeColumn;

    @FXML private ObservableList<User> usersList;

    private Connection conn;
    private StringBuilder query;

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == mainmenuTV)
                newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            else if(event.getSource() == logoutTV){
                LoadedUser.getInstance().clearUser();
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
            }else if(event.getSource() == modifyTV){
                User selectedUser = usersTable.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("StaffModify.fxml"));
                StaffModifyController controller = new StaffModifyController(selectedUser);
                loader.setController(controller);
                newScene = loader.load();
            }else if(event.getSource() == createTV){
                newScene = FXMLLoader.load(getClass().getResource("StaffCreateUser.fxml"));
            } else if (event.getSource() == deleteTV) {
                handleUserDelete();
                return;
            }
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
        query = new StringBuilder("SELECT * FROM users");
        try {
            populateListView();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        phoneNumberSearchField.setPromptText("Ex: 999-999-9999");
        typeSearchPicker.getItems().add("CUST");
        typeSearchPicker.getItems().add("EMP");
    }

    private void populateListView() throws SQLException, ClassNotFoundException {
        usersList = FXCollections.observableArrayList();
        query.append(";");
        conn = getConnection();
        ResultSet rs = conn.createStatement().executeQuery(query.toString());

        addUsers(rs);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        usersTable.setItems(usersList);
    }

    private void handleUserDelete(){
        User user = usersTable.getSelectionModel().getSelectedItem();

    }

    @FXML
    private void handleSearch(ActionEvent event) throws SQLException {
        query.deleteCharAt(query.length()-1);
        usersList.clear();
        buildQuery();
        System.out.println(query.toString());
        ResultSet rs = conn.createStatement().executeQuery(query.toString());
        addUsers(rs);
    }

    private void buildQuery(){
        query.append(" WHERE");
        String email = emailSearchField.getText();
        if(Validators.isValidEmailAddress(email)){
            query.append(" email=");
            query.append(email);
        }
        query.append(";");
    }

    private void addUsers(ResultSet rs) throws SQLException {
        while(rs.next()){
            User user = new User();
            user.setFirstName(rs.getString("fname"));
            user.setLastName(rs.getString("lname"));
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phone"));
            user.setType(rs.getString("usertype"));
            user.setDob(rs.getDate("dob").toLocalDate());
            usersList.add(user);
        }
    }
}
