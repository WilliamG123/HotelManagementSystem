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

public class UserMainMenuController implements Initializable {

    @FXML private Button createResBtn;

    @FXML private Button manageResBtn;

    @FXML private Button profileBtn;

    @FXML private Button logoutButton;

    // launches User Reservation Creation scene
    @FXML private Label welcomeMessage;

    // launches scenes using a set of if statements to determine btn pressed and launches new scene accordingly
    @FXML void launchScene(ActionEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == createResBtn)
                newScene = FXMLLoader.load(getClass().getResource("UserCreate.fxml"));
            else if (event.getSource() == manageResBtn)
                newScene = FXMLLoader.load(getClass().getResource("UserManage.fxml"));
            else if (event.getSource() == logoutButton)
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
            else if (event.getSource() == profileBtn)
                newScene = FXMLLoader.load(getClass().getResource("UserProfile.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // initializes FXML elements
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeMessage.setText("Welcome " + LoadedUser.getInstance().getUser().getFirstName());
    }
}
