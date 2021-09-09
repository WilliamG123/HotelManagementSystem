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

    @FXML private Label welcomeMessage;

    @FXML void launchCreateRes(ActionEvent event) {

    }

    @FXML void launchManageRes(ActionEvent event) {

    }

    @FXML void launchProfile(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeMessage.setText("Welcome " + LoadedUser.getInstance().getUser().getUserName());
    }
}
