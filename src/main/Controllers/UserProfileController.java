import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
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

    @FXML private DatePicker dobPicker;

    @FXML private TextField firstNameTF;

    @FXML private TextField lastNameTF;

    @FXML private TextField phoneTF;

    @FXML private Text mainmenuTV;

    @FXML private Text logoutTV;

    @FXML private ImageView pfpIV;

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == mainmenuTV) {
                if (LoadedUser.getInstance().getUser().getType().equals("CUST"))
                    newScene = FXMLLoader.load(getClass().getResource("UserMainMenu.fxml"));
                else if (LoadedUser.getInstance().getUser().getType().equals("EMP"))
                    newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            }else if(event.getSource() == logoutTV){
                newScene = FXMLLoader.load(getClass().getResource("login.fxml"));
            }
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
        usernameTF.setText(LoadedUser.getInstance().getUser().getEmail());
        firstNameTF.setText(LoadedUser.getInstance().getUser().getFirstName());
        lastNameTF.setText(LoadedUser.getInstance().getUser().getLastName());
        phoneTF.setText(LoadedUser.getInstance().getUser().getPhoneNumber());
        dobPicker.setValue(LoadedUser.getInstance().getUser().getDob());

        usernameTF.setEditable(false);
        firstNameTF.setEditable(false);
        lastNameTF.setEditable(false);
        phoneTF.setEditable(false);
        dobPicker.setDisable(true);
        dobPicker.setStyle("-fx-opacity: 1.0");
    }
}