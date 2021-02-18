package DAO;

import controller.main.MainController;
import model.EmployeeInfo;
import utilities.TimeConverter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class EmployeeInfoDAO extends DataAccessObject<EmployeeInfo> {

    private static final String GET_ONE_EMPLOYEE_INFO = "SELECT employee_info_id, rate " +
            "FROM employee_info WHERE person_id = ?";

    private static final String INSERT_EMPLOYEE_INFO = "INSERT INTO employee_info(person_id, rate, last_update_by, last_update)" +
            " VALUES(?, ?, ?, ?)";

    private static final String UPDATE_EMPLOYEE_INFO = "UPDATE employee_info SET rate = ?, " +
            "last_update_by = ?, last_update = ? " +
            "WHERE person_id = ? "; // left of person_id

    private static final String DELETE_EMPLOYEE_INFO = "DELETE FROM employee_info WHERE person_id = ?";

    public EmployeeInfoDAO(Connection connection) {
        super(connection);
    }

    @Override
    public EmployeeInfo findById(long id) {
        EmployeeInfo employeeInfo = new EmployeeInfo();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_EMPLOYEE_INFO)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                employeeInfo.setEmployeeInfoId(rs.getLong("employee_info_id"));
                employeeInfo.setRate(rs.getBigDecimal("rate"));
                employeeInfo.setRate(rs.getBigDecimal("rate").setScale(2));
                employeeInfo.setRate(rs.getBigDecimal("rate"));
                employeeInfo.setPersonId(id); // todo need?
            }
        } catch (SQLException e) {
            System.out.println("Find Employee Info by ID = FAILED");
            e.printStackTrace();
            throw new RuntimeException();
        }
        System.out.println("Find Employee Info by ID = SUCCESSFUL");
        return employeeInfo;
    }

    @Override
    public List<EmployeeInfo> findAll() {
        return null;
    }

    @Override
    public EmployeeInfo update(EmployeeInfo dto) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_EMPLOYEE_INFO)) {
            statement.setBigDecimal(1, dto.getRate());
            statement.setString(2, MainController.currentUser);
            statement.setTimestamp(3, Timestamp.valueOf(TimeConverter.toUTC(LocalDateTime.now())));
            statement.setLong(4, dto.getPersonId());
            statement.execute();

            this.connection.commit();
            EmployeeInfo employeeInfo = this.findById(dto.getEmployeeInfoId());
            System.out.println("Update Employee Info: SUCCESSFUL");

            return employeeInfo;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Update Employee Info: FAILED");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            throw new RuntimeException();
        }
    }

    @Override
    public EmployeeInfo create(EmployeeInfo dto) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT_EMPLOYEE_INFO)) {
            statement.setLong(1, dto.getPersonId());
            statement.setBigDecimal(2, dto.getRate());
            statement.setString(3, MainController.currentUser);
            statement.setTimestamp(4, Timestamp.valueOf(TimeConverter.toUTC(LocalDateTime.now())));

            statement.execute();
            this.connection.commit();
            System.out.println("Insert Employee Info: SUCCESSFUL");

            int id = this.getLastValue(EMPLOYEE_INFO_SEQUENCE);
            return this.findById(id);

        } catch (SQLException e) {
            System.out.println("Insert Employee Info: FAILED");
            e.printStackTrace();
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(long id) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_EMPLOYEE_INFO)) {
            statement.setLong(1, id);
            statement.execute();

            this.connection.commit();
            System.out.println("Delete Employee Info: SUCCESSFUL");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Delete Employee Info: FAILED");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}


