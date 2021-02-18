package controller.etc;

import controller.main.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tests.Tests;
import utilities.*;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    int returnedActiveStatus;
    String returnedUsername;
    String returnedPassword;
    String returnedUsersName;
    public static long returnedUserId;
    int returnedRoleId;

    private String enteredUsername;
    private String enteredPassword;

    ResultSet resultSet;

    SwitchScreen switchScreen = new SwitchScreen();

    private static final String SELECT_USER = "SELECT user_id, username, p_word, active_status, role_id " +
            "FROM user_data WHERE username = ?";

    private static final String SELECT_TUTOR = "SELECT person_id, role_id, f_name, l_name, phone, email, address_id," +
            " birthday, start_date FROM person WHERE user_id = ?";

    private static final String SELECT_ADMIN = "SELECT person_id, role_id, f_name, l_name, phone, email, address_id, " +
            "birthday, start_date FROM person WHERE user_id = ?";

//    private Connection connection = new DBConnection().startConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        DBConnection.connection = DBConnection.startConnection(); // establish db connection

        ResourceBundle rb = ResourceBundle.getBundle("resources/Language", Locale.getDefault());
        LoginUsernameTxt.setPromptText(rb.getString("username"));
        LoginPasswordField.setPromptText(rb.getString("password"));
        LoginButton.setText(rb.getString("login"));
        LoginButton.setDefaultButton(true); // uses enter to login
        LoginLabel.setText(rb.getString("login"));
    }

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label LoginLabel;

    @FXML
    private TextField LoginUsernameTxt;

    @FXML
    private PasswordField LoginPasswordField;

    @FXML
    private Button LoginButton;


    // ----------   Test for valid fields   ----------//
    public boolean hasEmptyFields()  {
        if(LoginUsernameTxt.getText().isEmpty()) { // test if username field is empty
            Alerts.emptyUsernameError();
            return true;
        }
        if (LoginPasswordField.getText().isEmpty()) { // test if password field is empty
            Alerts.emptyPasswordError();
            return true;
        }
        return false;
    }

    private void setEnteredVariables() {
        enteredUsername = LoginUsernameTxt.getText();
        enteredPassword = LoginPasswordField.getText();
    }

    private void setLocalVariables(String username) { // sets local var, verifies username = valid
        DBConnection dbConnection = new DBConnection();
        DBConnection.connection = dbConnection.startConnection(); // establish db connection // todo new, a little slower to
        // put it
//        DBConnection.connection = DBConnection.startConnection(); // establish db connection // old a little slower to put it
        // here than in initialize

        try (PreparedStatement statement = DBConnection.connection.prepareStatement(SELECT_USER)) {
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                returnedUserId = resultSet.getInt("user_id");
                returnedRoleId = resultSet.getInt("role_id");
                returnedUsername = resultSet.getString("username");
                returnedPassword = resultSet.getString("p_word");
                returnedActiveStatus = resultSet.getInt("active_status");
                System.out.println("Username: VALID"); // todo del?
            }
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
//            Alerts.SQLerror(); // todo del
            System.out.println("Username: INVALID"); // todo del?
            Alerts.invalidLoginError();

        }
    }

    private boolean isValidPassword(String enteredPassword) {
            if (enteredPassword.equals(returnedPassword)) {
                System.out.println("Password: VALID");
                return true;
            }
        Alerts.invalidLoginError();
        return false;
    }

    private boolean isActive() {
        if (returnedActiveStatus == 1) {
            System.out.println("User: ACTIVE");
            return true;
        }
        if (returnedActiveStatus == 0) {
            Alerts.inactiveUserAccountError();
            System.out.println("User: INACTIVE");
        }
        return false;
    }

    private void log(ActionEvent event) {
        if (!hasEmptyFields()) {
            setEnteredVariables();
            setLocalVariables(enteredUsername);
//            if (isValidUser(enteredUsername) &&  // check if user found in db, return rs with values from user table
//                setReturnedVariables(resultSet); // set local variables with vales from db
            if (isValidPassword(enteredPassword) && // check if password valid for username
                     isActive()) { // check if user is active
                    System.out.println("Login Attempt: SUCCESSFUL");
                    MainController.actionTimestamp = LocalDateTime.now(); // set the timestamp for the login
                    loginByRole(event);
                }

        } else {
            System.out.println("Login Attempt: FAILED");
        }
    }

    public boolean loginTest(String username, String password) {
        tests.Tests.testsRun++;
        setLocalVariables(username);
//        if (isValidUser(username)) { // check if user found in db, return rs with values from user table
//            setReturnedVariables(resultSet); // set local variables with vales from db
//        }
        if (isValidPassword(password) && // check if password valid for username
                isActive()) { // check if user is active
            Tests.testsPassed++;
            System.out.println("Login Test: PASSED");
            return true;
        } else {
            Tests.failedTests.add("Delete Appointment Test: FAILED\n"); // todo new
            System.out.println("Login Attempt: FAILED");
        }
        return false;
    }

    private void loginByRole(ActionEvent event) {
        switch (returnedRoleId) {
            case 1:
                System.out.println("Role: OWNER");
                displayOwnerMain(event); // todo fix to set controller and real ownerMain
                break;
            case 2:
                System.out.println("Role: ADMIN");
                displayAdminMain(event); // todo fix to set controller and real ownerMain
                break;
            case 3:
                System.out.println("Role: TUTOR");
                displayTutorMain(event);
                break;
        }
    }

    private void displayAdminMain(ActionEvent event) {
        new LoadForm().loadAdminMainView(event); // to main screen
    }

    private void displayOwnerMain(ActionEvent event) {
        new LoadForm().loadOwnerMainView(event);
    }

    private void displayTutorMain(ActionEvent event) {
        new LoadForm().loadTutorMainView(event);
    }

    @FXML
    void LoginButtonClicked(ActionEvent event) {
        log(event);
    }

}


