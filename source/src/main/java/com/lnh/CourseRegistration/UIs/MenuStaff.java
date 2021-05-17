package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.UIs.Screens.Staff.StaffScreen;
import com.lnh.CourseRegistration.UIs.Screens.Subject.SubjectScreen;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuStaff implements ActionListener {
    private JFrame AppFrame;
    private Staff currentLoggedIn;

    private JPanel mainPanel;
    private JButton btnLogOut;
    private JButton btnInfo;
    private JLabel txtStaff;

    private JButton btnStaff;
    private JButton btnStudent;
    private JButton btnCourse;
    private JButton btnClass;
    private JButton btnSubject;

    public MenuStaff(Account account) {
        initComponents();
        setVisible();
        fetchStaff(account);

        boolean isStaffNotExist = currentLoggedIn == null;
        if (isStaffNotExist) {
            DialogUtil.showErrorMessage(
                    "Account invalid(ID: "+account.getId()+"). Please contact administrator."
            );
            logOut();
            return;
        }

        setAccountText();
    }

    private void setVisible() {
        AppFrame = new JFrame("Staff Dashboard");
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
        btnStudent.addActionListener(this);
        btnClass.addActionListener(this);
        btnCourse.addActionListener(this);
        btnSubject.addActionListener(this);
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
        } else if (source == btnStudent) {

        } else if (source == btnClass) {

        } else if (source == btnCourse) {

        }
    }

    //Handling methods-------------------------------------------------------------------------------------------------
    private void setAccountText() {
        txtStaff.setText("Welcome, " + currentLoggedIn.getName());
    }

    private void fetchStaff(Account account) {
        try {
            currentLoggedIn = StaffDAO.getByAccountID(account.getId());
        } catch (Exception exception) {
            DialogUtil.showErrorMessage(exception.getMessage());
        }
    }

    private void logOut() {
        currentLoggedIn = null;
        new FormLogin();
        AppFrame.dispose();
    }

    private void openAccountForm() {
        JDialog dialog = new JDialog(this.AppFrame);
        new FormAccount(dialog, currentLoggedIn);
        fetchStaff(currentLoggedIn.getAccount());
        setAccountText();
    }

    private void showStaffScreen() {
        StaffScreen.getInstance().openInNewWindow();
    }

    private void showSubjectScreen() {
        SubjectScreen.getInstance().openInNewWindow();
    }
}
