package controller.appointment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;
import utilities.*;

public class AddAppointmentController extends AddAppointmentHelperController implements Initializable {

    @FXML
    private TextField studentSearchNameText;

    @FXML
    private TableView<Person> studentsTableView; // todo should be Student?

    @FXML
    private TableColumn<Person, String> studentsTableCol; // todo should be Student?

    @FXML
    private TextField tutorSearchNameText;

    @FXML
    private TextField subjectFocusTextField;

    @FXML
    private TableView<Person> tutorsTableView; // todo should be Tutor2?

    @FXML
    private TableColumn<Person, String> tutorsTableCol; // todo should be Tutor2?

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<Integer> startHour;

    @FXML
    private ChoiceBox<String> startMinute;

    @FXML
    private ChoiceBox<Integer> endHour;

    @FXML
    private ChoiceBox<String> endMinute;

    @FXML
    private TextArea appointmentNotesTextArea;

    @FXML
    void cancelButtonClicked(ActionEvent event) {
        cancel(event);
    }

    @FXML
    void saveButtonClicked(ActionEvent event) {
        insertAppointment(event);
    }

    @FXML
    void searchStudentsButtonClicked(ActionEvent event) {
        Search.adminSearchStudents(studentSearchNameText, studentsTableView, studentsTableCol);
    }

    @FXML
    void searchTutorsButtonClicked(ActionEvent event) {
        Search.searchTutors(tutorSearchNameText, tutorsTableView, tutorsTableCol);
    }


    protected void insertAppointment(ActionEvent event) {
        if (checksPassed()) {
            appointment = appointmentDAO.create(appointment);
            Alerts.appointmentCreatedConfirmation();
            new LoadForm().loadAdminMainView(event); // to main screen
        }
    }
}