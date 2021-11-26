import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class PropertyModifyController {
    @FXML private ImageView hotelImage;
    @FXML private TextField hotelName;
    @FXML private TextField hotelAddress;
    @FXML private TextField hotelNumRooms;
    @FXML private TextField hotelNumAmenities;
    @FXML private TextArea hotelDesc;
    @FXML private ListView<String> roomTypesLV;
    @FXML private ListView<String> amenitiesLV;

    private final Property property;

    public PropertyModifyController(Property property){
        this.property = property;
    }

    public void initalize(){
        File file = new File("Res/images/hotels/" + property.getPropertyName());
        Image image = new Image(file.toURI().toString());
        ImageView im = new ImageView();
        im.setImage(image);
        im.setPreserveRatio(true);
        im.setFitHeight(280);
        im.setFitWidth(280);
        hotelImage.setImage(im.getImage());
//        hotelImage = im;
        hotelName.setText(property.getPropertyName());
        hotelAddress.setText(property.getAddress());
        hotelDesc.setText(property.getDesc());
        hotelNumRooms.setText(Integer.toString(property.getNumberRooms()));
        hotelNumAmenities.setText(Integer.toString(property.getNumberAmenities()));

    }
}
