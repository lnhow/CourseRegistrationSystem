package com.lnh.CourseRegistration.Entities.PrimaryKey;

import com.lnh.CourseRegistration.Entities.Course;
import com.lnh.CourseRegistration.Entities.Student;

import java.io.Serializable;

public class PKRegistrationInfo implements Serializable {
    protected Student student;
    protected Course course;

    public PKRegistrationInfo() {}

    public PKRegistrationInfo(Student student, Course course) {
        this.student = student;
        this.course = course;
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
