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

    @OneToOne
    @JoinColumn(name = "ClassID")
    private ClassInfo classInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AccountID")
    private Account account;

    public Student() { }

    public Student(long studentNo, String id, String name, boolean isMale, ClassInfo classInfo, Account account) {
        this.studentNo = studentNo;
        this.id = id;
        this.name = name;
        this.isMale = isMale;
        this.classInfo = classInfo;
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

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
