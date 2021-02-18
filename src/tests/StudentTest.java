package tests;

import DAO.StudentDAO;
import controller.main.MainController;
import model.*;
import utilities.DBConnection;

import java.time.LocalDate;

public class StudentTest {

    // student person variables
    protected long personId = 0; // student
    protected long roleId = 4;
    protected long activeStatus = 1;
    protected String firstName = "STUDENT TEST FIRST";
    protected String lastName = "STUDENT TEST LAST";
    protected String fullName = firstName + " " + lastName;
    protected String phone = "111-111-1111";
    protected String email = "TEST@EMAIL.COM";
    protected LocalDate birthday = LocalDate.of(2020, 10, 27);
    protected LocalDate startDate = LocalDate.of(2020, 11, 2);

//     contact person variables
//    protected long contactPersonId = 999998; // student
//    protected long contactRoleId = 4;
//    protected long contactActiveStatus = 1;
//    protected String contactFirstName = "STUDENT TEST FIRST";
//    protected String lastName = "STUDENT TEST LAST";
//    protected String fullName = firstName + " " + lastName;
//    protected String phone = "111-111-1111";
//    protected String email = "TEST@EMAIL.COM";
//    protected LocalDate birthday = LocalDate.of(2020, 10, 27);
//    protected LocalDate startDate = LocalDate.of(2020, 11, 2);

    // address variables, used for contact and student
    protected long addressId = 0;
    protected String street1 = "TEST STREET 1";
    protected String street2 = "";
    protected String city = "TEST CITY";
    protected String state = "ST";
    protected String zip = "11111";

    protected int studentAge = 0; // derived // todo delete

    protected long gradeId = 0; // todo delete
    protected String studentGrade = "TEST GRADE"; // todo will have to fix this after enum

    protected long contactId = 0; // todo could test more thoroughly

    protected long platformId = 0; // todo delete
    protected String studentHandle = "TEST HANDLE";
    protected String studentPlatform = "TEST PLATFORM";

    protected String updatedFirstName = "UPDATED FIRST NAME";
    protected String updatedGradeYear = "UPDATED GRADE";
    protected String updatedPlatform = "UPDATED PLATFORM";
    protected String updatedStreet = "UPDATED STREET 1";

    StudentDAO studentDAO = new StudentDAO(DBConnection.connection);
//    StudentDAO studentDAO = new StudentDAO(MainController.connection);

    Address insertedAddress = new Address(street1, street2, city, state, zip);
    Address updatedAddress; // todo delete

    Person insertedPerson = new Person(roleId, activeStatus, firstName, lastName, fullName, phone, email,
            addressId,birthday, startDate);
    Person updatedPerson; // todo delete

    Grade insertedGrade = new Grade(studentGrade);
    Grade updatedGrade; // todo delete

    PlatformInfo insertedPlatformInfo = new PlatformInfo(studentHandle, studentPlatform);
    PlatformInfo updatePlatformInfo; // todo delete

    Student insertedStudent = new Student(insertedPerson, insertedAddress, insertedGrade, insertedPlatformInfo);
    Student updatedStudent;
    Student foundStudent;

    protected void setCurrentUser() {
        MainController.currentUser = "TEST CURRENT USER";
    }

    protected boolean insertStudentTest() {
        Tests.testsRun++;
        try {
            insertedStudent = studentDAO.create(insertedStudent);
            Tests.testsPassed++;
            System.out.println("Insert Student Test: PASSED");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Tests.failedTests.add("Insert Student Test: FAILED\n"); // todo new
            System.out.println("Insert Student Test: FAILED");
            return false;
        }
    }

    protected boolean findStudentTest() {
        Tests.testsRun++;
        try {
            foundStudent = studentDAO.findById(insertedStudent.getPersonId());
            if (insertedStudent.getStreet1().matches(foundStudent.getStreet1()) &&
                insertedStudent.getLastName().matches(lastName)) {

                Tests.testsPassed++;
                System.out.println("Find Student by ID Test: PASSED");
                return true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Tests.failedTests.add("Find Student by ID Test: FAILED\n"); // todo new
            System.out.println("Find Student by ID Test: FAILED");
        }
        return false;
    }

    protected boolean updateStudentTest() {
        Tests.testsRun++;
        try {
            updatedStudent = insertedStudent;
            updatedStudent.setFirstName(updatedFirstName);
            updatedStudent.setStreet1(updatedStreet);
            updatedStudent.setStudentGrade(updatedGradeYear);
            updatedStudent.setStudentPlatform(updatedPlatform);

            updatedStudent = studentDAO.update(updatedStudent);

            if (updatedStudent.getFirstName().matches(updatedFirstName) &&
                updatedStudent.getStudentGrade().matches(updatedGradeYear) &&
                !updatedStudent.getStudentPlatform().matches(studentPlatform)) {

                Tests.testsPassed++;
                System.out.println("Update Student Test: PASSED");
                return true;
            }
        } catch (Exception e) {
            Tests.failedTests.add("Update Student Test: FAILED\n"); // todo new
            System.out.println("Update Student Test: FAILED");
        }
        return false;
    }

    protected boolean deleteStudentTest() {
        Tests.testsRun++;
        studentDAO.delete(insertedStudent.getPersonId());
        try {
            foundStudent = studentDAO.findById(insertedStudent.getPersonId()); // exeption thrown bc of bday
        } catch (NullPointerException e) {
            System.out.println("Delete Student Test: PASSED");
            Tests.testsPassed++;
            return true;
        }
        Tests.failedTests.add("Delete Student Test: FAILED\n"); // todo new
        System.out.println("Delete Student Test: FAILED");
        return false;
    }

    protected void runStudentTests() {
        setCurrentUser();
        insertStudentTest();
        findStudentTest();
        updateStudentTest();
        deleteStudentTest();
    }

//    Person person = new Person(roleId, activeStatus, firstName, lastName, fullName, phone, email, addressId, street1, street2,
//            city, state, zip, birthday, startDate);
}
