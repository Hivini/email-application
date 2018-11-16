package application.controllers;

import application.classifier.SpamClassifier;
import application.contentLoaders.EmailContent;
import application.dataModels.Mail;
import application.handlers.MailDataHandler;
import application.handlers.SceneHandler;
import application.handlers.UserData;
import com.jfoenix.controls.JFXListView;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.util.Optional;

public class EmailController {

    @FXML
    private Button inboxButton;
    @FXML
    private Button spamButton;
    @FXML
    private JFXListView<Mail> emailList;
    @FXML
    private BorderPane emailPanel;
    @FXML
    private ContextMenu listContextMenu;

    private FilteredList<Mail> mails;
    private Node tempCenter;

    @FXML
    public void initialize() {

        tempCenter = null;

        // Mini menu for the items on the view list
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(event -> {
            Mail mail = emailList.getSelectionModel().getSelectedItem();
            deleteMail(mail);
        });
        listContextMenu.getItems().setAll(deleteMenuItem);

        MailDataHandler.getInstance().setEmailData(UserData.getInstance().getMailFilePath());
        //MailDataHandler.getInstance().flushEmails();
        MailDataHandler.getInstance().loadMails();


        // Add the list view listener for the selection made
        emailList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Mail mail = emailList.getSelectionModel().getSelectedItem();
                SceneHandler.setFromDetails(mail.getSendBy());
                SceneHandler.setSubject(mail.getSubject());
                SceneHandler.setMessage(mail.getBody());
                System.out.println(SpamClassifier.getInstance().isSpam(mail.getBody()));
                tempCenter = emailPanel.getCenter();
                emailPanel.setCenter(new EmailContent());
            }
        }));


        mails = new FilteredList<>(MailDataHandler.getInstance().getMails(), mail -> true);

        // Make the list compare the set dates
        SortedList<Mail> mailsSorted = new SortedList<>(mails, (o1, o2) ->
                o1.getSendDateTime().compareTo(o2.getSendDateTime()) > 0 ? -1 : 1);

        emailList.setItems(mailsSorted);
        emailList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //emailList.getSelectionModel().selectFirst();

        // Setting every cell property to show a menu
        emailList.setCellFactory(param -> {
            ListCell<Mail> cell = new ListCell<Mail>() {

                @Override
                protected void updateItem(Mail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.getSubject());
                    }
                }
            };

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(listContextMenu);
                }
            });

            // The method must return a cell
            return cell;
        });
    }

    @FXML
    public void handleButtonPressed(ActionEvent e) {
        if (e.getSource().equals(inboxButton)) {
            if (tempCenter != null) {
                emailPanel.setCenter(tempCenter);
                emailList.getSelectionModel().select(null);
                tempCenter = null;
            }

        } else if (e.getSource().equals(spamButton)) {
            // TODO: 11/15/18 Show spam
        }
    }

    @FXML
    public void showNewEmailDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(emailPanel.getScene().getWindow());
        dialog.setTitle("New Email");
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/mailDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        // Setting the buttons for the user
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MailDialogController mailDialogController = loader.getController();
            Mail newMail = mailDialogController.getNewMail();
            // TODO: 11/13/18 This should save the email to the other user
            MailDataHandler.getInstance().saveMailsToUser(newMail);
        }
    }

    private void deleteMail(Mail mail) {
        // Alert for the user
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete mail");
        alert.setHeaderText("Delete mail: " + mail.getSubject());
        alert.setContentText("Are you sure? There's no way to get them back");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MailDataHandler.getInstance().deleteMail(mail);
        }
    }

}
