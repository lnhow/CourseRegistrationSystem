package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.Course;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CourseDAO {
    public static List<Course> getAllInCurrentSemester() throws Exception {
        List<Course> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT c" +
                    " FROM Course c" +
                    " WHERE c.semester.isCurrentSemester = true";
            Query<Course> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static List<Object[]> getAllInCurrentSemesterWithInfo() throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT c," +
                    " SUM(CASE WHEN info.status.statusID < 3 THEN 1 ELSE 0 END)" +
                    " FROM Course c" +
                    " LEFT JOIN RegistrationInfo info"+
                    " ON c.id = info.courseID" +
                    " WHERE c.semester.isCurrentSemester = true" +
                    " GROUP BY c.id";
            Query<Object[]> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static Course getByCourseID(long courseID) throws Exception {
        Course result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            result = session.get(Course.class, courseID);
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return result;
    }

    //Bugged: Hibernate weird joining issues
    public static List<Object[]> searchInCurrentSemesterWithInfoByName(String value) throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT c," +
                    " SUM(CASE WHEN info.status < 3 THEN 1 ELSE 0 END)" +
                    " FROM Course c" +
                    " LEFT JOIN RegistrationInfo info"+
                    " ON c.id = info.course.id" +
                    " WHERE c.semester.isCurrentSemester = true" +
                            " AND lower(c.subject.subjectName) LIKE lower(:search_name)" +
                    " GROUP BY c";
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

    //Bugged: Hibernate weird joining issues
    public static List<Object[]> searchInCurrentSemesterWithInfoByShortName(String value) throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT c," +
                    " SUM(CASE WHEN info.status < 3 THEN 1 ELSE 0 END)" +
                    " FROM Course c" +
                    " LEFT JOIN RegistrationInfo info"+
                    " ON c.id = info.course.id" +
                    " WHERE c.semester.isCurrentSemester = true" +
                    " AND lower(c.subject.shortName) LIKE lower(:search_name)" +
                    " GROUP BY c";
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

    public static List<Course> searchInCurrentSemesterByName(String value) throws Exception {
        List<Course> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT c" +
                    " FROM Course c" +
                    " WHERE c.semester.isCurrentSemester = true" +
                    " AND lower(c.subject.subjectName) LIKE lower(:search_name)";
            Query<Course> query = session.createQuery(hql);
            query.setParameter("search_name", "%" + value + "%");
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static List<Course> searchInCurrentSemesterByShortName(String value) throws Exception {
        List<Course> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT c" +
                    " FROM Course c" +
                    " WHERE c.semester.isCurrentSemester = true" +
                    " AND lower(c.subject.shortName) LIKE lower(:search_name)";
            Query<Course> query = session.createQuery(hql);
            query.setParameter("search_name", "%" + value + "%");
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }


    public static void insert(Course newCourse) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        transaction = session.beginTransaction();
        try {
            session.save(newCourse);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void update(Course updatedCourse) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        transaction = session.beginTransaction();
        try {
            session.update(updatedCourse);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void delete(long CourseID) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Course courseToBeDeleted = session.get(Course.class, CourseID);

        if (courseToBeDeleted == null) {
            return;
        }

        Transaction transaction = null;
        transaction = session.beginTransaction();
        try {
            session.delete(courseToBeDeleted);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }
    }
}
