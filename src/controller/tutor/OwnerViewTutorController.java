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

public class OwnerViewTutorController extends AdminViewTutorController {

    @FXML
    private TextField tutorNameTextField;

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

    @FXML
    void closeButtonClicked(ActionEvent event) {
        new LoadForm().loadOwnerMainView(event);// todo new
    }

    public void populateTutorFields(long tutorId) {
        TutorDAO tutorDAO = new TutorDAO(DBConnection.connection);
        Tutor tutor = tutorDAO.findById(tutorId); // create new student obj
//        AddressDAO addressDAO = new AddressDAO(DBConnection.connection);// todo del
//        Address address = addressDAO.findById(tutor.getAddressId()); // create new address obj
        try {

            tutorNameTextField.setText(tutor.getFullName());
            tutorGradeTextField.setText(tutor.getTutorGrade());

            // todo del
//            street1TextField.setText(address.getStreet1());
//            street2TextField.setText(address.getStreet2());
//            if (street2TextField.getText().isEmpty()) {
//                street2TextField.setText(" ");
//            }
//            cityTextField.setText(address.getCity());
//            tutorStateTextField.setText(address.getState());
//            tutorZipTextField.setText(address.getZip());
            street1TextField.setText(tutor.getStreet1());
            street2TextField.setText(tutor.getStreet2());
            if (street2TextField.getText().isEmpty()) {
                street2TextField.setText(" ");
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

            rateTextField.setText(String.valueOf(tutor.getRate()));

            long activeStatus = tutor.getActiveStatus();
            if (activeStatus == 1) {
                activeRB.setSelected(true);
            } if (activeStatus == 0) {
                inactiveRB.setSelected(true);
            }

            // todo receive alerts, summary
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Populate Tutor Fields: FAILED");
        }
        System.out.println("Populate Tutor Fields: SUCCESSFUL");
    }

}
