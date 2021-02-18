package controller.student;

import DAO.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.*;
import utilities.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddStudentController implements Initializable { // todo once testing is done, can delete initializable

    long studentId;

    String studentFirstName;
    String studentLastName;

    String studentPhone;
    String studentEmail;

    String studentStreet1;
    String studentStreet2;
    String studentCity;
    String studentState;
    String studentZip;

    LocalDate studentStarDate;
    LocalDate studentBirthday;

    String studentGrade;
    String studentHandle;
    String studentPlatform;

    String contactFirstName;
    String contactLastName;

    String contactPhone;
    String contactEmail;

    String contactStreet1;
    String contactStreet2;
    String contactCity;
    String contactState;
    String contactZip;

    LocalDate contactStartDate;
    LocalDate contactBirthday = LocalDate.now().minusYears(100); // todo know it's a dummy date, fix this

    String relationship;


    StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
//    StudentDAO studentDAO = new StudentDAO(MainController.connection);
    ContactDAO contactDAO = new ContactDAO(DBConnection.connection);
//    ContactDAO contactDAO = new ContactDAO(MainController.connection);

    Student student = new Student();
    Contact contact = new Contact();
    Address contactAddress = new Address();

    SwitchScreen switchScreen = new SwitchScreen();

    @FXML
    private TextField studentFirstNameTextField;

    @FXML
    private TextField studentLastNameTextField;

    @FXML
    private TextField street1TextField;

    @FXML
    private TextField street2TextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField studentZipTextField;

    @FXML
    private TextField studentStateTextField;

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
    private TextField studentGradeTextField;

    @FXML
    private TextField studentEmailTextField;

    @FXML
    private TextField contactFirstNameTextField;

    @FXML
    private TextField contactLastNameTextField;

    @FXML
    private TextField contactStreet1TextField;

    @FXML
    private TextField contactStreet2TextField;

    @FXML
    private TextField contactCityTextField;

    @FXML
    private TextField contactZipTextField;

    @FXML
    private TextField contactStateTextField;

    @FXML
    private TextField relationshipTextField;

    @FXML
    private TextField contactPhoneTextField;

    @FXML
    private TextField contactEmailTextField;

    @FXML
    private Button saveButton;

    @FXML
    void cancelButtonClicked(ActionEvent event) {
        Alerts.cancelConfirmation(event);
    }

    @FXML
    void saveButtonClicked(ActionEvent event) {
        insertFormFields(event);
    }

    protected boolean fieldsAreValid() {
        if ((Validator.numberDoesNotContainLetters(studentPhone)) && // if phone #s don't contain letters
                Validator.numberDoesNotContainLetters(contactPhone)) {
            studentPhone = FormatAndParse.parsePhone(studentPhone); // parse it to xxx-xxx-xxxx
            contactPhone = FormatAndParse.parsePhone(contactPhone); // parse it to xxx-xxx-xxxx

            return (Validator.personFieldsAreValid(studentPhone, studentFirstName, studentLastName, studentCity, studentState,
                    studentZip, studentEmail)) &&
                    (Validator.personFieldsAreValid(contactPhone, contactFirstName, contactLastName, contactCity, contactState,
                            contactZip, contactEmail)) &&
                    (Validator.textIsValid(relationship));
        }
        return false;
    }
    protected void insertFormFields(ActionEvent event) {

        try {
            setLocalVariables();
            if (fieldsAreValid()) {
                insertStudent();
                insertContact();
                Alerts.studentCreatedConfirmation();
                new LoadForm().loadAdminMainView(event); // todo new
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Alerts.emptyTextFieldError();
        }
    }

    protected void setLocalVariables() {
        studentFirstName = studentFirstNameTextField.getText();
        studentLastName = studentLastNameTextField.getText();

        studentPhone = phoneTextField.getText();

        studentEmail = studentEmailTextField.getText();

        studentStreet1 = street1TextField.getText();
        studentStreet2 = street2TextField.getText();
        studentCity = cityTextField.getText();
        studentState = studentStateTextField.getText();
        studentZip = studentZipTextField.getText();

        studentStarDate = startDatePicker.getValue();
        studentBirthday = birthdayDatePicker.getValue();

        studentGrade = studentGradeTextField.getText();
        studentHandle = handleTextField.getText();
        studentPlatform = platformTextField.getText();

        contactFirstName = contactFirstNameTextField.getText();
        contactLastName = contactLastNameTextField.getText();

        contactPhone = contactPhoneTextField.getText();
        contactEmail = contactEmailTextField.getText();

        contactStreet1 = contactStreet1TextField.getText();
        contactStreet2 = contactStreet2TextField.getText();
        contactCity = contactCityTextField.getText();
        contactState = contactStateTextField.getText();
        contactZip = contactZipTextField.getText();

        contactStartDate = startDatePicker.getValue();

        relationship =relationshipTextField.getText();
    }

    protected Contact setContactVariables() {

        // address
        contact.setStreet1(contactStreet1);
        contact.setStreet2(contactStreet2);
        contact.setCity(contactCity);
        contact.setState(contactState);
        contact.setZip(contactZip);

        // person
        contact.setRoleId(5);

        contact.setFirstName(contactFirstName);
        contact.setLastName(contactLastName);
        contact.setFullName(contactFirstName + " " + contactLastName);

        contact.setPhone(contactPhone);
        contact.setEmail(contactEmail);

        contact.setStartDate(contactStartDate);
        contact.setBirthday(contactBirthday); // todo this is dummy data, fix this
        contact.setAddressId(contactAddress.getAddressId());

        contact.setRelationship(relationship);

        // todo receive alerts, summary
        return contact;
    }
    protected Student setStudentVariables() {
        // address
        student.setStreet1(studentStreet1);
        student.setStreet2(studentStreet2);
        student.setCity(studentCity);
        student.setState(studentState);
        student.setZip(studentZip);

        // person
        student.setRoleId(4);
        student.setActiveStatus(1);

        student.setFullName(studentFirstName + " " + studentLastName);
        student.setFirstName(studentFirstName);
        student.setLastName(studentLastName);

        student.setPhone(studentPhone);
        student.setEmail(studentEmail);

        student.setStartDate(studentStarDate);
        student.setBirthday(studentBirthday);

        // don't need age since it's derived when viewing student grade
        student.setStudentGrade(studentGrade); //todo enum

        // platform
        student.setStudentHandle(studentHandle);
        student.setStudentPlatform(studentPlatform);

//        student.setStudentReceiveAlerts(); // todo add
//        student.setStudentReceiveSummary(); // todo add

            return student;
    }

    protected void insertStudent() {
        student = setStudentVariables();
        student = studentDAO.create(student);
        studentId = student.getPersonId(); // get person id from recently inserted student
    }


    protected void insertContact() {
        contact.setStudentId(studentId); // get person id from recently inserted student
        contact = setContactVariables();
        contact = contactDAO.create(contact);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startDatePicker.setValue(LocalDate.now());
        saveButton.setDefaultButton(true);

    }
}
