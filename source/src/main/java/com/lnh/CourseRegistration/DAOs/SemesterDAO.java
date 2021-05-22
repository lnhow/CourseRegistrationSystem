package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.Semester;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SemesterDAO {
    public static List<Semester> getAll() throws Exception {
        List<Semester> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT sem FROM Semester sem";
            Query<Semester> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static void insert(Semester newSemester) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(newSemester);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void update(Semester updatedSemester) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            if (updatedSemester.isCurrentSemester()) {
                //Update previous current semester to false
                String hql = "SELECT sem" +
                        " FROM Semester sem" +
                        " WHERE sem.isCurrentSemester = true";
                List<?> list = session.createQuery(hql).list();

                if (list != null && list.size() > 0) {
                    Semester current = (Semester) list.get(0);
                    if (current.getId() != updatedSemester.getId()) {
                        current.setCurrentSemester(false);
                        session.update(current);
                    }
                }
            }

            session.update(updatedSemester);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void delete(int SemesterID) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Semester semesterToBeDeleted = session.get(Semester.class, SemesterID);

        if (semesterToBeDeleted == null) {
            return;
        }

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(semesterToBeDeleted);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static Semester getBySemesterID(int SemesterID) throws Exception {
        Semester result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT sem" +
                    " FROM Semester sem" +
                    " WHERE sem.id = :sem_id";

            Query query = session.createQuery(hql);
            query.setParameter("sem_id", SemesterID);
            List<Semester> list = query.list();

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
    public static Semester getCurrentSemester() throws Exception {
        Semester result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT sem" +
                    " FROM Semester sem" +
                    " WHERE sem.isCurrentSemester = true";

            Query query = session.createQuery(hql);
            List<Semester> list = query.list();

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
