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
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class EmailController {

    @FXML
    private Label userName;
    @FXML
    private Button inboxButton;
    @FXML
    private Button spamButton;
    @FXML
    private Button refreshButton;
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

        // Setting the label to the name of the user for reference
        userName.setText(UserData.getInstance().getUser().getFirstName() + " " +
                UserData.getInstance().getUser().getLastName());
        // Process the pending emails of the user in case they have some
        processUserEmails();

        // Mini menu for the items on the view list
        listContextMenu = new ContextMenu();

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(event -> {
            Mail mail = emailList.getSelectionModel().getSelectedItem();
            deleteMail(mail);
        });

        MenuItem markAsSpamItem = new MenuItem("Mark as Spam");
        markAsSpamItem.setOnAction(event -> {
            emailList.getSelectionModel().getSelectedItem().setSpam(true);
            Mail mail = emailList.getSelectionModel().getSelectedItem();
            markAsSpam(mail);
            listContextMenu.hide();
            mails.setPredicate(mailTo -> !mailTo.isSpam());
        });

        listContextMenu.getItems().setAll(deleteMenuItem, markAsSpamItem);

        MailDataHandler.getInstance().setEmailData(UserData.getInstance().getMailFilePath());
        MailDataHandler.getInstance().loadMails(false);
        MailDataHandler.getInstance().saveMails();

        // Add the list view listener for the selection made
        emailList.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (emailList.getSelectionModel().getSelectedItem() != null) {
                    // In case the context menu is open
                    listContextMenu.hide();
                    Mail mail = emailList.getSelectionModel().getSelectedItem();
                    SceneHandler.setFromDetails(mail.getSendBy());
                    SceneHandler.setSubject(mail.getSubject());
                    SceneHandler.setMessage(mail.getBody());
                    //System.out.println(SpamClassifier.getInstance().isSpam(mail.getBody()));
                    tempCenter = emailPanel.getCenter();
                    emailPanel.setCenter(new EmailContent());
                }
            }
        });

        mails = new FilteredList<>(MailDataHandler.getInstance().getMails(), mail -> true);

        // Make the list compare the set dates
        SortedList<Mail> mailsSorted = new SortedList<>(mails, (o1, o2) -> {
            if (o1.getSendDateTime().compareTo(o2.getSendDateTime()) == 0)
                return 0;
            return o1.getSendDateTime().compareTo(o2.getSendDateTime()) > 0 ? -1 : 1;
        });

        emailList.setItems(mailsSorted);
        emailList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // Only show emails that are not spam
        mails.setPredicate(mail -> !mail.isSpam());

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

            cell.setOnContextMenuRequested(event ->
                    listContextMenu.show(cell, event.getScreenX(), event.getScreenY()));

            // The method must return a cell
            return cell;
        });
    }

    @FXML
    public void handleButtonPressed(ActionEvent e) {
        // Change the view
        if (tempCenter != null) setCenterNode();

        if (e.getSource().equals(inboxButton)) {
            mails.setPredicate(mail -> !mail.isSpam());
        } else if (e.getSource().equals(spamButton)) {
            if (!mails.isEmpty()) {
                mails.setPredicate(Mail::isSpam);
            }
        } else if (e.getSource().equals(refreshButton)) {
            handleRefresh();
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

        final Button buttonOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        buttonOk.addEventFilter(ActionEvent.ACTION, event -> {
            MailDialogController mailDialogController = loader.getController();
            Mail newMail = mailDialogController.getNewMail();
            if (newMail == null) {
                dialog.setHeaderText("The email you entered is not correct");
                event.consume();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            MailDialogController mailDialogController = loader.getController();
            Mail newMail = mailDialogController.getNewMail();
            MailDataHandler.getInstance().saveMailsToUser(newMail);

        }
    }

    @FXML
    public void signOut() {
        MailDataHandler.getInstance().setHasSignOut(true);
        MailDataHandler.getInstance().saveMails();
        MailDataHandler.getInstance().getMails().clear();
        emailList.getSelectionModel().clearSelection();
        mails.clear();
        UserData.getInstance().resetUser();

        // Change to login screen
        Stage currentStage = (Stage) (emailPanel.getScene().getWindow());
        Parent emailResource = null;
        try {
            emailResource = FXMLLoader.load(getClass().getResource("/view/loginLayout.fxml"));
        } catch (IOException e) {
            System.out.println("The login file wasn't found");
            e.printStackTrace();
        }
        SceneHandler.changeScene(currentStage, emailResource, "Login");
    }

    private void markAsSpam(Mail mail) {
        SpamClassifier.getInstance().addSpamMail(mail.getBody());
        MailDataHandler.getInstance().saveMails();
    }

    private void handleRefresh() {
        MailDataHandler.getInstance().refreshMails();
    }

    private void processUserEmails() {
        MailDataHandler.getInstance().setEmailData(UserData.getInstance().getUserPendingPath());
        MailDataHandler.getInstance().loadMails(true);
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

    private void setCenterNode() {
        emailPanel.setCenter(tempCenter);
        emailList.getSelectionModel().select(null);
        tempCenter = null;
    }

}