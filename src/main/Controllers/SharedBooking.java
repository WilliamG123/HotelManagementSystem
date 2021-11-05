import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
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

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
