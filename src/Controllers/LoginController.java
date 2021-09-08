import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController extends User implements Initializable {
    @FXML private TextField usernameLoginField;
    @FXML private PasswordField passwordLoginField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Label loginMessagePrompt;
    @FXML public Button exitButton;
    private double xOffset =0; // <-- to move app window freely
    private double yOffset = 0;

    @FXML private void exit(){
        Platform.exit();
    }

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

    public void login(ActionEvent event) throws IOException, SQLException, NoSuchAlgorithmException {
        String username = usernameLoginField.getText();
        String password = Hasher.getInstance("SHA-256").hash(passwordLoginField.getText());
        System.out.println("password entered:"+password);
        switch (validate(username, password)) {
            case 0:
                loginMessagePrompt.setText("Sorry No Record Exist");
                break;
            case -1:
               loginMessagePrompt.setText("Incorrect password");
                break;
            case 1:
                loginMessagePrompt.setText("Great choice!");
                System.out.println(getType());
                if (getType().equals("EMP")) {
                    Parent root = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
                    Scene MainMenuScene = new Scene(root);//Creating a Scene object and passing in the Parent we just made
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    root.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) { xOffset = event.getSceneX();yOffset = event.getSceneY(); }});
                    root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) { window.setX(event.getScreenX() - xOffset);window.setY(event.getScreenY() - yOffset); }});
                    window.setScene(MainMenuScene);
                    window.show();
                }else {
                    Parent root = FXMLLoader.load(getClass().getResource("CustMainMenu.fxml"));
                    Scene MainMenuScene = new Scene(root);//Creating a Scene object and passing in the Parent we just made
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
                    window.setScene(MainMenuScene);
                    window.show();
                }
                break;
        }
        /**


         **/

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginButton.setOnAction(event -> {
            try {
                login(event);
            } catch (IOException | NoSuchAlgorithmException e) {
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