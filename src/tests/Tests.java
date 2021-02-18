package tests;

import controller.etc.LoginController;
import utilities.PropertiesFileCreator;

import java.io.IOException;
import java.util.ArrayList;

public class Tests {

    public static int testsPassed = 0;
    public static int testsRun = 0;
    public static ArrayList<String> failedTests = new ArrayList<String>();
    public static ArrayList<String> passedTests = new ArrayList<String>();

    public static void runAllTests() {
        // -----   LOGIN TEST   ----- //
        LoginController LC = new LoginController();
        LC.loginTest("test", "test");

        // -----    APPOINTMENT TEST   ----- //
        AppointmentTest appointmentTest = new AppointmentTest();
        appointmentTest.runAppointmentsTests();

        // -----   STUDENT TEST   ----- //
        StudentTest studentTest = new StudentTest();
        studentTest.runStudentTests();

        System.out.println(testsPassed + " out of " + testsRun + " tests passed.");
    }


    public static void main(String[] args) {

//        runAllTests();
        try {
            PropertiesFileCreator propertiesFileCreator = new PropertiesFileCreator();
            propertiesFileCreator.writeProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
