package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.ClassInfo;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ClassInfoDAO {
    public static List<ClassInfo> getAll() throws Exception {
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

    public static List<Object[]> getAllWithInfo() throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT cla, COUNT(stu.studentNo)," +
                    " SUM(CASE WHEN stu.isMale = true THEN 1 ELSE 0 END)," +
                    " SUM(CASE WHEN stu.isMale = false THEN 1 ELSE 0 END)" +
                    " FROM ClassInfo cla" +
                    " LEFT JOIN Student stu" +
                    " ON stu.classInfo.id = cla.id" +
                    " GROUP BY cla.id";
            Query<Object[]> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static void insert(ClassInfo newClassInfo) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(newClassInfo);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void update(ClassInfo updatedClassInfo) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(updatedClassInfo);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void delete(int ClassInfoID) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        ClassInfo ClassInfoToBeDeleted = session.get(ClassInfo.class, ClassInfoID);

        if (ClassInfoToBeDeleted == null) {
            return;
        }

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(ClassInfoToBeDeleted);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }
    }

    public static List<ClassInfo> searchByName(String value) throws Exception {
        List<ClassInfo> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            //Case insensitive search
            String hql = "SELECT cla"
                    + " FROM ClassInfo cla"
                    + " WHERE lower(cla.className) LIKE lower(:search_name)";
            Query<ClassInfo> query = session.createQuery(hql);
            query.setParameter("search_name", "%" + value + "%");
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static List<Object[]> searchByNameWithInfo(String value) throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT cla, COUNT(stu.studentNo)," +
                    " SUM(CASE WHEN stu.isMale = true THEN 1 ELSE 0 END)," +
                    " SUM(CASE WHEN stu.isMale = false THEN 1 ELSE 0 END)" +
                    " FROM ClassInfo cla" +
                    " LEFT JOIN Student stu" +
                    " ON stu.classInfo.id = cla.id" +
                    " WHERE lower(cla.className) LIKE lower(:search_name)" +
                    " GROUP BY cla.id";
            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("search_name", "%" + value + "%");
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public static ClassInfo getByClassInfoID(int classInfoID) throws Exception {
        ClassInfo result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT cla" +
                    " FROM ClassInfo cla" +
                    " WHERE cla.id = :cla_id";

            Query query = session.createQuery(hql);
            query.setParameter("cla_id", classInfoID);
            List<ClassInfo> list = query.list();

            if (list != null && list.size() > 0) {
                result = list.get(0);
            }

        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return result;
    }
}
