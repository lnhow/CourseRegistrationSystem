package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Entities.Subject;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SubjectDAO {
    public static List<Subject> getAll() throws Exception {
        List<Subject> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT subj FROM Subject subj";
            Query<Subject> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static void insert(Subject newSubject) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(newSubject);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void update(Subject updatedSubject) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(updatedSubject);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void delete(int subjectID) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Subject subjectToBeDeleted = session.get(Subject.class, subjectID);

        if (subjectToBeDeleted == null) {
            return;
        }

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(subjectToBeDeleted);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }
    }

    public static List<Subject> searchByName(String value) throws Exception {
        List<Subject> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            //Case insensitive search
            String hql = "SELECT subj"
                    + " FROM Subject subj"
                    + " WHERE lower(subj.subjectName) LIKE lower(:search_name)";
            Query<Subject> query = session.createQuery(hql);
            query.setParameter("search_name", "%" + value + "%");
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static List<Subject> searchByShortName(String value) throws Exception {
        List<Subject> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            //Case insensitive search
            String hql = "SELECT subj"
                    + " FROM Subject subj"
                    + " WHERE lower(subj.shortName) LIKE lower(:search_name)";
            Query<Subject> query = session.createQuery(hql);
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
    public static Subject getBySubjectID(int subjectID) throws Exception {
        Subject result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT subj" +
                    " FROM Subject subj" +
                    " WHERE subj.id = :subj_id";

            Query query = session.createQuery(hql);
            query.setParameter("subj_id", subjectID);
            List<Subject> list = query.list();

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
