package application.handlers;

import application.dataModels.User;

public class UserData {
    private static final UserData ourInstance = new UserData();

    private User user = new User();

    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
    }

    public void setUserName(String firstName, String lastName) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
    }

    public void setUserEmail(String email) {
        user.setEmail(email);
    }

    /**
     * The path of the directory will be the email
     * @return The email value
     */
    public String getUserDirectory() {
        // Returns db + the email username
        return "./db/" + user.getEmail().split("@")[0];
    }

    public void resetUser() {
        user = new User();
    }

    public String getMailFilePath() {
        return getUserDirectory() + "/mails.xml";
    }

    public String getUserDataPath() {
        return getUserDirectory() + "/users.csv";
    }

    public String getUserPendingPath() { return getUserDirectory() + "/pending.xml"; }

    public User getUser() {
        return user;
    }
}
