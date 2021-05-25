package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.PrimaryKey.PKRegistrationInfo;
import com.lnh.CourseRegistration.Entities.RegistrationInfo;
import com.lnh.CourseRegistration.Entities.SupportEntities.RegisterStatus;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RegistrationInfoDAO {
    /**
     * Get all registration info in Course
     * @param courseID ID of Course
     * @param searchValue Student name
     * @return List of Object[] (Object[0]: RegistrationInfo, Object[1]: Student in RegistrationInfo)
     * */
    public static List<Object[]> searchInCourse(long courseID, String searchValue, int registerStatus) throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT info, stu" +
                    " FROM RegistrationInfo info" +
                    " LEFT JOIN Student stu" +
                    " ON stu.studentNo = info.studentID" +
                    " WHERE info.courseID = :courseID" +
                    " AND info.status.id = :status" +
                    " AND lower(stu.name) LIKE lower(:search_name)" +
                    " ORDER BY stu.id ASC";
            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("courseID", courseID);
            query.setParameter("search_name",  "%" + searchValue + "%");
            query.setParameter("status", registerStatus);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }


    /**
     * Get all registration info in Course
     * @param courseID ID of Course
     * @return List of Object[] (Object[0]: RegistrationInfo, Object[1]: Student in RegistrationInfo)
     * */
    public static List<Object[]> getInCourse(long courseID) throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT info, stu" +
                    " FROM RegistrationInfo info" +
                    " LEFT JOIN Student stu" +
                    " ON stu.studentNo = info.studentID" +
                    " WHERE info.courseID = :courseID" +
                    " ORDER BY stu.id ASC";
            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("courseID", courseID);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    /**
     * Get all registration info in Class with status of registrationStatusID
     * @param courseID ID of Class
     * @param registrationStatusID ID of registration status of info
     * @return List of Object[] (Object[0]: RegistrationInfo, Object[1]: Student in RegistrationInfo)
     * */
    public static List<Object[]> getInCourse(long courseID, int registrationStatusID) throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT info, stu" +
                    " FROM RegistrationInfo info" +
                    " LEFT JOIN Student stu" +
                    " ON stu.studentNo = info.studentID" +
                    " WHERE info.courseID = :courseID" +
                    " AND info.status.id = :status" +
                    " ORDER BY stu.id ASC";
            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("courseID", courseID);
            query.setParameter("status", registrationStatusID);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    /**
     * Get all registration info associated with Student
     * @param studentDBID DatabaseID of Student associated
     * @return List of Object[] (Object[0]: RegistrationInfo, Object[1]: Course in RegistrationInfo)
     * */
    public static List<Object[]> getOfStudent(long studentDBID) throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT info, course" +
                    " FROM RegistrationInfo info" +
                    " LEFT JOIN Course course" +
                    " ON course.id = info.courseID" +
                    " WHERE info.studentID = :studentDBID";
            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("studentDBID", studentDBID);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    /**
     * Get all registration info associated with Student
     * @param studentDBID DatabaseID of Student associated
     * @param registrationStatusID ID of registration status of info
     * @return List of Object[] (Object[0]: RegistrationInfo, Object[1]: Course in RegistrationInfo)
     * */
    public static List<Object[]> getOfStudent(long studentDBID, int registrationStatusID) throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT info, course" +
                    " FROM RegistrationInfo info" +
                    " LEFT JOIN Course course" +
                    " ON course.id = info.courseID" +
                    " WHERE info.studentID = :studentDBID" +
                    " AND info.status.id = :status";
            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("studentDBID", studentDBID);
            query.setParameter("status", registrationStatusID);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static RegistrationInfo getByID(long studentDBID, long courseID) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        RegistrationInfo info = session.get(RegistrationInfo.class, new PKRegistrationInfo(studentDBID, courseID));
        session.close();
        return info;
    }

    /**
     * Get all registration info associated with Student
     * @param studentDBID DatabaseID of Student associated
     * @param registrationStatusID ID of registration status of info
     * @return List of Object[] (Object[0]: RegistrationInfo, Object[1]: Course in RegistrationInfo)
     * */
    public static List<Object[]> getOfStudentInCurrentSemester(
            long studentDBID, int registrationStatusID
    ) throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT info, course" +
                    " FROM RegistrationInfo info" +
                    " LEFT JOIN Course course" +
                    " ON course.id = info.courseID" +
                    " WHERE info.studentID = :studentDBID" +
                    " AND info.status.id = :status" +
                    " AND course.semester.isCurrentSemester = true";
            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("studentDBID", studentDBID);
            query.setParameter("status", registrationStatusID);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    /**
     * Get all registration info associated with Student
     * @param studentDBID DatabaseID of Student associated
     * @return List of Object[] (Object[0]: RegistrationInfo, Object[1]: Course in RegistrationInfo)
     * */
    public static List<Object[]> getOfStudentInCurrentSemester(long studentDBID) throws Exception {
        List<Object[]> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT info, course" +
                    " FROM RegistrationInfo info" +
                    " LEFT JOIN Course course" +
                    " ON course.id = info.courseID" +
                    " WHERE info.studentID = :studentDBID" +
                    " AND course.semester.isCurrentSemester = true";
            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("studentDBID", studentDBID);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    public static void insert(RegistrationInfo info) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        transaction = session.beginTransaction();
        try {
            session.save(info);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void update(RegistrationInfo info) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        transaction = session.beginTransaction();
        try {
            session.update(info);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }
}
