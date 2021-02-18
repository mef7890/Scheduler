package utilities;

import controller.appointment.ModifyAppointmentController;
import controller.main.AdminMainController;
import controller.main.OwnerMainController;
import controller.student.AdminViewStudentController;
import controller.student.ModifyStudentController;
import controller.main.TutorMainController;
import controller.appointment.ViewAppointmentController;
import controller.student.TutorViewStudentController;
import controller.tutor.AdminViewTutorController;
import controller.tutor.ModifyTutorController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadForm {

    // -----   LOAD TUTOR MAIN CONTROLLER   -----  //
    public void loadTutorMainView(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main/Main.fxml"));
        loader.setController(new TutorMainController());
        showResource(loader, event, "Main Portal");
    }

    // -----   LOAD ADMIN MAIN CONTROLLER   -----  //
    public void loadAdminMainView(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main/Main.fxml"));
        loader.setController(new AdminMainController());
        showResource(loader, event, "Main Portal");
    }

    // -----   LOAD OWNER MAIN CONTROLLER   -----  //
    public void loadOwnerMainView(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main/Main.fxml"));
        loader.setController(new OwnerMainController());
        showResource(loader, event, "Main Portal");
    }

    // -----   ADMIN LOAD VIEW TUTOR   ----- //
    public static void loadAdminViewTutor(ActionEvent event, long personId) {
        FXMLLoader loader = new LoadForm().setResource("/view/tutor/AdminViewTutor.fxml");
        AdminViewTutorController adminViewTutorController = loader.getController();
        adminViewTutorController.populateTutorFields(personId); // send info to populate tutor viewer fields
        showResourceFromButton(event, loader, "View Tutor");
    }

    // -----   ADMIN LOAD MODIFY TUTOR   ----- //
    public static void loadAdminModifyTutor(ActionEvent event, long personId) {
        FXMLLoader loader = new LoadForm().setResource("/view/tutor/ModifyTutor.fxml");
        ModifyTutorController modifyTutorController = loader.getController();
        modifyTutorController.populateTutorFields(personId);// send info to populate modify tutor fields
        showResourceFromButton(event, loader, "Modify Tutor");
    }

    // -----   ADMIN LOAD VIEW STUDENT   ----- //
    public static void loadAdminViewStudent(ActionEvent event, long personId) {
        FXMLLoader loader = new LoadForm().setResource("/view/student/AdminViewStudent.fxml");
        AdminViewStudentController adminViewStudentController = loader.getController(); // controller
        adminViewStudentController.populateFormFields(personId);// pop view fields from stud. obj. data
        showResourceFromButton(event, loader, "View Student");
    }

    // -----   ADMIN LOAD MODIFY STUDENT   ----- //
    public static void loadAdminModifyStudent(ActionEvent event, long personId) {
        FXMLLoader loader = new LoadForm().setResource("/view/student/ModifyStudent.fxml");
        ModifyStudentController modifyStudentController = loader.getController();
        modifyStudentController.populateFormFields(personId);// pop view fields from stud. obj. data
        showResourceFromButton(event, loader, "Modify Student");
    }

    // -----   ADMIN LOAD MODIFY APPOINTMENT   ----- //
    public static void loadAdminModifyAppointment(ActionEvent event, long appointmentId) {
        FXMLLoader loader = new LoadForm().setResource("/view/appointment/AdminModifyAppointment.fxml");
        ModifyAppointmentController modifyAppointmentController = loader.getController();
        modifyAppointmentController.populateFormFields(appointmentId); //send info to modify apt fields
        showResourceFromButton(event, loader, "Modify Appointment");
    }

    // -----   LOAD VIEW APPOINTMENT   ----- //
    public static void loadViewAppointment(ActionEvent event, long appointmentId) {
        FXMLLoader loader = new LoadForm().setResource("/view/appointment/ViewAppointment.fxml");
        ViewAppointmentController viewAppointmentController = loader.getController();
        viewAppointmentController.populateAppointmentFields(appointmentId);
        showResourceFromButton(event, loader, "View Appointment");
    }

    // -----   TUTOR LOAD VIEW STUDENT   ----- //
    public static void loadTutorViewStudent(ActionEvent event, long personId) {
        FXMLLoader loader = new LoadForm().setResource("/view/student/TutorViewStudent.fxml");
        TutorViewStudentController tutorViewStudentController = loader.getController();
        tutorViewStudentController.populateStudentFields(personId);
        showResourceFromButton(event, loader, "View Student");
    }

    // -----   LOAD TUTOR VIEW MY SCHEDULE   ----- //
    public static void loadTutorMySchedule(MenuItem menuItem) {
        showResourceFromMenu("/view/schedule/TutorMySchedule.fxml", "My Schedule", menuItem);
    }

    protected FXMLLoader setResource(String fxmlDoc) { // changed name from loadResource
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource(fxmlDoc));
            loader.load(); // return root
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            Alerts.rowNotSelectedError("person");
        }
        return loader;
    }

    public static void showResourceFromMenu(String docName, String title,  MenuItem menuItem)  {
        try {
            Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();  // the root
            stage.setTitle(title); // set window title
            Parent root = FXMLLoader.load(LoadForm.class.getResource(docName));  // load the resource
            stage.setScene(new Scene(root)); // open this new scene off the root
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void showResourceFromButton(ActionEvent event, FXMLLoader loader, String title) {
        Stage stage;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        Parent root = loader.getRoot(); // vs load()?
        stage.setScene(new Scene(root)); // why didn't I need stage.show()?
    }

    protected static void showResource(FXMLLoader loader, ActionEvent event, String title) {
        try {
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow(); // Stage stage
            stage.setTitle(title);
            Parent root = loader.load(); // vs getRoot()?
            stage.setScene(new Scene(root)); // why didn't I need stage.show()?
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}