package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import utilities.DBConnection;

import java.sql.*;
import java.util.List;

public class TutorDAO extends DataAccessObject<Tutor> {

    ObservableList<Person> allTutors = FXCollections.observableArrayList();

    private final static String GET_ALL_ACTIVE_TUTORS = "SELECT person_id, f_name, l_name " +
            "FROM person " +
            "WHERE role_id = 3 " +
            "AND active_status = 1 " +
            "ORDER BY f_name ASC"; // todo new

    public ObservableList<Person> getAllTutors() {
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(GET_ALL_ACTIVE_TUTORS)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Person person = new Person();
                person.setPersonId(rs.getLong("person_id"));
                person.setFirstName(rs.getString("f_name"));
                person.setLastName(rs.getString("l_name"));
                person.setFullName(person.getFirstName() + " " + person.getLastName());
                allTutors.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allTutors;
    }

    public TutorDAO(Connection connection) {
        super(connection);
    }

    public Tutor getBasicData(long id) {
        Tutor tutor = new Tutor();
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(DataAccessObject.GET_BASIC_PERSON)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                tutor.setPersonId(rs.getLong("person_id"));
                tutor.setFirstName(rs.getString("f_name"));
                tutor.setLastName(rs.getString("l_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return tutor;
    }

    protected static final String GET_ONE_TUTOR = "SELECT p.person_id, role_id, active_status, f_name, l_name, phone," +
            " email, " +
            "p.address_id, birthday, start_date " +
            ", street_1, street_2, city, state, zip " +
            ", platform, handle " +
            ", rate " +
            ", grade " +
            "FROM person p " +
            "JOIN address a ON p.address_id = a.address_id " +
            "JOIN platform_info pi ON pi.person_id = p.person_id " +
            "JOIN employee_info e ON p.person_id = e.person_id " +
            "JOIN grade g ON p.person_id = g.person_id " +
            "WHERE p.person_id = ?";

    @Override // -----   CREATE A TUTOR OBJ FROM DB   ----- //
    public Tutor findById(long id) {
        AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
        PersonDAO personDAO = new PersonDAO(DBConnection.connection);
        GradeDAO gradeDAO = new GradeDAO(DBConnection.connection);
        PlatformInfoDAO platformInfoDAO = new PlatformInfoDAO(DBConnection.connection);
        EmployeeInfoDAO employeeInfoDAO = new EmployeeInfoDAO(DBConnection.connection);

        Person person = personDAO.findById(id);
        Address address = addressDAO.findById(person.getAddressId());
        Grade grade = gradeDAO.findById(person.getPersonId());
        PlatformInfo platformInfo = platformInfoDAO.findById(person.getPersonId());
        EmployeeInfo employeeInfo = employeeInfoDAO.findById(person.getPersonId());

        Tutor tutor = new Tutor(person, address, grade, platformInfo, employeeInfo);
        System.out.println("Find tutor by id = SUCCESSFUL");
        return tutor;
    }

    @Override // -----   UPDATE ADDRESS, PERSON, GRADE, PLATFORM, EMPLOYEE RECORDS, RETURN TUTOR OBJ   ----- //
    public Tutor update(Tutor dto) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Address address = new Address();
            address.setAddress_id(dto.getAddressId());
            address.setStreet1(dto.getStreet1());
            address.setStreet2(dto.getStreet2());
            address.setCity(dto.getCity());
            address.setState(dto.getState());
            address.setZip(dto.getZip());

            Person person = new Person();
            person.setRoleId(3);
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

            Grade grade = new Grade();
            grade.setGradeId(dto.getGradeId());
            grade.setGrade(dto.getTutorGrade());

            PlatformInfo platformInfo = new PlatformInfo();
            platformInfo.setPlatformInfoId(dto.getPlatformId());
            platformInfo.setPlatform(dto.getTutorPlatform());
            platformInfo.setHandle(dto.getTutorHandle());

            EmployeeInfo employeeInfo = new EmployeeInfo();
            employeeInfo.setEmployeeInfoId(dto.getEmployeeInfoId());
            employeeInfo.setRate(dto.getRate());

            PersonDAO personDAO = new PersonDAO(DBConnection.connection);
            AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
            GradeDAO gradeDAO = new GradeDAO(DBConnection.connection);
            PlatformInfoDAO platformInfoDAO = new PlatformInfoDAO(DBConnection.connection);
            EmployeeInfoDAO employeeInfoDAO = new EmployeeInfoDAO(DBConnection.connection);

            addressDAO.update(address);
            personDAO.update(person);
            gradeDAO.update(grade);
            platformInfoDAO.update(platformInfo);
            employeeInfoDAO.update(employeeInfo);

            // todo add employee info
            // todo add communication pref
            this.connection.commit();
            System.out.println("Update Student: SUCCESSFUL");
            return this.findById(dto.getPersonId());

        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            e.printStackTrace();
            System.out.println("Update Student: FAILED");
            throw new RuntimeException();
        }
    }

