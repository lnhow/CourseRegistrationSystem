package com.lnh.CourseRegistration.Entities;

import javax.persistence.*;

@Entity
@Table(name = "Subject")
public class Subject {
    @Id
    @Column(name = "SubjectID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "SubjectShort", length = 10)
    private String shortName;

    @Column(name = "SubjectName", length = 50)
    private String subjectName;

    @Column(name = "NumCredit")
    private Integer numCredit;

    public Subject() {
    }

    public Subject(Integer id, String shortName, String subjectName, Integer numCredit) {
        this.id = id;
        this.shortName = shortName;
        this.subjectName = subjectName;
        this.numCredit = numCredit;
    }

    public Integer getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public Integer getNumCredit() {
        return numCredit;
    }


    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setNumCredit(Integer numCredit) {
        this.numCredit = numCredit;
    }
}
