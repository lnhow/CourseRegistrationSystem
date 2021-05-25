package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.App;
import com.lnh.CourseRegistration.Controllers.RegistrationInfoController;
import com.lnh.CourseRegistration.Entities.Course;
import com.lnh.CourseRegistration.Entities.PrimaryKey.PKRegistrationInfo;
import com.lnh.CourseRegistration.Entities.RegistrationInfo;
import com.lnh.CourseRegistration.Entities.Student;
import com.lnh.CourseRegistration.Entities.SupportEntities.RegisterStatus;
import com.lnh.CourseRegistration.Entities.SupportEntities.Shift;
import com.lnh.CourseRegistration.Entities.SupportEntities.Weekday;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Timestamp;
import java.util.Arrays;
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
     * @param registrationStatusIDs IDs of registration status of info
     * @return List of Object[] (Object[0]: RegistrationInfo, Object[1]: Course in RegistrationInfo)
     * */
    public static List<Object[]> getOfStudentInCurrentSemester(
            long studentDBID, List<Integer> registrationStatusIDs
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
                    " AND info.status.id IN (:status)" +
                    " AND course.semester.isCurrentSemester = true";
            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("studentDBID", studentDBID);
            query.setParameter("status", registrationStatusIDs);
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
            System.out.println(list.size());
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


    public static void register(long studentDBID, long courseID) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        //Check if course exists
        Course course = session.get(Course.class, courseID);
        if (course == null) {
            HelperUtils.throwException(
                    RegistrationInfoController.ERR_NO_COURSE_WITH_ID + " " + courseID);
            transaction.rollback();
            session.close();
            return;
        }

        //Check if student exists
        Student student = session.get(Student.class, studentDBID);
        if (student == null) {
            HelperUtils.throwException(
                    RegistrationInfoController.ERR_NO_STUDENT_WITH_ID + " " + studentDBID);
            transaction.rollback();
            session.close();
            return;
        }

        //Check if registered already
        RegistrationInfo info = session.get(RegistrationInfo.class, new PKRegistrationInfo(studentDBID, courseID));
        if (info != null) {
            int statusID = info.getStatus().getStatusID();
            if (statusID == RegisterStatus.STATUS_WAITING || statusID == RegisterStatus.STATUS_CONFIRMED) {
                HelperUtils.throwException(
                        RegistrationInfoController.ERR_REGISTERED_ALREADY);
                transaction.rollback();
                session.close();
                return;
            }
        }

        Long count = null;
        String hqlCourse = "SELECT count(info)" +
                " FROM RegistrationInfo info" +
                " WHERE info.courseID = :courseID" +
                " AND info.status.id NOT IN (:status)";
        try {
            Query query = session.createQuery(hqlCourse);
            query.setParameter("courseID", studentDBID);
            query.setParameter("status",
                    Arrays.asList(RegisterStatus.STATUS_CANCELLED, RegisterStatus.STATUS_CANCELLED_BY_STUDENT)
            );
            count = (Long) query.uniqueResult();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
            transaction.rollback();
            session.close();
            return;
        }

        if (count != null && count >= course.getMaxSlot()) {
            HelperUtils.throwException(
                    RegistrationInfoController.ERR_COURSE_MAXED_SLOT_ALREADY);
            transaction.rollback();
            session.close();
            return;
        }

        List<Object[]> list = null;
        String hqlList = "SELECT info, course" +
                " FROM RegistrationInfo info" +
                " LEFT JOIN Course course" +
                " ON course.id = info.courseID" +
                " WHERE info.studentID = :studentDBID" +
                " AND course.semester.isCurrentSemester = true" +
                " AND info.status.id NOT IN (:status)";
        try {
            Query<Object[]> query = session.createQuery(hqlList);
            query.setParameter("studentDBID", studentDBID);
            query.setParameter("status",
                    Arrays.asList(RegisterStatus.STATUS_CANCELLED, RegisterStatus.STATUS_CANCELLED_BY_STUDENT)
            );
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
            transaction.rollback();
            session.close();
            return;
        }

        if (list != null) {
            //Check if already register max allowed
            if (list.size() >= App.MAX_REGISTERED_COURSE) {
                HelperUtils.throwException(
                        RegistrationInfoController.ERR_MAX_REGISTERED_ALREADY);
                transaction.rollback();
                session.close();
                return;
            }

            //Check if there are courses that conflicted
            for (Object[] row: list) {
                Course rowCourse = (Course) row[1];
                Weekday weekday = rowCourse.getWeekday();
                Shift shift = rowCourse.getShift();
                boolean isSameTime =
                        (weekday.getWeekdayID() == course.getWeekday().getWeekdayID())
                                && (shift.getShiftID() == course.getShift().getShiftID());
                if (isSameTime) {
                    HelperUtils.throwException(
                            RegistrationInfoController.ERR_CONFLICT_TIME
                            + " (" + weekday.getWeekdayName() + " - " + shift.toString() + ") "
                    );
                    transaction.rollback();
                    session.close();
                    return;
                }
            }
        }

        try {
            if (info != null) {
                info.setStatus(RegisterStatusDAO.getStatus(RegisterStatus.STATUS_WAITING));
                info.setRegisterTime(new Timestamp(System.currentTimeMillis()));
                session.update(info);
            } else {
                info = new RegistrationInfo();
                info.setStudentID(studentDBID);
                info.setCourseID(courseID);
                info.setStatus(RegisterStatusDAO.getStatus(RegisterStatus.STATUS_WAITING));
                session.save(info);
            }
            transaction.commit();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }
    }
}
