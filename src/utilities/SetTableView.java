package utilities;

import DAO.*;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Person;

import java.time.LocalDate;
import java.time.LocalTime;

public class SetTableView {

    // -----   SET TABLE VIEW WITH ALL STUDENTS   ----- //
    public static void setAllStudentsTableView(TableView<Person> tableView, TableColumn<Person, String> tableColumn) {
        StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
        tableView.getSelectionModel().clearSelection();
        tableView.setItems(studentDAO.getAllStudents());
        tableColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    }

    // -----   SET TABLE VIEW WITH ALL TUTORS   ----- //
    public static void setAllTutorsTableView(TableView<Person> tableView, TableColumn<Person, String> tableColumn) {
        TutorDAO tutorDAO = new TutorDAO(DBConnection.connection);
        tableView.getSelectionModel().clearSelection();
        tableView.setItems(tutorDAO.getAllTutors());
        tableColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    }

    // -----   SET TABLE VIEW WITH ALL ADMINS   ----- //
    public static void setAllAdminsTableView(TableView<Person> tableView, TableColumn<Person, String> tableColumn) {
        AdminDAO adminDAO = new AdminDAO(DBConnection.connection);
        tableView.getSelectionModel().clearSelection();
        tableView.setItems(adminDAO.getAllAdmins());
        tableColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    }

    // -----   SET TABLE VIEW WITH STUDENTS ASSOCIATED WITH A PARTICULAR TUTOR  ----- //
    public static void setStudentsByTutorTableView(long tutorId, TableView<Person> tableView,
                                                   TableColumn<Person, String> tableColumn) {
        StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
        tableView.getSelectionModel().clearSelection();
        tableView.setItems(studentDAO.getStudentsByTutor(tutorId));
        tableColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    }

    public static void setModifyApptsStudentsTableView(String name, TableView<Person> tableView,
                                                       TableColumn<Person, String> tableColumn) {
        setAllStudentsTableView(tableView, tableColumn); // set the table view with all students

        // scroll to and select student
        int studentIndex = getStudentIndex(name);
        tableView.scrollTo(studentIndex);
        tableView.getSelectionModel().select(studentIndex);
    }

    public static void setModifyApptsTutorsTableView(String name, TableView<Person> tableView,
                                                  TableColumn<Person, String> tableColumn) {
        setAllTutorsTableView(tableView, tableColumn); // set the table view with all tutors

        // scroll to and select tutor
        int tutorIndex = getTutorIndex(name);
        tableView.scrollTo(tutorIndex);
        tableView.getSelectionModel().select(tutorIndex);
    }

    // -----   SET TABLE VIEW WITH SEARCH RESULTS   ----- //
    public static void setSearchResults(ObservableList<Person> searchResults, TableView<Person> tableView) {
        tableView.getSelectionModel().clearSelection();
        tableView.setItems(searchResults);
    }

    // -----   SET TABLE VIEW WITH APPOINTMENTS WITHIN TIME FRAME   ----- // oal used to specify person limitations
    public static void setScheduleByTimeFrame(ObservableList<Appointment> schedule,
                                              TableView<Appointment> tableView,
                                              TableColumn<Appointment, LocalTime>  startTimeColumn,
                                              TableColumn<Appointment, LocalTime>  endTimeColumn,
                                              TableColumn<Appointment, LocalDate> dateColumn,
                                              TableColumn<Appointment, String> studentNameColumn,
                                              TableColumn<Appointment, String> tutorNameColumn) {

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ee, MM dd, yyyy");// todo format date?

        try {
            tableView.getSelectionModel().clearSelection();
            tableView.setItems(schedule); // observable list of appointments
        } catch (NullPointerException e) {
            tableView.setItems(schedule); // observable list of appointments
        }
        // moved set items up to try catch block
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("apptStartTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("apptEndTime"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("apptDate"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        tutorNameColumn.setCellValueFactory(new PropertyValueFactory<>("tutorName"));
    }

    protected static int getStudentIndex(String name) { // used to get index of student so I can scroll there
        StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
        int index = 0;
        for (Person person : studentDAO.getAllStudents()) {
            if (person.getFullName().matches(name)) {
                break;
            }
            index++;
        }
        return index;
    }

    protected static int getTutorIndex(String name) { // used to get index of tutor so I can scroll there
        TutorDAO tutorDAO = new TutorDAO(DBConnection.connection);
        int index = 0;
        for (Person person : tutorDAO.getAllTutors()) { // todo would it be better to store and reuse?
            if (person.getFullName().matches(name)) {
                break;
            }
            index++;
        }
        return index;
    }

}
