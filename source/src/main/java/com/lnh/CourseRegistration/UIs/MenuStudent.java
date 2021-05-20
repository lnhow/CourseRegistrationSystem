package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.Controllers.LoginController;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuStudent implements ActionListener {
    private JPanel mainPanel;
    private JButton btnLogOut;
    private JButton btnInfo;
    private JLabel txtName;

    private JFrame AppFrame;

    public MenuStudent() {
        initComponents();
        setVisible();

        boolean isStudentNotExist =
                LoginController.getLogInAccountType() != Account.ACCOUNT_STUDENT
                || LoginController.getLogInStudent() == null;
        if (isStudentNotExist) {
            DialogUtil.showErrorMessage("Lỗi đăng nhập");
            return;
        }

        setAccountText();
    }

    private void setVisible() {
        AppFrame = new JFrame("Sinh viên");
        AppFrame.setContentPane(this.mainPanel);
        AppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AppFrame.pack();
        AppFrame.setVisible(true);
        AppFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        btnInfo.addActionListener(this);
        btnLogOut.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnInfo) {
            openAccountForm();
        } else if (source == btnLogOut) {
            logOut();
        }
    }

    //Handling methods-------------------------------------------------------------------------------------------------
    private void setAccountText() {
        txtName.setText("Xin chào, " + LoginController.getLogInStudent().getName());
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
}
