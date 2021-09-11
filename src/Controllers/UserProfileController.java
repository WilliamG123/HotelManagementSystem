import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;


/*
    scene controls the user profile scene
    TF = textfield
    TV = textview
 */

public class UserProfileController implements Initializable {

    @FXML private TextField usernameTF;

    @FXML private TextField passwordTF;

    @FXML private TextField dobTF;

    @FXML private TextField firstNameTF;

    @FXML private TextField lastNameTF;

    @FXML private TextField emailTF;

    @FXML private TextField phoneTF;

    @FXML private Text mainmenuTV;

    @FXML private Text logoutTV;

    @FXML private ImageView pfpIV;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}