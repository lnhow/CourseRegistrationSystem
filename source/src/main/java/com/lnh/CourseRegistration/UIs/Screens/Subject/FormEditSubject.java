package com.lnh.CourseRegistration.UIs.Screens.Subject;

import com.lnh.CourseRegistration.DAOs.SubjectDAO;
import com.lnh.CourseRegistration.Entities.Subject;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class FormEditSubject extends JDialog {
    private JPanel mainPanel;
    private JTextField txtShortName;
    private JTextField txtName;
    private JTextField txtSubjectID;
    private JPanel panelBtns;
    private JButton btnSaveInfo;
    private JFormattedTextField txtCredit;

    private SubjectScreen parent;
    private boolean isNewScreen;
    private Subject currentSubject;

    public FormEditSubject(JFrame parentFrame, Subject subject) {
        super(parentFrame, true);
        if (subject == null) {
            isNewScreen = true;
            initNewSubject();
        } else {
            currentSubject = subject;
            isNewScreen = false;
        }

        initComponents();
        parent = (SubjectScreen) parentFrame;
        initWindow();
    }

    private void initNewSubject() {
        currentSubject = new Subject();
        currentSubject.setSubjectName("");
        currentSubject.setShortName("");
        currentSubject.setNumCredit(0);
    }

    private void initComponents() {
        NumberFormat format = NumberFormat.getNumberInstance();
        int maxDigit = 2;
        format.setMaximumIntegerDigits(maxDigit);
        txtCredit.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(format)));

        refreshTextFields();

        if (isNewScreen) {
            setTitle("Thêm Môn học");
        }
        else {
            setTitle("Sửa Môn học");
        }

        btnSaveInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveInfo();
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
        txtSubjectID.setText(Integer.toString(currentSubject.getId()));
        txtShortName.setText(currentSubject.getShortName());
        txtName.setText(currentSubject.getSubjectName());
        txtCredit.setText(Integer.toString(currentSubject.getNumCredit()));
    }


    //Handler methods----------------------------------------------------------------------------
    private void saveInfo() {
        String name = txtName.getText().trim();
        String shortName = txtShortName.getText().trim();
        Integer numCredit = txtCredit.getValue() != null ? ((Long) txtCredit.getValue()).intValue(): null;

        if (name.equals("")) {
            DialogUtil.showWarningMessage("Tên Môn học không được để trống");
            return;
        } else if (shortName.equals("")) {
            DialogUtil.showWarningMessage("Mã môn học không được để trống");
            return;
        } else if (numCredit == null) {
            DialogUtil.showWarningMessage("Số tín chỉ không được để trống");
            return;
        }

        currentSubject.setSubjectName(name);
        currentSubject.setShortName(shortName);
        currentSubject.setNumCredit(numCredit);

        String msg = "Lưu thay đổi?";
        int option = JOptionPane.showConfirmDialog(parent, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveSubject();
        }
    }

    private void saveSubject() {
        try {
            parent.saveSubject(currentSubject);

            if (isNewScreen) {
                initNewSubject();
            }
            else {
                //Not new screen ==> SubjectId is not null
                currentSubject = SubjectDAO.getBySubjectID(currentSubject.getId());
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
