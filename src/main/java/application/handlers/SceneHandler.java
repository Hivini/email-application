package application.handlers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className SceneHandler
 * @date Nov/13/2018
 * @comments None
 */
public class SceneHandler {

    public static String fromDetails = "";
    public static String subject = "";
    public static String message = "";
    /**
     * This method changes the scene of the current stage to the passed one
     * @param currentStage The current stage that the application is running
     */
    public static void changeScene(Stage currentStage, Parent resource, String title) {
        currentStage.setScene(new Scene(resource, 1280, 720));
        currentStage.setTitle(title);
    }

    public static void setFromDetails(String fromDetails) {
        SceneHandler.fromDetails = fromDetails;
    }

    public static void setSubject(String subject) {
        SceneHandler.subject = subject;
    }

    public static void setMessage(String message) {
        SceneHandler.message = message;
    }
}
