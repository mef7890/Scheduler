package controller.etc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddUserController {

    @FXML
    private TextField tutorPhoneText;

    @FXML
    private TextField firstNameText;

    @FXML
    private ChoiceBox<?> primarySubjectChoice;

    @FXML
    private TextField lastNameText;

    @FXML
    private ChoiceBox<?> secondarySubjectChoice;

    @FXML
    private DatePicker tutorBirthdayDP;

    @FXML
    private TextField tutorEmailText;

    @FXML
    private TextField payText;

    @FXML
    private DatePicker startDateDP;

    @FXML
    private TextField ecPhoneText;

    @FXML
    private TextField ecRelationshipText;

    @FXML
    private TextField ecNameText;

    @FXML
    void cancelButtonClicked(ActionEvent event) {

    }

    @FXML
    void saveNewTuteeClicked(ActionEvent event) {

    }

}
