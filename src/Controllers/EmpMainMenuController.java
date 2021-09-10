import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmpMainMenuController implements Initializable {

    @FXML private Button propertyBtn;

    @FXML private Button reservationBtn;

    @FXML private Button accountBtn;
    @FXML private Button logoutButton;

    @FXML private Label welcomeMessage;


    // launches Account Management scene
    @FXML void launchAccount(ActionEvent event) {
        AnchorPane accountScreen = null;
        try {
            accountScreen = FXMLLoader.load(getClass().getResource("StaffAccounts.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(accountScreen);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // launches Property Management scene
    @FXML void launchProp(ActionEvent event) {
        AnchorPane propertiesScreen = null;
        try {
            propertiesScreen = FXMLLoader.load(getClass().getResource("StaffProperty.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(propertiesScreen);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // launches Reservation Management scene
    @FXML void launchRes(ActionEvent event) {
        AnchorPane reservationScreen = null;
        try {
            reservationScreen = FXMLLoader.load(getClass().getResource("StaffReservation.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(reservationScreen);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

<<<<<<< HEAD
    // initializes FXML elements
=======
    @FXML public void logout(ActionEvent event){
        AnchorPane loginScreen = null;
        try{
            loginScreen = FXMLLoader.load(getClass().getResource("login.fxml"));
        }catch (IOException e){
            e.printStackTrace();
        }
        LoadedUser.getInstance().resetUser();
        Scene scene = new Scene(loginScreen);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

>>>>>>> ace0355ea0527fc7bf3df7e52431d64eb8ce778c
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeMessage.setText("Welcome " + LoadedUser.getInstance().getUser().getUserName());
    }
}
