package controller.admin;

import DAO.AddressDAO;
import DAO.AdminDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.Address;
import model.Admin;
import utilities.DBConnection;
import utilities.LoadForm;

public class ViewAdminController {

    @FXML
    private TextField adminNameTextField;

    @FXML
    private TextField street1TextField;

    @FXML
    private TextField street2TextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField adminZipTextField;

    @FXML
    private TextField adminStateTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private RadioButton activeRB;

    @FXML
    private ToggleGroup statusTG;

    @FXML
    private RadioButton inactiveRB;

    @FXML
    private DatePicker birthdayDatePicker;

    @FXML
    private TextField platformTextField;

    @FXML
    private TextField handleTextField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField adminEmailTextField;

    @FXML
    private TextField rateTextField;

    @FXML
    void closeButtonClicked(ActionEvent event) {
        new LoadForm().loadOwnerMainView(event);// todo new
    }

//    public Admin populateAdminFields(Admin admin) {
    public Admin populateAdminFields(long adminId) {
        AdminDAO adminDAO = new AdminDAO(DBConnection.connection);
        Admin admin = adminDAO.findById(adminId); // create new admin obj
        AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
        Address address = addressDAO.findById(admin.getAddressId()); // create new address obj
        try {
            adminNameTextField.setText(admin.getFullName());

            street1TextField.setText(address.getStreet1());
            street2TextField.setText(address.getStreet2());
            if (street2TextField.getText().isEmpty()) {
                street2TextField.setText(" ");
            }
            cityTextField.setText(address.getCity());
            adminStateTextField.setText(address.getState());
            adminZipTextField.setText(address.getZip());

            adminEmailTextField.setText(admin.getEmail());
            phoneTextField.setText(admin.getPhone());

            birthdayDatePicker.setValue(admin.getBirthday());
            startDatePicker.setValue(admin.getStartDate());

            rateTextField.setText(String.valueOf(admin.getRate()));

            long activeStatus = admin.getActiveStatus();
            if (activeStatus == 1) {
                activeRB.setSelected(true);
            } if (activeStatus == 0) {
                inactiveRB.setSelected(true);
            }

//            System.out.println("Populate Admin Fields: SUCCESSFUL"); // todo delte
            // todo receive alerts, summary
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("Populate Admin Fields: FAILED"); // todo delete
        }
        return admin;
    }
}
