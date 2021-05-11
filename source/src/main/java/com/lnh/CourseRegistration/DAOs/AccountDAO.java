package com.lnh.CourseRegistration.DAOs;

import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.Student;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import com.lnh.CourseRegistration.Utils.HelperUtils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AccountDAO {
    public static List<Account> getAll() throws Exception {
        List<Account> list = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT acc FROM Account acc";
            Query<Account> query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException ex) {
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public static Account getByLoginInfo(String username, String password) throws Exception {
        Account result = null;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {
            String hql = "SELECT acc" +
                    " FROM Account acc" +
                    " WHERE acc.username = :acc_username AND acc.password = :acc_pwd";

            Query query = session.createQuery(hql);
            query.setParameter("acc_username", username);
            query.setParameter("acc_pwd", password);
            List<Account> list = query.list();

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

    public static void insert(Account newAccount) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(newAccount);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void update(Account updatedAccount) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(updatedAccount);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        }
        finally {
            session.close();
        }
    }

    public static void delete(long accountID) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Account accountToBeDeleted = session.get(Account.class, accountID);

        if (accountToBeDeleted == null) {
            return;
        }
        
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(accountToBeDeleted);
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            HelperUtils.throwException(ex.getMessage());
        } finally {
            session.close();
        }
    }
}
