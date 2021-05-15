package com.lnh.CourseRegistration.Utils;


import javax.swing.*;

/**
 * Wrapper class for displaying dialog messages
 * */
public class DialogUtil {
    static JFrame frame = null;

    public static void setFrame(JFrame frame)  { DialogUtil.frame = frame; }

    public static void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    public static void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    public static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
