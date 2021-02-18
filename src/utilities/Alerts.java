package utilities;

import DAO.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Appointment;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class Alerts {

    public static ResourceBundle rb = ResourceBundle.getBundle("resources/Language", Locale.getDefault());

    public static void resetConfirmation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Any data you have entered will not be saved. Are you sure you want to reset this form?");
        alert.setTitle("Cancellation Confirmation");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            // TODO clear fields of form
        }
    }

    public static void associatedAppointmentsError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "This person is associated with one or more appointments in the system. " +
                "You may not delete this person until all associated appointments have been deleted. Instead, you may wish to " +
                "make this person \"Inactive\".");
        alert.setTitle("Deletion Error");
        alert.showAndWait();
    }

    public static void deleteConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The record has been deleted.");
        alert.showAndWait();
    }

    public void deleteStudentConfirmation(long studentPersonId, String studentName)  {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "All information associated with the record you have selected " +
                        "will be deleted. Are you sure you want to delete this record?");
        alert.setTitle("Deletion Confirmation");
        Optional<ButtonType> result = alert.showAndWait();
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
        if (result.isPresent() && (result.get() == ButtonType.OK) &&
                (appointmentDAO.personHasNoAppointment(studentPersonId))) { // and person doesn't have assoc. appts

            StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
            studentDAO.delete(studentPersonId);
            Alerts.deleteConfirmation(); // show conf. record has been deleted
//            UserLog.actionLog("Deleted", studentName + "\'s Profile"); // todo include
        }
    }

    public void deleteAppointmentConfirmation(long appointmentId, String studentName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "All appointment information associated with the record you have selected " +
                        "will be deleted. " +
                        "Are you sure you want to delete this record?");
        alert.setTitle("Deletion Confirmation");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
            appointmentDAO.delete(appointmentId);
