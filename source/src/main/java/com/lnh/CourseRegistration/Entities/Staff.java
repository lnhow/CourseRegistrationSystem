package com.lnh.CourseRegistration.Entities;

import javax.persistence.*;

@Entity
@Table(name = "Staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StaffID")
    private long id;

    @Column(name = "StaffName")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AccountID")
    private Account account;

    public Staff() { }

    public Staff(long id, String name, Account account) {
        this.id = id;
        this.name = name;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
