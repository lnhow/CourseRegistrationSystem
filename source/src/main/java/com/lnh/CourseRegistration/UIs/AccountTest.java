package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.DAOs.AccountDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class AccountTest {
    public static void main(String[] args) {
        List<Account> list = null;

        try {
            list = AccountDAO.getAll();
            for (Account acc: list) {
                System.out.println(acc.getId() + " " + acc.getUsername() + " " + acc.getType());
            }

            Account login = AccountDAO.getByLoginInfo("admin", "admi");

            if (login != null) {
                System.out.println(login.getId() + " " + login.getUsername() + " " + login.getType());
            }
            else {
                System.out.println("Wrong login credential!");
            }

            Account newAccount = new Account();
            newAccount.setUsername("tester");
            newAccount.setPassword("tester");
            AccountDAO.insert(newAccount);

            newAccount = AccountDAO.getByLoginInfo("tester", "tester");
            System.out.println(newAccount.getId() + " " + newAccount.getUsername() + " " + newAccount.getType());

            newAccount.setUsername("newtester");
            AccountDAO.update(newAccount);
            newAccount = AccountDAO.getByLoginInfo("newtester", "tester");
            System.out.println(newAccount.getId() + " " + newAccount.getUsername() + " " + newAccount.getType());

            AccountDAO.delete(newAccount.getId());
            list = AccountDAO.getAll();
            for (Account acc: list) {
                System.out.println(acc.getId() + " " + acc.getUsername() + " " + acc.getType());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
