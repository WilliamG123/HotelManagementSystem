import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustMainMenuController implements Initializable {
    private double xOffset =0;
    private double yOffset = 0;

    @FXML private Button createResBtn;

    @FXML private Button manageResBtn;

    @FXML private Button profileBtn;

    @FXML private Button logoutButton;

    // launches User Reservation Creation sceneS
    @FXML private Label welcomeMessage;

    // launches scenes using a set of if statements to determine btn pressed and launches new scene accordingly
    @FXML void launchScene(ActionEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == this.createResBtn)
                newScene = FXMLLoader.load(getClass().getResource("UserCreate.fxml"));
            else if (event.getSource() == this.manageResBtn)
                newScene = FXMLLoader.load(getClass().getResource("UserManage.fxml"));
            else if (event.getSource() == this.logoutButton)
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
            else if (event.getSource() == this.profileBtn)
                newScene = FXMLLoader.load(getClass().getResource("UserProfile.fxml"));
        } catch (IOException e) {
            System.out.println("newScene failed");
            e.printStackTrace();
        }
        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        newScene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        newScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                window.setX(event.getScreenX() - xOffset);
                window.setY(event.getScreenY() - yOffset);
            }
        });


        window.show();
    }

    // initializes FXML elements
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeMessage.setText("Welcome " + LoadedUser.getInstance().getUser().getFirstName());
    }
}
