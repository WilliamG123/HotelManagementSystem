import com.mysql.jdbc.log.Log;
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

public class StaffMainMenuController implements Initializable {

    @FXML private Button propertyBtn;

    @FXML private Button reservationBtn;

    @FXML private Button accountBtn;

    @FXML private Button logoutButton;

    @FXML private Label welcomeMessage;


    // launches scenes using a set of if statements to determine btn pressed and launches new scene accordingly
    @FXML void launchScene(ActionEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == propertyBtn){
                newScene = FXMLLoader.load(getClass().getResource("StaffProperty.fxml"));
                System.out.println("Property Management Button Pressed!");
            }
            else if (event.getSource() == reservationBtn) {
                newScene = FXMLLoader.load(getClass().getResource("StaffReservation.fxml"));
                System.out.println("Staff Reservation Button Pressed!");
            }
            else if (event.getSource() == logoutButton) {
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
                System.out.println("Login Button Pressed!");
            }
            else if (event.getSource() == accountBtn) {
                newScene = FXMLLoader.load(getClass().getResource("StaffAccounts.fxml"));
                System.out.println("Staff Accounts Button Pressed!");
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
        welcomeMessage.setText("Welcome " + LoadedUser.getInstance().getUser().getFirstName());
    }
}
