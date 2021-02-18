package model;

import DAO.DataTransferObject;

import java.math.BigDecimal;
import java.util.Currency;

public class Admin extends Person implements DataTransferObject {
    private BigDecimal rate;

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "rate=" + rate +
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
