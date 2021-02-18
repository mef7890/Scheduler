package DAO;

import model.Owner;
import utilities.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OwnerDAO extends DataAccessObject<Owner> {

    public Owner getBasicData(long id) {
        Owner owner = new Owner();
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(DataAccessObject.GET_BASIC_PERSON)) {
            statement.setLong(1, id);
            System.out.println(statement.toString()); // todo delete
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                owner.setPersonId(rs.getLong("person_id"));
                owner.setFirstName(rs.getString("f_name"));
                owner.setLastName(rs.getString("l_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(owner.toString());
        return owner;
    }

    public OwnerDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Owner findById(long id) {
        Owner owner = new Owner();
        try (PreparedStatement statement = this.connection.prepareStatement(DataAccessObject.GET_ONE_PERSON)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                owner.setPersonId(rs.getLong("person_id"));
                owner.setRoleId(rs.getLong("role_id"));
                owner.setFirstName(rs.getString("f_name"));
                owner.setLastName(rs.getString("l_name"));
                owner.setPhone(rs.getString("phone"));
                owner.setEmail(rs.getString("email"));
                owner.setStartDate(rs.getDate("start_date").toLocalDate());
                owner.setBirthday(rs.getDate("birthday").toLocalDate());

                // address
                owner.setStreet1(rs.getString("street_1"));
                owner.setStreet2(rs.getString("street_2"));
                owner.setCity(rs.getString("city"));
                owner.setState(rs.getString("state"));
                owner.setZip(rs.getString("zip"));

                // rate
                owner.setRate(BigDecimal.valueOf(rs.getDouble("rate")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return owner;
    }

    @Override
    public List<Owner> findAll() {
        return null;
    }

    @Override
    public Owner update(Owner dto) {
        return null;
    }

    @Override
    public Owner create(Owner dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
