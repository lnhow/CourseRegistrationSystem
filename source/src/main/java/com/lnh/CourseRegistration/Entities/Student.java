package com.lnh.CourseRegistration.Entities;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "Student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudentNo")
    private long studentNo;

    @Column(name = "StudentID", length = 10, unique = true)
    private String id;

    @Column(name = "StudentName", length = 50)
    private String name;

    @Column(name = "Male")
    private boolean isMale;

    @Column(name = "ClassID")
    private int classID;

    @OneToOne
    @JoinColumn(name = "AccountID")
    private Account account;

    public Student() { }

    public Student(long studentNo, String id, String name, boolean isMale, int classID, Account account) {
        this.studentNo = studentNo;
        this.id = id;
        this.name = name;
        this.isMale = isMale;
        this.classID = classID;
        this.account = account;
    }

    public long getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(long studentNo) {
        this.studentNo = studentNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
