package utilities;

import DAO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Person;

public class Search {


    // -----   SEARCH STUDENTS, ADMIN ACCESS TO ALL STUDENTS   ----- //
    public static void adminSearchStudents(TextField searchNameText, TableView<Person> tableView,
                                           TableColumn<Person, String> tableColumn) {
        String nameToSearch = searchNameText.getText();
        ObservableList<Person> searchResult = adminSearchForStudent(nameToSearch);
        if (!searchResult.isEmpty()) {
            SetTableView.setSearchResults(searchResult, tableView); // set tv with matching items
            tableView.getSelectionModel().select(0); // highlight returned result

        } else {
            SetTableView.setAllStudentsTableView(tableView,tableColumn); // set tv with all students
            Alerts.personNotFoundAlert();
        }
    }


    // -----   SEARCH STUDENTS, ACCESS TO STUDENTS SEEN BY TUTOR   ----- //
    public static void tutorSearchStudents(long tutorId, TextField searchNameText, TableView<Person> tableView,
                                           TableColumn<Person, String> tableColumn) {
        String nameToSearch = searchNameText.getText();
        ObservableList<Person> searchResult = tutorSearchForStudent(tutorId, nameToSearch);
        if (!searchResult.isEmpty()) {
            SetTableView.setSearchResults(searchResult, tableView); // set tv with matching items
            tableView.getSelectionModel().select(0); // highlight returned result

        } else {
            SetTableView.setStudentsByTutorTableView(tutorId, tableView,tableColumn); // set tv with all students
            Alerts.personNotFoundAlert();
        }
    }

    // -----   CREATE OBSERVABLE LIST WITH STUDENT SEARCH RESULTS   ----- //
    public static ObservableList<Person> adminSearchForStudent(String student) {
        StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
        ObservableList<Person> foundStudents = FXCollections.observableArrayList();
        for (Person person : studentDAO.getAllStudents()) {
            if (person.getFullName().toUpperCase().contains(student.toUpperCase())) {
                foundStudents.add(person);
            }
        }
        return foundStudents;
    }

    // -----   CREATE OBSERVABLE LIST WITH STUDENT SEARCH RESULTS   ----- //
    public static ObservableList<Person> tutorSearchForStudent(long tutorId, String student) {
        StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
        ObservableList<Person> foundStudents = FXCollections.observableArrayList();
        for (Person person : studentDAO.getStudentsByTutor(tutorId)) {
            if (person.getFullName().toUpperCase().contains(student.toUpperCase())) {
                foundStudents.add(person);
            }
        }
        return foundStudents;
    }

    // -----   SEARCH TUTORS   ----- //
    public static void searchTutors(TextField searchNameText, TableView<Person> tableView,
                                    TableColumn<Person, String> tableColumn) {
        String nameToSearch = searchNameText.getText();
        ObservableList<Person> searchResult = searchForTutors(nameToSearch);
        if (!searchResult.isEmpty()) {
            SetTableView.setSearchResults(searchResult, tableView); // set tv with matching items
            tableView.getSelectionModel().select(0); // should highlight returned result
        } else {
            SetTableView.setAllTutorsTableView(tableView,tableColumn); // set tv with all students
            Alerts.personNotFoundAlert();
        }
    }

    // -----   CREATE OBSERVABLE LIST WITH TUTOR SEARCH RESULTS   ----- //
    public static ObservableList<Person> searchForTutors(String student) {
        TutorDAO tutorDAO = new TutorDAO(DBConnection.connection);
        ObservableList<Person> foundStudents = FXCollections.observableArrayList();
        for (Person person : (tutorDAO.getAllTutors())) {
            if (person.getFullName().toUpperCase().contains(student.toUpperCase())) {
                foundStudents.add(person);
            }
        }
        return foundStudents;
    }

    // -----   CREATE OBSERVABLE LIST WITH TUTOR SEARCH RESULTS   ----- //
    public static ObservableList<Person> searchForAdmins(String tutor) {
        ObservableList<Person> foundAdmins = FXCollections.observableArrayList();
        AdminDAO adminDAO = new AdminDAO(DBConnection.connection);
        for (Person person : adminDAO.getAllAdmins()) {
            if (person.getFullName().toUpperCase().contains(tutor.toUpperCase())) {
                foundAdmins.add(person);
            }
        }
        return foundAdmins;
    }

    public static void searchAdmins(TextField searchNameText, TableView<Person> tableView,
                             TableColumn<Person, String> tableColumn) {
        String nameToSearch = searchNameText.getText();
        ObservableList<Person> searchResult = searchForAdmins(nameToSearch);
        if (!searchResult.isEmpty()) {
            SetTableView.setSearchResults(searchResult, tableView);
            tableView.getSelectionModel().select(0); // should highlight returned result
        } else {
            SetTableView.setAllAdminsTableView(tableView, tableColumn);
            Alerts.personNotFoundAlert();
        }
    }
}


