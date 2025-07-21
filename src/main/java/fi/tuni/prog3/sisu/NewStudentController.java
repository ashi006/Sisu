package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asma Jamil
 */
public class NewStudentController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField numberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField startYearField;
    @FXML
    private TextField endYearField;
    @FXML
    private Label nameError;
    @FXML
    private Label numberError;
    @FXML
    private Label emailError; 
    @FXML
    private Label pwdError;
    @FXML
    private Label startError;
    @FXML
    private Label endError;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addTextLimiter(numberField, 11);
        addTextLimiter(startYearField, 4);
        addTextLimiter(endYearField, 4);
        nameField.textProperty().addListener(event -> {
            if(!nameField.getText().matches("[a-zA-z ]+")) {
                nameError.setText("Please enter a valid name");
                nameError.setVisible(true);
            } else
                nameError.setVisible(false);
        });
        numberField.textProperty().addListener(event -> {
            if(!numberField.getText().matches("[0-9]+")) {
                numberError.setText("Please enter a valid student number");
                numberError.setVisible(true);
            } else
                numberError.setVisible(false);
        });
        emailField.textProperty().addListener(event -> {
            if(!emailField.getText().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
                emailError.setText("Please enter a valid email");
                emailError.setVisible(true);
            } else
                emailError.setVisible(false);
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
    
    @FXML
    private void backToLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Sisu.changeScene(stage, "start.fxml");
    }

    @FXML
    private void createNewStudent(ActionEvent event) throws IOException {
        String name = nameField.getText();
        String studentNumber = numberField.getText();
        String email = emailField.getText();
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
        if(studentNumber.isBlank()) {
            numberError.setText("Please enter student number");
            numberError.setVisible(true);
        } else if(studentNumber.length() < 6) {
            numberError.setText("Student number must be atleast 6 digits long");
            numberError.setVisible(true);
        } 
        if(email.isBlank()) {
            emailError.setText("Please enter email");
            emailError.setVisible(true);
        } 
        if(password.isBlank()) {
            pwdError.setText("Please enter password");
            pwdError.setVisible(true);
        } else {
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
        boolean isNameValid  = name.matches("[a-zA-z ]+");
        boolean isNumberValid  = studentNumber.matches("[0-9]+");
        boolean isYearValid  = validateYears(startYear, endYear);
        if(isYearValid && isNameValid && isNumberValid && password.length()>6 && password.length()<20 && hasCapital && hasLower && hasDigit && hasSpecialChar) {
            if(!StudentData.getStudents().isEmpty() && (StudentData.isEmailFound(email) || StudentData.isNumberFound(studentNumber))) {
                Dialog<String> dialog = new Dialog<String>();
                dialog.setTitle("Error");
                ButtonType btn = new ButtonType("Ok", ButtonData.OK_DONE);
                dialog.setContentText("Student with the same student number/email already exists");
                dialog.getDialogPane().getButtonTypes().add(btn);
                dialog.showAndWait();
            } else {
                StudentData.addStudent(name, studentNumber, email, password, startYear, endYear);
                StudentData stdData = new StudentData();
                stdData.writeToFile("students.json");
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Sisu.changeScene(stage, "mainWindow.fxml");
            }
        }
    }

    /**
     * Validates start and completion years.
     * @param startYear The start year to be validated.
     * @param endYear The end year to be validated.
     * @return true if all tests were passed, otherwise return false and sets an error message against the invalid field.
     */
    public boolean validateYears(int startYear, int endYear){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy"); 
        int currentYear = Integer.parseInt(dtf.format(LocalDateTime.now()));
        if(startYear > currentYear) {
            startError.setText("Start year can be the current year at most");
            startError.setVisible(true);
            return false;
        } else if(startYear >= endYear){
            endError.setText("Completion year should be greater than the start year");
            endError.setVisible(true);
            return false;
        } else if(currentYear-5 > startYear) {
            startError.setText("Start year should be atmost 5 years earlier than current year");
            startError.setVisible(true);
            return false;
        } else if(endYear < currentYear){
            endError.setText("Estimated completion year can be the current year at least");
            endError.setVisible(true);
            return false;
        } else {
            return true;
        }
    }
}
