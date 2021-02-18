package controller.tutor;

import DAO.TutorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.Tutor;
import utilities.DBConnection;
import utilities.LoadForm;

public class AdminViewTutorController {

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
    private TextField tutorGradeTextField;

    @FXML
    private TextField tutorEmailTextField;

    @FXML
    void closeButtonClicked(ActionEvent event) {
        new LoadForm().loadAdminMainView(event);// todo new
    }

    public void populateTutorFields(long tutorId) {
        TutorDAO tutorDAO = new TutorDAO(DBConnection.connection);
//        TutorDAO tutorDAO = new TutorDAO(MainController.connection);
        Tutor tutor = tutorDAO.findById(tutorId); // create new student obj

        tutorFirstNameTextField.setText(tutor.getFirstName());
        tutorLastNameTextField.setText(tutor.getLastName());
        tutorGradeTextField.setText(tutor.getTutorGrade());

        street1TextField.setText(tutor.getStreet1());
        if (tutor.getStreet2().length() == 0) {
            street2TextField.setText(" ");
        } else {
            street2TextField.setText(tutor.getStreet2());
        }

        cityTextField.setText(tutor.getCity());
        tutorStateTextField.setText(tutor.getState());
        tutorZipTextField.setText(tutor.getZip());

        tutorEmailTextField.setText(tutor.getEmail());
        phoneTextField.setText(tutor.getPhone());

        platformTextField.setText(tutor.getTutorPlatform());
        handleTextField.setText(tutor.getTutorHandle());

        birthdayDatePicker.setValue(tutor.getBirthday());
        startDatePicker.setValue(tutor.getStartDate());

        long activeStatus = tutor.getActiveStatus();

        if (activeStatus == 1) {
            activeRB.setSelected(true);
        } if (activeStatus == 0) {
            inactiveRB.setSelected(true);
        }

        // todo receive alerts, summary
    }

}

