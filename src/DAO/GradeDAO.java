package DAO;

import model.Grade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GradeDAO extends DataAccessObject<Grade> {

    private static final String GET_ONE_GRADE = "SELECT grade_id, grade " +
            "FROM grade WHERE person_id = ?";

    private static final String INSERT_GRADE = "INSERT INTO grade(person_id, grade) VALUES(?, ?)";

    private static final String UPDATE_GRADE = "UPDATE grade SET grade = ? " +
            "WHERE grade_id = ? "; // could use person id
             // left off person_id

    private static final String DELETE_GRADE = "DELETE FROM grade WHERE person_id = ?";

    public GradeDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Grade findById(long id) {
        Grade grade = new Grade();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_GRADE)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                grade.setGradeId(rs.getLong("grade_id"));
                grade.setGrade(rs.getString("grade"));
                grade.setPersonId(id);
            }
        } catch (SQLException e) {
            System.out.println("Find Grade by ID = FAILED");
            e.printStackTrace();
            throw new RuntimeException();
        }
        System.out.println("Find Grade by ID = SUCCESSFUL");
        return grade;
    }

    @Override
    public List<Grade> findAll() {
        return null;
    }

    @Override
    public Grade update(Grade dto) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_GRADE)) {
            statement.setString(1, dto.getGrade());
            statement.setLong(2, dto.getGradeId());
            statement.execute();

            this.connection.commit();
            Grade grade = this.findById(dto.getGradeId());
            System.out.println("Update Grade: SUCCESSFUL");

            return grade;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Update Grade: FAILED");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            throw new RuntimeException();
        }
    }

    @Override
    public Grade create(Grade dto) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT_GRADE)) {
            statement.setLong(1, dto.getPersonId());
            statement.setString(2, dto.getGrade());

            statement.execute();
            this.connection.commit();
            System.out.println("Insert Grade: SUCCESSFUL");

            int id = this.getLastValue(GRADE_SEQUENCE);
            return this.findById(id);

        } catch (SQLException e) {
            System.out.println("Insert Grade: FAILED");
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
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_GRADE)) {
            statement.setLong(1, id);
            statement.execute();

            this.connection.commit();
            System.out.println("Delete Grade: SUCCESSFUL");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Delete Grade: FAILED");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}
