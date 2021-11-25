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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import org.controlsfx.control.textfield.TextFields;


import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;

/*
FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                LoginController controller = new LoginController();
                loader.setController(controller);
                newScene = loader.load();
 */

public class LoginController extends User implements Initializable {

    public static final int SHARED_BOOKING_KEY = 100;

    @FXML private TextField emailLoginField;
    @FXML private PasswordField passwordLoginField;
    @FXML private Button loginButton;
    @FXML private Button signUpButton;
    @FXML private Label loginMessagePrompt;
    @FXML public Button exitButton;
    public AnchorPane prompt;
    private Reservation reservation;
    private Hotels hotel;
    private int fromSharedBooking; // check to see if entering scene was SharedBooking

    public LoginController() {
        this.fromSharedBooking = -1;
    }

    public LoginController(Hotels hotel, Reservation reservation, int fromSharedBooking) {
        this.hotel = hotel;
        this.reservation = reservation;
        this.fromSharedBooking = fromSharedBooking;
        reservation.printKeys();
    }

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
        AnchorPane newScene = null;

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
                FXMLLoader loader;
                loginMessagePrompt.setText("Great choice!");
                System.out.println(getType());
                if(this.fromSharedBooking == SHARED_BOOKING_KEY) {
                    loader = new FXMLLoader(getClass().getResource("SharedBooking.fxml"));
                    SharedBooking controller = new SharedBooking(hotel, reservation);
                    loader.setController(controller);
                } else
                    loader = new FXMLLoader(getClass().getResource("UserCreate.fxml"));
                newScene = loader.load();
                Scene scene = new Scene(newScene);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
                break;
        }
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

        signUpButton.setOnAction(event -> {
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