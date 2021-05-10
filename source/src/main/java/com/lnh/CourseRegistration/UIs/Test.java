package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Account> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            String hql = "SELECT acc FROM Account acc";
            Query<Account> query = session.createQuery(hql);
            list = query.list();
        }  catch (HibernateException ex) {
            System.out.println(ex.getMessage());
        } finally {
            session.close();
        }

        for (Account acc: list) {
            System.out.println(acc.getId() + " " + acc.getUsername() + " " + acc.getType());
        }
    }
}
