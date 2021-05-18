package com.lnh.CourseRegistration.Entities;

import javax.persistence.*;

@Entity
@Table(name = "ClassInfo")
public class ClassInfo {
    @Id
    @Column(name = "ClassID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ClassName", length = 10)
    private String className;

    public ClassInfo() {
        id = 0; //Wrapper Integer Class do not have no args constructor
    }

    public ClassInfo(int id, String className) {
        this.id = id;
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
