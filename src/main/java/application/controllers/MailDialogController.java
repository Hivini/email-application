package application.controllers;

import application.dataModels.Mail;
import application.handlers.MailDataHandler;
import application.handlers.UserData;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.time.LocalDateTime;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className MailDialogController
 * @date Nov/13/2018
 * @comments None
 */
public class MailDialogController {

    @FXML
    private JFXTextField toField;
    @FXML
    private JFXTextField subjectField;
    @FXML
    private JFXTextArea messageArea;

    public Mail getNewMail() {
        if (MailDataHandler.validateEmail(toField.getText().trim())) {
            String send_to = toField.getText();
            String send_by = UserData.getInstance().getUser().getEmail();
            String subject = subjectField.getText();
            String message = messageArea.getText();

            //MailDataHandler.getInstance().addMail(mail);
            return new Mail(subject, message, send_by, send_to, LocalDateTime.now());
        }
        return null;
    }
}
