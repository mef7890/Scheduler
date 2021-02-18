package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Student extends Person { // removed implements DataTransferObject

    private int studentAge; // derived

    private long gradeId;
    private String studentGrade;

    private long contactId;

    private long platformId;

    private int studentReceiveAlerts;
    private int studentReceiveSummary;

    private String studentHandle;
    private String studentPlatform;

    public Student() {

    }


    public Student(Person person, Address address, Grade grade, PlatformInfo platformInfo) {
        this.setPersonId(person.getPersonId());
        this.setActiveStatus(person.getActiveStatus());

        this.setFirstName(person.getFirstName());
        this.setLastName(person.getLastName());
        this.setFullName(person.getFullName());
        this.setActiveStatus(person.getActiveStatus());
        this.setPhone(person.getPhone());
        this.setEmail(person.getEmail());
        this.setAddressId(person.getAddressId());
        this.setStartDate(person.getStartDate());
        this.setBirthday(person.getBirthday());

        this.setAddressId(address.getAddressId());
        this.setStreet1(address.getStreet1());
        this.setStreet2(address.getStreet2());
        this.setCity(address.getCity());
        this.setState(address.getState());
        this.setZip(address.getZip());

        this.setGradeId(grade.getGradeId());
        this.setStudentGrade(grade.getGrade());

        this.setPlatformId(platformInfo.getPlatformInfoId());
        this.setStudentPlatform(platformInfo.getPlatform());
        this.setStudentHandle(platformInfo.getHandle());

        this.setStudentAge();
    }

    public int getStudentAge() { // todo maybe calculate age here
        LocalDate birthday = getBirthday();
        LocalDate now = LocalDate.now();
        Duration diff = Duration.between(birthday.atStartOfDay(), now.atStartOfDay());
        studentAge = (int) (diff.toDays() / 365);
        return studentAge;
    }

    public void setStudentAge(int studentAge) {
        this.studentAge = studentAge;
    }

    public void setStudentAge() {
        LocalDate birthday = getBirthday();
        LocalDate now = LocalDate.now();
        Duration diff = Duration.between(birthday.atStartOfDay(), now.atStartOfDay());
        studentAge = (int) (diff.toDays() / 365);
    }

    public long getGradeId() {
        return gradeId;
    }

    public void setGradeId(long gradeId) {
        this.gradeId = gradeId;
    }

    public String getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(String studentGrade) {
        this.studentGrade = studentGrade;
    }

    public int getStudentReceiveAlerts() {
        return studentReceiveAlerts;
    }

    public void setStudentReceiveAlerts(int studentReceiveAlerts) {
        this.studentReceiveAlerts = studentReceiveAlerts;
    }

    public int getStudentReceiveSummary() {
        return studentReceiveSummary;
    }

    public void setStudentReceiveSummary(int studentReceiveSummary) {
        this.studentReceiveSummary = studentReceiveSummary;
    }

    public long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(long platformId) {
        this.platformId = platformId;
    }

    public String getStudentHandle() {
        return studentHandle;
    }

    public void setStudentHandle(String studentHandle) {
        this.studentHandle = studentHandle;
    }

    public String getStudentPlatform() {
        return studentPlatform;
    }

    public void setStudentPlatform(String studentPlatform) {
        this.studentPlatform = studentPlatform;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + getPersonId() +
                ", studentFirstName='" + getFirstName() + '\'' +
                ", studentLastName='" + getLastName() + '\'' +
                ", active=" + getActiveStatus() +
                ", studentBirthday=" + getBirthday() +
                ", studentAge=" + getStudentAge() +
                ", studentGrade='" + studentGrade + '\'' +
                ", studentAddressId=" + getAddressId() +
                ", contactId=" + contactId +
                ", address=" + getAddressId() +
                ", studentReceiveAlerts=" + studentReceiveAlerts +
                ", studentReceiveSummary=" + studentReceiveSummary +
                ", studentPhone='" + getPhone() + '\'' +
                ", studentEmail='" + getEmail() + '\'' +
                ", studentHandle='" + studentHandle + '\'' +
                ", platform='" + studentPlatform + '\'' +
                '}';
    }
}
