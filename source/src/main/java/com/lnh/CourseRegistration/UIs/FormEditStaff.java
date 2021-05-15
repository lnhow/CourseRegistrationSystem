package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.DAOs.AccountDAO;
import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormEditStaff {
    private JDialog AppFrame;
    private JPanel mainPanel;
    private JTextField txtStaffID;
    private JTextField txtName;
    private JTextField txtUsername;

    private JButton btnSaveInfo;
    private JButton btnResetPwd;
    private Staff currentStaff;

    public FormEditStaff(JDialog parentFrame, Staff staffAccount) {
        if (staffAccount == null) {
            initNewStaff();
        } else {
            currentStaff = staffAccount;
        }

        initComponents();
        setVisible(parentFrame);
    }

    private void setVisible(JDialog parentFrame) {
        AppFrame = new JDialog(parentFrame,"Staff Info", true);
        AppFrame.setContentPane(this.mainPanel);
        AppFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        AppFrame.pack();
        AppFrame.setVisible(true);
    }

    private void initNewStaff() {
        currentStaff = new Staff();
        currentStaff.setName("");
        currentStaff.setAccount(new Account());
        currentStaff.getAccount().setUsername("");
        currentStaff.getAccount().setPassword("");
        currentStaff.getAccount().setType(Account.ACCOUNT_STAFF);
    }

    private void initComponents() {
        refreshTextFields();

        btnSaveInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInfo();
            }
        });

        btnResetPwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetPassword();
            }
        });

    }

    private void refreshTextFields() {
        txtStaffID.setText(Long.toString(currentStaff.getId()));
        txtName.setText(currentStaff.getName());
        txtUsername.setText(currentStaff.getAccount().getUsername());
    }

    private void saveInfo() {
        String name = txtName.getText();
        String username = txtUsername.getText();

        if (name.equals("")) {
            DialogUtil.showWarningMessage("Name must not be empty");
            return;
        } else if (username.equals("")) {
            DialogUtil.showWarningMessage("Username must not be empty");
            return;
        }

        //Set default password
        if (currentStaff.getAccount().getPassword().equals("")) {
            currentStaff.getAccount().setPassword(username);
        }

        currentStaff.setName(name);
        currentStaff.getAccount().setUsername(username);

        String msg = "Confirm changes to this account?";
        int option = JOptionPane.showConfirmDialog(AppFrame, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveToDB();
        }
    }

    private void resetPassword() {
        currentStaff.getAccount().setPassword(currentStaff.getAccount().getUsername());

        String msg = "Do you really want to reset this account's password?";
        int option = JOptionPane.showConfirmDialog(AppFrame, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveToDB();
        }
    }

    private void saveToDB() {
        try {
            if (StaffDAO.getByAccountID(currentStaff.getId()) == null) {
                StaffDAO.insert(currentStaff);
            }
            else {
                StaffDAO.update(currentStaff);
            }

            //Update current account info cache with new id
            Account currentAccount = currentStaff.getAccount();
            currentAccount =
                    AccountDAO.getByLoginInfo(currentAccount.getUsername(), currentAccount.getPassword());
            currentStaff = StaffDAO.getByAccountID(currentAccount.getId());

            refreshTextFields();
        } catch (Exception ex) {
            String errorMessage = "Unable to save. Error:\n"+ex.getMessage();
            DialogUtil.showErrorMessage(errorMessage);
            return;
        }

        DialogUtil.showInfoMessage("Updated successfully");
    }
}
