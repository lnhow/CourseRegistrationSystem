package com.lnh.CourseRegistration.Entities.SupportEntities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RegisterStatus")
public class RegisterStatus {
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

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
