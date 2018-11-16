package application.controllers;

import application.handlers.SceneHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className EmailContentController
 * @date Nov/15/2018
 * @comments None
 */
public class EmailContentController {

    @FXML
    private Label fromDetails;
    @FXML
    private Label subjectDetails;
    @FXML
    private TextArea messageDetails;

    @FXML
    public void initialize() {
        fromDetails.setText(SceneHandler.fromDetails);
        subjectDetails.setText(SceneHandler.subject);
        messageDetails.setText(SceneHandler.message);

    }
}
