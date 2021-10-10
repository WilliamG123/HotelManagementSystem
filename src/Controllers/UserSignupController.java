import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSignupController extends User implements Initializable {
    //MACROS
    boolean errors = false;
    private static final int INCORRECT_PASSWORD = -1;
    @FXML private Button backToLoginButton;
    @FXML private TextField signupUsernameField;
    @FXML private PasswordField signupPasswordField;
    @FXML private PasswordField signupConfirmPasswordField;
    @FXML private TextField signupFirstNameField;
    @FXML private TextField signupLastNameField;
    @FXML private TextField signupEmailField;
    @FXML private TextField signupPhoneField;
    @FXML private DatePicker signupDOBField;
    @FXML private Button signupButton;
    @FXML private Label passwordErrorLabel;
    @FXML private Label signupEmailCheck;
    @FXML private Label signupResultLabel, emailErrorLabel, pNumErrorLabel;


    @FXML private void toLogin(ActionEvent event) throws IOException {
        AnchorPane loginScreen = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loginScreen);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        if (scene.focusOwnerProperty().get() instanceof TextArea) {
            TextArea focusedTextArea = (TextArea) scene.focusOwnerProperty().get();
        }
    }

    public void validateEmail() throws SQLException, ClassNotFoundException {

        if(!isValidEmail(signupEmailField.getText()))
        {
            signupEmailCheck.setTextFill(Color.RED);
            signupEmailCheck.setText("Username already exist");
            errors= true;
        }
        else{
            signupEmailCheck.setTextFill(Color.GREEN);
            signupEmailCheck.setText("Great choice");
            errors = false;
        }

    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isValidPhoneNumber(String pNum){
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

        Pattern p = Pattern.compile(patterns);
        Matcher m = p.matcher(pNum);
        return m.matches();
    }
    private void resetErrorLabels(){
        pNumErrorLabel.setText("");
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

    private boolean fieldsAreValid(String username, String password, String confPassword, String email, String pNum) throws SQLException, ClassNotFoundException {
        boolean returnval = true;

        if(!isValidEmail(email)){
            signupEmailCheck.setText("Username is not available!");
            signupEmailCheck.setTextFill(Color.web("#FF0000"));
            returnval = false;
        }
        if(password.equals("") || confPassword.equals("")){
            passwordErrorLabel.setText("Password fields cannot be blank");
            passwordErrorLabel.setTextFill(Color.web("#FF0000"));
            returnval = false;
        }else if(!password.equals(confPassword)){
            passwordErrorLabel.setText("Passwords do not match");
            passwordErrorLabel.setTextFill(Color.web("#FF0000"));
            returnval = false;
        }
        if(!isValidEmailAddress(email)){
            emailErrorLabel.setText("Invalid email format!");
            emailErrorLabel.setTextFill(Color.web("#FF0000"));
            returnval = false;
        }
        if(!isValidPhoneNumber(pNum)){
            pNumErrorLabel.setText("Invalid phone number format!");
            pNumErrorLabel.setTextFill(Color.web("#FF0000"));
            returnval = false;
        }

        return returnval;
    }
    private String getHash(String toHash, String algo) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algo);
        digest.update(toHash.getBytes(StandardCharsets.UTF_8));
        return new String(digest.digest());
    }


    public void validateConfirmPassword(){
      if(signupPasswordField.getText().equals(signupConfirmPasswordField.getText()))
      {
          passwordErrorLabel.setText("Match");
          passwordErrorLabel.setTextFill(Color.GREEN);
          errors= false;
      }
      else if (!signupPasswordField.getText().equals(signupConfirmPasswordField.getText())){
          passwordErrorLabel.setTextFill(Color.RED);
          passwordErrorLabel.setText("does not Match");
          errors = true;
      }

    }

   public boolean userSignUp() throws SQLException, NoSuchAlgorithmException, ClassNotFoundException {
       String username = signupEmailField.getText();
       String password = signupPasswordField.getText();
       String confPassword = signupConfirmPasswordField.getText();
       String email = signupEmailField.getText();
       String phoneNumber = signupPhoneField.getText();
       if(fieldsAreValid(username, password, confPassword, email, phoneNumber)) {
           resetErrorLabels();
           String passwordHash = Hasher.getInstance("SHA-256").hash(password); // this should be different
           String fName = signupFirstNameField.getText();
           String lName = signupLastNameField.getText();
           LocalDate dob = signupDOBField.getValue();
           User cust = new User(fName, lName, email, phoneNumber, dob, passwordHash, "CUST");
           Customer("add", cust);
            return true;
       }
       else {
           System.out.println("NO USER ADDED");
        return false;
       }

   }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetErrorLabels();



        signupConfirmPasswordField.setOnKeyReleased(event -> {
            validateConfirmPassword();
        });



        signupButton.setOnAction(event -> {


            try {
                if(userSignUp()) {
                    toLogin(event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        });


        signupEmailField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                   signupEmailField.getText();
                }
                else
                {
                    try {
                        validateEmail();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });






















    }
}