    @Override
    public List<Tutor> findAll() { // todo wouldn't let me return Tutor
        return null;
    }


    @Override // -----   CREATE ADDRESS, PERSON, GRADE, PLATFORM, EMPLOYEE RECORDS, RETURN TUTOR OBJ   ----- //
    public Tutor create(Tutor dto) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PersonDAO personDAO = new PersonDAO(DBConnection.connection);
            AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
            GradeDAO gradeDAO = new GradeDAO(DBConnection.connection);
            PlatformInfoDAO platformInfoDAO = new PlatformInfoDAO(DBConnection.connection);
            EmployeeInfoDAO employeeInfoDAO = new EmployeeInfoDAO(DBConnection.connection);

            Address address = new Address();
            address.setAddress_id(dto.getAddressId());
            address.setStreet1(dto.getStreet1());
            address.setStreet2(dto.getStreet2());
            address.setCity(dto.getCity());
            address.setState(dto.getState());
            address.setZip(dto.getZip());
            address = addressDAO.create(address);

            Person person = new Person();
            person.setRoleId(3);
            person.setFirstName(dto.getFirstName());
            person.setLastName(dto.getLastName());
            person.setFullName(dto.getFullName());
            person.setPhone(dto.getPhone());
            person.setEmail(dto.getEmail());
            person.setStartDate(dto.getStartDate());
            person.setBirthday(dto.getBirthday());
            person.setAddressId(address.getAddressId()); // from insert
            person = personDAO.create(person);

            Grade grade = new Grade();
            grade.setPersonId(person.getPersonId()); // from insert
            grade.setGrade(dto.getTutorGrade());
            grade = gradeDAO.create(grade);
            grade.setGradeId(grade.getGradeId()); // from insert

            PlatformInfo platformInfo = new PlatformInfo();
            platformInfo.setPlatform(dto.getTutorPlatform());
            platformInfo.setHandle(dto.getTutorHandle());
            platformInfo.setPersonId(person.getPersonId());
            System.out.println("calling platform info create");
            platformInfo = platformInfoDAO.create(platformInfo);
            platformInfo.setPlatformInfoId(platformInfo.getPlatformInfoId()); // from insert

            EmployeeInfo employeeInfo = new EmployeeInfo();
            employeeInfo.setPersonId(person.getPersonId());
            employeeInfo.setRate(dto.getRate());
            employeeInfo = employeeInfoDAO.create(employeeInfo);
            employeeInfo.setEmployeeInfoId(employeeInfo.getEmployeeInfoId());

            // todo add communication pref
            this.connection.commit();
            System.out.println("Insert Tutor: SUCCESSFUL");
            return this.findById(person.getPersonId());

        }catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            e.printStackTrace();
            System.out.println("Insert Tutor: FAILED");
            throw new RuntimeException();
        }

    }

    @Override // -----   DELETE ADDRESS, PERSON, GRADE, PLATFORM, EMPLOYEE RECORDS   ----- //
    public void delete(long id) {
        PersonDAO personDAO = new PersonDAO(DBConnection.connection);
        AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
        GradeDAO gradeDAO = new GradeDAO(DBConnection.connection);
        PlatformInfoDAO platformInfoDAO = new PlatformInfoDAO(DBConnection.connection);
        EmployeeInfoDAO employeeInfoDAO = new EmployeeInfoDAO(DBConnection.connection);

        Person person = personDAO.findById(id); //  could be more efficient?
        gradeDAO.delete(person.getPersonId());
        platformInfoDAO.delete(person.getPersonId());
        employeeInfoDAO.delete(person.getPersonId());
        personDAO.delete(person.getPersonId());
        addressDAO.delete(person.getAddressId());
    }
}

