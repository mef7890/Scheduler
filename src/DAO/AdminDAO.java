package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Admin;
import model.Person;
import utilities.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdminDAO extends DataAccessObject<Admin> {

    ObservableList<Person> allAdmins = FXCollections.observableArrayList();

    private final static String GET_ALL_ADMINS = "SELECT person_id, f_name, l_name " +
            "FROM person " +
            "WHERE role_id = 2";

    // todo use personDAO
    protected static final String GET_ONE_ADMIN = "SELECT p.person_id, role_id, active_status, f_name, l_name, phone," +
            " email, " +
            "p.address_id, birthday, start_date " +
            ", street_1, street_2, city, state, zip " +
            ", rate " +
            "FROM person p " +
            "JOIN address a ON p.address_id = a.address_id " +
            "JOIN employee_info e ON p.person_id = e.person_id " +
            "WHERE p.person_id = ?";

    public AdminDAO(Connection connection) {
        super(connection);
    }

    public Admin getBasicData(long id) {
        Admin admin = new Admin();
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(DataAccessObject.GET_BASIC_PERSON)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                admin.setPersonId(rs.getLong("person_id"));
                admin.setFirstName(rs.getString("f_name"));
                admin.setLastName(rs.getString("l_name"));
                admin.setFullName(admin.getFirstName() + " " + admin.getLastName()); // todo needed?
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public Admin findById(long id) {
        System.out.println("Finding admin by ID, creating admin obj. from DB..."); // todo delete
        Admin admin = new Admin();
            try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_ADMIN)) {
                statement.setLong(1, id);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    admin.setPersonId(rs.getLong("person_id"));
                    admin.setRoleId(rs.getLong("role_id"));
                    admin.setActiveStatus(rs.getLong("active_status"));
                    admin.setFirstName(rs.getString("f_name"));
                    admin.setLastName(rs.getString("l_name"));
                    admin.setFullName(admin.getFirstName() + " " + admin.getLastName());
                    admin.setPhone(rs.getString("phone"));
                    admin.setEmail(rs.getString("email"));
                    admin.setStartDate(rs.getDate("start_date").toLocalDate());
                    admin.setBirthday(rs.getDate("birthday").toLocalDate());

                    // address
                    admin.setAddressId(rs.getLong("address_id"));
                    admin.setStreet1(rs.getString("street_1"));
                    admin.setStreet2(rs.getString("street_2"));
                    admin.setCity(rs.getString("city"));
                    admin.setState(rs.getString("state"));
                    admin.setZip(rs.getString("zip"));

                    // rate
                    admin.setRate(BigDecimal.valueOf(rs.getDouble("rate")).setScale(2)); // todo warning "BigDecimal.setScale()
                    // " called without a rounding mode argument

                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return admin;
        }

    public ObservableList<Person> getAllAdmins() {
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(GET_ALL_ADMINS)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Person person = new Person();
                person.setPersonId(rs.getLong("person_id"));
                person.setFirstName(rs.getString("f_name"));
                person.setLastName(rs.getString("l_name"));
                person.setFullName(person.getFirstName() + " " + person.getLastName());
                allAdmins.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAdmins;
    }
    @Override
    public List<Admin> findAll() {
        return null;
    }

    @Override
    public Admin update(Admin dto) {
        return null;
    }

    @Override
    public Admin create(Admin dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
