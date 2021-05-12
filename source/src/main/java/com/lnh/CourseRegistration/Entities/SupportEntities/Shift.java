package com.lnh.CourseRegistration.Entities.SupportEntities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Time;

@Entity
@Table(name = "Shifts")
public class Shift {
    @Id
    @Column(name = "ShiftID", insertable = false, updatable = false)
    private int shiftID;

    @Column(name = "ShiftStart", insertable = false, updatable = false)
    private Time shiftStart;

    @Column(name = "ShiftEnd", insertable = false, updatable = false)
    private Time shiftEnd;

    public Shift() { }

    public Shift(int shiftID, Time shiftStart, Time shiftEnd) {
        this.shiftID = shiftID;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
    }

    public int getShiftID() {
        return shiftID;
    }

    public Time getShiftStart() {
        return shiftStart;
    }

    public Time getShiftEnd() {
        return shiftEnd;
    }
}
