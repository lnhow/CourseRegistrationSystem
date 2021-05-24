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
        initStatuesIfEmpty();
        return typeOfStatuses;
    }

    public static RegisterStatus getStatus(int statusID) throws Exception {
        initStatuesIfEmpty();

        for (RegisterStatus status: typeOfStatuses) {
            if (status.getStatusID() == statusID) {
                return status;
            }
        }

        return null;
    }

    private static void initStatuesIfEmpty() throws Exception {
        if (typeOfStatuses == null) {
            typeOfStatuses = Collections.unmodifiableList(getNewInstance());
        }
    }

    private static List<RegisterStatus> getNewInstance() throws Exception {
        List<RegisterStatus> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT status" +
                    " FROM RegisterStatus status" +
                    " ORDER BY status.statusID ASC";
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
