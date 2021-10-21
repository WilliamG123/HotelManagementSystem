import javafx.fxml.FXML;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Label;

public class StaffCreateUserController {
    @FXML private Text createTV;
    @FXML private Text backTV;
    @FXML private Label errorLabel;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confPasswordField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> typePicker;

    @FXML private void handleTV(MouseEvent event){
        AnchorPane newScene = null;
        try{
            if(event.getSource() == createTV){
                handleCreate();
                return;
            }else if(event.getSource() == backTV){
                newScene = FXMLLoader.load(getClass().getResource("StaffAccounts.fxml"));
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    private void handleCreate(){

    }
}
