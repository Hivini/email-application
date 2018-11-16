package application.contentLoaders;

import application.controllers.EmailController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className EmailContent
 * @date Nov/15/2018
 * @comments None
 */
public class EmailContent extends BorderPane {

    public EmailContent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/mailListContent.fxml"));
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("The file couldn't be loaded");
            e.printStackTrace();
        }
    }
}
