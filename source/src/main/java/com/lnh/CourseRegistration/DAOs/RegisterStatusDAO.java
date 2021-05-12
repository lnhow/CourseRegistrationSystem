package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.SupportEntities.RegisterStatus;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class RegisterStatusDAO {
    //Singleton: RegisterStatus is READONLY and DON'T CHANGE in runtime
    static List<RegisterStatus> typeOfStatuses = null;

    public static List<RegisterStatus> getAll() throws Exception {
        if (typeOfStatuses == null) {
            typeOfStatuses = Collections.unmodifiableList(getNewInstance());
        }

        return typeOfStatuses;
    }

    private static List<RegisterStatus> getNewInstance() throws Exception {
        List<RegisterStatus> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT status FROM RegisterStatus status";
            Query<RegisterStatus> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        if (list == null)  {
            HelperUtils.throwException("Unable to initialize statuses");
        }

        return list;
    }
}
