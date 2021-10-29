import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import org.controlsfx.control.textfield.TextFields;


import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController extends User implements Initializable {
    public AnchorPane prompt;

    @FXML private TextField emailLoginField;
    @FXML private PasswordField passwordLoginField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Label loginMessagePrompt;
    @FXML public Button exitButton;
    private double xOffset =0; // <-- to move app window freely
    private double yOffset = 0;

    @FXML private void exit(){
        Platform.exit();
        System.out.println("EXIT");
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

    public void login(ActionEvent event) throws IOException, SQLException, NoSuchAlgorithmException, ClassNotFoundException {


        String email = emailLoginField.getText();
        String password = Hasher.getInstance("SHA-256").hash(passwordLoginField.getText());
        System.out.println("password entered:"+password);
        switch (validate(email, password)) {
            case 0:
                loginMessagePrompt.setText("Sorry No Record Exist");
                break;
            case -1:
               loginMessagePrompt.setText("Incorrect password");
                break;
            case 1:
                loginMessagePrompt.setText("Great choice!");
                System.out.println(getType());
                Parent root;
                if (LoadedUser.getInstance().getUser().getType().equals("EMP")) {
                    root = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
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
                    root = FXMLLoader.load(getClass().getResource("UserMainMenu.fxml"));
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
                new animatefx.animation.RollIn(root).play();
                break;
        }
        /**


         **/

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       // passwordLoginField = TextFields.createClearablePasswordField();
        LoadedUser.getInstance().resetUser();
        String[] possibleWords ={"EMP", "CUST"};
        TextFields.bindAutoCompletion(emailLoginField, possibleWords);

        loginButton.setOnAction(event -> {
            try {
                login(event);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

        loginButton.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                loginButton.fire();
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