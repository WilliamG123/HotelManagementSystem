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

public class CustMainMenuController implements Initializable {

    @FXML private Button createResBtn;

    @FXML private Button manageResBtn;

    @FXML private Button profileBtn;


    // launches User Reservation Creation scene

    @FXML private Label welcomeMessage;


    @FXML void launchCreateRes(ActionEvent event) {
        AnchorPane createRes = null;
        try {
            createRes = FXMLLoader.load(getClass().getResource("UserCreate.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(createRes);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // launches User Reservation Management scene
    @FXML void launchManageRes(ActionEvent event) {
        AnchorPane resManage = null;
        try {
            resManage = FXMLLoader.load(getClass().getResource("UserManage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(resManage);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // launches User Profile scene
    @FXML void launchProfile(ActionEvent event) {
        AnchorPane profile = null;
        try {
            profile = FXMLLoader.load(getClass().getResource("UserProfile.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(profile);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // initializes FXML elements
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeMessage.setText("Welcome " + LoadedUser.getInstance().getUser().getUserName());
    }
}
