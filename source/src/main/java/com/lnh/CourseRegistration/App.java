package com.lnh.CourseRegistration;

import com.lnh.CourseRegistration.UIs.FormLogin;

import javax.swing.*;

public class App {

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
