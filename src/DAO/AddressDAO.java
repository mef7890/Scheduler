package DAO;

import controller.main.MainController;
import model.Address;
import utilities.TimeConverter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class AddressDAO extends DataAccessObject<Address>{

    private static final String GET_ONE_ADDRESS = "SELECT address_id, street_1, street_2, " +
            "city, state, zip " +
            "FROM address " +
            "WHERE address_id = ? ";

    private static final String INSERT_ADDRESS = "INSERT INTO address(street_1, street_2, city, state, zip, " +
            "last_update_by, last_update) " +
            "VALUES(?,?,?,?,?,?,?);";

    private static final String UPDATE_ADDRESS = "UPDATE address SET " +
            "street_1 = ?, street_2 = ?, city = ?, state = ?, zip = ?, last_update_by = ?, last_update = ? " +
            "WHERE address_id = ?";

    private static final String DELETE_ADDRESS = "DELETE FROM address " +
            "WHERE address_id = ?";

    public AddressDAO(Connection connection) {
        super(connection);
    }

    // -----   CREATE AN ADDRESS OBJECT WITH VALUES RETRIEVED FROM DB   ----- //
    @Override
    public Address findById(long id) {
        Address address = new Address();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_ADDRESS)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                address.setAddress_id(rs.getLong("address_id"));
                address.setStreet1(rs.getString("street_1"));
                address.setStreet2(rs.getString("street_2"));
                address.setCity(rs.getString("city"));
                address.setState(rs.getString("state"));
                address.setZip(rs.getString("zip"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Retrieve Address: FAILED");
        }
        System.out.println("Retrieve Address: SUCCESSFUL");
        return address;
    }

    @Override
    public List<Address> findAll() {
        return null;
    }

    @Override
    public Address update(Address dto) {
        Address address = new Address();
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_ADDRESS)) {
            statement.setString(1, dto.getStreet1());
            statement.setString(2, dto.getStreet2());
            statement.setString(3, dto.getCity());
            statement.setString(4, dto.getState());
            statement.setString(5, dto.getZip());
            statement.setString(6, MainController.currentUser);
            statement.setTimestamp(7, Timestamp.valueOf(TimeConverter.toUTC(LocalDateTime.now())));
            statement.setLong(8, dto.getAddressId());
            statement.execute();

            System.out.println("ADDRESS SQL = " + statement); // todo delete
            this.connection.commit();

            address = this.findById(dto.getAddressId());
            System.out.println("Update Address: SUCCESSFUL");
            return address;
        } catch (SQLException e) {

            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

            e.printStackTrace();
            System.out.println("Update Address: FAILED");
            throw new RuntimeException();
        }
    }

    @Override
    public Address create(Address dto) {
        System.out.println("creating address record in DB..."); // todo delete
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT_ADDRESS);) {
            statement.setString(1, dto.getStreet1());
            statement.setString(2, dto.getStreet2());
            statement.setString(3, dto.getCity());
            statement.setString(4, dto.getState());
            statement.setString(5, dto.getZip());
            statement.setString(6, MainController.currentUser);
            statement.setTimestamp(7, Timestamp.valueOf(TimeConverter.toUTC(LocalDateTime.now())));
            statement.execute();


            this.connection.commit();
            System.out.println("Insert Address: SUCCESSFUL");

            int id = this.getLastValue(ADDRESS_SEQUENCE);
            return this.findById(id); // returns student object with info from db
//            return null;
        } catch (SQLException e) {
            try {
                this.connection.rollback();
                System.out.println("Insert Address: FAILED");
            } catch (SQLException sqle) {
                e.printStackTrace();
                throw new RuntimeException(sqle);
            }
            e.printStackTrace();
            throw new RuntimeException(e); // todo why this?
        }
    }


    @Override
    public void delete(long id) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_ADDRESS)) {
            statement.setLong(1, id);
            statement.execute();

            this.connection.commit();
            System.out.println("Delete Address: SUCCESSFUL");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Delete Address: FAILED");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            throw new RuntimeException();
        }

    }
}
