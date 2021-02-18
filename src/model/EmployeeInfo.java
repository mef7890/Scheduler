package model;

import DAO.DataTransferObject;

import java.math.BigDecimal;

public class EmployeeInfo implements DataTransferObject {

    private long employeeInfoId;
    private long personId;
    private BigDecimal rate;

    public long getEmployeeInfoId() {
        return employeeInfoId;
    }

    public void setEmployeeInfoId(long employeeInfoId) {
        this.employeeInfoId = employeeInfoId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String toString() {
        return "EmployeeInfo{" +
                "employeeInfoId=" + employeeInfoId +
                ", personId=" + personId +
                ", rate=" + rate +
                '}';
    }
}
