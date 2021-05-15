package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.DAOs.AccountDAO;
import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormAccount {
    private JDialog AppFrame;

    private JPanel mainPanel;
    private JTextField txtName;
    private JPasswordField txtCurrentPwd;
    private JPasswordField txtNewPwd;
    private JPasswordField txtNewPwdConfirm;
    private JButton btnSaveInfo;
    private JButton btnSavePwd;
    private JComboBox selectGender;
    private JLabel lblGender;
    private JPanel infoPanel;

    private Staff currentStaffAccount;

    FormAccount(JDialog parentFrame, Staff staffAccount) {
        if (staffAccount == null) {
            DialogUtil.showErrorMessage("Error getting account info");
            return;
        }

        currentStaffAccount = staffAccount;
        initComponentsForStaff();
        initChangePasswordComponents();
        setVisible(parentFrame);
    }

    private void initComponentsForStaff() {
        txtName.setText(currentStaffAccount.getName());
        //Staff do not have these info
        infoPanel.remove(lblGender);
        infoPanel.remove(selectGender);

        btnSaveInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStaffInfo();
            }
        });
    }

    void initChangePasswordComponents() {
        txtCurrentPwd.setText("");
        txtNewPwd.setText("");
        txtNewPwdConfirm.setText("");

        btnSavePwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStaffAccountPassword();
            }
        });
    }

    private void setVisible(JDialog parentFrame) {
        AppFrame = new JDialog(parentFrame,"Account Info", true);
        AppFrame.setContentPane(this.mainPanel);
        AppFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        AppFrame.pack();
        AppFrame.setVisible(true);
    }

    //Staff account updating ------------------------------------------------------------------------
    private void updateStaffInfo() {
        String newStaffName = txtName.getText().trim();

        if (newStaffName.equals("")) {
            DialogUtil.showWarningMessage("New name can't be empty");
        } else {
            currentStaffAccount.setName(newStaffName);
            try {
                StaffDAO.update(currentStaffAccount);
                DialogUtil.showInfoMessage("Updated successfully!");
            } catch (Exception exception) {
                DialogUtil.showErrorMessage("Error Updating Account Info\n"+ exception.getMessage());
            }
        }
    }
    private void updateStaffAccountPassword() {
        String currentPwd = new String(txtCurrentPwd.getPassword());
        String newPwd = new String(txtNewPwd.getPassword());
        String newPwdConfirm = new String(txtNewPwdConfirm.getPassword());

        if (!currentPwd.equals(currentStaffAccount.getAccount().getPassword())) {
            DialogUtil.showWarningMessage("Wrong current password");
            return;
        } else if (newPwd.equals("")) {
            DialogUtil.showWarningMessage("New password cannot be empty");
            return;
        } else if (!newPwd.equals(newPwdConfirm)) {
            DialogUtil.showWarningMessage("New password and confirm do not match");
            return;
        }

        currentStaffAccount.getAccount().setPassword(newPwd);
        try {
            AccountDAO.update(currentStaffAccount.getAccount());
            DialogUtil.showInfoMessage("Updated successfully!");
        } catch (Exception exception) {
            DialogUtil.showErrorMessage("Error Updating Password\n"+ exception.getMessage());
        }
    }

    //Staff account updating ------------------------------------------------------------------------

}
