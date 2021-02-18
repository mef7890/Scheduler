package controller.student;

import DAO.AppointmentDAO;
import DAO.StudentDAO;
import controller.main.TutorMainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Student;
import utilities.DBConnection;
import utilities.LoadForm;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TutorViewStudentController implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField gradeTextField;

    @FXML
    private TextField platformTextField;

    @FXML
    private TextField handleTextField;

    @FXML
    private TextField nextAptTextField;

    @FXML
    private TableView<Appointment> summaryTableView;

    @FXML
    private TableColumn<Appointment, LocalDate> dateColumn;

    @FXML
    private TableColumn<Appointment, String> summaryColumn;


    @FXML
    void closeButtonClicked(ActionEvent event) {
        new LoadForm().loadTutorMainView(event);// todo new
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void populateStudentFields(long studentId) {
        StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
        Student student = studentDAO.findById(studentId); // new

        System.out.println(student.getFullName());
        nameTextField.setText(student.getFullName());
        ageTextField.setText(String.valueOf(student.getStudentAge()));
        gradeTextField.setText(student.getStudentGrade());

        platformTextField.setText(student.getStudentPlatform());
        handleTextField.setText(student.getStudentHandle());

        // set next apt date w/ formatting
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy");
            LocalDateTime nextAptWTutor = studentDAO.getStudentsNextAptWithTutor(TutorMainController.tutor.getPersonId(),
                    student.getPersonId());
            if (nextAptWTutor.isAfter(LocalDateTime.now())) {
                String formattedLDT = formatter.format(nextAptWTutor);
                nextAptTextField.setText(formattedLDT);
            } else {
                nextAptTextField.setText("N/A");
            }

        } catch (NullPointerException e) {
            nextAptTextField.setText("N/A");
        }

        setSummaryTableView(student.getPersonId());
    }

    protected void setSummaryTableView(long studentId) {
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
        summaryTableView.setItems(appointmentDAO.getBasicSummaryInfo(studentId));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("aptStartDate"));
        summaryColumn.setCellValueFactory(new PropertyValueFactory<>("summary"));
    }
}
