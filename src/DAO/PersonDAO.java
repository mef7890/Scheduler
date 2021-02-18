package DAO;

import controller.main.MainController;
import model.Person;
import utilities.TimeConverter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class PersonDAO extends DataAccessObject<Person> {

    private static final String INSERT_PERSON = "INSERT INTO person(role_id, f_name, l_name, phone, email, " +
            "address_id, birthday, start_date, last_update_by, active_status, last_update) " +  // added active status
            "VALUES(?,?,?,?,?,?,?,?,?,?,?);";

    private static final String GET_ONE_PERSON ="SELECT person_id, role_id, active_status, f_name, l_name, phone, email, " + //
            "p.address_id, birthday, start_date, " +
            "street_1, street_2, city, state, zip " +
            "FROM person p " +
            "JOIN address a ON p.address_id = a.address_id " +
            "WHERE person_id = ?";

    private static final String UPDATE_PERSON = "UPDATE person SET f_name=?, l_name=?, phone=?, email=?, " +
//            "address_id=?, " +
            "birthday=?, start_date=?, last_update_by=?, active_status = ?, last_update = ? " + // added active status
            "WHERE person_id=?";

    private static final String DELETE_PERSON = "DELETE FROM person WHERE person_id = ?";

    public PersonDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Person findById(long id) {
        Person person = new Person();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_PERSON)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                person.setPersonId(rs.getLong("person_id"));
                person.setRoleId(rs.getLong("role_id"));
                person.setActiveStatus(rs.getLong("active_status"));
                person.setFirstName(rs.getString("f_name"));
                person.setLastName(rs.getString("l_name"));
                person.setPhone(rs.getString("phone"));
                person.setEmail(rs.getString("email"));
                person.setStartDate(rs.getDate("start_date").toLocalDate());
                person.setBirthday(rs.getDate("birthday").toLocalDate());

                // address
                person.setAddressId(rs.getLong("address_id"));
                person.setStreet1(rs.getString("street_1"));
                person.setStreet2(rs.getString("street_2"));
                person.setCity(rs.getString("city"));
                person.setState(rs.getString("state"));
                person.setZip(rs.getString("zip"));

                System.out.println("Find by personId = SUCCESSFUL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Find by personId = FAILED");
            throw new RuntimeException(e);
        }
        return person;
    }


    @Override
    public List<Person> findAll() {
        return null;
    }

    // -----   UPDATE PERSON VALUES IN DB, RETURN PERSON OBJECT WITH UPDATED VALUES RETRIEVED FROM DB   ----- //
    @Override
    public Person update(Person dto) {
        Person person; // removed new Person()
        try {
            this.connection.setAutoCommit(false); // turn off auto commit
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_PERSON)) {
            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getLastName());
            statement.setString(3, dto.getPhone());
            statement.setString(4, dto.getEmail());
            statement.setDate(5, Date.valueOf(dto.getBirthday()));
            statement.setDate(6, Date.valueOf(dto.getStartDate()));
            statement.setString(7, MainController.currentUser);
            statement.setLong(8, dto.getActiveStatus()); // todo new
            statement.setTimestamp(9, Timestamp.valueOf(TimeConverter.toUTC(LocalDateTime.now())));
            statement.setLong(10, dto.getPersonId());

            statement.execute();

            this.connection.commit(); // todo handling commit in student, contact, tutor daos

            person = this.findById(dto.getPersonId());

        } catch (SQLException e) {
            try {
                this.connection.rollback(); // rollback transaction
                System.out.println("Update Person: FAILED");
            } catch (SQLException e2) {
                e2.printStackTrace();
                throw new RuntimeException(e2);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("Update Person: SUCCESSFUL");
        return person;
    }

    @Override // -----   CREATE PERSON RECORD, RETURN PERSON OBJECT   ----- //
    public Person create(Person dto) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT_PERSON)) {

            statement.setLong(1, dto.getRoleId());
            statement.setString(2, dto.getFirstName());
            statement.setString(3, dto.getLastName());
            statement.setString(4, dto.getPhone());
            statement.setString(5, dto.getEmail());
            statement.setLong(6, dto.getAddressId());
            statement.setDate(7, Date.valueOf(dto.getBirthday()));
            statement.setDate(8, Date.valueOf(dto.getStartDate()));
            statement.setString(9, MainController.currentUser);
            statement.setLong(10, 1);
            statement.setTimestamp(11, Timestamp.valueOf(TimeConverter.toUTC(LocalDateTime.now())));


            statement.execute();
            this.connection.commit(); // todo possible to only commit if all successful

            System.out.println("Insert Person: SUCCESSFUL");
            int id = this.getLastValue(PERSON_SEQUENCE);
            return this.findById(id); // returns person object with info from db

        } catch (SQLException e) {
            try {
                this.connection.rollback();
                System.out.println("Insert Person: FAILED");
            } catch (SQLException e2) {
                e.printStackTrace();
                throw new RuntimeException(e2);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override // -----   DELETE PERSON RECORD   ----- //
    public void delete(long id) {
        try  {
            this.connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_PERSON)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
