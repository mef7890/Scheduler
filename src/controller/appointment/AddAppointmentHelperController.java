package controller.appointment;

import DAO.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;
import utilities.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddAppointmentHelperController implements Initializable {

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

    }

    @FXML
    void searchStudentsButtonClicked(ActionEvent event) {
        Search.adminSearchStudents(studentSearchNameText, studentsTableView, studentsTableCol);
    }

    @FXML
    void searchTutorsButtonClicked(ActionEvent event) {
        Search.searchTutors(tutorSearchNameText, tutorsTableView, tutorsTableCol);
    }

    protected static ObservableList<Integer> hours = FXCollections.observableArrayList(8, 9, 10, 11, 12,
            13, 14, 15, 16, 17, 18, 19, 20);
    ObservableList<String> minutes = FXCollections.observableArrayList("00", "15", "30", "45");

    Appointment appointment = new Appointment();
    AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startHour.setItems(hours);
        endHour.setItems(hours);
        startMinute.setItems(minutes);
        endMinute.setItems(minutes);
        SetTableView.setAllStudentsTableView(studentsTableView, studentsTableCol);
        SetTableView.setAllTutorsTableView(tutorsTableView, tutorsTableCol);

        studentsTableView.getSelectionModel().select(0); // preselect first student in list
        tutorsTableView.getSelectionModel().select(0); // preselect first tutor in list

        datePicker.setValue(LocalDate.now().plusDays(1)); // preselect tomorrow's date
        startHour.setValue(11); // todo delete
        startMinute.setValue("15"); // todo delete
        endHour.setValue(13);// todo delete
        endMinute.setValue("30");// todo delete
        subjectFocusTextField.setText("German"); // todo delete

//        saveButton.setDefaultButton(true);

    }

    protected void cancel(ActionEvent event) {
        Alerts.cancelConfirmation(event);
    }

    protected boolean setApptPersonVariables() {
        try {
            appointment.setStudentId(studentsTableView.getSelectionModel().getSelectedItem().getPersonId());
            appointment.setStudentName(studentsTableView.getSelectionModel().getSelectedItem().getFullName());
        } catch (NullPointerException e) {
            Alerts.rowNotSelectedError("student");
            return false;
        }
        try {
            appointment.setTutorId(tutorsTableView.getSelectionModel().getSelectedItem().getPersonId());
            appointment.setTutorName(tutorsTableView.getSelectionModel().getSelectedItem().getFullName());
        } catch (NullPointerException e) {
            Alerts.rowNotSelectedError("tutor");
            return false;
        }

        return true;
    }

    protected boolean setAppointmentVariables() {

        LocalDate startDate = datePicker.getValue();

        if (startDate == null) {
            Alerts.noDateSelected();
            return false;
        } else {
            appointment.setApptDate(startDate);
        }

        try { // get start time, start dt
            int startHr = startHour.getValue();
            String startMin = startMinute.getValue();
            LocalTime startTime = LocalTime.of(startHr, Integer.parseInt(startMin));
            appointment.setApptStartTime(startTime);
            appointment.setApptStartDT(LocalDateTime.of(startDate, startTime));
        } catch (NullPointerException e) {
            Alerts.noTimeSelected("start time");
            return false;
        }
        try { // get end time
            int endHr = endHour.getValue();
            String endMin = endMinute.getValue();
            LocalTime endTime = LocalTime.of(endHr, Integer.parseInt(endMin));
            appointment.setApptEndTime(endTime);
            appointment.setApptEndDT(LocalDateTime.of(startDate, endTime));
        } catch (NullPointerException e) {
            Alerts.noTimeSelected("end time");
            return false;
        }
        // subject focus
        if (subjectFocusTextField.getText().isEmpty()) {
            Alerts.emptySubjectFocus();
            return false;
        } else {
            appointment.setSubjectFocus(subjectFocusTextField.getText());
        }

        // appointment note
        if (appointmentNotesTextArea.getText().isEmpty()) {
            appointment.setSchedulingNote("N/A");
        } else {
            appointment.setSchedulingNote(appointmentNotesTextArea.getText());
        }
        return true;
    }


    protected boolean canSetVariables() {
        return setApptPersonVariables() && setAppointmentVariables();
    }

    protected boolean hasValidData() {
        return Validator.subjectIsValid(subjectFocusTextField.getText());
    }

    protected boolean checksPassed() {
        return canSetVariables() &&
                hasValidData() &&
                Validator.isNotDoubleBooking(appointment) &&
                Validator.hasValidEndTime(appointment.getApptStartTime(), appointment.getApptEndTime()) &&
                Validator.isInFuture(appointment.getApptStartDT());
    }

}