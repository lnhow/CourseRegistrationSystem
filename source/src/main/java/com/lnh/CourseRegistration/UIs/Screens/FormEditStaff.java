package com.lnh.CourseRegistration.UIs.Screens;

import com.lnh.CourseRegistration.DAOs.AccountDAO;
import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormEditStaff extends JDialog {
    private JPanel mainPanel;
    private JTextField txtStaffID;
    private JTextField txtName;
    private JTextField txtUsername;

    private JButton btnSaveInfo;
    private JButton btnResetPwd;
    private JPanel panelBtns;

    private StaffScreen parent;
    private Staff currentStaff;
    private boolean isNewScreen;

    public FormEditStaff(JFrame parentFrame, Staff staffAccount) {
        super(parentFrame, true);
        if (staffAccount == null) {
            isNewScreen = true;
            initNewStaff();
        } else {
            currentStaff = staffAccount;
            isNewScreen = false;
        }

        initComponents();
        parent = (StaffScreen) parentFrame;
        initWindow();
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

        if (isNewScreen) {
            panelBtns.remove(btnResetPwd);
            setTitle("Thêm Giáo vụ");
        }
        else {
            setTitle("Sửa Giáo vụ");
        }

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

    private void initWindow() {
        setContentPane(this.mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshTextFields() {
        txtStaffID.setText(Long.toString(currentStaff.getId()));
        txtName.setText(currentStaff.getName());
        txtUsername.setText(currentStaff.getAccount().getUsername());
    }


    //Handler methods----------------------------------------------------------------------------
    private void saveInfo() {
        String name = txtName.getText();
        String username = txtUsername.getText();

        if (name.equals("")) {
            DialogUtil.showWarningMessage("Họ tên không được để trống");
            return;
        } else if (username.equals("")) {
            DialogUtil.showWarningMessage("Tên đăng nhập không được để trống");
            return;
        }

        //Set default password
        if (currentStaff.getAccount().getPassword().equals("")) {
            currentStaff.getAccount().setPassword(username);
        }

        currentStaff.setName(name);
        currentStaff.getAccount().setUsername(username);

        String msg = "Lưu thay đổi?";
        int option = JOptionPane.showConfirmDialog(parent, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveStaff();
        }
    }

    private void resetPassword() {
        currentStaff.getAccount().setPassword(currentStaff.getAccount().getUsername());

        String msg = "Reset mật khẩu tài khoản này?";
        int option = JOptionPane.showConfirmDialog(parent, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveStaff();
        }
    }

    private void saveStaff() {
        try {
            parent.saveStaff(currentStaff);

            if (isNewScreen) {
                initNewStaff();
            }
            else {
                //Not new screen ==> staffId is not null
                currentStaff = StaffDAO.getByStaffID(currentStaff.getId());
            }

            refreshTextFields();
        } catch (Exception ex) {
            String errorMessage = "Lỗi không lưu được:\n"+ex.getMessage();
            DialogUtil.showErrorMessage(errorMessage);
            return;
        }

        DialogUtil.showInfoMessage("Lưu thành công!");
    }
}
