package controller.tutor;

import DAO.TutorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Tutor;
import utilities.*;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddTutorController {

    @FXML
    private TextField tutorFirstNameTextField;

    @FXML
    private TextField tutorLastNameTextField;

    @FXML
    private TextField street1TextField;

    @FXML
    private TextField street2TextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField tutorZipTextField;

    @FXML
    private TextField tutorStateTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private DatePicker birthdayDatePicker;

    @FXML
    private TextField platformTextField;

    @FXML
    private TextField handleTextField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField tutorGradeTextField;

    @FXML
    private TextField tutorEmailTextField;

    @FXML
    private TextField rateTextField;

    @FXML
    private TextField tutorMajorTextField;

    @FXML
    private Button saveButton;

    @FXML
    void cancelButtonClicked(ActionEvent event) {
        Alerts.cancelConfirmation(event);
    }

    @FXML
    void saveButtonClicked(ActionEvent event) {
        insertTutor(event);
    }

    SwitchScreen switchScreen = new SwitchScreen();

    Tutor tutor = new Tutor();
    TutorDAO tutorDAO = new TutorDAO(DBConnection.connection);
//    TutorDAO tutorDAO = new TutorDAO(MainController.connection);

    long activeStatus;

    String firstName;
    String lastName;
    String fullName;

    String phone;
    String email;

    String street1;
    String street2;
    String city;
    String state;
    String zip;

    LocalDate birthday;
    LocalDate startDate;

    String handle;
    String platform;

    String grade;

    BigDecimal rate;
    String rateString;

    protected boolean fieldsAreValid() {

        if (Validator.numberDoesNotContainLetters(phone)) { // if phone doesn't contain letters
            phone = FormatAndParse.parsePhone(phone); // parse it to xxx-xxx-xxxx
            return (Validator.personFieldsAreValid(phone, firstName, lastName, city, state, zip, email) &&
                    (Validator.numberDoesNotContainLetters(rateString)));
        }
        return false;
    }

    protected void setLocalVariables() throws NullPointerException{ // todo could get rid of this and pass getText...to validator
        firstName = tutorFirstNameTextField.getText();
        lastName = tutorLastNameTextField.getText();
        fullName = firstName + " " + lastName;

        phone = phoneTextField.getText();

        email = tutorEmailTextField.getText();
        street1 = street1TextField.getText();
        try {
            street2 = street2TextField.getText();
        } catch (NullPointerException e) {
            street2 = "";
        }
        city = cityTextField.getText();
        state = tutorStateTextField.getText();
        zip = tutorZipTextField.getText();

        birthday = birthdayDatePicker.getValue();
        startDate = startDatePicker.getValue();

        handle = platformTextField.getText();
        platform = handleTextField.getText();

        grade = tutorGradeTextField.getText();

        try { // needed to do this since modify tutor doesn't have rateTextField... can rework if add rate text field
            rateString = FormatAndParse.parseRateString(rateTextField.getText()); // remove '$' and add '.00' if necessary
        } catch (NullPointerException e) {
            rateString =tutor.getRate().toString(); // use the rate in the tutor obj (for modify tutor)
        }
    }


    protected Tutor setTutorVariables() {
        tutor.setRoleId(3);

        tutor.setActiveStatus(1); // this will be overridden in modify tutor

        tutor.setFullName(fullName);
        tutor.setFirstName(firstName);
        tutor.setLastName(lastName);

        tutor.setStreet1(street1);
        tutor.setStreet2(street2);
        tutor.setCity(city);
        tutor.setState(state);
        tutor.setZip(zip);

        tutor.setPhone(phone); // parsed in valid checker

        tutor.setEmail(email);

        tutor.setStartDate(startDate);
        tutor.setBirthday(birthday);

        tutor.setTutorPlatform(platform);
        tutor.setTutorHandle(handle);

        tutor.setTutorGrade(grade);

        tutor.setRate(FormatAndParse.parseRate(rateString));

        return tutor;
    }

    protected void insertTutor(ActionEvent event) {
        try {
            setLocalVariables();
//            if (tutor.tutorFieldsAreValid(rateString)) {
            if (fieldsAreValid()) {
                tutor = setTutorVariables(); // sets obj with values from text fields
                tutor = tutorDAO.create(tutor); // make sure it works with a local tutor variable
                new LoadForm().loadAdminMainView(event); // todo new

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Alerts.emptyTextFieldError();
        }
    }

}
