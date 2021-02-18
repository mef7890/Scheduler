package controller.main;

import DAO.AppointmentDAO;
import DAO.TutorDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.*;
import utilities.*;

import java.time.LocalDate;
import java.time.LocalTime;

//import controller.schedule.ScheduleController;

public class TutorMainController extends MainController implements Initializable {

        String currentMonth = LocalDate.now().getMonth().toString();
        TutorDAO tutorDAO= new TutorDAO(DBConnection.connection);
        public static Tutor tutor; //  made public static // todo make local?

        @FXML
        private MenuBar menuBar;

        @FXML
        private Menu fileMenu;

        @FXML
        private MenuItem runTestsMenuItem;

        @FXML
        private MenuItem logoutMenuItem;

        @FXML
        private MenuItem exitMenuItem;

        @FXML
        private Menu schedulesMenu;

        @FXML
        private MenuItem scheduleByTutorMenuItem;

        @FXML
        private MenuItem scheduleByStudentMenuItem;

        @FXML
        private MenuItem scheduleByAdminMenuItem;

        @FXML
        private Menu reportsMenu;

        @FXML
        private MenuItem summariesForStudentMenuItem;

        @FXML
        private Menu financialsMenu;

        @FXML
        private MenuItem studentBillMenuItem;

        @FXML
        private MenuItem tutorEarningsMenuItem;

        @FXML
        private Menu adminMenu;

        @FXML
        private MenuItem createNewUserMenuItem;

        @FXML
        private MenuItem updateQuoteMenuItem;

        @FXML
        private Label welcomeMessage;

        @FXML
        private Label PersonListLabel;

        @FXML
        private RadioButton tutorsRB;

        @FXML
        private RadioButton studentsRB;

        @FXML
        private RadioButton adminsRB;

        @FXML
        private TextField searchNameText;

        @FXML
        private TableView<Person> personTableView; //  can switch back to Student

        @FXML
        private TableColumn<Person, String> personTableCol; //  can switch back to Student

        @FXML
        private RadioButton weekRB;

        @FXML
        private ToggleGroup calendarTG;

        @FXML
        private RadioButton monthRB;

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
        private Button deletePersonButton;

        @FXML
        private Button modifyPersonButton;

        @FXML
        private Button addPersonButton;

        @FXML
        private Button viewPersonButton;

        @FXML
        private Button deleteAppointmentButton;

        @FXML
        private Button modifyAppointmentButton;

        @FXML
        private Button addAppointmentButton;

        @FXML
        private Button viewAppointmentButton;

        @FXML
        private HBox appointmentHBox;

        @FXML
        private HBox personHBox;

        @FXML
        private HBox personToggleHBox;

        @FXML
        private VBox personVBox;

        @FXML
        void createNewPersonClicked(ActionEvent event) {

        }

        @FXML
        void createNewUserSelected(ActionEvent event) {

        }

        @FXML
        void deleteAppointmentButtonClicked(ActionEvent event) {

        }

        @FXML
        void deletePersonButtonClicked(ActionEvent event) {

        }

        @FXML
        void generateByStudentClicked(ActionEvent event) {

        }

        @FXML
        void generateByTutorClicked(ActionEvent event) {

        }

        @FXML
        void logoutSelected(ActionEvent event) {
                logout();
        }

        @FXML
        void modifyAppointmentButtonClicked(ActionEvent event) {

        }

        @FXML
        void modifyPersonButtonClicked(ActionEvent event) {

        }

        @FXML
        void monthViewSelected(ActionEvent event) { // todo exclude past appts? limit to future in month?
            setMonthRB();
        }

        @FXML
        void scheduleNewAppointmentButtonClicked(ActionEvent event) {

        }

        @FXML
        void searchButtonClicked(ActionEvent event) {
            search();
        }

        @FXML
        void updateQuoteSelected(ActionEvent event) {

        }

        @FXML
        void viewAppointmentButtonClicked(ActionEvent event) {
            viewAppointment(event);
        }

        @FXML
        void viewPersonButtonClicked(ActionEvent event) {
            loadViewPersonForm(event);
        }

