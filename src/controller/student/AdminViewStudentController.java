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
import model.*;
import utilities.DBConnection;
import utilities.LoadForm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminViewStudentController {

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
    private TextField studentEmailTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private RadioButton activeRB;

    @FXML
    private ToggleGroup statusTG;

    @FXML
    private RadioButton inactiveRB;

    @FXML
    private DatePicker birthdayDatePicker;

    @FXML
    private TextField platformTextField;

    @FXML
    private TextField handleTextField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField studentAgeTextField;

    @FXML
    private TextField studentGradeTextField;

    @FXML
    private TextField contactFirstNameTextField;

    @FXML
    private TextField contactLastNameTextField;

    @FXML
    private TextField contactPhoneTextField;

    @FXML
    private TextField contactEmailTextField;

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
    private TextField nextAptTutorTextField;

    @FXML
    private TextField nextAptDTTextField;

    @FXML
    void closeButtonClicked(ActionEvent event) {
        new LoadForm().loadAdminMainView(event);// todo new
    }

    public boolean populateContactPersonFields(long studentId) {
        try {
            ContactDAO contactDAO = new ContactDAO(DBConnection.connection);
//            ContactDAO contactDAO = new ContactDAO(MainController.connection);
            Contact contact = contactDAO.findById(studentId); // todo sep obj for contact, instead of putting
            // todo fields in student obj? // NEVERMIND just need person
            contactFirstNameTextField.setText(contact.getFirstName());
            contactLastNameTextField.setText(contact.getLastName());
            contactPhoneTextField.setText(contact.getPhone());
            contactEmailTextField.setText(contact.getEmail());
            relationshipTextField.setText(contact.getRelationship());

            contactStreet1TextField.setText(contact.getStreet1());
            try {
                contactStreet2TextField.setText(contact.getStreet2());
                if (contactStreet2TextField.getText().isEmpty()) {
                    contactStreet2TextField.setText(" ");
                }
            } catch (NullPointerException e) {
                contactStreet2TextField.setText(" ");
            }
            contactCityTextField.setText(contact.getCity());
            contactStateTextField.setText(contact.getState());
            contactZipTextField.setText(contact.getZip());
            // todo add alert, summary
            System.out.println("Populate Contact Fields: SUCCESSFUL");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Populate Contact Fields: FAILED");
            throw new RuntimeException();
        }
    }
// populate address fields()
//    public boolean populateAddressFields(long addressId) {
    //        try {
//            Address address = new Address();
//            address1TextField.setText(address.getStreet1());
//            address2TextField.setText(address.getStreet2());
//            cityTextField.setText(address.getCity());
//            studentStateTextField.setText(address.getState());
//            zipTextField.setText(address.getZip());
//            System.out.println("Populate Address Fields: SUCCESSFUL");
//            return true;
//        } catch (Exception e) {
//            System.out.println("Populate Address Fields: FAILED");
//            return false;
//        }
//    }

    public boolean populateNextApt(long studentId) {
        StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
//        StudentDAO studentDAO = new StudentDAO(MainController.connection);
        // set next apt date w/ formatting
        try {
            Appointment appointment = studentDAO.getStudentsNextAppointment(studentId);
            System.out.println(appointment); // todo delete
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy HH:mm");
            LocalDateTime nextApt = appointment.getApptStartDT();
            String formattedLDT = formatter.format(nextApt);
            nextAptDTTextField.setText(formattedLDT);
            nextAptTutorTextField.setText(appointment.getTutorName());
            System.out.println("Populate Next Apt Fields: SUCCESSFUL");
            return true;

        } catch (NullPointerException e) {
            System.out.println("Populate Next Apt Fields: SUCCESSFUL");
            nextAptDTTextField.setText("N/A");
            nextAptTutorTextField.setText("N/A");
            return true;
        } catch (Exception e) {
            System.out.println("Populate Next Apt Fields: FAILED");
            throw new RuntimeException();
        }
    }

    public boolean populateFormFields(long studentId) {

        try {
            populateStudentFields(studentId); // populate student fields
            populateNextApt(studentId); // populate next apt fields
//            populateAddressFields(student.getAddressId()); // populate address fields
            populateContactPersonFields(studentId);
            System.out.println("Populate Form Fields: SUCCESSFUL");
            System.out.println();
            System.out.println();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Populate Form Fields: FAILED");
            throw new RuntimeException();
        }
    }

    public Student populateStudentFields(long studentId) {
        StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
//        StudentDAO studentDAO = new StudentDAO(MainController.connection);
        Student student = studentDAO.findById(studentId); // create new student obj
            System.out.println(student.toString()); // todo delete
            System.out.println(student); // todo delete

        AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
//        AddressDAO addressDAO = new AddressDAO(MainController.connection);
        Address address = addressDAO.findById(student.getAddressId()); // create new address obj
        try {

            studentFirstNameTextField.setText(student.getFirstName());
            studentLastNameTextField.setText(student.getLastName());
            studentAgeTextField.setText(String.valueOf(student.getStudentAge()));
            studentGradeTextField.setText(student.getStudentGrade());

            phoneTextField.setText(student.getPhone());
            studentEmailTextField.setText(student.getEmail());

            platformTextField.setText(student.getStudentPlatform());
            handleTextField.setText(student.getStudentHandle());

            birthdayDatePicker.setValue(student.getBirthday());
            System.out.println("student bday = " + student.getBirthday()); // todo delete

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
//        return addressId;
    }
}

