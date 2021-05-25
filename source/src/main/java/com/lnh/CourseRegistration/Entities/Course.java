package com.lnh.CourseRegistration.Entities;

import com.lnh.CourseRegistration.Entities.SupportEntities.Shift;
import com.lnh.CourseRegistration.Entities.SupportEntities.Weekday;

import javax.persistence.*;

@Entity
@Table(name = "Course")
public class Course {
    @Id
    @Column(name = "CourseID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "RoomName", length = 15)
    private String roomName;

    @Column(name = "TeacherName", length = 50)
    private String teacherName;

    @Column(name = "MaxSlot")
    private int maxSlot;

    @OneToOne
    @JoinColumn(name = "Subject")
    private Subject subject;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "ClassID")
    private ClassInfo classInfo;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "Weekday")
    private Weekday weekday;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "Shift")
    private Shift shift;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "SemesterID")
    private Semester semester;

    public Course() {
    }

    public Course(
            long id, String roomName, String teacherName,
            int maxSlot, Subject subject, ClassInfo classInfo,
            Weekday weekday, Shift shift, Semester semester
    ) {
        this.id = id;
        this.roomName = roomName;
        this.teacherName = teacherName;
        this.maxSlot = maxSlot;
        this.subject = subject;
        this.classInfo = classInfo;
        this.weekday = weekday;
        this.shift = shift;
        this.semester = semester;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setMaxSlot(int maxSlot) {
        this.maxSlot = maxSlot;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getMaxSlot() {
        return maxSlot;
    }

    public Subject getSubject() {
        return subject;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public Shift getShift() {
        return shift;
    }

    public Semester getSemester() {
        return semester;
    }
}
