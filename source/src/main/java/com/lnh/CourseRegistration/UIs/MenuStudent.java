package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.DAOs.StudentDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.Student;
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
    private Student currentLoggedIn;

    public MenuStudent(Account account) {
        initComponents();
        setVisible();
        fetchStudent(account);

        boolean isStudentNotExist = currentLoggedIn == null;
        if (isStudentNotExist) {
            DialogUtil.showErrorMessage(
                    "Tài khoản không tồn tại(ID: "+account.getId()+"). Vui lòng liên hệ admin."
            );
            logOut();
            return;
        }

        setAccountText();
    }

    private void setVisible() {
        AppFrame = new JFrame("Menu Sinh viên");
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
        txtName.setText("Xin chào, " + currentLoggedIn.getName());
    }

    private void fetchStudent(Account account) {
        try {
            currentLoggedIn = StudentDAO.getByAccountID(account.getId());
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
        fetchStudent(currentLoggedIn.getAccount());
        setAccountText();
    }
}
