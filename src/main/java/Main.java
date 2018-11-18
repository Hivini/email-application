import application.classifier.SpamClassifier;
import application.handlers.MailDataHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginLayout.fxml"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("LoginController");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // !(MailDataHandler.getInstance().getEmailData() == null)
        if (!MailDataHandler.getInstance().isHasSignOut())
            MailDataHandler.getInstance().saveMails();
    }

    public static void main(String[] args) {
        SpamClassifier.getInstance().initializeData();
        launch(args);

    }
}
