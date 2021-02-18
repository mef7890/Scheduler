package controller.appointment;

import DAO.AppointmentDAO;
import DAO.PlatformInfoDAO;
import DAO.StudentDAO;
import controller.main.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Appointment;
import model.PlatformInfo;
import model.Student;
import utilities.*;

import java.time.format.DateTimeFormatter;

public class ViewAppointmentController {

    @FXML
    private TextField startHour;

//    @FXML
//    private TextField startMinute;

    @FXML
    private TextField endHour;

//    @FXML
//    private TextField endMinute;

    @FXML
    private Label dateLabel;

    @FXML
    private TextField dateText;

    @FXML
    private TextField subjectFocusTextField;

    @FXML
    private TextField studentHandleText;

    @FXML
    private TextField tutorNameText;

    @FXML
    private TextField studentNameTextField;

    @FXML
    private TextField tuteeAgeText;

    @FXML
    private TextField tuteeGradeText;

    @FXML
    private TextField platformText;

    @FXML
    private TextArea appointmentNotesTextArea;

    @FXML
    void closeButtonClicked(ActionEvent event) {
        MainController.loadMainForUser(event);
    }

    public void populateAppointmentFields(long appointmentId) { // todo better to use viewAppointment() from ApptDAO
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
        Appointment appointment = appointmentDAO.findById(appointmentId);

        PlatformInfoDAO platformInfoDAO = new PlatformInfoDAO(DBConnection.connection);
        PlatformInfo platformInfo = platformInfoDAO.findById(appointment.getStudentId());

        StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
        Student student = studentDAO.findById(appointment.getStudentId());

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy HH:mm");
//            String startDate = TimeConverter.toLocalTZ(appointment.getApptStartDT()).format(formatter); // convert appt to
//            // local time, format it // todo new // todo, better to format elsewhere
            String startDate = appointment.getApptStartDT().format(formatter);
            String endTime = appointment.getApptEndTime().toString();
            String classTime = startDate + " - " + endTime;

            dateLabel.setText(classTime);

            studentNameTextField.setText(appointment.getStudentName());
            tutorNameText.setText(appointment.getTutorName());

            subjectFocusTextField.setText(appointment.getSubjectFocus());
            appointmentNotesTextArea.setText(appointment.getSchedulingNote());

            platformText.setText(platformInfo.getPlatform());
            studentHandleText.setText(platformInfo.getHandle());

            tuteeGradeText.setText(student.getStudentGrade());
            tuteeAgeText.setText(String.valueOf(student.getStudentAge()));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Populate Appointment Fields: FAILED");
            throw new RuntimeException();
        }
        System.out.println("Populate Appointment Fields: SUCCESSFUL");
    }

}
