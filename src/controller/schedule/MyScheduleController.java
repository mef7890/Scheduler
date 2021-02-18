package controller.schedule;

import DAO.AppointmentDAO;
import controller.main.TutorMainController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import model.Appointment;
import utilities.DBConnection;
import utilities.SetTableView;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class MyScheduleController extends TutorScheduleController {

    @FXML
    private Label scheduleLabel;

    @FXML
    private ComboBox<String> timeFrameComboBox;

    @FXML
    private TableView<Appointment> calendarTableView;

    @FXML
    private TableColumn<Appointment, LocalDate> dateCol;

    @FXML
    private TableColumn<Appointment, LocalTime> startTimeCol;

    @FXML
    private TableColumn<Appointment, LocalTime> endTimeCol;

    @FXML
    private TableColumn<Appointment, String> studentCol;

    @FXML
    private TableColumn<Appointment, String> tutorCol;

    @FXML
    void closeButtonClicked(ActionEvent event) {
        close(event);
    }

    @FXML
    void personSelected(MouseEvent event) {

    }

    @FXML
    void timeFrameSelected(ActionEvent event) {
        displaySchedule(tutorId);
    }

    private final long tutorId = TutorMainController.tutor.getPersonId();

    protected ObservableList<Appointment> getAppointments(long personId) { // get appts for specific tutor within time frame

        String startDate = setStartDate(timeFrameComboBox.getSelectionModel().getSelectedItem());
        String endDate = setEndDate(timeFrameComboBox.getSelectionModel().getSelectedItem());

        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
        appointments = appointmentDAO.findTutorApptsByDates(personId, startDate, endDate);
        return appointments;
    }

    protected void displaySchedule(long personId) {
        appointments = getAppointments(personId);
        calendarTableView.setPlaceholder(new Label("There are no appointments in this time frame"));
        SetTableView.setScheduleByTimeFrame(appointments, calendarTableView, startTimeCol, endTimeCol, dateCol, studentCol,
                tutorCol);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scheduleLabel.setText("My Schedule");
        timeFrameComboBox.setItems(timeFrameOptions);
        timeFrameComboBox.getSelectionModel().select("This Week"); // default schedule view is for the current week
        displaySchedule(tutorId);
    }
}
