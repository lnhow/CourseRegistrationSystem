package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.Controllers.LoginController;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.UIs.Screens.ClassInfo.ClassInfoScreen;
import com.lnh.CourseRegistration.UIs.Screens.RegistrationSession.SessionScreen;
import com.lnh.CourseRegistration.UIs.Screens.Semester.SemesterScreen;
import com.lnh.CourseRegistration.UIs.Screens.Staff.StaffScreen;
import com.lnh.CourseRegistration.UIs.Screens.Subject.SubjectScreen;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuStaff implements ActionListener {
    private JFrame AppFrame;

    private JPanel mainPanel;
    private JButton btnLogOut;
    private JButton btnInfo;
    private JLabel txtStaff;

    private JButton btnStaff;
    private JButton btnStudent;
    private JButton btnCourse;
    private JButton btnClass;
    private JButton btnSubject;
    private JButton btnSemester;
    private JButton btnRegisterSession;

    public MenuStaff() {
        initComponents();
        setVisible();

        boolean isStaffNotExist =
                LoginController.getLogInAccountType() != Account.ACCOUNT_STAFF
                || LoginController.getLogInStaff() == null;
        if (isStaffNotExist) {
            DialogUtil.showErrorMessage("Lỗi đăng nhập");
            logOut();
            return;
        }

        setAccountText();
    }

    private void setVisible() {
        AppFrame = new JFrame("Giáo vụ");
        AppFrame.setContentPane(this.mainPanel);
        AppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AppFrame.pack();
        AppFrame.setVisible(true);
    }

    private void initComponents() {
        btnInfo.addActionListener(this);
        btnLogOut.addActionListener(this);
        btnStaff.addActionListener(this);
        btnSubject.addActionListener(this);
        btnSemester.addActionListener(this);
        btnStudent.addActionListener(this);
        btnClass.addActionListener(this);
        btnCourse.addActionListener(this);
        btnRegisterSession.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnInfo) {
            openAccountForm();
        } else if (source == btnLogOut) {
            logOut();
        } else if (source == btnStaff) {
            showStaffScreen();
        } else if (source == btnSubject) {
            showSubjectScreen();
        } else if (source == btnSemester) {
            showSemesterScreen();
        } else if (source == btnClass) {
            showClassInfoScreen();
        } else if (source == btnRegisterSession) {
            showSessionScreen();
        } else if (source == btnStudent) {

        } else if (source == btnCourse) {

        }
    }

    //Handling methods-------------------------------------------------------------------------------------------------
    private void setAccountText() {
        txtStaff.setText("Xin chào, " + LoginController.getLogInStaff().getName());
    }

    private void logOut() {
        LoginController.logOut();
        new FormLogin();
        AppFrame.dispose();
    }

    private void openAccountForm() {
        JDialog dialog = new JDialog(this.AppFrame);
        new FormAccount(dialog);
        setAccountText();
    }

    private void showStaffScreen() {
        StaffScreen.getInstance().openInNewWindow();
    }

    private void showSubjectScreen() {
        SubjectScreen.getInstance().openInNewWindow();
    }

    private void showSemesterScreen() {
        SemesterScreen.getInstance().openInNewWindow();
    }

    private void showClassInfoScreen() {
        ClassInfoScreen.getInstance().openInNewWindow();
    }

    private void showSessionScreen() {
        SessionScreen.getInstance().openInNewWindow();
    }
}
