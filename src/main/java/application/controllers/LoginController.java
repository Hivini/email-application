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
import java.util.Objects;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className LoginController
 * @date Nov/12/2018
 * @comments None
 */
public class LoginController {

    @FXML
    private Button loginButton;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private Label errorLabel;


    @FXML
    public void initialize() {
    }

    @FXML
    public void handleButtonPressed(ActionEvent e) {
        // Get the current stage where the button is
        Stage currentStage = (Stage) ((Node)e.getSource()).getScene().getWindow();

        if (e.getSource() == loginButton) {

            // If is an email proceed with the operation
            if (MailDataHandler.validateEmail(emailField.getText())) {
                UserData.getInstance().setUserEmail(emailField.getText());

                File file = new File(UserData.getInstance().getUserDirectory());
                // If the directory already exists
                if (file.isDirectory()) {
                    try {
                        if (validatePassword(passwordField.getText())) {
                            this.changeToEmailScene(currentStage);
                        } else {
                            errorLabel.setText("Email or password is wrong");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        System.out.println("The login file was not found");
                    }

                } else {
                    errorLabel.setText("The user doesn't exists");
                }
            } else {
                errorLabel.setText("Invalid email");
            }



        } else if (e.getSource() == registerButton) {
            try {
                this.changeToRegisterScene(currentStage);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * This method changes the scene of the current stage to the email page
     * @param currentStage The current stage that the application is running
     * @throws IOException In case there's a problem in the files
     */
    private void changeToEmailScene(Stage currentStage) throws IOException {
        //System.out.println(getClass().getResource("/view/emailLayout.fxml").getPath());
        Parent emailResource =  FXMLLoader.load(getClass().getResource("/view/emailLayout.fxml"));
        SceneHandler.changeScene(currentStage, emailResource, "Email Layout");
    }

    /**
     * This method changes the scene of the current stage to the register page
     * @param currentStage The current stage that the application is running
     * @throws IOException In case there's a problem in the files
     */
    private void changeToRegisterScene(Stage currentStage) throws IOException {
        Parent registerResource =  FXMLLoader.load(getClass().getResource("/view/registerLayout.fxml"));
        SceneHandler.changeScene(currentStage, registerResource, "Register");
    }

    private boolean validatePassword(String password) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(UserData.getInstance().getUserDataPath()));
        String line = br.readLine();

        // Use comma as separator
        String[] data = line.split(",");

        if (!Objects.requireNonNull(MailDataHandler.getSHA(password)).equals(data[3]))
            return false;

        UserData.getInstance().setUserName(data[0], data[1]);

        return true;
    }
}