        @FXML
        void weekViewSelected(ActionEvent event) {
            setWeekRB();
        }

        @FXML
        void scheduleByTutorSelected(ActionEvent event) {
            LoadForm.loadTutorMySchedule(scheduleByTutorMenuItem);
        }

        @FXML
        void studentsRBselected(ActionEvent event) {

        }

        @FXML
        void tutorsRBselected(ActionEvent event) {
        }

    protected void setCalendar(ObservableList<Appointment> appointments) {
            SetTableView.setScheduleByTimeFrame(appointments, calendarTableView, startTimeCol, endTimeCol, dateCol,
                    studentCol, tutorCol);
            calendarTableView.setPlaceholder(new Label("There are no appointments in this time frame"));
    }

    public ObservableList<Appointment> getTutorAppointments(String timeframe) {
        String startDate = getStartDate(timeframe);
        String endDate = getEndDate(timeframe);

        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
        return appointmentDAO.findTutorApptsByDates(tutor.getPersonId(), startDate, endDate);
    }

    @Override
    public void setPersonTableView() {
        SetTableView.setStudentsByTutorTableView(tutor.getPersonId(), personTableView, personTableCol);
    }

    protected void hideAppointmentButtons() {
        appointmentHBox.getChildren().remove(deleteAppointmentButton);
        appointmentHBox.getChildren().remove(modifyAppointmentButton);
        appointmentHBox.getChildren().remove(addAppointmentButton);
        viewAppointmentButton.setAlignment(Pos.CENTER);
    }

    public void hidePersonButtons() {
        personHBox.getChildren().remove(deletePersonButton);
        personHBox.getChildren().remove(modifyPersonButton);
        personHBox.getChildren().remove(addPersonButton);
        viewPersonButton.setAlignment(Pos.CENTER);
    }

    public void setMenuOptions() {
        menuBar.getMenus().remove(adminMenu);
        financialsMenu.getItems().remove(studentBillMenuItem);
        schedulesMenu.getItems().remove(scheduleByAdminMenuItem);
        schedulesMenu.getItems().remove(scheduleByStudentMenuItem);
        fileMenu.getItems().remove(runTestsMenuItem);
        scheduleByTutorMenuItem.setText("My Schedule");
        financialsMenu.setText("My Earnings");
    }

    public void hidePersonToggleButtons() {
        personVBox.getChildren().remove(personToggleHBox);
        personVBox.setAlignment(Pos.TOP_CENTER);

        PersonListLabel.setText("Students");
        personTableCol.setText("Students");
    }

    @Override
    protected void setButtonVisibility(Person person) {
        setMenuOptions();
        hidePersonToggleButtons();
        hidePersonButtons();
        hideAppointmentButtons();
    }

    @Override
    public Person createUserPerson(long id) {
        tutor = tutorDAO.getBasicData(id);
        MainController.currentUser = tutor.getFullName();
        MainController.currentUserType = "tutor";
        return tutor;
    }

    @Override
    protected void loadViewPersonForm(ActionEvent event) {
        loadViewStudent(event);
    }

    @Override
    protected void loadViewStudent(ActionEvent event) {
        try {
            personSourceRB = "students";
            LoadForm.loadTutorViewStudent(event, getSelectedPerson().getPersonId());
        } catch (NullPointerException e) {
            Alerts.rowNotSelectedToViewError("student");
        }
    }

    @Override
    protected void search() {
        personSourceRB = "students";
        if (Validator.hasSearchText(searchNameText)) {
            Search.tutorSearchStudents(tutor.getPersonId(), searchNameText, personTableView, personTableCol);
        }
    }

    @Override
    protected void setWeekRB() {
        calendarSourceRB = "week";
        weekRB.setSelected(true);

        setCalendar(getTutorAppointments(calendarSourceRB));
    }

    @Override
    protected void setMonthRB() {
        calendarSourceRB = "month";
        monthRB.setSelected(true);

        setCalendar(getTutorAppointments(calendarSourceRB));
    }
}