import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController extends User implements Initializable {
    @FXML public Button exitButton;
    @FXML private TextField usernameLoginField;
    @FXML private PasswordField passwordLoginField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Label loginMessagePrompt;
    private double xOffset =0; // <-- to move app window freely
    private double yOffset = 0;

    @FXML private void toSignup(ActionEvent event) throws IOException {
        AnchorPane registerScreen = null;
        try {
            registerScreen = FXMLLoader.load(getClass().getResource("createUserAccount.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(registerScreen);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML private void exit(){
        Platform.exit();
    }

    public void login(ActionEvent event) throws IOException, SQLException {

        //UserInfo ui = new UserInfo(userName.getText(), passWord.getText());
        if (!validate(usernameLoginField.getText(), passwordLoginField.getText())) {
            loginMessagePrompt.setText("Please Register no Record found!");

        } else {
            setUserName(getUserName());
        }

        /*
         //adding everything in FXML into this Parent object and calling it MainMenuParent
         // Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml")); <---------------------Still need to make this scene

         Scene MainMenuScene = new Scene(root);//Creating a Scene object and passing in the Parent we just made

         //This line gets the Stage information
         //action event when you say get Source doesnt know what is returned, so we make it a node type object
         // then because its a node we can get the scene and get the window then cast it as Stage
         Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

         //code below for GUI movability
         root.setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
        }
        });
         root.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
        window.setX(event.getScreenX() - xOffset);
        window.setY(event.getScreenY() - yOffset);

        }
        });
         */

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginButton.setOnAction(event -> {
            try {
                login(event);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        signupButton.setOnAction(event -> {
            try {
                toSignup(event);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        exitButton.setOnAction(event -> {
            exit();
        });
    }
}