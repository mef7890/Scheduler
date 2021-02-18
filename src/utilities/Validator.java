package utilities;

import DAO.AppointmentDAO;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import model.Appointment;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Validator {

    // checks for non-letters, allows comma and space
    public static boolean subjectIsValid(String textToCheck) {
        for (int i = 0; i < textToCheck.length(); i++) {
            char x = textToCheck.charAt(i);
            if (!Character.isLetter(x) && // if not a letter
                    (!Character.isSpaceChar(x)) && // if not a space
                    (!String.valueOf(x).equalsIgnoreCase(",")) // if not a comma
            ) {
                Alerts.invalidEntryError(textToCheck, x);
                return false;
            }
        }
        return true;
    }

    // checks for non-letters, allows hyphen and space
    public static boolean textIsValid(String textToCheck) {
        for (int i = 0; i < textToCheck.length(); i++) {
            char x = textToCheck.charAt(i);
            if (!Character.isLetter(x) && // if not a letter
                    (!Character.isSpaceChar(x)) && // if not a space
                    (!String.valueOf(x).equalsIgnoreCase("-")) // if not a comma
            ) {
                Alerts.invalidEntryError(textToCheck, x);
                return false;
            }
        }
        return true;
    }

    // checks to make sure email includes '@' and '.'
    public static boolean emailIsValid(String textToCheck) {
        int containsAt = 0;
        int containsPeriod = 0;
        for (int i = 0; i < textToCheck.length(); i++) {
            char x = textToCheck.charAt(i);

            if (Character.toString(x).equalsIgnoreCase("!") ||
                    (Character.toString(x).equalsIgnoreCase("#")) ||
                    (Character.toString(x).equalsIgnoreCase("$")) ||
                    (Character.toString(x).equalsIgnoreCase("%")) ||
                    (Character.toString(x).equalsIgnoreCase("&")) ||
                    (Character.toString(x).equalsIgnoreCase("~"))) {

                Alerts.invalidEntryError(textToCheck, x); // if contains illegal char
                return false;
            }

            if (Character.isWhitespace(x)) {
                Alerts.spaceInEmailError(); // if contains illegal space
                return false;
            }
            try {
                if (String.valueOf(textToCheck.charAt(textToCheck.length() - 4)).equalsIgnoreCase(".")) { // check for '.xxx'
                    containsPeriod = 1; //
                }
            } catch (StringIndexOutOfBoundsException e) {
                Alerts.emailMissingPeriodError();
                return false;
            }
            if (Character.toString(x).equalsIgnoreCase("@")) { // check for @
                containsAt = 1;
            }
        }

        if (containsAt == 0) { // if email missing '@'
            Alerts.emailMissingAtError();
            return false;
        }
        if (containsPeriod == 0) { // if email missing '.xxx'
            Alerts.emailMissingPeriodError();
            return false;
        }
        return true;
    }

    // if zip has a letter or is not 5 numbers
    public static boolean zipIsValid(String numberToCheck) {
        if (!isOnlyNumbers(numberToCheck)) { // if zip contains letters
            return false;
        }
        if (numberToCheck.length() != 5) {
            Alerts.zipLengthError();
            return false;
        }
        return true;
    }

    // if number contains non-numeric characters
    public static boolean isOnlyNumbers(String numberToCheck) {
        for (int i = 0; i < numberToCheck.length(); i++)
            if (!Character.isDigit(numberToCheck.charAt(i))) { // check if it's not a digit
                Alerts.invalidEntryError(numberToCheck, numberToCheck.charAt(i));
                return false;
            }
        return true;
    }

    // if number has a letter -> error
    public static boolean numberDoesNotContainLetters(String numberToCheck) { // used for phone #
        for (int i = 0; i < numberToCheck.length(); i++)
            if (Character.isLetter(numberToCheck.charAt(i))) { // check if it's not a digit
                Alerts.invalidEntryError(numberToCheck, numberToCheck.charAt(i));
                return false;
            }
        return true;
    }


    public static boolean phoneIsValidLength(String phoneNumber) {
        if (phoneNumber.length() != 12) {
            Alerts.phoneLengthError();
            return false;
        }
        return true;
    }

    // ------- CHECK IF TRYING TO BOOK APPOINTMENT IN THE PAST    ------- //
    public static boolean isInFuture(LocalDateTime localDateTime) {
        if (localDateTime.isBefore(LocalDateTime.now())) {
            Alerts.appointmentInPastError();
            return false;
        } else {
            return true;
        }
    }

    public static boolean hasSearchText(TextField searchNameText) {
        if (searchNameText.getText().isEmpty()) {
            Alerts.emptyNameSearchError();
            return false;
        }
        return true;
    }

    // -----   CHECK IF APPT IS WITHIN BIZ HOURS   ----- // // todo redo this to check if appt within tutor's available time
    public static boolean isWithinBizHours(LocalDateTime apptStartLDT, LocalTime endLT) {

        if (String.valueOf(apptStartLDT.getDayOfWeek()).equals("SUNDAY")) {
//                    || (String.valueOf(appointmentDate.getDayOfWeek()).equals("SATURDAY"))) { // todo cmt out sat
            Alerts.outsideBizHoursError();
            return false;
        }
        if ((apptStartLDT.toLocalTime().isBefore(LocalTime.of(8, 0)))
                || (apptStartLDT.toLocalTime().isAfter(LocalTime.of(20, 0)))) {
            Alerts.outsideBizHoursError();
            return false;
        }
        if (endLT.isAfter(LocalTime.of(20, 0))) {
            Alerts.outsideBizHoursError();
            return false;
        }
        return true;
    }

    // -----   CHECK IF END TIME AFTER START   ----- //
    public static boolean hasValidEndTime(LocalTime startLT, LocalTime endLT) {
        if (endLT.isBefore(startLT)) { // if end before start
            Alerts.invalidEndTime();
            return false;
        } else {
            return true;
        }
    }

    // remember to convert potential appt times to utc for comparison with db appt times
    public static boolean isNotDoubleBooking(Appointment appointment2) {
        AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);

        LocalDateTime startLDT = appointment2.getApptStartDT();
        LocalDateTime endLDT = appointment2.getApptEndDT();
        long studentId = appointment2.getStudentId();
        long tutorId = appointment2.getTutorId();
        long appointmentId = appointment2.getAppointmentId();

        ObservableList<Appointment> apptsOnDay = appointmentDAO.allApptsForPersonOnDay(appointmentId, studentId, tutorId,
                startLDT, endLDT);

        for (Appointment appointment : apptsOnDay) {

            // if appts start at same time
            if (appointment.getApptStartDT().isEqual(TimeConverter.toUTC(startLDT))) {
                System.out.println("Booking Error: Trying to book an appointment to start at same time as an existing " +
                        "appointment");
                Alerts.overlappingApptError(appointment2, appointment);
                return false;
            }

            // if appts end at same time and therefore overlap
            if (appointment.getApptEndDT().isEqual(TimeConverter.toUTC(endLDT))) {
                System.out.println("Booking Error: Trying to book an appointment that overlaps with an existing appt");
                Alerts.overlappingApptError(appointment2, appointment);
                return false;
            }

            // if appt starts in the middle of existing appt
            if (appointment.getApptStartDT().isBefore(TimeConverter.toUTC(startLDT)) && appointment.getApptEndDT().isAfter(TimeConverter.toUTC(startLDT))) {
                // if potential appt start is after the start of an existing appt and before the end of that appt
                System.out.println("Booking Error: Trying to book an appointment to start in the middle of an existing " +
                        "appointment");
                Alerts.overlappingApptError(appointment2, appointment);
                return false;
            }
            // if appt ends in the middle of an existing appt
            if (appointment.getApptStartDT().isBefore(TimeConverter.toUTC(endLDT)) && appointment.getApptEndDT().isAfter(TimeConverter.toUTC(endLDT))) {
                // if potential appt ends after the start of an existing appt and before the end of that appt
                System.out.println("Booking Error: Trying to book an appointment which ends in the middle of an existing " +
                        "appointment");
                Alerts.overlappingApptError(appointment2, appointment);
                return false;
            }
        }
        return true;
    }

    public static boolean personFieldsAreValid(String phone, String firstName, String lastName, String city, String state,
                                               String zip, String email) {

        return (phoneIsValidLength(phone)) &&
                (textIsValid(firstName)) &&
                (textIsValid(lastName)) &&
                (textIsValid(city)) &&
                (textIsValid(state)) &&
                (zipIsValid(zip)) &&
                (emailIsValid(email));
    }
}