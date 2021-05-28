package com.lnh.CourseRegistration;

import com.lnh.CourseRegistration.UIs.FormLogin;

import javax.swing.*;
import java.awt.*;

public class App {
    public static final int MAX_REGISTERED_COURSE = 8;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            setUIFont(new javax.swing.plaf.FontUIResource("Sans Serif", Font.PLAIN,12));
        } catch (Exception ex) {
            System.err.println( "Failed to initialize LaF" );
        }
        SwingUtilities.invokeLater(() -> new FormLogin());
    }

    //https://stackoverflow.com/questions/7434845/setting-the-default-font-of-swing-program
    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }
}
