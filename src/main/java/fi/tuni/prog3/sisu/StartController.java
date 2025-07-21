package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asma Jamil
 */
public class StartController implements Initializable {
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label emailError;
    @FXML
    private Label pwdError;
    @FXML
    private Label loginError;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emailTextField.textProperty().addListener(event -> {
            emailError.setVisible(false);
            loginError.setVisible(false);
        });
        passwordTextField.textProperty().addListener(event -> {
            pwdError.setVisible(false);
            loginError.setVisible(false);
        });
    }
    
    @FXML
    private void signIn(ActionEvent event) throws IOException {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        if(email.isBlank()) {
            emailError.setText("Please enter email");
            emailError.setVisible(true);
        } else if(!email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
             emailError.setText("Invalid email");
             emailError.setVisible(true);
        } else if(password.isBlank()) {
            pwdError.setText("Please enter password");
            pwdError.setVisible(true);
        } else {
            if (StudentData.validatePassword(email, password)) {
                APIData api = new APIData();
                DegreeProgramme degProg = StudentData.getCurrent().getDegree();
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Sisu.changeScene(stage, "mainWindow.fxml");
            } else {
                loginError.setVisible(true);
            }
        }
    }
    
    @FXML
    private void createNewStudent(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Sisu.changeScene(stage, "newStudent.fxml");
    }    
}
