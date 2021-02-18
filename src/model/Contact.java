package model;

public class Contact extends Person {
    private long contactTableId;
    private long studentId;
    private long contactId;
    private String relationship;

    private long receiveAlert;
    private long receiveSummary;

    public long getContactTableId() {
        return contactTableId;
    }

    public void setContactTableId(long contactTableId) {
        this.contactTableId = contactTableId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public long getReceiveAlert() {
        return receiveAlert;
    }

    public void setReceiveAlert(long receiveAlert) {
        this.receiveAlert = receiveAlert;
    }

    public long getReceiveSummary() {
        return receiveSummary;
    }

    public void setReceiveSummary(long receiveSummary) {
        this.receiveSummary = receiveSummary;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactTableId=" + contactTableId +
                ", studentId=" + studentId +
                ", contactId=" + contactId +
                ", relationship='" + relationship + '\'' +
                ", receiveAlert=" + receiveAlert +
                ", receiveSummary=" + receiveSummary +
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
