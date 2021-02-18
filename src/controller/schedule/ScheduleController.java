package controller.schedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Appointment;
import model.Person;
import utilities.LoadForm;
import utilities.SetTableView;

import java.net.URL;
import java.time.*;
import java.util.Calendar;
import java.util.ResourceBundle;

public abstract class ScheduleController implements Initializable {

    @FXML
    private ComboBox<String> timeFrameComboBox;

    @FXML
    private TableView<Person> personTableView;

    @FXML
    private TableColumn<Person, String> personTableCol;

    @FXML
    private Label scheduleLabel;

    @FXML
    private Label searchHeader;

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
    private TextField searchNameText;

    @FXML
    void closeButtonClicked(ActionEvent event)  {

    }

    protected void close(ActionEvent event) {
        new LoadForm().loadAdminMainView(event);
    }

    public ObservableList<String> timeFrameOptions = FXCollections.observableArrayList("This Week", "January", "February",
            "March",
        "April", "May", "June", "July", "August", "September", "October", "November", "December", "All Time"); // todo all time

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

//    protected String startDate;
//    protected String endDate;
//    protected String year;

    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    protected static boolean isLeapYear(int year) { // made static
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    public static String setSundayDate() {
        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return LocalDate.now().toString();
        } else {
            int todayValue = today.getDayOfWeek().getValue();
            return today.minusDays(todayValue).toString();

        }
    }

    public static String setSaturdayDate() {

        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return today.plusDays(6).toString();
        }
        if (!today.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            int todayValue = today.getDayOfWeek().getValue();
            return today.plusDays(6 - todayValue).toString();
        } else {
            return today.toString(); // if today is Saturday, return current date
        }
    }

    public static String setStartDate(String timeFrame) { // made public bad
        int currentYear = LocalDateTime.now().getYear();
        String startDate = "";
        switch (timeFrame.toUpperCase()) {
            case "THIS WEEK":
                startDate = setSundayDate();
                break;
            case "JANUARY":
                startDate = LocalDate.of(currentYear, 1, 1).toString();
                break;
            case "FEBRUARY": //  would have been easier to just make my query < 1st of next month
                startDate = LocalDate.of(currentYear, 2, 1).toString();
                break;
            case "MARCH":
                startDate = LocalDate.of(currentYear, 3, 1).toString();
                break;
            case "APRIL":
                startDate = LocalDate.of(currentYear, 4, 1).toString();
                break;
            case "MAY":
                startDate = LocalDate.of(currentYear, 5, 1).toString();
                break;
            case "JUNE":
                startDate = LocalDate.of(currentYear, 6, 1).toString();
                break;
            case "JULY":
                startDate = LocalDate.of(currentYear, 7, 1).toString();
                break;
            case "AUGUST":
                startDate = LocalDate.of(currentYear, 8, 1).toString();
                break;
            case "SEPTEMBER":
                startDate = LocalDate.of(currentYear, 9, 1).toString();
                break;
            case "OCTOBER":
                startDate = LocalDate.of(currentYear, 10, 1).toString();
                break;
            case "NOVEMBER":
                startDate = LocalDate.of(currentYear, 11, 1).toString();
                break;
            case "DECEMBER":
                startDate = LocalDate.of(currentYear, 12, 1).toString();
                break;
            case "ALL TIME":
                startDate = LocalDate.of(1900, 1, 1).toString();
                break;
        }
        return startDate;
    }

    public static String setEndDate(String timeFrame) { // made public static
        int currentYear = LocalDateTime.now().getYear();
        String endDate = "";
            switch (timeFrame.toUpperCase()) {
                case "THIS WEEK":
                    endDate = setSaturdayDate();
                    break;
                case "JANUARY":
                    endDate = LocalDate.of(currentYear, 1, Month.JANUARY.maxLength()).toString();
                    break;
                case "FEBRUARY": // would have been easier to just make my query < 1st of next month
                    endDate = LocalDate.of(currentYear, 3, 1).toString(); // todo new
//                    endDate = LocalDate.of(currentYear, 2, Month.FEBRUARY.length(isLeapYear(currentYear))).toString(); // check if
                    // current year is a leap year
                    break;
                case "MARCH":
                    endDate = LocalDate.of(currentYear, 3, Month.MARCH.maxLength()).toString();
                    break;
                case "APRIL":
                    endDate = LocalDate.of(currentYear, 4, Month.APRIL.maxLength()).toString();
                    break;
                case "MAY":
                    endDate = LocalDate.of(currentYear, 5, Month.MAY.maxLength()).toString();
                    break;
                case "JUNE":
                    endDate = LocalDate.of(currentYear, 6, Month.JUNE.maxLength()).toString();
                    break;
                case "JULY":
                    endDate = LocalDate.of(currentYear, 7, Month.JULY.maxLength()).toString();
                    break;
                case "AUGUST":
                    endDate = LocalDate.of(currentYear, 8, Month.AUGUST.maxLength()).toString();
                    break;
                case "SEPTEMBER":
                    endDate = LocalDate.of(currentYear, 9, Month.SEPTEMBER.maxLength()).toString();
                    break;
                case "OCTOBER":
                    endDate = LocalDate.of(currentYear, 10, Month.OCTOBER.maxLength()).toString();
                    break;
                case "NOVEMBER":
                    endDate = LocalDate.of(currentYear, 11, Month.NOVEMBER.maxLength()).toString();
                    break;
                case "DECEMBER":
                    endDate = LocalDate.of(currentYear, 12, Month.DECEMBER.maxLength()).toString();
                    break;
                case "ALL TIME":
                    endDate = LocalDate.now().plusYears(1).toString();
                    break;
        }
        return endDate;
    }

    protected Person getSelectedPerson() {
        return personTableView.getSelectionModel().getSelectedItem();
    }

//    protected abstract ObservableList<Appointment2> getAppointments(long personId); // get observable list of appts
    protected abstract ObservableList<Appointment> getAppointments(); // get observable list of appts

    protected void displaySchedule() {
        appointments = getAppointments();
        scheduleLabel.setText("Schedule for " + getSelectedPerson().getFullName());
        calendarTableView.setPlaceholder(new Label("There are no appointments in this time frame"));
        SetTableView.setScheduleByTimeFrame(appointments, calendarTableView, startTimeCol, endTimeCol, dateCol, studentCol,
                    tutorCol);
    }

}
