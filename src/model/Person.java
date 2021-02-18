package model;

import DAO.DataTransferObject;
import utilities.FormatAndParse;
import utilities.Validator;

import java.time.LocalDate;

public class Person implements DataTransferObject {


    private long personId;
    private long roleId;

    private long activeStatus;

    private String firstName;
    private String lastName;
    private String fullName;

    private String phone;
    private String email;

    private long addressId;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zip;

    private LocalDate birthday;
    private LocalDate startDate;

    public Person() {
    }

    public Person(
        long roleId,
        long activeStatus,
        String firstName,
        String lastName,
        String fullName,
        String phone,
        String email,
        long addressId,
        LocalDate birthday,
        LocalDate startDate) {
            this.roleId = roleId;
            this.activeStatus = activeStatus;
            this.addressId = addressId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.fullName = fullName;
            this.phone = phone;
            this.email = email;
            this.birthday = birthday;
            this.startDate = startDate;

    }
    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(long activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        fullName = firstName + " " + lastName;
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", roleId=" + roleId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", addressId=" + addressId +
                ", street1='" + street1 + '\'' +
                ", street2='" + street2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", birthday=" + birthday +
                ", startDate=" + startDate +
                '}';
    }

    protected boolean personFieldsAreValid() { // todo not working

        // moved to set local var
//        try { // needed to do this since modify tutor doesn't have rateTextField... can rework if add rate text field
//            rateString = FormatAndParse.parseRateString(rateTextField.getText()); // remove '$' and add '.00' if necessary
//        } catch (NullPointerException e) {
//            rateString =tutor.getRate().toString();
//        }

        return (Validator.phoneIsValidLength(phone)) &&
                (Validator.numberDoesNotContainLetters(phone)) &&
                (Validator.textIsValid(firstName)) &&
                (Validator.textIsValid(lastName)) &&
                (Validator.textIsValid(city)) &&
                (Validator.textIsValid(state)) &&
//                (Validator.numberDoesNotContainLetters(rateString)) && // remove $ sign))
                (Validator.zipIsValid(zip)) &&
                (Validator.emailIsValid(email));
    }
}
