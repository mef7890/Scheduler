package controller.tutor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import utilities.Alerts;
import utilities.LoadForm;
import utilities.SwitchScreen;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyTutorController extends AddTutorController {

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
    private TextField rateTextField;

    SwitchScreen switchScreen = new SwitchScreen();

    @FXML
    void cancelButtonClicked(ActionEvent event) {
        Alerts.cancelConfirmation(event);
    }

    @FXML
    void saveButtonClicked(ActionEvent event) {
        updateTutor(event);
    }

    // todo copy over from view tutor? I tried and got null ptr exception when trying to set text fields
    public void populateTutorFields(long personId) { // used to take tutorId and create the obj in the method
        tutor = tutorDAO.findById(personId);
        tutor.setPersonId(tutor.getPersonId()); // set this val now since don't inherit this value setting
        tutor.setActiveStatus(tutor.getActiveStatus()); // set this val now since don't inherit this value setting

        try {
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

            activeStatus = tutor.getActiveStatus();
            if (activeStatus == 1) {
                activeRB.setSelected(true);
            } if (activeStatus == 0) {
                inactiveRB.setSelected(true);
            }

            // todo receive alerts, summary
        } catch (Exception e) { // todo del
            e.printStackTrace();// todo del
            System.out.println("Populate Tutor Fields: FAILED");// todo del
            throw new RuntimeException();// todo del
        }
        System.out.println("Populate Tutor Fields: SUCCESSFUL");// todo del
//        //        return addressId;
    }

    protected long setActiveStatus() {
        if (activeRB.isSelected()) {
            activeStatus = 1;
        } if (inactiveRB.isSelected()) {
            activeStatus = 0;
        }
        return activeStatus;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // todo can remove once initializable removed from add tutor
    }

    protected void updateTutor(ActionEvent event) {
        try {
            setLocalVariables(); // from add tutor controller
            if (fieldsAreValid()) { // from add tutor
                tutor = setTutorVariables();// from add tutor, but need to get the id from orig, set id when populated form
                tutor.setActiveStatus(setActiveStatus()); // get active status from RB (overrides what is inherited from add tutor)
                tutor = tutorDAO.update(tutor);
                new LoadForm().loadAdminMainView(event); // todo new

            }
        } catch (NullPointerException e) {
            Alerts.emptyTextFieldError();
        }
    }

}

