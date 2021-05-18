package com.lnh.CourseRegistration.UIs.Screens.ClassInfo;

import com.lnh.CourseRegistration.DAOs.ClassInfoDAO;
import com.lnh.CourseRegistration.Entities.ClassInfo;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormEditClassInfo extends JDialog {
    private JTextField txtName;
    private JTextField txtID;
    private JButton btnSave;
    private JPanel mainPanel;

    private ClassInfoScreen parent;
    private ClassInfo currentClassInfo;   //Current ClassInfo to edit
    private boolean isNewScreen;

    public FormEditClassInfo(JFrame parentFrame, ClassInfo ClassInfo) {
        super(parentFrame, true);
        if (ClassInfo == null) {
            isNewScreen = true;
            initNewClassInfo();
        } else {
            currentClassInfo = ClassInfo;
            isNewScreen = false;
        }

        initComponents();
        parent = (ClassInfoScreen) parentFrame;
        initWindow();
    }

    private void initNewClassInfo() {
        currentClassInfo = new ClassInfo();
        currentClassInfo.setClassName("");
    }

    private void initComponents() {
        refreshTextFields();

        if (isNewScreen) {
            setTitle("Thêm Lớp học");
        }
        else {
            setTitle("Sửa Lớp học");
        }

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInfo();
            }
        });
    }

    private void initWindow() {
        setContentPane(this.mainPanel);
        getRootPane().setDefaultButton(btnSave);    //Allow btn to trigger if hit Enter
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshTextFields() {
        txtID.setText(Integer.toString(currentClassInfo.getId()));
        txtName.setText(currentClassInfo.getClassName());
    }


    //Handler methods----------------------------------------------------------------------------
    private void saveInfo() {
        String name = txtName.getText();

        if (name.equals("")) {
            DialogUtil.showWarningMessage("Họ tên không được để trống");
            return;
        }

        currentClassInfo.setClassName(name);

        String msg = "Lưu thay đổi?";
        int option = JOptionPane.showConfirmDialog(parent, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveClassInfo();
        }
    }

    private void saveClassInfo() {
        try {
            parent.saveClassInfo(currentClassInfo);

            if (isNewScreen) {
                initNewClassInfo();
            }
            else {
                //Not new screen ==> ClassInfoId is not null
                currentClassInfo = ClassInfoDAO.getByClassInfoID(currentClassInfo.getId());
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
