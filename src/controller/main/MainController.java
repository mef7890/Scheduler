package controller.main;

import controller.etc.LoginController;
import controller.schedule.ScheduleController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;
import utilities.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public abstract class MainController implements Initializable {

    String currentMonth = LocalDate.now().getMonth().toString();
    protected Stage stage;
    protected Parent scene;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem runTestsMenuItem;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem scheduleByTutorMenuItem;

    @FXML
    private MenuItem scheduleByStudentMenuItem;

    @FXML
    private MenuItem scheduleByAdminMenuItem;

    @FXML
    private MenuItem summariesForStudentMenuItem;

    @FXML
    private MenuItem studentBillMenuItem;

    @FXML
    private MenuItem tutorEarningsMenuItem;

    @FXML
    private Label welcomeMessage;

    @FXML
    private Label PersonListLabel;

    @FXML
    private RadioButton tutorsRB;

    @FXML
    private RadioButton studentsRB;

    @FXML
    private RadioButton adminRB;

    @FXML
    private ToggleGroup personTG;

    @FXML
    private TextField searchNameText;

    @FXML
    private TableView<Person> personTableView;

    @FXML
    private TableColumn<Person, String> personTableCol;

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
    void adminRBselected(ActionEvent event) {

    }

    @FXML
    void createNewPersonClicked(ActionEvent event) {

    }

    @FXML
    void deleteAppointmentButtonClicked(ActionEvent event) {

    }

    @FXML
    void deletePersonButtonClicked(ActionEvent event) {
    }


    @FXML
    void exitSelected(ActionEvent event) {

    }

    @FXML
    void logoutSelected(ActionEvent event) {

    }

    @FXML
    void generateByStudentClicked(ActionEvent event) {

    }

    @FXML
    void generateByTutorClicked(ActionEvent event) {

    }

    @FXML
    void modifyAppointmentButtonClicked(ActionEvent event) {

    }

    @FXML
    void modifyPersonButtonClicked(ActionEvent event) {

    }

    @FXML
    void monthViewSelected(ActionEvent event) {

    }

    @FXML
    void runTestsSelected(ActionEvent event) {

    }

    @FXML
    void scheduleByAdminSelected(ActionEvent event) {

    }

    @FXML
    void scheduleByStudentSelected(ActionEvent event) {

    }

    @FXML
    void scheduleByTutorSelected(ActionEvent event) {

    }

    @FXML
    void scheduleNewAppointmentButtonClicked(ActionEvent event) {

    }

    @FXML
    void searchButtonClicked(ActionEvent event) {

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

    }

    @FXML
    void viewPersonButtonClicked(ActionEvent event) {

    }

    @FXML
    void weekViewSelected(ActionEvent event) {

    }

    @FXML
    void studentsRBselected(ActionEvent event) {

    }

    @FXML
    void tutorsRBselected(ActionEvent event) {
    }

    public static String currentUser; // set in initialize,
    public static String currentUserType;
    public static LocalDateTime actionTimestamp;
    protected static String personSourceRB = "tutors";
    protected static String calendarSourceRB = "week";

    public abstract Person createUserPerson(long id);
    protected abstract void setButtonVisibility(Person person);
    protected abstract void loadViewPersonForm(ActionEvent event);
    protected abstract void loadViewStudent(ActionEvent event); // used in loadViewPersonForm
    protected abstract void setPersonTableView(); // changed from public
    protected abstract void search();

    protected abstract void setWeekRB();
    protected abstract void setMonthRB();

    protected void exit() {
        DBConnection.closeConnection();
        stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }

    protected void logout() {
        DBConnection.closeConnection();
        try {
            scene = FXMLLoader.load(getClass().getResource("/view/etc/Login.fxml"));
            stage = (Stage) menuBar.getScene().getWindow();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Person getSelectedPerson() {
        return personTableView.getSelectionModel().getSelectedItem();
    }

    protected Appointment getSelectedAppointment() {
        return calendarTableView.getSelectionModel().getSelectedItem();
    }


    public void setStudentsRB() {
        personSourceRB = "students";

        studentsRB.setSelected(true);
        PersonListLabel.setText("Students");
        personTableCol.setText("Students");
        searchNameText.setPromptText("Student Name");
        SetTableView.setAllStudentsTableView(personTableView, personTableCol);
    }


    public void setPersonRB() {
        if (personSourceRB.matches("students")) {
            studentsRB.setSelected(true);
        }
        if (personSourceRB.matches("tutors")) {
            tutorsRB.setSelected(true);
        }
        setPersonTableView();
    }

    public void setWelcomeMessage(String user) { // resize text size to fit within space

        double MAX_TEXT_WIDTH = 375; //maximum width of the text/label

        double defaultFontSize = 50; //default (nonscaled) font size of the text/label
        Font defaultFont = Font.font(defaultFontSize);

        TextField tf = new TextField("Welcome, " + user + "!"); //welcome label
        welcomeMessage.setFont(defaultFont);
        welcomeMessage.textProperty().addListener((observable, oldValue, newValue) -> {
            //create temp Text object with the same text as the label
            //and measure its width using default label font size
            Text tempText = new Text(newValue);
            tempText.setFont(defaultFont);

            double textWidth = tempText.getLayoutBounds().getWidth();
            //check if text width is smaller than maximum width allowed
            if (textWidth <= MAX_TEXT_WIDTH) {
                welcomeMessage.setFont(defaultFont);
            } else {
                //and if it isn't, calculate new font size,
                // so that label text width matches MAX_TEXT_WIDTH
                double newFontSize = (defaultFontSize * MAX_TEXT_WIDTH) / textWidth;
                welcomeMessage.setFont(Font.font(defaultFont.getFamily(), newFontSize));
            }
        });

        welcomeMessage.textProperty().bind(tf.textProperty());
    }

    protected void setCalendarSourceRB() {
        if (weekRB.isSelected()) {
            calendarSourceRB = "week";
        }
        if (monthRB.isSelected()) {
            calendarSourceRB = "month";
        }
    }

    public static void loadMainForUser(ActionEvent event) {
        if (MainController.currentUserType.matches("admin")) {
            new LoadForm().loadAdminMainView(event);
        } else if (MainController.currentUserType.matches("tutor")) {
            new LoadForm().loadTutorMainView(event);
        }
    }

    protected void viewAppointment(ActionEvent event) {
        setCalendarSourceRB(); // set marker to remember which view to come back to
        try {
            LoadForm.loadViewAppointment(event, getSelectedAppointment().getAppointmentId());
        } catch (NullPointerException e) {
            Alerts.rowNotSelectedToViewError("appointment");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Person person = createUserPerson(LoginController.returnedUserId); // used as name for log records and for welcome message

        setButtonVisibility(person);
        setWelcomeMessage(person.getFirstName());  // uses current user
        calendarTableView.setPlaceholder(new Label("There are no appointments in this time frame"));

//        checkForUpcomingAppointments();

        setPersonRB();
        setCalendarSourceRB();
        setCalendarTableView();

    }

    // only used in tutor main at present
    protected void setCalendar(ActionEvent event, ObservableList<Appointment> appointments) {
        SetTableView.setScheduleByTimeFrame(appointments, calendarTableView, startTimeCol, endTimeCol, dateCol,
                studentCol, tutorCol);
    }

    // try to combine with setCalendar?
    protected void setCalendarTableView() {
        if (calendarSourceRB.matches("week")) {
            setWeekRB();

        } else if (calendarSourceRB.matches("month")){
            setMonthRB();
        }
    }

    protected String getStartDate(String timeframe) {
        String startDate = "";
        if (timeframe.matches("month")) {
            startDate = ScheduleController.setStartDate(currentMonth);
            monthRB.setSelected(true); // todo del?
        } else if (timeframe.matches("week")) {
            startDate = ScheduleController.setStartDate("THIS WEEK");
            weekRB.setSelected(true); // todo del?
        }
        return startDate;
    }

    protected String getEndDate(String timeframe) {
        String endDate = "";
        if (timeframe.matches("month")) {
            endDate = ScheduleController.setEndDate(currentMonth);

        } else if (timeframe.matches("week")) {
            endDate = ScheduleController.setEndDate("THIS WEEK");

        }
        return endDate;
    }



    // todo fix and include
//    public void checkForUpcomingAppointments() {
//        System.out.println("IN MAIN ALERT CHECK");
//        LocalDateTime now = LocalDateTime.now(); // get time now
//        LocalDateTime endWindow = now.plusMinutes(15); // add 15 min to now
//        System.out.println("Current user = " + CurrentUser.getCurrentUsersName());
//
//        for (Appointment a : Appointment.getAllAppointments()) {
//            if (a.getAppointmentStartDT().isBefore(endWindow) && // if apt is after now and within 15 minutes....
//                    a.getAppointmentStartDT().isAfter(now) &&
//                    (a.getTutorName().equals(CurrentUser.getCurrentUsersName()))
//            ) {
//                Alerts.upcomingAppointmentAlert(a.getTuteeName(), a.getAppointmentStartTime(), a.getPlatformName(),
//                        a.getTuteeHandle()); //...display alert
//            }
//        }
//    }
//

}
