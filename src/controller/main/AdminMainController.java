package controller.main;

import DAO.AdminDAO;
import DAO.AppointmentDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;
import utilities.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class AdminMainController extends MainController implements Initializable {

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
    private Label welcomeMessage1;

    @FXML
    private Label PersonListLabel;

    @FXML
    private RadioButton tutorsRB;

    @FXML
    private RadioButton studentsRB;

    @FXML
    private RadioButton adminsRB;

    @FXML
    private ToggleGroup personTG;

    @FXML
    private TextField searchNameText;

    @FXML
    private TableView<Person> personTableView;

    @FXML
    private TableColumn<Person, String> personTableCol;

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
    void createNewPersonClicked(ActionEvent event) {
        loadCreateNewStudent(event);
        loadCreateNewTutor(event);
    }
    @FXML
    void createNewUserSelected(ActionEvent event) {

    }

    @FXML
    void deleteAppointmentButtonClicked(ActionEvent event) {
        System.out.println("Delete appt clicked");
        setCalendarSourceRB(); // set source value
        deleteAppointment();
        if (calendarSourceRB.matches("month")) {
            setMonthRB();
        } else if (calendarSourceRB.matches("week")) {
            setWeekRB();
        }
//        setCalendarTableView();// todo or liek students
    }

    @FXML
    void deletePersonButtonClicked(ActionEvent event) {

        deletePerson(event);
        if (personSourceRB.matches("students")) {
                setStudentsRB();
        } else if (personSourceRB.matches("tutors")) {
                setTutorsRB();
        }
    }

    @FXML
    void exitSelected(ActionEvent event) {
        exit();
    }

    @FXML
    void logoutSelected(ActionEvent event) {
        logout();
    }

    @FXML
    void generateByStudentClicked(ActionEvent event) {

    }

    @FXML
    void generateByTutorClicked(ActionEvent event) {

    }

    @FXML
    void modifyAppointmentButtonClicked(ActionEvent event) {
        loadModifyAppointment(event);
        setCalendarSourceRB();
    }

    @FXML
    void modifyPersonButtonClicked(ActionEvent event) {
        loadModifyStudent(event);
        loadModifyTutor(event);
    }

    @FXML
    void monthViewSelected(ActionEvent event) {
        setMonthRB();
//        SetTableView.setScheduleByTimeFrame(getAllAppointments(event), calendarTableView, startTimeCol, endTimeCol, dateCol,
//                studentCol, tutorCol);
    }
    @Override
    protected void setMonthRB() {
        calendarSourceRB = "month";
        monthRB.setSelected(true);

        SetTableView.setScheduleByTimeFrame(getAllAppointments(calendarSourceRB), calendarTableView, startTimeCol, endTimeCol, dateCol,
//        SetTableView.setScheduleByTimeFrame(getAllAppointments(event), calendarTableView, startTimeCol, endTimeCol, dateCol,
                studentCol, tutorCol);
    }

    @FXML
    void runTestsSelected(ActionEvent event) {
        tests.Tests.runAllTests();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, tests.Tests.testsPassed + " out of " + tests.Tests.testsRun + " " +
                "tests passed. \n");

        alert.showAndWait();
    }

    @FXML
    void scheduleByAdminSelected(ActionEvent event) {

    }

    @FXML
    void scheduleByStudentSelected(ActionEvent event) {
        switchScreen.toStudentSchedule(scheduleByStudentMenuItem);
    }

    @FXML
    void scheduleByTutorSelected(ActionEvent event) {
        switchScreen.toTutorSchedule(scheduleByTutorMenuItem);
    }

    @FXML
    void scheduleNewAppointmentButtonClicked(ActionEvent event) {
        loadCreateNewAppointment(event);
    }

    @FXML
    void searchButtonClicked(ActionEvent event) {
        search();
    }

    @FXML
    void updateQuoteSelected(ActionEvent event) {

    }
    @FXML
    void studentBillSelected(ActionEvent event) {

    }

    @FXML
    void summariesByStudentSelected(ActionEvent event) {

    }

    @FXML
    void totalEarningsSelected(ActionEvent event) {

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
    void weekViewSelected(ActionEvent event) { // todo exclude past appts?
//        setCalendarSourceRB();
        setWeekRB();
//        SetTableView.setScheduleByTimeFrame(getAllAppointments(event), calendarTableView, startTimeCol, endTimeCol, dateCol,
//                studentCol, tutorCol);
    }

    @Override
    protected void setWeekRB() {
        calendarSourceRB = "week";
        weekRB.setSelected(true);

        SetTableView.setScheduleByTimeFrame(getAllAppointments(calendarSourceRB), calendarTableView, startTimeCol, endTimeCol, dateCol,
//        SetTableView.setScheduleByTimeFrame(getAllAppointments(event), calendarTableView, startTimeCol, endTimeCol, dateCol,
                studentCol, tutorCol);
    }
    @FXML
    void studentsRBselected(ActionEvent event) {
        setStudentsRB();
    }

    @FXML
    void tutorsRBselected(ActionEvent event) {
        setTutorsRB();
    }

    Admin admin; //  made public static
    AdminDAO adminDAO = new AdminDAO(DBConnection.connection);
    SwitchScreen switchScreen = new SwitchScreen();
    Alerts alerts = new Alerts();

    // -----   SEARCH   ----- //
    @Override
    protected void search() {
        if (Validator.hasSearchText(searchNameText)) {
            if (studentsRB.isSelected()) {
                Search.adminSearchStudents(searchNameText, personTableView, personTableCol);
            }
            if (tutorsRB.isSelected()) {
                Search.searchTutors(searchNameText, personTableView, personTableCol);
            }
        }
    }


    public ObservableList<Appointment> getAllAppointments(String timeframe) {
        String startDate = getStartDate(timeframe);
        String endDate = getEndDate(timeframe);

        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
        return appointmentDAO.getApptBasicInfoForTimeFrame(startDate, endDate); // get month basic info using start and end dates
    }

    // -----   CREATE USER OBJECT   ----- // used to id privileges
    @Override
    public Person createUserPerson(long id) {
        admin = adminDAO.getBasicData(id);
        MainController.currentUser = admin.getFullName();
        MainController.currentUserType = "admin";
        return admin;
    }

    // -----   SET PERSON TABLE VIEW   ----- //
    @Override
    protected void setPersonTableView() { // changed from public
        if (personSourceRB.matches("students")) {
            setStudentsRB();

        } else if (personSourceRB.matches("tutors")) {
            setTutorsRB();
        }
    }

    // -----   SET INFO BASED ON RB   ----- //
    @Override
    protected void setButtonVisibility(Person person) {
        adminsRB.setVisible(false);
        scheduleByAdminMenuItem.setVisible(false);
    }

    public void setTutorsRB() {
        personSourceRB = "tutors";
        tutorsRB.setSelected(true);
        PersonListLabel.setText("Tutors");
        personTableCol.setText("Tutors");
        searchNameText.setPromptText("Tutor Name");
        SetTableView.setAllTutorsTableView(personTableView, personTableCol);
    }

    // -----   LOAD VIEW PERSON   ----- //
    @Override
    protected void loadViewPersonForm(ActionEvent event) {
        loadViewStudent(event);
        loadViewTutor(event);
    }

    @Override
    protected void loadViewStudent(ActionEvent event) {
        if (studentsRB.isSelected()) {
            try {
                LoadForm.loadAdminViewStudent(event, getSelectedPerson().getPersonId()); // use personId to load form
                personSourceRB = "students";

            } catch (NullPointerException e) {
                Alerts.rowNotSelectedToViewError("student");
            }
        }
    }

    protected void loadViewTutor(ActionEvent event) {
        if (tutorsRB.isSelected()) {
            try {
                LoadForm.loadAdminViewTutor(event, getSelectedPerson().getPersonId());
                personSourceRB = "tutors";
            }
            catch (NullPointerException e) {
                Alerts.rowNotSelectedToViewError("tutor");
            }
        }
    }

    // -----   LOAD MODIFY  ----- //
    protected void loadModifyStudent(ActionEvent event) {
        if (studentsRB.isSelected()) {
            try {
                LoadForm.loadAdminModifyStudent(event, getSelectedPerson().getPersonId()); // use personId to load form
                personSourceRB = "students";
            } catch (NullPointerException e) {
                Alerts.rowNotSelectedToModifyError("student");
            }
        }
    }

    protected void loadModifyTutor(ActionEvent event) {
        if (tutorsRB.isSelected()) {
            try {
                LoadForm.loadAdminModifyTutor(event, getSelectedPerson().getPersonId()); // use personId to load form
                personSourceRB = "tutors";
            }
            catch (NullPointerException e) {
                Alerts.rowNotSelectedToModifyError("tutor");
            }
        }
    }

    protected void loadModifyAppointment(ActionEvent event) {
        setCalendarSourceRB();
        try {
            LoadForm.loadAdminModifyAppointment(event, getSelectedAppointment().getAppointmentId()); // use apptID to load form
        } catch (NullPointerException e) {
            Alerts.rowNotSelectedToViewError("appointment");
        }
    }
    // -----   CREATE   ----- //
    protected void loadCreateNewStudent(ActionEvent event) {
        if (studentsRB.isSelected()) {
            switchScreen.toAddStudent(event);
        }
    }

    protected void loadCreateNewTutor(ActionEvent event) {
        if (tutorsRB.isSelected()) {
            switchScreen.toAddTutor(event);
        }
    }

    protected void loadCreateNewAppointment(ActionEvent event) {
        setCalendarSourceRB();
        switchScreen.toAdminAddAppointment(event);
    }


    // -----   DELETE  ----- //
    protected void deletePerson(ActionEvent event) {
        deleteStudent();
        deleteTutor();
    }

    protected void deleteStudent() {
        if (studentsRB.isSelected()) {
            long studentPersonId = getSelectedPerson().getPersonId();
            String studentName = getSelectedPerson().getFullName();
            alerts.deleteStudentConfirmation(studentPersonId, studentName);
        }
    }

    protected void deleteTutor() {
        if (tutorsRB.isSelected()) {
            long tutorPersonId = getSelectedPerson().getPersonId();
            String tutorName = getSelectedPerson().getFullName();
            alerts.deleteTutorConfirmation(tutorPersonId, tutorName);
        }
    }

    protected void deleteAppointment() {
        long appointmentId = getSelectedAppointment().getAppointmentId();
        String name = getSelectedAppointment().getStudentName();
        alerts.deleteAppointmentConfirmation(appointmentId, name);
    }
}