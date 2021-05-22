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
    @OneToOne
    @JoinColumn(name = "StudentID", nullable = false)
    private Student student;

    @Id
    @OneToOne
    @JoinColumn(name = "CourseID", nullable = false)
    private Course course;

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
            Student student, Course course, Timestamp registerTime, RegisterStatus status, String notes
    ) {
        this.student = student;
        this.course = course;
        this.registerTime = registerTime;
        this.status = status;
        this.notes = notes;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
