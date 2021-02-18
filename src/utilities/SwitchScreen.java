package utilities;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitchScreen {


    Stage stage;
    Parent root;

    public void switchScreen(String docName, ActionEvent event, String title)   {

        try {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow(); // Stage stage
            root = FXMLLoader.load(getClass().getResource(docName)); // Parent scene
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toAdminAddAppointment(ActionEvent event) {
        switchScreen("/view/appointment/AdminAddAppointment.fxml", event, "Add Appointment");
    }

    public void toAddStudent(ActionEvent event) {
        switchScreen("/view/student/AddStudent.fxml", event, "Add Student");
    }

    public void toAddTutor(ActionEvent event) {
            switchScreen("/view/tutor/AddTutor.fxml", event, "Add Tutor");
    }

    public void toTutorSchedule(MenuItem menuItem) {
        switchScreenFromMenu("/view/schedule/TutorSchedule.fxml", "Tutor Schedule", menuItem);
    }

    public void toStudentSchedule(MenuItem menuItem) {
        switchScreenFromMenu("/view/schedule/StudentSchedule.fxml", "Student Schedule", menuItem);
    }

    public void switchScreenFromMenu(String docName, String title,  MenuItem menuItem)  {
        try {
            stage = (Stage) menuItem.getParentPopup().getOwnerWindow();  // the root
            root = FXMLLoader.load(getClass().getResource(docName));  // load the resource
            stage.setScene(new Scene(root)); // open this new scene off the root
            stage.show();
            stage.setTitle(title); // set window title
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
