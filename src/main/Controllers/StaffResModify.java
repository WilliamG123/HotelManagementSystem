import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/*****************************************************************
 *                     StaffResModify Class
 * - displays a detailed view of a reservation
 * - allows employee to modify the dates or delete reservation
 *****************************************************************/

public class StaffResModify implements Initializable {

    @FXML private Text hNameTF;

    @FXML private Text hAddressTF;

    @FXML private Text hRatingTF;

    @FXML private TextArea descriptionTA;

    @FXML private Text resIdTF;

    @FXML private Text checkInTF;

    @FXML private Text checkOutTF;

    @FXML private Text daysTF;

    @FXML private Text priceTF;

    @FXML private Text adultsTF;

    @FXML private Text childrenTF;

    @FXML private Text returnTF;

    @FXML private Text modDatesTF;

    @FXML private Text deleteTF;

    @FXML private TextArea roomsTA;
    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        // try block attempts to load a new scene
        try {
            if (event.getSource() == returnTF)
                newScene = FXMLLoader.load(getClass().getResource("StaffReservation.fxml"));
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