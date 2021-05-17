package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.DAOs.AccountDAO;
import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.DAOs.StudentDAO;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Entities.Student;
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
    private Student currentStudentAccount;

    //STAFF & STUDENT BOTH USE THESE------------------------------------------------------------------------------------
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
                if (currentStaffAccount != null) {
                    updateStaffAccountPassword();
                } else if (currentStudentAccount != null) {
                    updateStudentAccountPassword();
                }

            }
        });
    }

    //STAFF-------------------------------------------------------------------------------------------------------------
    FormAccount(JDialog parentFrame, Staff staffAccount) {
        if (staffAccount == null) {
            DialogUtil.showErrorMessage("Lỗi lấy thông tin tài khoản");
            return;
        }

        currentStaffAccount = staffAccount;
        initComponentsForStaff();
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

    //Staff account updating ------------------------------------------------------------------------
    private void updateStaffInfo() {
        String newStaffName = txtName.getText().trim();

        if (newStaffName.equals("")) {
            DialogUtil.showWarningMessage("Tên mới không được để trống");
        } else {
            currentStaffAccount.setName(newStaffName);
            try {
                StaffDAO.update(currentStaffAccount);
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
            AccountDAO.update(currentStaffAccount.getAccount());
            DialogUtil.showInfoMessage("Cập nhật thành công!");
        } catch (Exception exception) {
            DialogUtil.showErrorMessage("Lỗi cập nhật mật khẩu\n"+ exception.getMessage());
        }
    }

    //Staff account updating ------------------------------------------------------------------------

    //STUDENT-----------------------------------------------------------------------------------------------------------
    FormAccount(JDialog parentFrame, Student studentAccount) {
        if (studentAccount == null) {
            DialogUtil.showErrorMessage("Lỗi lấy thông tin tài khoản");
            return;
        }

        currentStudentAccount = studentAccount;
        initComponentsForStudent();
        initChangePasswordComponents();
        setVisible(parentFrame);
    }

    private void initComponentsForStudent() {
        txtName.setText(currentStudentAccount.getName());
        selectGender.setSelectedIndex(currentStudentAccount.isMale()? 0 : 1);

        btnSaveInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudentInfo();
            }
        });
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
                StudentDAO.update(currentStudentAccount);
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
            AccountDAO.update(currentStudentAccount.getAccount());
            DialogUtil.showInfoMessage("Cập nhật thành công!");
        } catch (Exception exception) {
            DialogUtil.showErrorMessage("Lỗi cập nhật mật khẩu\n"+ exception.getMessage());
        }
    }

    //Student account updating ----------------------------------------------------------------------

}
