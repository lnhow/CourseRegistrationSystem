package com.lnh.CourseRegistration.DAOs;


import com.lnh.CourseRegistration.Entities.SupportEntities.Shift;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class ShiftDAO {
    //Singleton: Shifts is READONLY and DON'T CHANGE in runtime
    static List<Shift> shifts = null;

    public static List<Shift> getAll() throws Exception {
        if (shifts == null) {
            shifts = Collections.unmodifiableList(getNewInstance());
        }

        return shifts;
    }

    private static List<Shift> getNewInstance() throws Exception {
        List<Shift> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT s FROM Shift s";
            Query<Shift> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        if (list == null)  {
            HelperUtils.throwException("Unable to initialize shifts");
        }

        return list;
    }
}
