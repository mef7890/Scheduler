package controller.main;

import DAO.OwnerDAO;
import controller.admin.ViewAdminController;
import controller.student.AdminViewStudentController;
import controller.tutor.OwnerViewTutorController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import utilities.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class OwnerMainController extends AdminMainController {

    OwnerDAO ownerDAO = new OwnerDAO(DBConnection.connection);
    public static Owner owner; // made public static


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
    private RadioButton studentsRB;

    @FXML
    private ToggleGroup personTG;

    @FXML
    private RadioButton adminsRB;

    @FXML
    private RadioButton tutorsRB;

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
    private TableColumn<Student, String> studentCol;

    @FXML
    private TableColumn<Tutor, String> tutorCol;

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
    void scheduleNewAppointmentButtonClicked(ActionEvent event) {

    }

    @FXML
    void searchButtonClicked(ActionEvent event) {
        search();
    }

    @FXML
    void adminRBselected(ActionEvent event) {
        setAdminsRB();
    }

    @FXML
    void studentsRBselected(ActionEvent event) {
        setStudentsRB();
    }

    @FXML
    void tutorsRBselected(ActionEvent event) {
        setTutorsRB();

    }

    @FXML
    void viewAppointmentButtonClicked(ActionEvent event) {

    }

    @FXML
    void viewPersonButtonClicked(ActionEvent event) {
        loadViewPersonForm(event);

    }

    @FXML
    void weekViewSelected(ActionEvent event) {

    }

    // -----   Set Table View Based On Selected RB   ----- //
    @Override
    protected void setPersonTableView() { // changed from public
        if (personTG.getSelectedToggle().equals(this.studentsRB)) {
            SetTableView.setAllStudentsTableView(personTableView, personTableCol);
        }
        if (personTG.getSelectedToggle().equals(this.tutorsRB)) {
            SetTableView.setAllTutorsTableView(personTableView, personTableCol);
        }
        if (personTG.getSelectedToggle().equals(this.adminsRB)) { //
            SetTableView.setAllAdminsTableView(personTableView, personTableCol);
        }
    }

    @Override // todo made method in testMainController, undone
    public Person createUserPerson(long id) {
        System.out.println("Setting basic owner info...");
        owner = ownerDAO.getBasicData(id);
        MainController.currentUser = owner.getFullName();
        return owner;
    }

    public void setAdminsRB() {
        adminsRB.setSelected(true);
        PersonListLabel.setText("Admins");
        personTableCol.setText("Admins");
        searchNameText.setPromptText("Admin Name");
        setPersonTableView();
    }

    @Override
    public void setButtonVisibility(Person person) { // todo rename to setPersonSection
        setTutorsRB(); // todo setAdminsRB?
        }

    @Override
    protected void search() {
        if (Validator.hasSearchText(searchNameText)) {
            Search.adminSearchStudents(searchNameText, personTableView, personTableCol);
            Search.searchTutors(searchNameText, personTableView, personTableCol);
            Search.searchAdmins(searchNameText, personTableView, personTableCol);
        }
    }

    public void searchAdmins() {
        if (personSourceRB.equalsIgnoreCase("admins")) {
            Search.searchAdmins(searchNameText, personTableView, personTableCol);
        }
    }

    @Override
    protected void loadViewPersonForm(ActionEvent event) {
        loadViewStudent(event);
        loadViewTutor(event);
        loadViewAdmin(event);
    }

    protected void loadViewAdmin(ActionEvent event) {
        if (adminsRB.isSelected()) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/admin/ViewAdmin.fxml"));
                loader.load();

                ViewAdminController ownerViewAdminController = loader.getController();

                long adminId = personTableView.getSelectionModel().getSelectedItem().getPersonId(); // get
                System.out.println("Admin id  = " + adminId); // todo delete

                ownerViewAdminController.populateAdminFields(adminId);// send info to populate admin
                // viewer fields

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
                System.out.println("Set Admin Fields: SUCCESSFUL");


            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Set Admin Fields: FAILED");
            } catch (NullPointerException e) {
                Alerts.rowNotSelectedToViewError("admin");
            }
        }
    }

    protected void loadViewTutor(ActionEvent event) {
        if (tutorsRB.isSelected()) {

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/tutor/OwnerViewTutor.fxml"));
                loader.load();

                OwnerViewTutorController ownerViewTutorController = loader.getController();

                long tutorId = personTableView.getSelectionModel().getSelectedItem().getPersonId(); // get
                System.out.println("Tutor id  = " + tutorId); // todo delete

                ownerViewTutorController.populateTutorFields(tutorId);
//                adminViewTutorController.populateTutorFields(tutor.getPersonId()); // send info to populate tutor
                // viewer fields


                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
                System.out.println("Set Tutor Fields: SUCCESSFUL");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Set Tutor Fields: FAILED");
            } catch (NullPointerException e) {
                Alerts.rowNotSelectedToViewError("tutor");
            }
        }
    }

    @Override
    protected void loadViewStudent(ActionEvent event) {
        if (studentsRB.isSelected()) {

            try {
                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(getClass().getResource("/view/student/AdminViewStudent.fxml"));
                loader.load();

                AdminViewStudentController adminViewStudentController = loader.getController();

                long studentId = personTableView.getSelectionModel().getSelectedItem().getPersonId(); // get
                System.out.println("Student id  = " + studentId); // todo delete

//                StudentDAO studentDAO = new StudentDAO(MainController.connection);
//                Student student = studentDAO.findById(studentId); // create student obj from db by id

                adminViewStudentController.populateFormFields(studentId);// send info to populate fields
//            if (tutorsRB.isSelected()) {
//                loader.setLocation(getClass().getResource("/view/OwnerViewTutor.fxml"));
//                loader.load();
//
//                AdminViewTutorController adminViewTutorController = loader.getController();
//
//                long tutorId = personTableView.getSelectionModel().getSelectedItem().getPersonId(); // get
//                System.out.println("Tutor id  = " + tutorId); // todo delete
//
//                TutorDAO tutorDAO = new TutorDAO(MainController.connection);
//                Tutor2 tutor = tutorDAO.findById(tutorId); // create tutor obj from db by id
//
//                adminViewTutorController.populateTutorFields(tutor.getPersonId()); // send info to populate tutor
//                // viewer fields
//            }

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
                System.out.println("Set Student Fields: SUCCESSFUL");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Set Student Fields: FAILED");
            } catch (NullPointerException e) {
                Alerts.rowNotSelectedToViewError("student");
            }
        }
    }
}


