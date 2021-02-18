package DAO;

import model.Relationship;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RelationshipDAO extends DataAccessObject<Relationship> {

    private static final String GET_ONE_RELATIONSHIP = "SELECT contact_table_id, student_id, contact_id, relationship" +
            " FROM contact " +
            "WHERE student_id = ? ";

    private static final String INSERT_RELATIONSHIP = "INSERT INTO contact(student_id, contact_id, relationship) " +
            "VALUES(?, ?, ?)";

    private static final String UPDATE_RELATIONSHIP = "UPDATE contact " +
            "SET contact_id = ?, relationship = ? " +
            "WHERE student_id = ? ";

    private static final String DELETE_RELATIONSHIP = "DELETE FROM contact WHERE student_id = ?";

    public RelationshipDAO(Connection connection) {
        super(connection);
    }

    @Override // -----   FIND ONE RECORD BY ID, RETURN OBJ   ----- //
    public Relationship findById(long id) {
        Relationship relationship = new Relationship();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_RELATIONSHIP)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                relationship.setContactTableId(rs.getLong("contact_table_id"));
                relationship.setStudentId(id);
                relationship.setContactId(rs.getLong("contact_id"));
                relationship.setRelationship(rs.getString("relationship"));
                System.out.println("Retrieve Relationship: SUCCESSFUL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Retrieve Relationship: FAILED");
        }
        return relationship;
    }

    @Override
    public List<Relationship> findAll() {
        return null;
    }

    @Override // -----   UPDATE RELATIONSHIP RECORD, RETURN OBJ   ----- //
    public Relationship update(Relationship dto) {
        Relationship relationship = new Relationship();
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_RELATIONSHIP)) {
            statement.setLong(1, dto.getContactId());
            statement.setString(2, dto.getRelationship());
            statement.setLong(3, dto.getStudentId());
            statement.execute();
            this.connection.commit();
            System.out.println("Update Relationship: SUCCESSFUL");
        } catch (SQLException e) {
            System.out.println("Update Relationship: FAILED");
            e.printStackTrace();
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return relationship;
    }

    @Override // -----   CREATE RELATIONSHIP RECORD, RETURN OBJ   ----- //
    public Relationship create(Relationship dto) {
        try {
            this.connection.setAutoCommit(false); // todo, I bet there was a way to just turn it off once
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT_RELATIONSHIP)) {
            statement.setLong(1, dto.getStudentId());
            statement.setLong(2, dto.getContactId());
            statement.setString(3, dto.getRelationship());
            statement.execute();
            this.connection.commit();

            System.out.println("Insert Relationship: SUCCESSFUL");

            long id = this.getLastValue(RELATIONSHIP_SEQUENCE);
            return this.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Insert Relationship: SUCCESSFUL");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            throw new RuntimeException();
        }
    }

    @Override // -----   DELETE RELATIONSHIP RECORD   ----- //
    public void delete(long id) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_RELATIONSHIP)) {
            statement.setLong(1, id);
            statement.execute();
            this.connection.commit();
            System.out.println("Delete Relationship: SUCCESSFUL");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Delete Relationship: SUCCESSFUL");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

    }
}
