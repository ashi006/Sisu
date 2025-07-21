package fi.tuni.prog3.sisu;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * A controller class to start the application.
 * @author Asma Jamil
 */
public class Sisu extends Application {

    /**
     * Sets the first screen of the application.
     * @param stage
     * @throws IOException 
     */
    @Override
    public void start(Stage stage) throws IOException {
        StudentData stdData = new StudentData();
        stdData.readFromFile("students.json");
        FXMLLoader fxmlLoader;
        fxmlLoader = new FXMLLoader(Sisu.class.getResource("start.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Sisu");
        stage.show();
    }

    /**
     * Changes the scene with the given FXML file on the given stage.
     * @param stage
     * @param fxmlFile
     * @throws IOException 
     */
    public static void changeScene(Stage stage, String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Sisu.class.getResource(fxmlFile));
        Parent newWindowParent = fxmlLoader.load();
        Scene newWindowScene = new Scene(newWindowParent);
        stage.setScene(newWindowScene);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    
    /**
     * Staring point of the application.
     * @param args 
     */
    public static void main(String[] args) {
        launch();
    }
}