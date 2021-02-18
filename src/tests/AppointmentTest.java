package tests;

import DAO.AddressDAO;
import DAO.AppointmentDAO;
import DAO.PersonDAO;
import controller.main.MainController;
import model.Address;
import model.Appointment;
import model.Person;
import utilities.DBConnection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentTest {

    // insert address variables
    protected long addressId = 1;
//    protected long addressId = insertAddress();
    protected String street1 = "TEST STREET 1";
    protected String street2 = "";
    protected String city = "TEST CITY";
    protected String state = "ST";
    protected String zip = "11111";

    AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
//    AddressDAO addressDAO = new AddressDAO(MainController.connection);
    Address insertedAddress = new Address(street1, street2, city, state, zip);

    // insert person variables
    protected long personId = 0; // student // todo make static in student test?
    protected long roleId = 4;
    protected long activeStatus = 1;
    protected String firstName = "TEST FIRST";
    protected String lastName = "TEST LAST";
    protected String fullName = firstName + " " + lastName;
    protected String phone = "111-111-1111";
    protected String email = "TEST@EMAIL.COM";
    protected LocalDate birthday = LocalDate.of(2020, 10, 27);
    protected LocalDate startDate = LocalDate.of(2020, 11, 2);

    PersonDAO personDAO = new PersonDAO(DBConnection.connection);
//    PersonDAO personDAO = new PersonDAO(MainController.connection);
    Person insertedPerson = new Person(roleId, activeStatus, firstName, lastName, fullName, phone, email,
            addressId, birthday, startDate);

    // insert appointment variables
    long appointmentId = 0;
    long tutorId = 0;
    long studentId = 0;

    LocalDateTime apptStartDT = LocalDateTime.of(1990, 10, 27, 10, 27);
    LocalDateTime apptEndDT = LocalDateTime.of(1990, 11, 2, 11, 2);
    String subjectFocus = "TEST SUBJECT";
    String schedulingNote = "TEST NOTE";
    String summary = "TEST SUMMARY";

    LocalDateTime modifiedApptStartDT = LocalDateTime.of(1984, 12, 5, 12, 5);
    String modifiedSubjectFocus = "MODIFIED SUBJECT";

    AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
//    AppointmentDAO appointmentDAO = new AppointmentDAO(MainController.connection);

    Appointment insertedAppointment = new Appointment(tutorId, studentId, apptStartDT, apptEndDT,
            subjectFocus, schedulingNote, summary);
    Appointment foundAppointment;
    Appointment modifiedAppointment;



    protected void setCurrentUser() {
        MainController.currentUser = "TEST CURRENT USER";
    }

    protected long insertAddress() {
        try {

            insertedAddress = addressDAO.create(insertedAddress);
            return insertedAddress.getAddressId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected long insertPerson(String name, long roleId) {
        try {
            insertedPerson.setFirstName(name);
            insertedPerson.setAddressId(insertAddress());
            insertedPerson.setRoleId(roleId);
            insertedPerson = personDAO.create(insertedPerson);
            return insertedPerson.getPersonId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected boolean insertAppointmentTest() {
        Tests.testsRun++;
        try {
            tutorId = insertPerson("Tutor", 3);
            studentId = insertPerson("Student", 4);
            insertedAppointment.setTutorId(tutorId);
            insertedAppointment.setStudentId(studentId);
            AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.connection);
//            AppointmentDAO appointmentDAO = new AppointmentDAO(MainController.connection);
            insertedAppointment = appointmentDAO.create(insertedAppointment);
            Tests.testsPassed++;
            System.out.println("Insert Appointment Test: PASSED");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Tests.failedTests.add("Insert Appointment Test: FAILED\n"); // todo new
            System.out.println("Insert Appointment Test: FAILED");
            throw new RuntimeException();
        }
    }

    protected boolean findAppointmentTest() {
        Tests.testsRun++;
        try {
            foundAppointment = appointmentDAO.findById(insertedAppointment.getAppointmentId());
            if (insertedAppointment.getApptStartDT().isEqual(foundAppointment.getApptStartDT()) &&
                insertedAppointment.getSchedulingNote().matches(foundAppointment.getSchedulingNote())) {
                Tests.testsPassed++;
                System.out.println("Find Appointment by ID Test: PASSED");
                return true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Tests.failedTests.add("Find Appointment by ID Test: FAILED\n"); // todo new
            System.out.println("Find Appointment by ID Test: FAILED");
            throw new RuntimeException();
        }
        return false;
    }

    protected boolean updateAppointmentTest() {
        Tests.testsRun++;
        try {
            modifiedAppointment = insertedAppointment; // todo they're now linked, changes to modified affect inserted
            modifiedAppointment.setApptStartDT(modifiedApptStartDT);
            modifiedAppointment.setSubjectFocus(modifiedSubjectFocus);

            modifiedAppointment = appointmentDAO.update(modifiedAppointment);

            if (modifiedAppointment.getApptStartDT().isEqual(modifiedApptStartDT) &&
                    modifiedAppointment.getSubjectFocus().matches(modifiedSubjectFocus) &&
                    !modifiedAppointment.getApptStartDT().isEqual(apptStartDT)) {
                Tests.testsPassed++;
                System.out.println("Update Appointment Test: PASSED");
                return true;
            }
        } catch (Exception e) {
            Tests.failedTests.add("Update Appointment Test: FAILED\n"); // todo new
            System.out.println("Update Appointment Test: FAILED");
            throw new RuntimeException();
        }
        return false;
    }

    protected boolean deleteAppointmentTest() {

        Tests.testsRun++;
        appointmentDAO.delete(insertedAppointment.getAppointmentId());
        personDAO.delete(tutorId);
        personDAO.delete(studentId);
        if (appointmentDAO.findById(insertedAppointment.getAppointmentId()).getAppointmentId() == 0) { // if not found in db
            Tests.testsPassed++;
            return true;
        }
        Tests.failedTests.add("Delete Appointment Test: FAILED\n"); // todo new
        System.out.println("Delete Appointment Test: FAILED");
        return false;
    }

    protected void runAppointmentsTests() {
        setCurrentUser();
        insertAppointmentTest();
        findAppointmentTest();
        updateAppointmentTest();
        deleteAppointmentTest();
    }
}
