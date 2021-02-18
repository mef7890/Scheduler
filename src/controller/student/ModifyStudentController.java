package controller.student;

import DAO.AddressDAO;
import DAO.ContactDAO;
import DAO.StudentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.Address;
import model.Contact;
import model.Student;
import utilities.Alerts;
import utilities.DBConnection;
import utilities.LoadForm;
import utilities.SwitchScreen;

public class ModifyStudentController extends AdminViewStudentController {

    @FXML
    private TextField studentFirstNameTextField;

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
    private TextField studentLastNameTextField;

    @FXML
    private TextField studentGradeTextField;

    @FXML
    private TextField studentEmailTextField;

    @FXML
    private TextField contactFirstNameTextField;

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
    private TextField contactLastNameTextField;

    @FXML
    private RadioButton activeRB;

    @FXML
    private ToggleGroup statusTG;

    @FXML
    private RadioButton inactiveRB;

    StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
//    StudentDAO studentDAO = new StudentDAO(MainController.connection);
    ContactDAO contactDAO = new ContactDAO(DBConnection.connection);
//    ContactDAO contactDAO = new ContactDAO(MainController.connection);

    Contact contact = new Contact();
    Student student = new Student();

    SwitchScreen switchScreen = new SwitchScreen();


    @FXML
    void cancelButtonClicked(ActionEvent event) {
        Alerts.cancelConfirmation(event);
    }

    @FXML
    void saveButtonClicked(ActionEvent event) {
        updateForm();
        Alerts.studentUpdatedConfirmation();
        new LoadForm().loadAdminMainView(event); // todo new
    }

    public Student populateStudentFields(long studentId) {
        StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
        student = studentDAO.findById(studentId); // create new student obj

        AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
        Address address = addressDAO.findById(student.getAddressId()); // create new address obj
        try {

            studentFirstNameTextField.setText(student.getFirstName());
            studentLastNameTextField.setText(student.getLastName());
            studentGradeTextField.setText(student.getStudentGrade());

            phoneTextField.setText(student.getPhone());
            studentEmailTextField.setText(student.getEmail());

            platformTextField.setText(student.getStudentPlatform());
            handleTextField.setText(student.getStudentHandle());

            birthdayDatePicker.setValue(student.getBirthday());

            street1TextField.setText(address.getStreet1());
            street2TextField.setText(address.getStreet2());
            if (street2TextField.getText().isEmpty()) {
                street2TextField.setText(" ");
            }
            cityTextField.setText(address.getCity());
            studentStateTextField.setText(address.getState());
            studentZipTextField.setText(address.getZip());

            startDatePicker.setValue(student.getStartDate());
            long activeStatus = student.getActiveStatus();
            if (activeStatus == 1) {
                activeRB.setSelected(true);
            } if (activeStatus == 0) {
                inactiveRB.setSelected(true);
            }

            // todo receive alerts, summary
//            addressId = student.getAddressId();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Populate Student Fields: FAILED");
            throw new RuntimeException();
        }
        System.out.println("Populate Student Fields: SUCCESSFUL");
        return student;
    }

    public boolean populateFormFields(long studentPersonId) {
        createInitialStudentObj(studentPersonId); // create initial obj with all the necessary ids to update
        createInitialContactObj(studentPersonId); // create initial obj with all the necessary ids to update

        try {
            populateStudentFields(studentPersonId); // populate student fields
            populateContactPersonFields(studentPersonId); // populate contact fields
            System.out.println("Populate Form Fields: SUCCESSFUL");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Populate Form Fields: FAILED");
            throw new RuntimeException();
        }
    }

    protected void createInitialContactObj(long studentPersonId) {
        contact = contactDAO.findById(studentPersonId);
        System.out.println("initial contact ob = " + contact); //todo delete

    }

    protected void createInitialStudentObj(long studentPersonId) {
        student = studentDAO.findById(studentPersonId);
    }

