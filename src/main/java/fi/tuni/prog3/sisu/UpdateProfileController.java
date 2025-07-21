package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * A FXML controller class for update student profile screen.
 * @author Asma Jamil
 */
public class UpdateProfileController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField startYearField;
    @FXML
    private TextField endYearField;
    @FXML
    private Label nameError;
    @FXML
    private Label pwdError;
    @FXML
    private Label startError;
    @FXML
    private Label endError;
    @FXML
    private Button exitBtn;
    
    Student student;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        student = StudentData.getCurrent();
        nameField.setText(student.getName());
        startYearField.setText(Integer.toString(student.getStartYear()));
        endYearField.setText(Integer.toString(student.getEndYear()));
        addTextLimiter(startYearField, 4);
        addTextLimiter(endYearField, 4);
        nameField.textProperty().addListener(event -> {
            if(!nameField.getText().matches("[a-zA-z ]+")) {
                nameError.setText("Please enter a valid name");
                nameError.setVisible(true);
            } else
                nameError.setVisible(false);
        });
        passwordField.textProperty().addListener(event -> {
            pwdError.setVisible(false);
        });
        startYearField.textProperty().addListener(event -> {
            if(!startYearField.getText().matches("[0-9]+")) {
                startError.setText("Please enter a valid start year");
                startError.setVisible(true);
            } else
                startError.setVisible(false);
        });
        endYearField.textProperty().addListener(event -> {
            if(!endYearField.getText().matches("[0-9]+")) {
                endError.setText("Please enter a valid completion year");
                endError.setVisible(true);
            } else
                endError.setVisible(false);
        });
    }

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((final ObservableValue<? extends String> ov, final String oldValue, final String newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }
     
    /**
     * Updates the student details.
     * @param event
     */
    public void updateStudent(ActionEvent event) throws IOException {
        String name = nameField.getText();
        String password = passwordField.getText();
        int startYear = 0;
        int endYear = 0;
        boolean hasCapital = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        if(name.isBlank()) {
            nameError.setText("Please enter name");
            nameError.setVisible(true);
        }
        if(!password.isBlank()) {
            char ch;
            for (int i = 0; i < password.length(); i++){
                ch = password.charAt(i);
                if (Character.isLowerCase(ch)){
                   hasLower = true;
                }
                else if(Character.isUpperCase(ch)){
                   hasCapital = true;
                } else if(Character.isDigit(ch)){
                    hasDigit = true;
                } else if(!Character.isDigit(ch) && !Character.isAlphabetic(ch)){
                    hasSpecialChar = true;
                }
            }
            if(password.length() < 6 || password.length() > 20)
                pwdError.setText("Password must be 6 to 20 characters long");
            if(!hasCapital)
                pwdError.setText("Enter atleast 1 upper case letter");
            if(!hasLower)
                pwdError.setText("Enter atleast 1 lower case letter");
            if(!hasDigit)
                pwdError.setText("Enter atleast 1 digit");      
            if(!hasSpecialChar)
                pwdError.setText("Enter atleast 1 special character");      
            if(password.length() < 6 || password.length() > 20 || !hasCapital || !hasLower || !hasDigit || !hasSpecialChar)
                pwdError.setVisible(true);
            else
                pwdError.setVisible(false);
        }
        if(startYearField.getText().isBlank()) {
            startError.setText("Please enter start year");
            startError.setVisible(true);
        } else
            startYear = Integer.parseInt(startYearField.getText());
        if(endYearField.getText().isBlank()) {
            endError.setText("Please enter end year");
            endError.setVisible(true);
        } else
            endYear = Integer.parseInt(endYearField.getText());
        boolean isNameValid = name.matches("[a-zA-z ]+");
        boolean isYearValid = new NewStudentController().validateYears(startYear, endYear);
        if(isYearValid && isNameValid) {
            student.setName(name);
            student.setStartYear(startYear);
            student.setEndYear(endYear);
            if(!password.isBlank() && password.length()>6 && password.length()<20 && hasCapital && hasLower && hasDigit && hasSpecialChar) {
                StudentData.updatePassword(password);
            }
            new StudentData().writeToFile("students.json"); 
            exitBtn.fire();
            Stage stage  = (Stage)((Node)event.getSource()).getScene().getWindow();
            Toast.makeText(stage, "Profile information updated successfully", 2500, 400, 400);
        }
    }
    
    /**
     * Closes the update profile stage.
     * @param event
     */
    public void exitStage(ActionEvent event) {
        Node source = (Node) event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
