package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.Student;
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
}
