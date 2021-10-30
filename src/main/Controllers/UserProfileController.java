import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;


/*
    scene controls the user profile scene
    TF = textfield
    TV = textview
 */

public class UserProfileController extends DBConnection implements Initializable {
    @FXML private TextField usernameTF;
    @FXML private DatePicker dobPicker;
    @FXML private TextField firstNameTF;
    @FXML private TextField lastNameTF;
    @FXML private TextField phoneTF;
    @FXML private PasswordField passwordField;
    @FXML private Text mainmenuTV;
    @FXML private Text logoutTV;
    @FXML private ImageView pfpIV;
    @FXML private Button saveBtn;
    @FXML private Button modifyBtn;
    @FXML private Label modifyMsgLabel;

    private User toModify;

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == mainmenuTV) {
                if (LoadedUser.getInstance().getUser().getType().equals("CUST"))
                    newScene = FXMLLoader.load(getClass().getResource("UserMainMenu.fxml"));
                else if (LoadedUser.getInstance().getUser().getType().equals("EMP"))
                    newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            }else if(event.getSource() == logoutTV){
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void handleModify(ActionEvent event) {
        if(event.getSource() == modifyBtn){
            saveBtn.setDisable(false);
            modifyBtn.setDisable(true);
            enableFields(true);

        }else if(event.getSource() == saveBtn){
            saveBtn.setDisable(true);
            modifyBtn.setDisable(false);
            enableFields(false);

            try {
                if (modifyUser()) {
                    LoadedUser.getInstance().updateUser(toModify);
                    String query = "UPDATE users SET fname=?, lname=?, phone=?, password=?, dob=? WHERE email=?";
                    Connection conn = getConnection();
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, toModify.getFirstName());
                    ps.setString(2, toModify.getLastName());
                    ps.setString(3, toModify.getPhoneNumber());
                    ps.setDate(4, Date.valueOf(toModify.getDob()));
                    ps.setString(5, toModify.getEmail());
                    ps.executeQuery();
                }
            }catch(NoSuchAlgorithmException | ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }
        }
    }

    private boolean modifyUser() throws NoSuchAlgorithmException {
        boolean modified = false;

        String firstName = firstNameTF.getText();
        String lastName = lastNameTF.getText();
        String phoneNumber = phoneTF.getText();
        String password = passwordField.getText();
        String hashed = (password == null) ? null : Hasher.getInstance("SHA-256").hash(password);
        LocalDate dob = dobPicker.getValue();

        if(!Validators.isValidPhoneNumber(phoneNumber)){
            modifyMsgLabel.setText("Invalid phone number format");
            modifyMsgLabel.setTextFill(Color.web("#FF0000"));
        }else{
            if(!toModify.getFirstName().equals(firstName)) {
                toModify.setFirstName(firstName);
                modified = true;
            }
            if(!toModify.getLastName().equals(lastName)){
                toModify.setLastName(lastName);
                modified = true;
            }
            if(hashed != null && !toModify.getPassword().equals(hashed)){
                toModify.setPassword(password);
                modified = true;
            }
            if(!toModify.getPhoneNumber().equals(phoneNumber)){
                toModify.setPhoneNumber(phoneNumber);
                modified = true;
            }
            if(toModify.getDob().compareTo(dob) != 0){
                toModify.setDob(dob);
                modified = true;
            }
        }

        return modified;
    }

    private void enableFields(boolean enable){
        if(enable){
            firstNameTF.setEditable(true);
            lastNameTF.setEditable(true);
            phoneTF.setEditable(true);
            dobPicker.setDisable(false);
            passwordField.setEditable(true);
        }else{
            firstNameTF.setEditable(false);
            lastNameTF.setEditable(false);
            phoneTF.setEditable(false);
            dobPicker.setDisable(true);
            passwordField.setEditable(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toModify = LoadedUser.getInstance().getUser();
        usernameTF.setText(toModify.getEmail());
        firstNameTF.setText(toModify.getFirstName());
        lastNameTF.setText(toModify.getLastName());
        phoneTF.setText(toModify.getPhoneNumber());
        dobPicker.setValue(toModify.getDob());

        saveBtn.setDisable(true);

        usernameTF.setEditable(false);
        firstNameTF.setEditable(false);
        lastNameTF.setEditable(false);
        phoneTF.setEditable(false);
        passwordField.setEditable(false);
        dobPicker.setDisable(true);
        dobPicker.setStyle("-fx-opacity: 1.0");
    }
}