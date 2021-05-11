package com.lnh.CourseRegistration.Tests;

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
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT status FROM RegisterStatus status";
            Query<RegisterStatus> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            System.out.println(ex.getMessage());
        } finally {
            session.close();
        }

        if (list == null)  {
            System.out.println("List empty");
            return;
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
            String hql = "SELECT w FROM Weekday w";
            Query<Weekday> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            System.out.println(ex.getMessage());
        } finally {
            session.close();
        }

        if (list == null)  {
            System.out.println("List empty");
            return;
        }

        for (Weekday weekday : list) {
            System.out.println(weekday.getWeekdayID() + " | " + weekday.getWeekdayName());
        }
    }

    private static void testShifts() {
        List<Shift> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT s FROM Shift s";
            Query<Shift> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            System.out.println(ex.getMessage());
        } finally {
            session.close();
        }

        if (list == null)  {
            System.out.println("List empty");
            return;
        }

        for (Shift shift : list) {
            System.out.println(shift.getShiftID() + " | " + shift.getShiftStart() + " - " + shift.getShiftEnd());
        }
    }
}
