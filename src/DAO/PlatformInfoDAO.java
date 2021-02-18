package DAO;

import controller.main.MainController;
import model.PlatformInfo;
import utilities.TimeConverter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class PlatformInfoDAO extends DataAccessObject<PlatformInfo> {

    private static final String INSERT_PLATFORM_INFO = "INSERT INTO platform_info( " +
            "person_id, platform, handle, last_update_by, last_update) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ONE_PLATFORM_INFO = "SELECT platform_id, person_id, platform, handle " +
            "FROM platform_info " +
            "WHERE person_id = ?";

    private static final String UPDATE_PLATFORM_INFO = "UPDATE platform_info SET " +
            "platform = ?, handle = ?, last_update_by = ?, last_update = ? " + // todo prob don't need person_id
            "WHERE platform_id = ?";

    private static final String DELETE_PLATFORM_INFO = "DELETE FROM platform_info " +
            "WHERE person_id = ?";

    public PlatformInfoDAO(Connection connection) {
        super(connection);
    }

    // -----   CREATE A PLATFORM OBJECT WITH VALUES RETRIEVED FROM DB   ----- //
    @Override
    public PlatformInfo findById(long id) {
        PlatformInfo platformInfo = new PlatformInfo();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_PLATFORM_INFO)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                platformInfo.setPlatformInfoId(rs.getLong("platform_id"));
                platformInfo.setPersonId(rs.getLong("person_id"));
                platformInfo.setPlatform(rs.getString("platform"));
                platformInfo.setHandle(rs.getString("handle"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Retrieve Platform Info: FAILED");
            throw new RuntimeException();
        }
        System.out.println("Retrieve Platform Info: SUCCESSFUL");
        return platformInfo;
    }

    @Override
    public List<PlatformInfo> findAll() {
        return null;
    }

    @Override // -----   UPDATE PLATFORM RECORD, RETURN PLATFORM OBJ   ----- //
    public PlatformInfo update(PlatformInfo dto) {
        PlatformInfo platformInfo = new PlatformInfo();
        try{
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_PLATFORM_INFO)) {
//            statement.setLong(1, dto.getPersonId());
            statement.setString(1, dto.getPlatform());
            statement.setString(2, dto.getHandle());
            statement.setString(3, MainController.currentUser);
            statement.setTimestamp(4, Timestamp.valueOf(TimeConverter.toUTC(LocalDateTime.now())));

            statement.setLong(5, dto.getPlatformInfoId());

            statement.execute();

            this.connection.commit();
            System.out.println("Update Platform Info: SUCCESSFUL");

            platformInfo = this.findById(dto.getPlatformInfoId());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Update Platform Info: FAILED");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            throw new RuntimeException();
        }
        return platformInfo;
    }

    @Override // -----   CREATE PLATFORM RECORD, RETURN PLATFORM OBJ   ----- //
    public PlatformInfo create(PlatformInfo dto) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT_PLATFORM_INFO)) {
            statement.setLong(1, dto.getPersonId());
            statement.setString(2, dto.getPlatform());
            statement.setString(3, dto.getHandle());
            statement.setString(4, MainController.currentUser);
            statement.setTimestamp(5, Timestamp.valueOf(TimeConverter.toUTC(LocalDateTime.now())));


            statement.execute();
            this.connection.commit();

            System.out.println("Insert Platform Info: SUCCESSFUL");
            int id = this.getLastValue(PLATFORM_INFO_SEQUENCE);
            return this.findById(id); // returns person object with info from db

        } catch (SQLException e) {
            System.out.println("Insert Platform Info: FAILED");
            e.printStackTrace();
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            throw new RuntimeException(); // b/c this, return statement ok up there
        }
    }

    @Override // -----   DELETE PLATFORM RECORD   ----- //
    public void delete(long id) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_PLATFORM_INFO)) {
            statement.setLong(1, id);
            statement.execute();

            this.connection.commit();
            System.out.println("Delete Platform Info: SUCCESSFUL");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Delete Platform Info: FAILED");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}
