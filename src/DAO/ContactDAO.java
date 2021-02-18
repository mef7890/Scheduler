package DAO;

import model.*;
import utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ContactDAO extends DataAccessObject<Contact> {

    public ContactDAO(Connection connection) {
        super(connection);
    }

    private static final String GET_ONE_CONTACT ="SELECT contact_table_id, student_id, contact_id, relationship, "+
        "p.person_id, active_status, role_id, f_name, l_name, phone, email, " +
        "p.address_id, birthday, start_date " +
        ", street_1, street_2, city, state, zip " +
//        ", receive_alert, receive_summary " +
        // todo join with receive alerts
        "FROM contact c "+
        "JOIN person p ON c.contact_id = p.person_id " +
//        "JOIN communication_pref cp ON cp.person_id = p.person_id " +
        "JOIN address a ON p.address_id = a.address_id " +
        "WHERE c.student_id = ? ";

    // -----   CREATE A CONTACT OBJECT WITH VALUES RETRIEVED FROM DB   ----- //
    @Override
    public Contact findById(long studentId) {
        Contact contact = new Contact();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_CONTACT)) {
            statement.setLong(1, studentId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                contact.setPersonId(rs.getLong("person_id"));
                System.out.println("contact person ID = " + contact.getPersonId()); // todo delete
                contact.setActiveStatus(rs.getInt("active_status"));
                contact.setFirstName(rs.getString("f_name"));
                contact.setLastName(rs.getString("l_name"));
                contact.setFullName(contact.getFirstName() + " " + contact.getLastName());
                contact.setPhone(rs.getString("phone"));
                contact.setEmail(rs.getString("email"));
                contact.setAddressId(rs.getLong("address_id"));
                contact.setStartDate(rs.getDate("start_date").toLocalDate());
                contact.setBirthday(rs.getDate("birthday").toLocalDate());

                contact.setStreet1(rs.getString("street_1"));
                contact.setStreet2(rs.getString("street_2"));
                contact.setCity(rs.getString("city"));
                contact.setState(rs.getString("state"));
                contact.setZip(rs.getString("zip"));

                contact.setContactTableId(rs.getLong("contact_table_id"));
                contact.setStudentId(rs.getLong("student_id"));
                contact.setContactId(rs.getLong("contact_id"));
                contact.setRelationship(rs.getString("relationship"));

//                contact.setReceiveAlert(rs.getLong("receive_alert")); // todo should be int?
//                contact.setReceiveSummary(rs.getInt("receive_summary")); // todo should be int?
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contact;
    }

    @Override
    public List<Contact> findAll() {
        return null;
    }

    // -----   UPDATE CONTACT VALUES IN DB, RETURN CONTACT OBJECT WITH UPDATED VALUES RETRIEVED FROM DB   ----- //
    @Override
    public Contact update(Contact dto) {
        Contact contact = new Contact();
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PersonDAO personDAO = new PersonDAO(DBConnection.connection);
            AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
            RelationshipDAO relationshipDAO = new RelationshipDAO(DBConnection.connection);

            Address address = new Address();
            address.setAddress_id(dto.getAddressId());
            address.setStreet1(dto.getStreet1());
            address.setStreet2(dto.getStreet2());
            address.setCity(dto.getCity());
            address.setState(dto.getState());
            address.setZip(dto.getZip());

            Person person = new Person();
            person.setActiveStatus(dto.getActiveStatus());
            person.setPersonId(dto.getPersonId());
            person.setAddressId(dto.getAddressId());
            person.setFirstName(dto.getFirstName());
            person.setLastName(dto.getLastName());
            person.setFullName(dto.getFullName());
            person.setPhone(dto.getPhone());
            person.setEmail(dto.getEmail());
            person.setStartDate(dto.getStartDate());
            person.setBirthday(dto.getBirthday());

            Relationship relationship = new Relationship();
            relationship.setRelationship(dto.getRelationship());
            relationship.setStudentId(dto.getStudentId());
            relationship.setContactId(dto.getContactId());
            relationship.setContactTableId(dto.getContactTableId());

            addressDAO.update(address);
            personDAO.update(person);
            relationshipDAO.update(relationship);

            this.connection.commit();
            System.out.println();
            System.out.println("Update Contact: SUCCESSFUL");

            return this.findById(dto.getPersonId());

        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            e.printStackTrace();
            System.out.println("Update Contact: FAILED");
            throw new RuntimeException();
        }
    }

    @Override // -----   CREATE CONTACT   ----- //
    public Contact create(Contact dto) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PersonDAO personDAO = new PersonDAO(DBConnection.connection);
            AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
            RelationshipDAO relationshipDAO = new RelationshipDAO(DBConnection.connection);

            Address address = new Address();
            address.setAddress_id(dto.getAddressId());
            address.setStreet1(dto.getStreet1());
            address.setStreet2(dto.getStreet2());
            address.setCity(dto.getCity());
            address.setState(dto.getState());
            address.setZip(dto.getZip());
            address = addressDAO.create(address);

            Person person = new Person();
            person.setRoleId(5); // contacts are 5
            person.setFirstName(dto.getFirstName());
            person.setLastName(dto.getLastName());
            person.setFullName(dto.getFullName());
            person.setPhone(dto.getPhone());
            person.setEmail(dto.getEmail());
            person.setStartDate(dto.getStartDate());
            person.setBirthday(dto.getBirthday());
            person.setAddressId(address.getAddressId()); // from insert
            person = personDAO.create(person);

            Relationship relationship = new Relationship();
            relationship.setRelationship(dto.getRelationship());
            relationship.setStudentId(dto.getStudentId());
            relationship.setContactId(person.getPersonId()); // from insert
            relationship = relationshipDAO.create(relationship);
            relationship.setContactTableId(relationship.getContactTableId()); // from insert

            this.connection.commit();
            System.out.println("Insert Contact: SUCCESSFUL");
            return this.findById(person.getPersonId());

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            System.out.println("Insert Contact: FAILED");
            throw new RuntimeException();
        }
    }

    @Override // -----   DELETE CONTACT   ----- //
    public void delete(long studentId) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PersonDAO personDAO = new PersonDAO(DBConnection.connection);
            AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
            RelationshipDAO relationshipDAO = new RelationshipDAO(DBConnection.connection);

            Contact contact = new ContactDAO(DBConnection.connection).findById(studentId);

            relationshipDAO.delete(contact.getStudentId());
            personDAO.delete(contact.getPersonId());
            addressDAO.delete(contact.getAddressId());

            this.connection.commit();
            System.out.println("Delete Contact: SUCCESSFUL");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Delete Contact: FAILED");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}
