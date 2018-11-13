package application.dataModels;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className User
 * @date Nov/12/2018
 * @comments None
 */
public class User {

    private SimpleStringProperty firstName = new SimpleStringProperty("");
    private SimpleStringProperty lastName = new SimpleStringProperty("");
    private SimpleStringProperty email = new SimpleStringProperty("");

    public User() {
    }

    public User(String firstName, String lastName, String email) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.email.set(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email +
                '}';
    }

    /* Getters and setters */
    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
