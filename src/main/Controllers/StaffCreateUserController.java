import javafx.fxml.FXML;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
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
    @FXML private Text mainmenuTV;
    @FXML private Text logoutTV;
    @FXML private Label errorLabel;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confPasswordField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> typePicker;
    private StringBuffer errorMsg;

    @FXML void sceneChange(MouseEvent event) {
        AnchorPane newScene = null;

        try {
            if (event.getSource() == mainmenuTV) {
                newScene = FXMLLoader.load(getClass().getResource("StaffMainMenu.fxml"));
            } else if(event.getSource() == logoutTV) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                LoginController controller = new LoginController();
                loader.setController(controller);
                newScene = loader.load();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(newScene);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

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
        if(fieldsAreValid()){
            errorLabel.setText("");
            try {
                String fname = firstNameField.getText();
                String lname = lastNameField.getText();
                String email = emailField.getText();
                String pnum = phoneNumberField.getText();
                String password = Hasher.getInstance("SHA-256").hash(passwordField.getText());
                LocalDate dob = dobPicker.getValue();
                String type = typePicker.getValue();
                User user = new User(fname, lname, email, pnum, dob, password, type);
                User.Customer("add", user);
                System.out.println(user);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }else{
            errorLabel.setText(errorMsg.toString());
            errorLabel.setTextFill(Color.web("#FF0000"));
        }
    }

    private boolean fieldsAreValid(){
        boolean returnval = true;
        errorMsg = new StringBuffer("Error:\n");
        String fname = firstNameField.getText();
        String lname = lastNameField.getText();
        String password = passwordField.getText();
        String confPassword = confPasswordField.getText();
        String pnumber = phoneNumberField.getText();
        String email = emailField.getText();
        String type = typePicker.getValue();
        LocalDate dob = dobPicker.getValue();
        if(fname.equals("")){
            errorMsg.append("--> Invalid First Name value\n");
            returnval = false;
        }
        if(lname.equals("")){
            errorMsg.append("--> Invalid Last Name value\n");
            returnval = false;
        }
        if(email.equals("") || !Validators.isValidEmailAddress(email)){
            errorMsg.append("--> Invalid Email value\n");
            returnval = false;
        }
        if(pnumber.equals("") || !Validators.isValidPhoneNumber(pnumber)){
            errorMsg.append("--> Invalid Phone Number value\n");
            returnval = false;
        }
        if(password.equals("") || confPassword.equals("")){
            errorMsg.append("--> Invalid Password value\n");
        }
        else if(!password.equals(confPassword)){
            errorMsg.append("--> Passwords do not match\n");
            returnval = false;
        }
        if(dob == null){
            errorMsg.append("--> Invalid Date of Birth value\n");
            returnval = false;
        }
        if(type == null || type.equals("")){
            errorMsg.append("--> Invalid Type value\n");
            returnval = false;
        }
        return returnval;
    }

    @FXML
    void initialize(){
        typePicker.getItems().add("CUST");
        typePicker.getItems().add("EMP");
        phoneNumberField.setPromptText("Ex: 999-999-9999");
    }
}
