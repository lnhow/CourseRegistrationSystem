package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.Controllers.LoginController;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.UIs.Screens.ClassInfo.ClassInfoScreen;
import com.lnh.CourseRegistration.UIs.Screens.Course.CourseScreen;
import com.lnh.CourseRegistration.UIs.Screens.RegistrationSession.SessionScreen;
import com.lnh.CourseRegistration.UIs.Screens.Semester.SemesterScreen;
import com.lnh.CourseRegistration.UIs.Screens.Staff.StaffScreen;
import com.lnh.CourseRegistration.UIs.Screens.Subject.SubjectScreen;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuStaff implements ActionListener {
    private JFrame AppFrame;

    private JPanel mainPanel;
    private JButton btnLogOut;
    private JButton btnInfo;
    private JLabel txtStaff;

    private static final String SCREEN_STAFF = "QL Giáo vụ";
    private static final String SCREEN_SUBJECT = "QL Môn học";
    private static final String SCREEN_CLASS = "QL Lớp học/ Sinh viên";
    private static final String SCREEN_SEMESTER = "QL Học kỳ";
    private static final String SCREEN_SESSION = "QL ĐKHP";
    private static final String SCREEN_COURSE = "QL Học phần";


    private StaffScreen staffScreen;
    private SubjectScreen subjectScreen;
    private ClassInfoScreen classInfoScreen;
    private SemesterScreen semesterScreen;
    private SessionScreen sessionScreen;
    private CourseScreen courseScreen;

    private JButton btnStaff;
    private JButton btnSubject;
    private JButton btnClass;
    private JButton btnSemester;
    private JButton btnRegisterSession;
    private JButton btnCourse;


    private JPanel paneCard;
    private CardLayout cardLayout;

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
        AppFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        btnInfo.addActionListener(this);
        btnLogOut.addActionListener(this);
        btnStaff.addActionListener(this);
        btnSubject.addActionListener(this);
        btnSemester.addActionListener(this);
        btnClass.addActionListener(this);
        btnCourse.addActionListener(this);
        btnRegisterSession.addActionListener(this);

        initPaneCard();
    }

    private void initPaneCard() {
        staffScreen = new StaffScreen();
        paneCard.add(staffScreen.getMainPanel(), SCREEN_STAFF);
        subjectScreen = new SubjectScreen();
        paneCard.add(subjectScreen.getMainPanel(), SCREEN_SUBJECT);
        classInfoScreen = new ClassInfoScreen();
        paneCard.add(classInfoScreen.getMainPanel(), SCREEN_CLASS);

        cardLayout = (CardLayout) paneCard.getLayout();
        showStaffScreen();
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
        } else if (source == btnCourse) {
            showCourseScreen();
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
        removeAllExceptScreen(SCREEN_STAFF);
        staffScreen.refreshData();
        cardLayout.show(paneCard, SCREEN_STAFF);
    }

    private void showSubjectScreen() {
        removeAllExceptScreen(SCREEN_SUBJECT);
        subjectScreen.refreshData();
        cardLayout.show(paneCard, SCREEN_SUBJECT);
    }

    private void showClassInfoScreen() {
        removeAllExceptScreen(SCREEN_CLASS);
        classInfoScreen.refreshData();
        cardLayout.show(paneCard, SCREEN_CLASS);
    }

    private void showSemesterScreen() {
        SemesterScreen.getInstance().openInNewWindow();
    }

    private void showSessionScreen() {
        SessionScreen.getInstance().openInNewWindow();
    }

    private void showCourseScreen() {
        CourseScreen.getInstance().openInNewWindow();
    }

    private void removeAllExceptScreen(String screenName) {
        System.out.println("Remove except " + screenName);
        if (!screenName.equals(SCREEN_STAFF)) {
            staffScreen.removeData();
        }
        if (!screenName.equals(SCREEN_SUBJECT)) {
            subjectScreen.removeData();
        }
        if (!screenName.equals(SCREEN_CLASS)) {
            classInfoScreen.removeData();
        }
        if (!screenName.equals(SCREEN_SEMESTER)) {

        }
        if (!screenName.equals(SCREEN_SESSION)) {

        }
        if (!screenName.equals(SCREEN_COURSE)) {

        }
    }
}
