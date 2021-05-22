package com.lnh.CourseRegistration.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Semester")
public class Semester {
    @Id
    @Column(name = "SemesterID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "SemesterName", length = 15)
    private String semesterName;

    @Column(name = "SemesterYear")
    private Integer semesterYear;

    @Column(name = "SemesterStart")
    private Date semesterStart;

    @Column(name = "SemesterEnd")
    private Date semesterEnd;

    @Column(name = "IsCurrentSemester", insertable = false)
    private boolean isCurrentSemester;

    public Semester() {
        id = 0; //Wrapper Integer Class do not have no args constructor
    }

    public Semester(
            int id, String semesterName, Integer semesterYear,
            Date semesterStart, Date semesterEnd, boolean isCurrentSemester
    ) {
        this.id = id;
        this.semesterName = semesterName;
        this.semesterYear = semesterYear;
        this.semesterStart = semesterStart;
        this.semesterEnd = semesterEnd;
        this.isCurrentSemester = isCurrentSemester;
    }


    public int getId() {
        return id;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public Integer getSemesterYear() {
        return semesterYear;
    }

    public Date getSemesterStart() {
        return semesterStart;
    }

    public Date getSemesterEnd() {
        return semesterEnd;
    }

    public boolean isCurrentSemester() {
        return isCurrentSemester;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public void setSemesterYear(Integer semesterYear) {
        this.semesterYear = semesterYear;
    }

    public void setSemesterStart(Date semesterStart) {
        this.semesterStart = semesterStart;
    }

    public void setSemesterEnd(Date semesterEnd) {
        this.semesterEnd = semesterEnd;
    }

    public void setCurrentSemester(boolean currentSemester) {
        isCurrentSemester = currentSemester;
    }
}
