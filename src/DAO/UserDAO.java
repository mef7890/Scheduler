package DAO;

import model.Student;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class UserDAO extends DataAccessObject<User> {

    private static final String SELECT_USER = "SELECT user_id, username, p_word, active_status, tutor_id, " +
        "admin_privilege FROM user WHERE username = ?";

    public UserDAO(Connection connection) {
        super(connection);
    }

    @Override
    public User findById(long id) {
        System.out.println("Finding user by id, creating user object from DB...");
        User user = new User();
        try (PreparedStatement statement = this.connection.prepareStatement(SELECT_USER)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
//                user.
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    return user;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User update(User dto) {
        return null;
    }

    @Override
    public User create(User dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
