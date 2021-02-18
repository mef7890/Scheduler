package model;

import DAO.DataTransferObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment implements DataTransferObject {

    long appointmentId;
    long tutorId;
    long studentId;

    String tutorName;
    String studentName;

    LocalDate apptDate;
    LocalTime apptStartTime;
    LocalTime apptEndTime;
    LocalDateTime apptStartDT;
    LocalDateTime apptEndDT;

    String subjectFocus;
    String schedulingNote;
    String summary;

    public Appointment() {
    }
    public Appointment(
            long tutorId,
            long studentId,

            LocalDateTime apptStartDT,
            LocalDateTime apptEndDT,

            String subjectFocus,
            String schedulingNote,
            String summary) {

        this.tutorId = tutorId;
        this.studentId = studentId;

        this.apptStartDT = apptStartDT;
        this.apptEndDT = apptEndDT;

        this.subjectFocus = subjectFocus;
        this.schedulingNote = schedulingNote;
        this.summary = summary;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public long getTutorId() {
        return tutorId;
    }

    public void setTutorId(long tutorId) {
        this.tutorId = tutorId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDate getApptDate() {
        return apptDate;
    }

    public void setApptDate(LocalDate apptDate) {
        this.apptDate = apptDate;
    }

    public LocalTime getApptStartTime() {
        return apptStartTime;
    }

    public void setApptStartTime(LocalTime apptStartTime) {
        this.apptStartTime = apptStartTime;
    }


    public LocalTime getApptEndTime() {
        return apptEndTime;
    }

    public void setApptEndTime(LocalTime apptEndTime) {
        this.apptEndTime = apptEndTime;
    }

    public LocalDateTime getApptStartDT() {
        return apptStartDT;
    }

    public void setApptStartDT(LocalDateTime apptStartDT) {
        this.apptStartDT = apptStartDT;
    }

    public LocalDateTime getApptEndDT() {
        return apptEndDT;
    }

    public void setApptEndDT(LocalDateTime apptEndDT) {
        this.apptEndDT = apptEndDT;
    }

    public String getSubjectFocus() {
        return subjectFocus;
    }

    public void setSubjectFocus(String subjectFocus) {
        this.subjectFocus = subjectFocus;
    }

    public String getSchedulingNote() {
        return schedulingNote;
    }

    public void setSchedulingNote(String schedulingNote) {
        this.schedulingNote = schedulingNote;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String toString() {
        return "Appointment2{" +
                "appointmentId=" + appointmentId +
                ", tutorId=" + tutorId +
                ", studentId=" + studentId +
                ", tutorName='" + tutorName + '\'' +
                ", studentName='" + studentName + '\'' +
                ", apptDate=" + apptDate +
                ", apptStartTime=" + apptStartTime +
                ", apptEndTime=" + apptEndTime +
                ", apptStartDT=" + apptStartDT +
                ", apptEndDT=" + apptEndDT +
                ", subjectFocus='" + subjectFocus + '\'' +
                ", schedulingNote='" + schedulingNote + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
