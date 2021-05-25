package com.lnh.CourseRegistration;

import com.lnh.CourseRegistration.DAOs.RegistrationInfoDAO;
import com.lnh.CourseRegistration.Entities.RegistrationInfo;
import com.lnh.CourseRegistration.UIs.FormLogin;

import javax.swing.*;
import java.util.List;

public class App {
    public static final int MAX_REGISTERED_COURSE = 8;

    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (
//                ClassNotFoundException | InstantiationException
//                        | IllegalAccessException | UnsupportedLookAndFeelException e
//        ) {
//            e.printStackTrace();
//        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormLogin();
            }
        });
    }
}
