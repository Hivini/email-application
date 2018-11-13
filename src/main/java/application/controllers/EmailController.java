package application.controllers;

import application.dataModels.Mail;
import application.handlers.MailDataHandler;
import application.handlers.UserData;
import javafx.fxml.FXML;

public class EmailController {

    private MailDataHandler mailDataHandler;

    @FXML
    public void initialize() {

        mailDataHandler = new MailDataHandler(UserData.getInstance().getMailFilePath());
        mailDataHandler.loadMails();
        mailDataHandler.addMail(new Mail("asdf", "adsfasdfasdf","adsfadsfasdf", "afrgerg"));
        mailDataHandler.saveMails();

        for (Mail mail : mailDataHandler.getMails()) {
            System.out.println(mail.getBody());
        }
    }
}