    // -----   SET OBJECT VARIABLES FROM VALUES IN FORM   ----- // // todo move to single setStudentVariables method
    protected Student setStudentAddressVariables() {
        student.setStreet1(street1TextField.getText());
        student.setStreet2(street2TextField.getText());
        student.setCity(cityTextField.getText());
        student.setState(studentStateTextField.getText());
        student.setZip(studentZipTextField.getText());
        System.out.println("set stud add"); // todo delete

        return student;
    }

    protected Student setStudentPersonVariables() {

        student.setRoleId(4);
        if (activeRB.isSelected()) {
            student.setActiveStatus(1);
        } else if (inactiveRB.isSelected()) {
            student.setActiveStatus(0);
        }

        String firstName = studentFirstNameTextField.getText();
        String lastName = studentLastNameTextField.getText();
        String fullName = firstName + " " + lastName;
        student.setFullName(fullName);
        student.setFirstName(firstName);
        student.setLastName(lastName);

        student.setPhone(phoneTextField.getText());
        student.setEmail(studentEmailTextField.getText());

        student.setStartDate(startDatePicker.getValue());
        student.setBirthday(birthdayDatePicker.getValue());
        student.setStudentAge();

        //  not putting last update by as person variable, putting it in insert
//            student.setStudentAge(Integer.parseInt(studentAgeTextField.getText()));
//            student.setStudentGrade(studentGradeTextField.getText()); //todo enum
//
//            student.setStudentPlatform(platformTextField.getText());
//            student.setStudentHandle(handleTextField.getText());
//
//            student.setStreet1(street1TextField.getText());
//            student.setStreet2(street2TextField.getText());
//            student.setCity(cityTextField.getText());
//            student.setState(studentStateTextField.getText());
//            student.setZip(studentZipTextField.getText());

        return student;
    }

    protected Student setOtherStudentVariables() {
        student.setStudentGrade(studentGradeTextField.getText()); //todo enum

        student.setStudentPlatform(platformTextField.getText());
        student.setStudentHandle(handleTextField.getText());

//        student.setStudentReceiveAlerts(); // todo add
//        student.setStudentReceiveSummary(); // todo add

        return student;
    }

    protected Contact setContactAddressVariables() {
        contact.setStreet1(contactStreet1TextField.getText());
        contact.setStreet2(contactStreet2TextField.getText());
        contact.setCity(contactCityTextField.getText());
        contact.setState(contactStateTextField.getText());
        contact.setZip(contactZipTextField.getText());
        return contact;
    }

    protected Contact setContactPersonVariables() {
        contact.setRoleId(5);

        String firstName = contactFirstNameTextField.getText();
        String lastName = contactLastNameTextField.getText();
        String fullName = firstName + " " + lastName;
        contact.setFullName(fullName);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setPhone(contactPhoneTextField.getText());
        contact.setEmail(contactEmailTextField.getText());

        contact.setStartDate(startDatePicker.getValue());
        contact.setBirthday(birthdayDatePicker.getValue());
        contact.setAddressId(contact.getAddressId());

        contact.setRelationship(relationshipTextField.getText());

        // todo receive alerts
        return contact;
    }

    protected void setObjectVariables() {
        student = setStudentAddressVariables();
        student = setStudentPersonVariables();
        student = setOtherStudentVariables();
        contact = setContactAddressVariables();
        contact = setContactPersonVariables();
    }
    protected void updateStudent() { // todo return void?
        try {
            student = studentDAO.update(student);
//            contact = contactDAO.update(contact);
//            contactAddress = addressDAO.update(contactAddress);
//            contact = personDAO.update(contact);
            System.out.println("Update Student: SUCCESSFUL");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Update Student: FAILED");
            throw new RuntimeException();
        }
    }
    protected void updateForm() { // todo return void?
        try {
            setObjectVariables();
            updateStudent();
            updateContact();
            System.out.println("Update Form: SUCCESSFUL");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Update Form: FAILED");
            throw new RuntimeException();
        }
    }

    protected void updateContact() {
        try {
            contact = contactDAO.update(contact);
            System.out.println("Update Contact: SUCCESSFUL");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Update Contact: FAILED");
            throw new RuntimeException();
        }
    }

}
