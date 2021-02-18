package model;

import DAO.DataTransferObject;

public class Grade implements DataTransferObject {

    long gradeId;
    long personId;
    String grade;

    public Grade(){}

    public Grade(String grade) {
        this.grade = grade;
    }

    public long getGradeId() {
        return gradeId;
    }

    public void setGradeId(long gradeId) {
        this.gradeId = gradeId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public long getId() {
        return 0;
    }
}
