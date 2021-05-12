package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.SupportEntities.Weekday;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class WeekdayDAO {
    //Singleton: Weekdays is READONLY and DON'T CHANGE in runtime
    static List<Weekday> weekdays = null;

    public static List<Weekday> getAll() throws Exception {
        if (weekdays == null) {
            weekdays = getNewInstance();
        }

        return weekdays;
    }

    private static List<Weekday> getNewInstance() throws Exception {
        List<Weekday> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT w FROM Weekday w";
            Query<Weekday> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        if (list == null)  {
            HelperUtils.throwException("Unable to initialize Weekdays");
        }

        return list;
    }
}
