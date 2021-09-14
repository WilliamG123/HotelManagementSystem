import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == mainmenuTV)
                newScene = FXMLLoader.load(getClass().getResource("UserMainMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}