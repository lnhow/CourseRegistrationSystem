package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.ClassInfo;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class CourseDAO {
    public static List<ClassInfo> getAllCurrentSemester() throws Exception {
        List<ClassInfo> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT cla FROM ClassInfo cla";
            Query<ClassInfo> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }
}
