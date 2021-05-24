package com.lnh.CourseRegistration.Entities;

import com.lnh.CourseRegistration.Entities.PrimaryKey.PKRegistrationInfo;
import com.lnh.CourseRegistration.Entities.SupportEntities.RegisterStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@IdClass(PKRegistrationInfo.class)
@Table(name = "RegistrationInfo")
public class RegistrationInfo {
    @Id
    @Column(name = "StudentID")
    private long studentID;

    @Id
    @Column(name = "CourseID")
    private long courseID;

    @Column(name = "RegisterTime", insertable = false)
    private Timestamp registerTime;

    @OneToOne
    @JoinColumn(name = "Status")
    private RegisterStatus status;

    @Column(name = "Notes", length = 30)
    private String notes;

    public RegistrationInfo() {
    }

    public RegistrationInfo(
            long studentID, long courseID, Timestamp registerTime,
            RegisterStatus status, String notes
    ) {
        this.studentID = studentID;
        this.courseID = courseID;
        this.registerTime = registerTime;
        this.status = status;
        this.notes = notes;
    }

    public long getStudentID() {
        return studentID;
    }

    public void setStudentID(long studentDBID) {
        this.studentID = studentDBID;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public RegisterStatus getStatus() {
        return status;
    }

    public void setStatus(RegisterStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
