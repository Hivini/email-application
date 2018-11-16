package application.contentLoaders;

import application.controllers.EmailController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className MailList
 * @date Nov/15/2018
 * @comments None
 */
public class MailList extends BorderPane {

    public MailList() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/mailList.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("The file couldn't be loaded");
            e.printStackTrace();
        }
    }
}
