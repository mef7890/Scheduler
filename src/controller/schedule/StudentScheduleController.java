package controller.schedule;

import DAO.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Appointment;
import model.Person;
import utilities.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class StudentScheduleController extends ScheduleController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeFrameComboBox.setItems(timeFrameOptions); // todo months?
        SetTableView.setAllStudentsTableView(personTableView, personTableCol);
        searchButton.setDefaultButton(true);
    }
    ObservableList<Appointment> appointments = FXCollections.observableArrayList(); // todo can remove from here


    @FXML
    private Label searchHeader;

    @FXML
    private TextField searchNameText;

    @FXML
    private Button searchButton;

    @FXML
    private TableView<Person> personTableView;

    @FXML
    private TableColumn<Person, String> personTableCol;

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

    @Override
    public ObservableList<Appointment> getAppointments() { // get appts for specific student within time frame
        String startDate = setStartDate(timeFrameComboBox.getSelectionModel().getSelectedItem());
        String endDate = setEndDate(timeFrameComboBox.getSelectionModel().getSelectedItem());

        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
        appointments = appointmentDAO.findStudentByDates(getSelectedPerson().getPersonId(), startDate, endDate);

        return appointments;
    }

    protected void searchStudents() {
        appointments.clear(); // reset the list before each search
        Search.adminSearchStudents(searchNameText, personTableView, personTableCol); // search students, sets table view
    }
    @FXML
    void searchButtonClicked(ActionEvent event) {
        searchStudents();
        personTableView.getSelectionModel().select(0); // select first of search results
        personSelected(); // display appts for selected tutor
    }

    @FXML
    void timeFrameSelected(ActionEvent event) {
        if ((personTableView.getSelectionModel().getSelectedIndex() != -1)) { // if person selected
            displaySchedule();
        }
    }

    @FXML
    void personSelected() {
        if ((timeFrameComboBox.getSelectionModel().getSelectedIndex() != -1)) { // if a timeframe is selected
            displaySchedule();
        }
    }

}
