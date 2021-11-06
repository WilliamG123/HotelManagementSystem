import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SharedBooking implements Initializable {

    @FXML private Text hNameTF;
    @FXML private Text hAddressTF;
    @FXML private Text hRatingTF;
    @FXML private TextArea descriptionTF;
    @FXML private Text checkInTF;
    @FXML private DatePicker checkInDP;
    @FXML private Text checkOutTF;
    @FXML private DatePicker checkOutDP;
    @FXML private TextField adultsTF;
    @FXML private TextField childrenTF;
    @FXML private Text returnTF;
    @FXML private Text bookTF;
    @FXML private TableView<?> roomTB;
    @FXML private ListView<?> amenitiesLV;

    @FXML void book(MouseEvent event) {

    }

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;
        try{
            if(event.getSource() == returnTF){
                newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            }
        }catch(IOException e){
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
