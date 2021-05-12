package com.lnh.CourseRegistration.Tests;

import com.lnh.CourseRegistration.DAOs.RegisterStatusDAO;
import com.lnh.CourseRegistration.DAOs.ShiftDAO;
import com.lnh.CourseRegistration.DAOs.WeekdayDAO;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Entities.SupportEntities.RegisterStatus;
import com.lnh.CourseRegistration.Entities.SupportEntities.Shift;
import com.lnh.CourseRegistration.Entities.SupportEntities.Weekday;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class SupportEntitiesTest {
    public static void main(String[] args) {
        testStatuses();
        testWeekday();
        testShifts();
    }

    private static void testStatuses() {
        List<RegisterStatus> list = null;

        try {
            list = RegisterStatusDAO.getAll();
        } catch (Exception exception) {
            System.out.println("Unable to get statuses");
            return;
        }

        for (RegisterStatus status : list) {
            System.out.println(status.getStatusID() + " | " + status.getStatusDesc());
        }

        try {
            list = RegisterStatusDAO.getAll();
        } catch (Exception exception) {
            System.out.println("Unable to get statuses");
        }

        for (RegisterStatus status : list) {
            System.out.println(status.getStatusID() + " | " + status.getStatusDesc());
        }
    }

    private static void testWeekday() {
        List<Weekday> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            list = WeekdayDAO.getAll();
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        for (Weekday weekday : list) {
            System.out.println(weekday.getWeekdayID() + " | " + weekday.getWeekdayName());
        }

        try {
            list = WeekdayDAO.getAll();
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        for (Weekday weekday : list) {
            System.out.println(weekday.getWeekdayID() + " | " + weekday.getWeekdayName());
        }
    }

    private static void testShifts() {
        List<Shift> list = null;

        try {
            list = ShiftDAO.getAll();
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        for (Shift shift : list) {
            System.out.println(shift.getShiftID() + " | " + shift.getShiftStart() + " - " + shift.getShiftEnd());
        }

        try {
            list = ShiftDAO.getAll();
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        for (Shift shift : list) {
            System.out.println(shift.getShiftID() + " | " + shift.getShiftStart() + " - " + shift.getShiftEnd());
        }
    }
}
