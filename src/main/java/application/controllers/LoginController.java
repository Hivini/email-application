package application.controllers;

import application.handlers.MailDataHandler;
import application.handlers.UserData;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void initialize() {

        emailField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            // When lost focus
            if (!newValue) {
                // Pattern to validate email
                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

                String email = "test@test.com";
                Matcher matcher = pattern.matcher(email);

                if (!matcher.matches()) {
                    emailField.setText("");
                }
            }
        });
    }

    @FXML
    public void handleButtonPressed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            // TODO: 11/12/18 Hardcoded this
            UserData.getInstance().setUser("Test", "Test", "hahaha@gmail.com");

            File file = new File(UserData.getInstance().getUserDirectory());
            if (file.isDirectory()) {
                System.out.println(file.getPath());
            } else {
                file.mkdirs();
                try {
                    MailDataHandler mailDataHandler = new MailDataHandler(UserData.getInstance().getMailFilePath());
                    mailDataHandler.saveMails();
                    PrintWriter userDataWriter = new PrintWriter(UserData.getInstance().getUserDataPath(), "UTF-8");

                    userDataWriter.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
            // Get the current stage where the button is
            Stage currentStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            try {
                this.changeToLoginScreen(currentStage);
            } catch (IOException e1) {
                e1.printStackTrace();
                System.out.println("The login file was not found");
            }
        }
    }

    /**
     * This method changes the scene of the current stage to the login page
     * @param currentStage The current stage that the application is running
     * @throws IOException In case there's a problem in the files
     */
    private void changeToLoginScreen(Stage currentStage) throws IOException {
        System.out.println(getClass().getResource("/view/emailLayout.fxml").getPath());
        Parent emailResource =  FXMLLoader.load(getClass().getResource("/view/emailLayout.fxml"));
        currentStage.setScene(new Scene(emailResource, 1280, 720));
        currentStage.setTitle("Email Layout");
    }
}
