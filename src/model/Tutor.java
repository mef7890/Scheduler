package model;

import utilities.Validator;

import java.math.BigDecimal;

public class Tutor extends Person {
    private String tutorMajor;

    private String tutorGrade;
    private long gradeId;
    private long platformId;
    private String tutorPlatform;
    private String tutorHandle;

    private int tutorReceiveAlert;
    private int tutorReceiveSummary;

    private long employeeInfoId;
    private BigDecimal rate;

    public String getTutorMajor() {
        return tutorMajor;
    } // todo include?

    public Tutor() {

    }

    public Tutor(Person person, Address address, Grade grade, PlatformInfo platformInfo, EmployeeInfo employeeInfo) {
        this.setPersonId(person.getPersonId());
        this.setActiveStatus(person.getActiveStatus());
        this.setRoleId(person.getRoleId());

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
        this.setTutorGrade(grade.getGrade());

        this.setPlatformId(platformInfo.getPlatformInfoId());
        this.setTutorPlatform(platformInfo.getPlatform());
        this.setTutorHandle(platformInfo.getHandle());

        this.setEmployeeInfoId(employeeInfo.getEmployeeInfoId());
        this.setRate(employeeInfo.getRate());
    }

    public void setTutorMajor(String tutorMajor) {
        this.tutorMajor = tutorMajor;
    }

    public int getTutorReceiveAlert() {
        return tutorReceiveAlert;
    }

    public void setTutorReceiveAlert(int tutorReceiveAlert) {
        this.tutorReceiveAlert = tutorReceiveAlert;
    }

    public int getTutorReceiveSummary() {
        return tutorReceiveSummary;
    }

    public void setTutorReceiveSummary(int tutorReceiveSummary) {
        this.tutorReceiveSummary = tutorReceiveSummary;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getTutorGrade() {
        return tutorGrade;
    }

    public void setTutorGrade(String tutorGrade) {
        this.tutorGrade = tutorGrade;
    }

    public long getGradeId() {
        return gradeId;
    }

    public void setGradeId(long gradeId) {
        this.gradeId = gradeId;
    }

    public long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(long platformId) {
        this.platformId = platformId;
    }

    public long getEmployeeInfoId() {
        return employeeInfoId;
    }

    public void setEmployeeInfoId(long employeeInfoId) {
        this.employeeInfoId = employeeInfoId;
    }

    public String getTutorPlatform() {
        return tutorPlatform;
    }

    public void setTutorPlatform(String tutorPlatform) {
        this.tutorPlatform = tutorPlatform;
    }

    public String getTutorHandle() {
        return tutorHandle;
    }

    public void setTutorHandle(String tutorHandle) {
        this.tutorHandle = tutorHandle;
    }

    @Override
    public String toString() {
        return "Tutor2{" +
                "tutorGrade='" + getTutorGrade() + '\'' +
                ", tutorPlatform='" + getTutorPlatform() + '\'' +
                ", tutorHandle='" + getTutorHandle() + '\'' +
//                ", tutorReceiveAlert=" + tutorReceiveAlert +
//                ", tutorReceiveSummary=" + tutorReceiveSummary +
                ", rate=" + getRate() +
                ", personId=" + getPersonId() +
                ", roleId=" + getRoleId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", fullName ='" + getFullName() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", addressId=" + getAddressId() +
                ", street1='" + getStreet1() + '\'' +
                ", street2='" + getStreet2() + '\'' +
                ", city='" + getCity() + '\'' +
                ", state='" + getState() + '\'' +
                ", zip='" + getZip() + '\'' +
                ", birthday=" + getBirthday() +
                ", startDate=" + getStartDate() +
                '}';
    }
}
