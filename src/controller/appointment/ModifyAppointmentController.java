package controller.appointment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Person;
import utilities.*;

public class ModifyAppointmentController extends AddAppointmentHelperController {

    @FXML
    private TextField studentSearchNameText;

    @FXML
    private TableView<Person> studentsTableView; // todo should be Student?

    @FXML
    private TableColumn<Person, String> studentsTableCol; // todo should be Student?

    @FXML
    private TextField tutorSearchNameText;

    @FXML
    private TextField subjectFocusTextField;

    @FXML
    private TableView<Person> tutorsTableView; // todo should be Tutor2?

    @FXML
    private TableColumn<Person, String> tutorsTableCol; // todo should be Tutor2?

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<Integer> startHour;

    @FXML
    private ChoiceBox<String> startMinute;

    @FXML
    private ChoiceBox<Integer> endHour;

    @FXML
    private ChoiceBox<String> endMinute;

    @FXML
    private TextArea appointmentNotesTextArea;

    @FXML
    private Button saveButton;

    @FXML
    void cancelButtonClicked(ActionEvent event) {
        cancel(event);
    }

    @FXML
    void saveButtonClicked(ActionEvent event) {
        updateAppointment(event);

    }

    @FXML
    void searchStudentsButtonClicked(ActionEvent event) {
        Search.adminSearchStudents(studentSearchNameText, studentsTableView, studentsTableCol);

    }

    @FXML
    void searchTutorsButtonClicked(ActionEvent event) {
        Search.searchTutors(tutorSearchNameText, tutorsTableView, tutorsTableCol);
    }


    protected void createInitialAppointmentObj(long appointmentId) {
        appointment = appointmentDAO.findById(appointmentId);
    }

    public void populateAppointmentFields() {
        try {
            datePicker.setValue(appointment.getApptDate());
            startHour.setValue(FormatAndParse.hour(appointment.getApptStartTime()));
            startMinute.setValue(FormatAndParse.minute(appointment.getApptStartTime()));
            endHour.setValue(FormatAndParse.hour(appointment.getApptEndTime()));
            endMinute.setValue(FormatAndParse.minute(appointment.getApptEndTime()));
            subjectFocusTextField.setText(appointment.getSubjectFocus());
            appointmentNotesTextArea.setText(appointment.getSchedulingNote());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Populate Appointment Fields: FAILED");
            throw new RuntimeException();
        }
        System.out.println("Populate Appointment Fields: SUCCESSFUL");
    }

    public void populateFormFields(long appointmentId) {

        createInitialAppointmentObj(appointmentId); // create initial obj with all the necessary ids to update
        SetTableView.setModifyApptsStudentsTableView(appointment.getStudentName(), studentsTableView, studentsTableCol);
        SetTableView.setModifyApptsTutorsTableView(appointment.getTutorName(), tutorsTableView, tutorsTableCol);
        populateAppointmentFields(); // populate tutor fields // removed id arg

    }

    protected void updateAppointment(ActionEvent event) {

        if (checksPassed()) {
            appointment = appointmentDAO.update(appointment);
            Alerts.appointmentUpdatedConfirmation();
            new LoadForm().loadAdminMainView(event);
        }
    }

}
