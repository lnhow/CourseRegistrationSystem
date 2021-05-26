package com.lnh.CourseRegistration;

import com.formdev.flatlaf.FlatLightLaf;
import com.lnh.CourseRegistration.UIs.FormLogin;

import javax.swing.*;

public class App {
    public static final int MAX_REGISTERED_COURSE = 8;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormLogin();
            }
        });
    }
}
