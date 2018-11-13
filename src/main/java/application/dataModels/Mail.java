package application.dataModels;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className Mail
 * @date Nov/12/2018
 * @comments None
 */
public class Mail {

    private SimpleStringProperty subject = new SimpleStringProperty("");
    private SimpleStringProperty body= new SimpleStringProperty("");
    private SimpleStringProperty sendBy = new SimpleStringProperty("");
    private SimpleStringProperty sendTo = new SimpleStringProperty("");

    public Mail() {
        // This is for the MailDataHandler
    }

    public Mail(String subject, String body, String sendBy, String sendTo) {
        this.subject.set(subject);
        this.body.set(body);
        this.sendBy.set(sendBy);
        this.sendTo.set(sendTo);
    }

    /* Getters and setters */

    public String getSubject() {
        return subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getBody() {
        return body.get();
    }

    public SimpleStringProperty bodyProperty() {
        return body;
    }

    public void setBody(String body) {
        this.body.set(body);
    }

    public String getSendBy() {
        return sendBy.get();
    }

    public SimpleStringProperty sendByProperty() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy.set(sendBy);
    }

    public String getSendTo() {
        return sendTo.get();
    }

    public SimpleStringProperty sendToProperty() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo.set(sendTo);
    }
}
