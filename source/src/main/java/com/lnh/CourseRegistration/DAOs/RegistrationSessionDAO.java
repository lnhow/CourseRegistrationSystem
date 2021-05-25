package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.RegistrationSession;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Timestamp;
import java.util.List;

public class RegistrationSessionDAO {
    public static List<RegistrationSession> getAll() throws Exception {
        List<RegistrationSession> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT session FROM RegistrationSession session";
            Query<RegistrationSession> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static void insert(RegistrationSession newRegistrationSession) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(newRegistrationSession);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void update(RegistrationSession updatedRegistrationSession) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(updatedRegistrationSession);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void delete(long RegistrationSessionID) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        RegistrationSession sessionToBeDeleted = session.get(RegistrationSession.class, RegistrationSessionID);

        if (sessionToBeDeleted == null) {
            return;
        }

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(sessionToBeDeleted);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static RegistrationSession getBySessionID(long registrationSessionID) throws Exception {
        RegistrationSession result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT session" +
                    " FROM RegistrationSession session" +
                    " WHERE session.id = :session_id";

            Query query = session.createQuery(hql);
            query.setParameter("session_id", registrationSessionID);
            List<RegistrationSession> list = query.list();

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

    @SuppressWarnings("unchecked")
    public static RegistrationSession getOpenSession(Timestamp timestamp) throws Exception {
        RegistrationSession result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT session" +
                    " FROM RegistrationSession session" +
                    " WHERE :date BETWEEN session.sessionStart AND session.sessionEnd" +
                    " ORDER BY session.sessionStart DESC";

            Query query = session.createQuery(hql);
            query.setParameter("date", timestamp);
            List<RegistrationSession> list = query.list();

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
