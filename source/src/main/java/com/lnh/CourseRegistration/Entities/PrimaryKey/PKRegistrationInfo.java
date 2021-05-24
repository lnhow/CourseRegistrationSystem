package com.lnh.CourseRegistration.Entities.PrimaryKey;

import java.io.Serializable;

public class PKRegistrationInfo implements Serializable {
    protected long studentID;
    protected long courseID;

    public PKRegistrationInfo() {
    }

    public PKRegistrationInfo(long studentID, long courseID) {
        this.studentID = studentID;
        this.courseID = courseID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
