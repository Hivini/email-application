package application.controllers;

import application.handlers.MailDataHandler;
import application.handlers.SceneHandler;
import application.handlers.UserData;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className RegisterController
 * @date Nov/13/2018
 * @comments None
 */
public class RegisterController {

    @FXML
    private Button backButton;
    @FXML
    private Button registerButton;
    @FXML
    private JFXTextField firstNameField;
    @FXML
    private JFXTextField lastNameField;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    public void handleButtonPressed(ActionEvent e) {
        // Get the current stage where the button is
        Stage currentStage = (Stage) ((Node)e.getSource()).getScene().getWindow();

        if (e.getSource() == registerButton &&
                !firstNameField.getText().trim().equals("") &&
                !lastNameField.getText().trim().equals("") &&
                !emailField.getText().trim().equals("") &&
                !passwordField.getText().trim().equals("")) {

            if (MailDataHandler.validateEmail(emailField.getText())) {
                UserData.getInstance().setUserEmail(emailField.getText());

                File file = new File(UserData.getInstance().getUserDirectory());
                if (file.isDirectory()) {
                    errorLabel.setText("User already registered");
                } else {
                    // Make the directories for the files
                    if (file.mkdirs()) {
                        try {
                            // Set the pending mails file
                            MailDataHandler.getInstance().setEmailData(UserData.getInstance().getUserPendingPath());
                            MailDataHandler.getInstance().saveMails();
                            // Set the mails file
                            MailDataHandler.getInstance().setEmailData(UserData.getInstance().getMailFilePath());
                            MailDataHandler.getInstance().saveMails();

                            PrintWriter userDataWriter = new PrintWriter(UserData.getInstance().
                                    getUserDataPath(), "UTF-8");
                            // Making the string that would be written as data
                            String sb = firstNameField.getText() +
                                    "," +
                                    lastNameField.getText() +
                                    "," +
                                    emailField.getText() +
                                    "," +
                                    MailDataHandler.getSHA(passwordField.getText());
                            userDataWriter.write(sb);
                            userDataWriter.close();

                            changeToLoginScene(currentStage);
                        } catch (IOException e1) {
                            errorLabel.setText("Error getting the file");
                            e1.printStackTrace();
                        }
                    }

                }
            } else {
                // Valid formatted email
                errorLabel.setText("The email has not a valid format");
            }

        } else if (e.getSource() == backButton) {
            try {
                changeToLoginScene(currentStage);
            } catch (IOException e1) {
                errorLabel.setText("Error getting the login file");
                e1.printStackTrace();
            }
        } else {
            errorLabel.setText("Complete the form please");
        }
    }

    private void changeToLoginScene(Stage currentStage) throws IOException {
        Parent emailResource =  FXMLLoader.load(getClass().getResource("/view/loginLayout.fxml"));
        SceneHandler.changeScene(currentStage, emailResource, "Login");
    }
}
