package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.Student;
import com.lnh.CourseRegistration.Utils.HelperUtils;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StudentDAO {
    public static void insert(Student newStudent) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(newStudent);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void update(Student updatedStudent) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(updatedStudent);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void delete(long StudentDBID) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Student studentToBeDeleted = session.get(Student.class, StudentDBID);

        if (studentToBeDeleted == null) {
            return;
        }

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(studentToBeDeleted);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }
    }

    public static List<Student> searchByName(String value, int classID) throws Exception {
        List<Student> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            //Case insensitive search
            String hql = "SELECT stu"
                    + " FROM Student stu"
                    + " WHERE lower(stu.name) LIKE lower(:search_name)"
                    + " AND stu.classInfo.id = :classID";
            Query<Student> query = session.createQuery(hql);
            query.setParameter("search_name", "%" + value + "%");
            query.setParameter("classID", classID);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }
    
    @SuppressWarnings("unchecked")
    public static Student getByAccountID(long accountID) throws Exception {
        Student result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT stu" +
                    " FROM Student stu" +
                    " WHERE stu.account.id = :acc_id";

            Query query = session.createQuery(hql);
            query.setParameter("acc_id", accountID);
            List<Student> list = query.list();

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
    public static Student getByID(long studentDBID) throws Exception {
        Student result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT student" +
                    " FROM Student student" +
                    " WHERE student.studentNo = :stu_dbid";

            Query query = session.createQuery(hql);
            query.setParameter("stu_dbid", studentDBID);
            List<Student> list = query.list();

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
    public static List<Student> getByClassID(int classID) throws Exception {
        List<Student> result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT stu" +
                    " FROM Student stu" +
                    " WHERE stu.classInfo.id = :cla_id";

            Query query = session.createQuery(hql);
            query.setParameter("cla_id", classID);
            result = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return result;
    }
}
