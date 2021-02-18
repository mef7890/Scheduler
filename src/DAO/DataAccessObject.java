package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class DataAccessObject <T extends DataTransferObject> {

    protected final Connection connection;
    protected final static String LAST_VALUE = "SELECT last_insert_id() FROM ";
    protected final static String ADDRESS_SEQUENCE = " address";
    protected final static String APPOINTMENT_SEQUENCE = " appointment";
    protected final static String COMMUNICATION_PREF_SEQUENCE = " communication_pref";
    protected final static String EMPLOYEE_INFO_SEQUENCE = " employee_info";
    protected final static String GRADE_SEQUENCE = " grade";
    protected final static String PERSON_SEQUENCE = " person";
    protected final static String PLATFORM_INFO_SEQUENCE = " platform_info";
    protected final static String RELATIONSHIP_SEQUENCE = " contact";
    protected final static String USER_DATA_SEQUENCE = " user_data";
    protected final static String USER_ROLE_SEQUENCE = " user_role";

    protected static final String GET_ONE_PERSON = "SELECT p.person_id, role_id, f_name, l_name, phone, email, " +
            "p.address_id, birthday, start_date " +
            ", street_1, street_2, city, state, zip " +
            "FROM person p " +
            "JOIN address a ON p.address_id = a.address_id " +
            "WHERE p.person_id = ?";

    protected static final String GET_BASIC_PERSON= "SELECT p.person_id, u.user_id, f_name, l_name " +
            "FROM person p " +
            "JOIN user_data u ON p.person_id = u.person_id " +
            "WHERE u.user_id = ?";

    public DataAccessObject(Connection connection) {
        super();
        this.connection = connection;
    }

    public abstract T findById(long id);
    public abstract List<T> findAll();
    public abstract T update(T dto);
    public abstract T create(T dto);
    public abstract void delete(long id);

    protected int getLastValue(String sequence) {
        int key = 0;
        String sql = LAST_VALUE + sequence;
        try (Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                key = rs.getInt(1);
            }
            return key;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
