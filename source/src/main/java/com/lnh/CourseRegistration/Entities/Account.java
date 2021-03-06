package com.lnh.CourseRegistration.Entities;

import javax.persistence.*;

@Entity
@Table(name = "Account")
public class Account {
    public static final int ACCOUNT_INVALID = -1;
    public static final int ACCOUNT_STUDENT = 0;
    public static final int ACCOUNT_STAFF = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID")
    private long id;

    @Column(name = "Username", length = 20, unique = true, nullable = false)
    private String username;

    @Column(name = "Password", length = 50, nullable = false)
    private String password;

    @Column(name = "AccountType")
    private int type;

    public Account() {
    }

    public Account(int id, String username, String password, int type) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
