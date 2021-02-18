package model;

import DAO.DataTransferObject;

public class Relationship implements DataTransferObject {

    private long contactTableId;
    private long studentId;
    private long contactId;
    private String relationship;

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

    @Override
    public long getId() {
        return 0;
    }
}
