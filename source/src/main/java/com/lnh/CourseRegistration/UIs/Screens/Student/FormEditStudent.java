package com.lnh.CourseRegistration.UIs.Screens.Student;

import com.lnh.CourseRegistration.DAOs.StudentDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.ClassInfo;
import com.lnh.CourseRegistration.Entities.Student;
import com.lnh.CourseRegistration.Utils.DialogUtil;
import com.sun.istack.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormEditStudent extends JDialog {

    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtUsername;
    private JTextField txtDBID;
    private JPanel panelBtns;
    private JButton btnSaveInfo;
    private JButton btnResetPwd;
    private JTextField txtID;
    private JComboBox selectGender;
    private JTextField txtClass;

    private StudentScreen parent;
    private Student currentStudent;
    private ClassInfo currentClass;
    private boolean isNewScreen;

    public FormEditStudent(JFrame parentFrame, Student student, @NotNull ClassInfo classInfo) {
        super(parentFrame, true);
        currentClass = classInfo;
        if (student == null) {
            isNewScreen = true;
            initNewStudent();
        } else {
            currentStudent = student;
            isNewScreen = false;
        }

        initComponents();
        parent = (StudentScreen) parentFrame;
        initWindow();
    }

    private void initNewStudent() {
        currentStudent = new Student();
        currentStudent.setName("");
        currentStudent.setAccount(new Account());
        currentStudent.setMale(true);
        currentStudent.setClassInfo(currentClass);
        currentStudent.getAccount().setUsername("");
        currentStudent.getAccount().setPassword("");
        currentStudent.getAccount().setType(Account.ACCOUNT_STUDENT);
    }

    private void initComponents() {
        refreshTextFields();

        if (isNewScreen) {
            panelBtns.remove(btnResetPwd);
            setTitle("Thêm Sinh viên");
        }
        else {
            setTitle("Sửa Sinh viên");
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
                resetAccount();
            }
        });
    }

    private void initWindow() {
        setContentPane(this.mainPanel);
        getRootPane().setDefaultButton(btnSaveInfo);    //Allow btn to trigger if hit Enter
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshTextFields() {
        txtDBID.setText(Long.toString(currentStudent.getStudentNo()));
        txtID.setText(currentStudent.getId());
        txtName.setText(currentStudent.getName());
        selectGender.setSelectedIndex(currentStudent.isMale() ? 0 : 1);
        txtClass.setText(currentStudent.getClassInfo().getClassName());
        txtUsername.setText(currentStudent.getAccount().getUsername());
    }


    //Handler methods----------------------------------------------------------------------------
    private void saveInfo() {
        String id = txtID.getText();
        String name = txtName.getText();
        String username = txtUsername.getText();
        boolean isMale = selectGender.getSelectedIndex() == 0;

        if (name.equals("")) {
            DialogUtil.showWarningMessage("Họ tên không được để trống");
            return;
        } else if (id.equals("")) {
            DialogUtil.showWarningMessage("Tên đăng nhập không được để trống");
            return;
        }

        //Set default username
        if (currentStudent.getAccount().getUsername().equals("")) {
            currentStudent.getAccount().setUsername(id);
        }

        //Set default password
        if (currentStudent.getAccount().getPassword().equals("")) {
            currentStudent.getAccount().setPassword(username);
        }

        currentStudent.setId(id);
        currentStudent.setName(name);
        currentStudent.setMale(isMale);

        String msg = "Lưu thay đổi?";
        int option = JOptionPane.showConfirmDialog(parent, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveStudent();
        }
    }

    private void resetAccount() {
        currentStudent.getAccount().setUsername(currentStudent.getId());
        currentStudent.getAccount().setPassword(currentStudent.getAccount().getUsername());

        String msg = "Reset mật khẩu tài khoản này?";
        int option = JOptionPane.showConfirmDialog(parent, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveStudent();
        }
    }

    private void saveStudent() {
        try {
            parent.saveStudent(currentStudent);

            if (isNewScreen) {
                initNewStudent();
            }
            else {
                //Not new screen ==> staffId is not null
                currentStudent = StudentDAO.getByID(currentStudent.getStudentNo());
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
