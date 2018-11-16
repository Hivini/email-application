package application.dataModels;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;

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
    private LocalDateTime sendDateTime;
    private boolean isSpam;
    private boolean isRead;

    public Mail() {
        this.isSpam = false;
        this.isRead = false;
    }

    public Mail(String subject, String body, String sendBy, String sendTo, LocalDateTime sendDateTime) {
        this.subject.set(subject);
        this.body.set(body);
        this.sendBy.set(sendBy);
        this.sendTo.set(sendTo);
        this.sendDateTime = sendDateTime;
    }

    @Override
    public String toString() {
        return this.getSubject();
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

    public LocalDateTime getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(LocalDateTime sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    public boolean isSpam() {
        return isSpam;
    }

    public void setSpam(boolean spam) {
        isSpam = spam;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
