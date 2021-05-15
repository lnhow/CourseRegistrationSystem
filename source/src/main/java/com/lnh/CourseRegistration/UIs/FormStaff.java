package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormStaff {
    static JFrame AppFrame;
    private Staff currentLoggedIn;

    private JPanel mainPanel;
    private JButton btnLogOut;
    private JButton btnInfo;
    private JLabel txtStaff;

    FormStaff(Account account) {
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

    private void initComponents() {
        btnInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAccountForm();
            }
        });
        btnLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOut();
            }
        });
    }

    private void setAccountText() {
        txtStaff.setText("Welcome, " + currentLoggedIn.getName());
    }

}
