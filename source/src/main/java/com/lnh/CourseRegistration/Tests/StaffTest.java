package com.lnh.CourseRegistration.Tests;


import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.Staff;

import java.util.List;

public class StaffTest {
    public static void main(String[] args) {
        List<Staff> list = null;

        try {
            list = StaffDAO.getAll();
            for (Staff staff: list) {
                System.out.println(staff.getId() + " " + staff.getName() + " " + staff.getName() +
                        " [" + staff.getAccount().getId() + " " + staff.getAccount().getType() +"]");
            }

            Staff staff = StaffDAO.getByAccountID(2);

            if (staff != null) {
                System.out.println(staff.getId() + " " + staff.getName() + " " + staff.getName() +
                        " [" + staff.getAccount().getId() + " " + staff.getAccount().getType() +"]");
            } else {
                System.out.println("Not found staff");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