//            UserLog.actionLog("Deleted", studentName + "\'s Profile"); // todo include
        }
    }

    public void deleteTutorConfirmation(long tutorPersonId, String tutorName)  {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "All information associated with the record you have selected " +
                        "will be deleted. Are you sure you want to delete this record?");
        alert.setTitle("Deletion Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
        if (result.isPresent() && (result.get() == ButtonType.OK) &&
                (appointmentDAO.personHasNoAppointment(tutorPersonId))) { // and person doesn't have assoc. appts

            TutorDAO tutorDAO = new TutorDAO(DBConnection.connection);
            tutorDAO.delete(tutorPersonId);
            Alerts.deleteConfirmation(); // show conf. record has been deleted
            System.out.println("Delete Tutor: SUCCESSFUL");

//            UserLog.actionLog("Deleted", tutorName + "\'s Profile"); // todo include later
        }
    }

    public static void studentCreatedConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "The student profile has been created.");
        alert.setTitle("Student Created Confirmation");
        alert.showAndWait();
    }

    public static void studentUpdatedConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "The student profile has been updated.");
        alert.setTitle("Student Updated Confirmation");
        alert.showAndWait();
    }

    public static void appointmentCreatedConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "The appointment has been booked.");
        alert.setTitle("Appointment Created Confirmation");
        alert.showAndWait();
    }

    public static void appointmentUpdatedConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "The appointment has been updated.");
        alert.setTitle("Appointment Updated Confirmation");
        alert.showAndWait();
    }

    public static void cancelConfirmation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your changes have not been saved. " +
                "Are you sure you want to cancel?");
        alert.setTitle("Cancellation Confirmation");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            new LoadForm().loadAdminMainView(event); // todo new
        }
    }

    public static void outsideBizHoursError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "You cannot book an appointment outside of business " +
                "hours (M-S 8:00-20:00 local time). Please " +
                "reschedule your appointment.");
        alert.setTitle("Error");
        alert.show();
    }

    public static void appointmentInPastError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "You are trying to book an appointment for a time in the past." +
                " Please reschedule your appointment.");
        alert.setTitle("Error");
        alert.show();
    }

    public static void emptySubjectFocus() {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "Please enter a subject for the appointment.");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void noDateSelected() {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "Please enter a date for the appointment.");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void noTimeSelected(String time) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "Please select a " + time + " for the appointment.");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void noAppointmentsScheduled(String tutor, String timeframe) {
        timeframe = FormatAndParse.formatTimeFrame(timeframe);
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "There are no appointments scheduled for " + tutor + " " + timeframe);
        alert.setTitle("Information");
        alert.showAndWait();
    }

    public static void rowNotSelectedToModifyError(String rowType) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a " + rowType + " to modify");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void rowNotSelectedError(String rowType) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a " + rowType + ".");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void rowNotSelectedToViewError(String rowType) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a " + rowType + " to view");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void overlappingApptError(Appointment apptToBook, Appointment existingAppt) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy @ HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy");

        Alert alert = new Alert(Alert.AlertType.ERROR, "This new appointment: " +
                "\n\n \t Student: \t" + apptToBook.getStudentName() +
                "\n \t Tutor: \t" + apptToBook.getTutorName() +
                "\n \t On: \t" + apptToBook.getApptStartDT().format(dateTimeFormatter) + " - " + apptToBook.getApptEndTime().toString() +
                "\n\noverlaps with this existing appointment: " +
                "\n\n \t Student: \t" + existingAppt.getStudentName() +
                "\n \t Tutor: \t" + existingAppt.getTutorName() +
                "\n \t On: " + existingAppt.getApptStartDT().toLocalDate().format(dateFormatter) + " @ " + TimeConverter.toLocalTZ(existingAppt.getApptStartDT()).toLocalTime().toString()  +
                " - " + TimeConverter.toLocalTZ(existingAppt.getApptEndDT()).toLocalTime().toString() +
                "\n\nPlease reschedule this appointment.");
        alert.setTitle("Double Booking Error");
        alert.showAndWait();
    }

    public static void invalidLoginError() {
        //Using resource bundle ... creating rb object ... to change language
        //ResourceBundle rb = ResourceBundle.getBundle("resources/Language", Locale.getDefault());

//        if (Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("en")) {
//            System.out.println(rb.getString("email") + " " + rb.getString("password"));
//        }
        Alert alert = new Alert(Alert.AlertType.ERROR, rb.getString("incorrectCombo"));
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void emptyUsernameError() {
        //ResourceBundle rb = ResourceBundle.getBundle("resources/Language", Locale.getDefault());

        Alert alert = new Alert(Alert.AlertType.ERROR, rb.getString("emptyUsername"));
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void emptyPasswordError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, rb.getString("emptyPassword"));
        alert.setTitle("Error");
        alert.showAndWait();
    }


    public static void invalidEntryError(String string, char illegalChar) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "\"" + string + "\"" + " contains an illegal character. \n\n Illegal character: \'" + illegalChar +
                "\'");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void spaceInEmailError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Email should not contain any spaces.");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void emailMissingPeriodError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Email should contain a period (\'.\') " +
                "followed by a top-level domain. For " +
                "example, \'.com\', \'.edu\', or \'.net\'");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void emailMissingAtError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Email should contain \'@\' followed by a domain name. " +
                "For example, \'@gmail\'");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void emptyTextFieldError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "One or more fields is missing data. Please enter valid data " +
                "for all text fields.");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void emptyNameSearchError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a person's name to search.");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void zipLengthError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "The zip code should contain five numbers.");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void phoneLengthError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "\"Phone #\" should contain ten numbers.");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void invalidEndTime() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "The end time cannot precede the start time.");
        alert.setTitle("Error");
        alert.showAndWait();
    }
    public static void upcomingAppointmentAlert(String tutee, LocalTime startTime, String platform
            , String handle) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "You have an appointment with " + tutee +
                " at " + startTime + " on " + platform + ". " + tutee + "\'s handle is: " + handle);
        alert.setTitle("Upcoming Appointment Alert");
        alert.showAndWait();
    }

    public static void personNotFoundAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Person not found");
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void inactiveUserAccountError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Your account has been marked inactive. Please contact your " +
                "supervisor.");
        alert.setTitle("Error");
        alert.showAndWait();
    }


}
