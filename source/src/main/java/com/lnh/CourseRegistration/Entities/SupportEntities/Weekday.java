package com.lnh.CourseRegistration.Entities.SupportEntities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Weekdays")
public class Weekday {
    @Id
    @Column(name = "WeekdayID", insertable = false, updatable = false)
    private int weekdayID;

    @Column(name = "WeekdayName", length = 9, insertable = false, updatable = false)
    private String weekdayName;

    public Weekday() {
    }

    public Weekday(int weekdayID, String weekdayName) {
        this.weekdayID = weekdayID;
        this.weekdayName = weekdayName;
    }

    public int getWeekdayID() {
        return weekdayID;
    }

    public String getWeekdayName() {
        return weekdayName;
    }
}
