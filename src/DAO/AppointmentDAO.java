package DAO;

import controller.main.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import utilities.Alerts;
import utilities.DBConnection;
import utilities.TimeConverter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentDAO extends DataAccessObject<Appointment> {

    ObservableList<Appointment> allUpcomingAppts = FXCollections.observableArrayList();

    private static final String GET_ONE_APPOINTMENT_BY_TUTOR = "SELECT appointment_id, tutor_id, student_id " +
            "apt_start, apt_end, subject_focus, scheduling_note, summary " +
            "FROM appointment " +
            "WHERE tutor_id = ?"; // limit one???

    private static final String GET_ONE_APPOINTMENT_BY_STUDENT = "SELECT appointment_id, tutor_id, student_id " +
            "apt_start, apt_end, subject_focus, scheduling_note, summary " +
            "FROM appointment " +
            "WHERE student_id = ?";

    protected final static String GET_ALL_SUMMARIES_FOR_STUDENT = "SELECT summary, apt_start " +
            "FROM appointment " +
            "WHERE student_id = ? "; // todo add option to view just "my" notes for student

    private static final String GET_ONE_APPOINTMENT_BY_ID = "SELECT appointment_id, tutor_id, student_id, " +
            "apt_start, apt_end, subject_focus, scheduling_note, summary, " +
            "tp.f_name AS t_fn, tp.l_name AS t_ln, " +
            "sp.f_name AS s_fn, sp.l_name AS s_ln " +
            "FROM appointment a " +
            "JOIN person tp ON tp.person_id = a.tutor_id " +
            "JOIN person sp ON sp.person_id = a.student_id " +
            "WHERE a.appointment_id = ?";

    private static final String GET_ALL_APPOINTMENTS_BY_DATE = "SELECT appointment_id, tutor_id, student_id, " +
            "apt_start, apt_end, subject_focus, scheduling_note, summary, " + // todo might delete sched note, summary, ids
            "tp.f_name AS t_fn, tp.l_name AS t_ln, " +
            "sp.f_name AS s_fn, sp.l_name AS s_ln " +
            "FROM appointment a " +
            "JOIN person tp ON tp.person_id = a.tutor_id " +
            "JOIN person sp ON sp.person_id = a.student_id " +
            "AND a.apt_start >= ? " + // is after/equal to start of month
            "AND a.apt_start < ?" + // is less than the start of the next month
//            "AND a.apt_start <= ?" + // is before/equal to end of month
            "ORDER BY apt_start ASC"; // order by date

    private static final String GET_ALL_APPOINTMENTS_BY_TUTOR_WITHIN_TIME_FRAME = "SELECT appointment_id, tutor_id, student_id, " + // todo
            // might not need tutor/student_id
            "apt_start, apt_end, subject_focus, scheduling_note, summary, " + // todo might delete sched note, summry
            "tp.f_name AS t_fn, tp.l_name AS t_ln, " +
            "sp.f_name AS s_fn, sp.l_name AS s_ln " +
            "FROM appointment a " +
            "JOIN person tp ON tp.person_id = a.tutor_id " +
            "JOIN person sp ON sp.person_id = a.student_id " +
            "WHERE tp.person_id = ? " +
            "AND a.apt_start >= ? " + // is after/equal to start of month
            "AND a.apt_start <= ? " + // is before/equal to end of month
            "ORDER BY apt_start ASC"; // order by date

    private static final String GET_ALL_APPOINTMENTS_BY_STUDENT_WITHIN_TIME_FRAME = "SELECT appointment_id, tutor_id, student_id, " + // todo
            // might not need tutor/student_id
            "apt_start, apt_end, subject_focus, scheduling_note, summary, " + // todo might delete sched note, summry
            "tp.f_name AS t_fn, tp.l_name AS t_ln, " +
            "sp.f_name AS s_fn, sp.l_name AS s_ln " +
            "FROM appointment a " +
            "JOIN person tp ON tp.person_id = a.tutor_id " +
            "JOIN person sp ON sp.person_id = a.student_id " +
            "WHERE sp.person_id = ? " +
            "AND a.apt_start >= ? " + // is after/equal to start of month
            "AND a.apt_start <= ? " + // is before/equal to end of month
            "ORDER BY apt_start ASC"; // order by date
//            "WHERE a.appointment_id = ?";

    private static final String HAS_APPOINTMENT = "Select appointment_id FROM appointment WHERE student_id = ? OR tutor_id = ?";

    private static final String INSERT_APPOINTMENT = "INSERT INTO appointment " + // todo not inc summary, that will
            // be an update to insert it
            "(tutor_id, student_id, apt_start, apt_end, subject_focus, scheduling_note, last_update_by, last_update) " +
            "VALUES(?,?,?,?,?,?,?,?)";

    // checks for overlapping appointment
    private static final String PERSON_HAS_APPT_ON_DATE = "SELECT appointment_id, tutor_id, student_id, apt_start, apt_end, " +
            "tp.f_name AS t_fn, tp.l_name AS t_ln, " + // tutor info
            "sp.f_name AS s_fn, sp.l_name AS s_ln " +  // student info
            "FROM appointment a " +
            "JOIN person tp ON tp.person_id = a.tutor_id " + // tutor info
            "JOIN person sp ON sp.person_id = a.student_id " + // student info
            "WHERE (student_id = ? OR tutor_id = ?) " + // either tutor or student
            "AND (apt_start BETWEEN ? AND ? " + // existing appointment starts in the middle of existing appt
            "OR apt_end BETWEEN ? AND ?" + // existing appointment ends in the middle of existing appt
            "OR (apt_start <= ? AND apt_end >= ?)) " + // new appointment starts and ends in middle of existing appt
            "AND appointment_id <> ? ";  // checks that you're not just updating an appt

    private static final String UPDATE_APPOINTMENT = "UPDATE appointment SET " +
            "tutor_id = ?, student_id = ?, apt_start = ?, apt_end = ?, subject_focus = ?, scheduling_note = ?, last_update_by " +
            "=?, last_update = ? " +
            "WHERE appointment_id = ?";

    private static final String DELETE_APPOINTMENT = "DELETE FROM appointment WHERE appointment_id = ?";

    public AppointmentDAO(Connection connection) {
        super(connection);
    }

    // -----   GET ALL APPOINTMENTS WITHIN A TIME FRAME   ----- //
    public ObservableList<Appointment> getApptBasicInfoForTimeFrame(String monthStart, String monthEnd) { // used to populate table on main screen
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ALL_APPOINTMENTS_BY_DATE)) {
            statement.setString(1, monthStart);
            statement.setString(2, monthEnd);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getLong("appointment_id"));

                // converting stored utc timestamp to local time ldt
                appointment.setApptStartDT(TimeConverter.toLocalTZ(rs.getTimestamp("apt_start").toLocalDateTime()));
                appointment.setApptEndDT(TimeConverter.toLocalTZ(rs.getTimestamp("apt_end").toLocalDateTime()));

                // set date, times from ldt
                appointment.setApptDate(appointment.getApptStartDT().toLocalDate());
                appointment.setApptStartTime(appointment.getApptStartDT().toLocalTime());
                appointment.setApptEndTime(appointment.getApptEndDT().toLocalTime());

                String firstName = rs.getString("t_fn");
                String lastName = rs.getString("t_ln");
                appointment.setTutorName(firstName + " " + lastName);
                firstName = rs.getString("s_fn");
                lastName = rs.getString("s_ln");
                appointment.setStudentName(firstName + " " + lastName);
                allUpcomingAppts.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUpcomingAppts;
    }

    // -----   GET BASIC SUMMARY INFO FOR A SPECIFIC STUDENT   ----- //
    public ObservableList<Appointment> getBasicSummaryInfo(long studentId) { // used in TutorViewStudent
        ObservableList<Appointment> summaryItems = FXCollections.observableArrayList();
        Appointment appointment = new Appointment();

        try (PreparedStatement statement = DBConnection.connection.prepareStatement(GET_ALL_SUMMARIES_FOR_STUDENT)) {
            statement.setLong(1, studentId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                appointment.setSummary(rs.getString("summary"));
                appointment.setApptDate(rs.getDate("apt_start").toLocalDate());
                summaryItems.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summaryItems;
    }

    // todo probably need to fix this
//    public ObservableList<String> getSummaryText(long studentId) {
//        ObservableList<String> summaryItems = FXCollections.observableArrayList();
//        Appointment2 appointment = new Appointment2();
//
//        try (PreparedStatement statement = MainController.connection.prepareStatement(GET_ALL_SUMMARIES_FOR_STUDENT)) {
//            statement.setLong(1, studentId);
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//                appointment.setSummary(rs.getString("summary"));
//                summaryItems.add(appointment.getSummary());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return  summaryItems;
//    }

    // -----   CHECK IF A PERSON HAS ASSOCIATED APPT   ----- //
    // must be true before allowing delete
    public boolean personHasNoAppointment(long personId) {
        try (PreparedStatement statement = this.connection.prepareStatement(HAS_APPOINTMENT)) {
            statement.setLong(1, personId);
            statement.setLong(2, personId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Alerts.associatedAppointmentsError(); // todo check position
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    // -----   GET ALL APPOINTMENTS FOR A TUTOR WITHIN A TIME FRAME   ----- //
    public ObservableList<Appointment> findTutorApptsByDates(long tutorId, String monthStart, String monthEnd) {
        ObservableList<Appointment> foundAppointments = FXCollections.observableArrayList();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ALL_APPOINTMENTS_BY_TUTOR_WITHIN_TIME_FRAME)) {
            statement.setLong(1, tutorId);
            statement.setString(2, monthStart);
            statement.setString(3, monthEnd);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getLong("appointment_id"));
                appointment.setTutorId(rs.getLong("tutor_id"));
                appointment.setStudentId(rs.getLong("student_id"));

                // converting stored utc appt timestamp to local time ldt
                appointment.setApptStartDT(TimeConverter.toLocalTZ(rs.getTimestamp("apt_start").toLocalDateTime()));
                appointment.setApptEndDT(TimeConverter.toLocalTZ(rs.getTimestamp("apt_end").toLocalDateTime()));

                // set date, times from ldt
                appointment.setApptDate(appointment.getApptStartDT().toLocalDate());
                appointment.setApptStartTime(appointment.getApptStartDT().toLocalTime());
                appointment.setApptEndTime(appointment.getApptEndDT().toLocalTime());

                appointment.setSubjectFocus(rs.getString("subject_focus"));
                appointment.setSchedulingNote(rs.getString("scheduling_note"));

                String firstName = rs.getString("t_fn");
                String lastName = rs.getString("t_ln");
                appointment.setTutorName(firstName + " " + lastName);
                firstName = rs.getString("s_fn");
                lastName = rs.getString("s_ln");
                appointment.setStudentName(firstName + " " + lastName);

                foundAppointments.add(appointment);
            }

            return foundAppointments;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // -----   GET ALL APPOINTMENTS FOR A STUDENT WITHIN A TIME FRAME   ----- //
    public ObservableList<Appointment> findStudentByDates(long studentId, String monthStart, String monthEnd) {

        ObservableList<Appointment> foundAppointments = FXCollections.observableArrayList(); // to hold found appts

        try (PreparedStatement statement = this.connection.prepareStatement(GET_ALL_APPOINTMENTS_BY_STUDENT_WITHIN_TIME_FRAME)) {
            statement.setLong(1, studentId);
            statement.setString(2, monthStart);
            statement.setString(3, monthEnd);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getLong("appointment_id"));
                appointment.setTutorId(rs.getLong("tutor_id"));
                appointment.setStudentId(rs.getLong("student_id"));

                // converting stored utc appt timestamp to local time ldt
                appointment.setApptStartDT(TimeConverter.toLocalTZ(rs.getTimestamp("apt_start").toLocalDateTime()));
                appointment.setApptEndDT(TimeConverter.toLocalTZ(rs.getTimestamp("apt_end").toLocalDateTime()));

                // set date, times from ldt
                appointment.setApptDate(appointment.getApptStartDT().toLocalDate());
                appointment.setApptStartTime(appointment.getApptStartDT().toLocalTime());
                appointment.setApptEndTime(appointment.getApptEndDT().toLocalTime());

                appointment.setSubjectFocus(rs.getString("subject_focus"));
                appointment.setSchedulingNote(rs.getString("scheduling_note"));

                String firstName = rs.getString("t_fn");
                String lastName = rs.getString("t_ln");
                appointment.setTutorName(firstName + " " + lastName);
                firstName = rs.getString("s_fn");
                lastName = rs.getString("s_ln");
                appointment.setStudentName(firstName + " " + lastName);

                foundAppointments.add(appointment);
            }

            return foundAppointments;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override // -----   FIND A SPECIFIC APPT BY APPT ID   ----- //
    public Appointment findById(long id) {
        Appointment appointment = new Appointment();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_APPOINTMENT_BY_ID)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                appointment.setAppointmentId(rs.getLong("appointment_id"));
                appointment.setTutorId(rs.getLong("tutor_id"));
                appointment.setStudentId(rs.getLong("student_id"));

                // converting stored utc timestamp to local time ldt
                appointment.setApptStartDT(TimeConverter.toLocalTZ(rs.getTimestamp("apt_start").toLocalDateTime()));
                appointment.setApptEndDT(TimeConverter.toLocalTZ(rs.getTimestamp("apt_end").toLocalDateTime()));

                // set date, times from ldt
                appointment.setApptDate(appointment.getApptStartDT().toLocalDate());
                appointment.setApptStartTime(appointment.getApptStartDT().toLocalTime());
                appointment.setApptEndTime(appointment.getApptEndDT().toLocalTime());

                appointment.setSubjectFocus(rs.getString("subject_focus"));
                appointment.setSchedulingNote(rs.getString("scheduling_note"));

                String firstName = rs.getString("t_fn");
                String lastName = rs.getString("t_ln");
                appointment.setTutorName(firstName + " " + lastName);
                firstName = rs.getString("s_fn");
                lastName = rs.getString("s_ln");
                appointment.setStudentName(firstName + " " + lastName);
            }

            return appointment;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public List<Appointment> findAll() {
        return null;
    }

    @Override // -----   UPDATE APPT   ----- //
    public Appointment update(Appointment dto) {
        Appointment appointment; // removed new Apponitment()
        try {
            this.connection.setAutoCommit(false); // turn off auto commit
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_APPOINTMENT)) {

            statement.setLong(1, dto.getTutorId());
            statement.setLong(2, dto.getStudentId());

            // change to utc before inserting
            statement.setTimestamp(3, Timestamp.valueOf(TimeConverter.toUTC(dto.getApptStartDT())));
            statement.setTimestamp(4, Timestamp.valueOf(TimeConverter.toUTC(dto.getApptEndDT())));

            statement.setString(5, dto.getSubjectFocus());
            statement.setString(6, dto.getSchedulingNote());
            // summary isn't included yet, bc it comes after apt, not at sched time
            statement.setString(7, MainController.currentUser);
            statement.setTimestamp(8, Timestamp.valueOf(TimeConverter.toUTC(LocalDateTime.now())));
            statement.setLong(9, dto.getAppointmentId());

            statement.execute();

            this.connection.commit(); // todo handling commit in student, contact, tutor daos

            appointment = this.findById(dto.getAppointmentId());

        } catch (SQLException e) {
            try {
                this.connection.rollback(); // rollback transaction
                System.out.println("Update Appointment: FAILED");
            } catch (SQLException e2) {
                e2.printStackTrace();
                throw new RuntimeException(e2);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("Update Appointment: SUCCESSFUL");
        return appointment;
    }

    @Override // -----   ADD APPT   ----- //
    public Appointment create(Appointment dto) {
        try {
            this.connection.setAutoCommit(false); // turn off auto commit
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT_APPOINTMENT)) {
            statement.setLong(1, dto.getTutorId());
            statement.setLong(2, dto.getStudentId());

            // change to utc before inserting
            statement.setTimestamp(3, Timestamp.valueOf(TimeConverter.toUTC(dto.getApptStartDT())));
            statement.setTimestamp(4, Timestamp.valueOf(TimeConverter.toUTC(dto.getApptEndDT())));

            statement.setString(5, dto.getSubjectFocus());
            statement.setString(6, dto.getSchedulingNote());
            // summary isn't included yet, bc it comes after apt, not at sched time
            statement.setString(7, MainController.currentUser);
            statement.setTimestamp(8, Timestamp.valueOf(TimeConverter.toUTC(LocalDateTime.now())));

            statement.execute();
            this.connection.commit();
            System.out.println("Insert Appointment: SUCCESSFUL");

            int id = this.getLastValue(APPOINTMENT_SEQUENCE);
            return this.findById(id); // returns appt object with info from db
        } catch (SQLException e) {
            try {
                this.connection.rollback();
                System.out.println("Insert Appointment: FAILED");
            } catch (SQLException e2) {
                e.printStackTrace();
                throw new RuntimeException(e2);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override // -----   DELETE APPT   ----- //
    public void delete(long id) {
        System.out.println("Deleting appointment record for appointmentId = " + id); // todo delete?
        try  {
            this.connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_APPOINTMENT)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // checks if person has pre-existing appt on date in question, and within time range
    public ObservableList<Appointment> allApptsForPersonOnDay(long appointmentId, long studentId, long tutorId,
                                                              LocalDateTime startLDT, LocalDateTime endLDT) {
        ObservableList<Appointment> appointmentsOnDay = FXCollections.observableArrayList(); // todo right kind of list?
            try (PreparedStatement statement = this.connection.prepareStatement(PERSON_HAS_APPT_ON_DATE)) {
                statement.setLong(1, studentId);
                statement.setLong(2, tutorId);
                statement.setTimestamp(3, Timestamp.valueOf(TimeConverter.toUTC(startLDT))); // convert to utc
                statement.setTimestamp(4, Timestamp.valueOf(TimeConverter.toUTC(endLDT))); // convert to utc
                statement.setTimestamp(5, Timestamp.valueOf(TimeConverter.toUTC(startLDT))); // convert to utc
                statement.setTimestamp(6, Timestamp.valueOf(TimeConverter.toUTC(endLDT)));
                statement.setTimestamp(7, Timestamp.valueOf(TimeConverter.toUTC(startLDT))); // convert to utc
                statement.setTimestamp(8, Timestamp.valueOf(TimeConverter.toUTC(endLDT)));
                statement.setLong(9, appointmentId); // if updating existing appt, it shouldn't get flagged

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentId(rs.getLong("appointment_id"));
                    appointment.setStudentId(rs.getLong("student_id"));
                    appointment.setTutorId(rs.getLong("tutor_id"));
                    appointment.setApptStartDT(rs.getTimestamp("apt_start").toLocalDateTime());
                    appointment.setApptEndDT(rs.getTimestamp("apt_end").toLocalDateTime());

                    appointment.setTutorName(rs.getString("t_fn") + " " + rs.getString("t_ln"));
                    appointment.setStudentName(rs.getString("s_fn") + " " + rs.getString("s_ln"));

                    appointmentsOnDay.add(appointment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
//        }
        return appointmentsOnDay;
    }
}
