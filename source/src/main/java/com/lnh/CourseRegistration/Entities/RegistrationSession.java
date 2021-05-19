package com.lnh.CourseRegistration.Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "RegistrationSession")
public class RegistrationSession {
    @Id
    @Column(name = "SessionID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "SemesterID")
    private Semester semester;

    @Column(name = "SessionStart")
    private Timestamp sessionStart;

    @Column(name = "SessionEnd")
    private Timestamp sessionEnd;

    @Column(name = "CreatedAt")
    private Timestamp createdAt;

    @OneToOne
    @JoinColumn(name = "CreatedBy")
    private Staff createdBy;

    public RegistrationSession() {
    }

    public RegistrationSession(
            long id, Semester semester, Timestamp sessionStart,
            Timestamp sessionEnd, Timestamp createdAt, Staff createdBy
    ) {
        this.id = id;
        this.semester = semester;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public RegistrationSession(Semester semester, Staff createdBy) {
        this.semester = semester;
        this.createdBy = createdBy;
    }

    public long getId() {
        return id;
    }

    public Semester getSemester() {
        return semester;
    }

    public Timestamp getSessionStart() {
        return sessionStart;
    }

    public Timestamp getSessionEnd() {
        return sessionEnd;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Staff getCreatedBy() {
        return createdBy;
    }

    public void setSessionStart(Timestamp sessionStart) {
        this.sessionStart = sessionStart;
    }

    public void setSessionEnd(Timestamp sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public void setCreatedBy(Staff createdBy) {
        this.createdBy = createdBy;
    }
}
