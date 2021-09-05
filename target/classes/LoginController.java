import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private TextField usernameLoginField;
    @FXML private TextField passwordLoginField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;


    @FXML private void toSignup(ActionEvent event) throws IOException{
        AnchorPane registerScreen = null;
        try {
            registerScreen = FXMLLoader.load(getClass().getResource("createUserAccount.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(registerScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signupButton.setOnAction(event -> {
            try {
                toSignup(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}