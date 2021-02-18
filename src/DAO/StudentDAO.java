package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import utilities.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class StudentDAO extends DataAccessObject<Student> {

    private final static String GET_ALL_ACTIVE_STUDENTS = "SELECT person_id, f_name, l_name " +
            "FROM person " +
            "WHERE role_id = 4 " +
            "AND active_status = 1 " +
            "ORDER BY f_name ASC";

    private final static String GET_STUDENTS_BY_TUTOR = "SELECT DISTINCT p.person_id, f_name, l_name " +
            "FROM appointment a " +
            "JOIN person p " +
            "ON a.student_id = p.person_id " +
            "WHERE a.tutor_id =  ? " +
            "ORDER BY f_name ASC";


    private static final String STUDENTS_NEXT_APT_WITH_TUTOR = "SELECT apt_start FROM appointment " +
            "WHERE tutor_id = ? " +
            "AND student_id = ? " +
            "AND apt_start >= curdate() " + // todo new
            "ORDER BY apt_start ASC " +
            "LIMIT 1";

    private static final String GET_STUDENTS_NEXT_APPOINTMENT = "SELECT apt_start, f_name, l_name " + // get appt dets for next
            // appt
            "FROM appointment a " +
            "JOIN person p ON a.tutor_id = p.person_id " +
            "WHERE student_id = ? " +
            "AND apt_start >= date_sub(curdate(), INTERVAL 1 DAY) " + // todo check this
            "ORDER BY apt_start ASC " +
            "LIMIT 1";

    ObservableList<Person> allStudents = FXCollections.observableArrayList();

    public StudentDAO(Connection connection) {
        super(connection);
    }

    public ObservableList<Person> getAllStudents() {
        try (PreparedStatement statement = DBConnection.connection.prepareStatement(GET_ALL_ACTIVE_STUDENTS)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Person person = new Person();
                person.setPersonId(rs.getLong("person_id"));
                person.setFirstName(rs.getString("f_name"));
                person.setLastName(rs.getString("l_name"));
                person.setFullName(person.getFirstName() + " " + person.getLastName());
                allStudents.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allStudents;
    }
    public Appointment getStudentsNextAppointment(long studentId) {
        Appointment appointment = new Appointment();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_STUDENTS_NEXT_APPOINTMENT)) {
            statement.setLong(1, studentId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                LocalDateTime nextApt;
                String tutorName;
                LocalDate nextAptDate = rs.getDate("apt_start").toLocalDate();
                LocalTime nextAptTime = rs.getTime("apt_start").toLocalTime();
                nextApt = LocalDateTime.of(nextAptDate, nextAptTime);
                appointment.setApptStartDT(nextApt);

                String fName = rs.getString("f_name");
                String lName = rs.getString("l_name");
                tutorName = fName + " " + lName;
                appointment.setTutorName(tutorName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    public LocalDateTime getStudentsNextAptWithTutor(long tutorId, long studentId) {
        LocalDateTime nextApt = null;
        try (PreparedStatement statement = this.connection.prepareStatement(STUDENTS_NEXT_APT_WITH_TUTOR)) {
            statement.setLong(1, tutorId);
            statement.setLong(2, studentId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                LocalDate nextAptDate = rs.getDate("apt_start").toLocalDate();
                LocalTime nextAptTime = rs.getTime("apt_start").toLocalTime();
                nextApt = LocalDateTime.of(nextAptDate, nextAptTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextApt;
    }


    @Override // -----   CREATE A STUDENT OBJECT WITH VALUES RETRIEVED FROM DB   ----- //
    public Student findById(long id) {
        // use all the find by ids, then put them into the overloaded person constructor
        AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
        PersonDAO personDAO = new PersonDAO(DBConnection.connection);
        GradeDAO gradeDAO = new GradeDAO(DBConnection.connection);
        PlatformInfoDAO platformInfoDAO = new PlatformInfoDAO(DBConnection.connection);
//        ContactDAO contactDAO = new ContactDAO(MainController.connection); // todo delete?

        Address address; // removed new X();
        Person person;
        Grade grade;
        PlatformInfo platformInfo;
//        Contact contact = new Contact(); // todo delete

        person = personDAO.findById(id);
        address = addressDAO.findById(person.getAddressId());
        grade = gradeDAO.findById(person.getPersonId());
        platformInfo = platformInfoDAO.findById(person.getPersonId());

        return new Student(person, address, grade, platformInfo);
    }

    public ObservableList<Person> getStudentsByTutor(long tutorId) {
        ObservableList<Person> allStudentsForTutor = FXCollections.observableArrayList();

        try (PreparedStatement statement = DBConnection.connection.prepareStatement(GET_STUDENTS_BY_TUTOR)) {
            statement.setLong(1, tutorId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setPersonId(rs.getLong("person_id"));
                student.setFirstName(rs.getString("f_name"));
                student.setLastName(rs.getString("l_name"));
                student.setFullName(student.getFirstName() + " " + student.getLastName());
                allStudentsForTutor.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allStudentsForTutor;
    }

    @Override
    public List<Student> findAll() {
        return null;
    }



    @Override // -----   UPDATE RECORDS FOR PERSON, ADDRESS, GRADE, PLATFORM, RETURN STUDENT OBJ   ----- //
    public Student update(Student dto) {
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
            person.setRoleId(4);
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
            grade.setGrade(dto.getStudentGrade());

            PlatformInfo platformInfo = new PlatformInfo();
            platformInfo.setPlatformInfoId(dto.getPlatformId());
            platformInfo.setPlatform(dto.getStudentPlatform());
            platformInfo.setHandle(dto.getStudentHandle());

            PersonDAO personDAO = new PersonDAO(DBConnection.connection);
            AddressDAO addressDAO = new AddressDAO(DBConnection.connection);
            GradeDAO gradeDAO = new GradeDAO(DBConnection.connection);
            PlatformInfoDAO platformInfoDAO = new PlatformInfoDAO(DBConnection.connection);

            addressDAO.update(address);
            personDAO.update(person);
            gradeDAO.update(grade);
            platformInfoDAO.update(platformInfo);

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

    @Override // -----   CREATE RECORDS FOR PERSON, ADDRESS, GRADE, PLATFORM, RETURN STUDENT OBJ   ----- //
    public Student create(Student dto) {
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
            // todo should probably include contact here, too

            Address address = new Address();
            address.setStreet1(dto.getStreet1());
            address.setStreet2(dto.getStreet2());
            address.setCity(dto.getCity());
            address.setState(dto.getState());
            address.setZip(dto.getZip());
            address = addressDAO.create(address);

            Person person = new Person();
            person.setRoleId(4); // students are 4
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
            grade.setGrade(dto.getStudentGrade());
            grade = gradeDAO.create(grade);
            grade.setGradeId(grade.getGradeId()); // from insert

            PlatformInfo platformInfo = new PlatformInfo();
            platformInfo.setPersonId(person.getPersonId());
            platformInfo.setPlatformInfoId(dto.getPlatformId());
            platformInfo.setPlatform(dto.getStudentPlatform());
            platformInfo.setHandle(dto.getStudentHandle());
            platformInfo = platformInfoDAO.create(platformInfo);
            platformInfo.setPlatformInfoId(platformInfo.getPlatformInfoId()); // from insert

            // todo add employee info
            // todo add communication pref
            this.connection.commit();
            System.out.println("Insert Student: SUCCESSFUL");
            return this.findById(person.getPersonId());

        }catch (SQLException e) {
        try {
            this.connection.rollback();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        e.printStackTrace();
        System.out.println("Insert Student: FAILED");
        throw new RuntimeException();
    }

    }

    @Override // -----   DELETE PERSON, ADDRESS, GRADE, PLATFORM, CONTACT RECORDS   ----- //
    public void delete(long studentPersonId) {
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
            ContactDAO contactDAO = new ContactDAO(DBConnection.connection);
            // todo delete contact unless it's associated with more than one student

            Person person = personDAO.findById(studentPersonId); // could be more efficient?
            long addressId = person.getAddressId();
            contactDAO.delete(studentPersonId); // studentID is correct, I find contact by studentId
            gradeDAO.delete(studentPersonId);
            platformInfoDAO.delete(person.getPersonId());
            personDAO.delete(person.getPersonId());
            addressDAO.delete(addressId);

            this.connection.commit();
            System.out.println("Delete Student: SUCCESSFUL");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Delete Student: FAILED");
            try {
                this.connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}

