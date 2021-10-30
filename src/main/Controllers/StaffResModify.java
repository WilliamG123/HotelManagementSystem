import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}