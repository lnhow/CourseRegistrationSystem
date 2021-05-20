package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.Controllers.LoginController;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Entities.Student;
import com.lnh.CourseRegistration.Utils.DialogUtil;
import com.lnh.CourseRegistration.Utils.HelperUtils;

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
    private Student currentStudentAccount;

    //STAFF & STUDENT BOTH USE THESE------------------------------------------------------------------------------------
    FormAccount(JDialog parentFrame) {
        try {
            LoginController.refreshLoggedInAccount();
            int accountType = LoginController.getLogInAccountType();
            switch (accountType) {
                case Account.ACCOUNT_STAFF:
                    currentStaffAccount = LoginController.getLogInStaff();
                    if (currentStaffAccount == null) {
                        HelperUtils.throwException("Không lấy được tài khoản");
                    }
                    initComponentsForStaff();
                    break;
                case Account.ACCOUNT_STUDENT:
                    currentStudentAccount = LoginController.getLogInStudent();
                    if (currentStudentAccount == null) {
                        HelperUtils.throwException("Không lấy được tài khoản");
                    }
                    initComponentsForStudent();
                    break;
                default:
                    HelperUtils.throwException("Loại tài khoản không hợp lệ");
            }
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Lỗi lấy thông tin tài khoản\n" + ex.getMessage());
            return;
        }

        initChangePasswordComponents();
        setVisible(parentFrame);
    }

    private void setVisible(JDialog parentFrame) {
        AppFrame = new JDialog(parentFrame,"Thông tin tài khoản", true);
        AppFrame.setContentPane(this.mainPanel);
        AppFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        AppFrame.setLocationRelativeTo(null);
        AppFrame.pack();
        AppFrame.setVisible(true);
    }


    void initChangePasswordComponents() {
        txtCurrentPwd.setText("");
        txtNewPwd.setText("");
        txtNewPwdConfirm.setText("");

        btnSavePwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStaffAccount != null) {
                    updateStaffAccountPassword();
                } else if (currentStudentAccount != null) {
                    updateStudentAccountPassword();
                }

            }
        });
    }

    //STAFF-------------------------------------------------------------------------------------------------------------
    private void initComponentsForStaff() {
        refreshStaffInfo();
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

    private void refreshStaffInfo() {
        txtName.setText(currentStaffAccount.getName());
    }

    //Staff account updating ------------------------------------------------------------------------
    private void updateStaffInfo() {
        String newStaffName = txtName.getText().trim();

        if (newStaffName.equals("")) {
            DialogUtil.showWarningMessage("Tên mới không được để trống");
        } else {
            currentStaffAccount.setName(newStaffName);
            try {
                LoginController.updateAccountInfo(currentStaffAccount);
                currentStaffAccount = LoginController.getLogInStaff();

                refreshStaffInfo();
                DialogUtil.showInfoMessage("Cập nhật thành công!");
            } catch (Exception exception) {
                DialogUtil.showErrorMessage("Lỗi cập nhật thông tin tài khoản\n"+ exception.getMessage());
            }
        }
    }
    private void updateStaffAccountPassword() {
        String currentPwd = new String(txtCurrentPwd.getPassword());
        String newPwd = new String(txtNewPwd.getPassword());
        String newPwdConfirm = new String(txtNewPwdConfirm.getPassword());

        if (!currentPwd.equals(currentStaffAccount.getAccount().getPassword())) {
            DialogUtil.showWarningMessage("Sai mật khẩu hiện tại");
            return;
        } else if (newPwd.equals("")) {
            DialogUtil.showWarningMessage("Mật khẩu mới không được được để trống");
            return;
        } else if (!newPwd.equals(newPwdConfirm)) {
            DialogUtil.showWarningMessage("Mật khẩu mới không khớp");
            return;
        }

        currentStaffAccount.getAccount().setPassword(newPwd);
        try {
            LoginController.updateAccount(currentStaffAccount.getAccount());
            currentStaffAccount = LoginController.getLogInStaff();

            DialogUtil.showInfoMessage("Cập nhật thành công!");
        } catch (Exception exception) {
            DialogUtil.showErrorMessage("Lỗi cập nhật mật khẩu\n"+ exception.getMessage());
        }
    }

    //Staff account updating ------------------------------------------------------------------------

    //STUDENT-----------------------------------------------------------------------------------------------------------
    private void initComponentsForStudent() {
        refreshStudentInfo();

        btnSaveInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudentInfo();
            }
        });
    }

    private void refreshStudentInfo() {
        txtName.setText(currentStudentAccount.getName());
        selectGender.setSelectedIndex(currentStudentAccount.isMale()? 0 : 1);
    }

    //Student account updating ----------------------------------------------------------------------
    private void updateStudentInfo() {
        String newStudentName = txtName.getText().trim();
        boolean studentGender = selectGender.getSelectedIndex() == 0;

        if (newStudentName.equals("")) {
            DialogUtil.showWarningMessage("Tên mới không được để trống");
        } else {
            currentStudentAccount.setName(newStudentName);
            currentStudentAccount.setMale(studentGender);
            try {
                LoginController.updateAccountInfo(currentStudentAccount);
                currentStudentAccount = LoginController.getLogInStudent();

                refreshStudentInfo();
                DialogUtil.showInfoMessage("Cập nhật thành công!");
            } catch (Exception exception) {
                DialogUtil.showErrorMessage("Lỗi cập nhật thông tin tài khoản\n"+ exception.getMessage());
            }
        }
    }

    private void updateStudentAccountPassword() {
        String currentPwd = new String(txtCurrentPwd.getPassword());
        String newPwd = new String(txtNewPwd.getPassword());
        String newPwdConfirm = new String(txtNewPwdConfirm.getPassword());

        if (!currentPwd.equals(currentStudentAccount.getAccount().getPassword())) {
            DialogUtil.showWarningMessage("Sai mật khẩu hiện tại");
            return;
        } else if (newPwd.equals("")) {
            DialogUtil.showWarningMessage("Mật khẩu mới không được được để trống");
            return;
        } else if (!newPwd.equals(newPwdConfirm)) {
            DialogUtil.showWarningMessage("Mật khẩu mới không khớp");
            return;
        }

        currentStudentAccount.getAccount().setPassword(newPwd);
        try {
            LoginController.updateAccount(currentStudentAccount.getAccount());
            DialogUtil.showInfoMessage("Cập nhật thành công!");
        } catch (Exception exception) {
            DialogUtil.showErrorMessage("Lỗi cập nhật mật khẩu\n"+ exception.getMessage());
        }
    }

    //Student account updating ----------------------------------------------------------------------

}
