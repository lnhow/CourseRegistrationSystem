package com.lnh.CourseRegistration.Entities.SupportEntities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RegisterStatus")
public class RegisterStatus {
    public static final int STATUS_WAITING = 1;
    public static final int STATUS_CONFIRMED = 2;
    public static final int STATUS_CANCELLED = 3;
    public static final int STATUS_CANCELLED_BY_STUDENT = 4;

    @Id
    @Column(name = "StatusID", insertable = false, updatable = false)
    private int statusID;

    @Column(name = "StatusDesc", length = 40, insertable = false, updatable = false)
    private String statusDesc;

    public RegisterStatus() { }

    public RegisterStatus(int statusID, String statusDesc) {
        this.statusID = statusID;
        this.statusDesc = statusDesc;
    }

    public int getStatusID() {
        return statusID;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
}
