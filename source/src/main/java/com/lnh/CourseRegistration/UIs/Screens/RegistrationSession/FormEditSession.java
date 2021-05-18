package com.lnh.CourseRegistration.UIs.Screens.RegistrationSession;

import com.lnh.CourseRegistration.Controllers.SemesterController;
import com.lnh.CourseRegistration.DAOs.RegistrationSessionDAO;
import com.lnh.CourseRegistration.Entities.RegistrationSession;
import com.lnh.CourseRegistration.Entities.Semester;
import com.lnh.CourseRegistration.Utils.DialogUtil;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

//TODO: Add Time picker
public class FormEditSession extends JDialog {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtID;
    private JPanel holderStart;
    private JPanel holderEnd;
    private JButton btnSave;
    private JTextField txtYear;

    //Date - Year pickers
    private JDateChooser chooserDateStart;
    private JDateChooser chooserDateEnd;

    private SessionScreen parent;
    private RegistrationSession currentSession;   //Current Session to edit
    private boolean isNewScreen;

    public FormEditSession(JFrame parentFrame, RegistrationSession session) {
        super(parentFrame, true);
        if (session == null) {
            Semester current = SemesterController.getCurrentSemester();

            if (current == null) {
                DialogUtil.showWarningMessage("Chưa có học kỳ nào được đặt làm Học kỳ hiện tại");
                return;
            }

            isNewScreen = true;
            initNewSession();
        } else {
            currentSession = session;
            isNewScreen = false;
        }

        initComponents();
        parent = (SessionScreen) parentFrame;
        initWindow();
    }

    private void initNewSession() {
        Semester current = SemesterController.getCurrentSemester();
        currentSession = new RegistrationSession(current, null);
        currentSession.setSessionStart(new Date());
        currentSession.setSessionEnd(new Date());
    }

    private void initComponents() {
        chooserDateStart = new JDateChooser();
        chooserDateEnd = new JDateChooser();

        //Capital MM is Month while mm is minute in Java
        chooserDateStart.setDateFormatString("dd/MM/yyyy");
        chooserDateEnd.setDateFormatString("dd/MM/yyyy");
        
        holderStart.add(chooserDateStart, BorderLayout.CENTER);
        holderEnd.add(chooserDateEnd, BorderLayout.CENTER);
        refreshTextFields();

        if (isNewScreen) {
            setTitle("Thêm Học kì");
        }
        else {
            setTitle("Sửa Học kì");
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
        txtID.setText(Long.toString(currentSession.getId()));
        txtName.setText(currentSession.getSemester().getSemesterName());
        txtYear.setText(Integer.toString(currentSession.getSemester().getSemesterYear()));
        chooserDateStart.setDate(currentSession.getSessionStart());
        chooserDateEnd.setDate(currentSession.getSessionEnd());
    }


    //Handler methods----------------------------------------------------------------------------
    private void saveInfo() {
        String name = txtName.getText();
        Date startDate = chooserDateStart.getDate();
        Date endDate = chooserDateEnd.getDate();
        Semester current = SemesterController.getCurrentSemester();

        if (name.equals("")) {
            DialogUtil.showWarningMessage("Họ tên không được để trống");
            return;
        }


        String msg = "Lưu thay đổi?";
        int option = JOptionPane.showConfirmDialog(parent, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveSession();
        }
    }

    private void saveSession() {
        try {
            parent.saveSession(currentSession);

            if (isNewScreen) {
                initNewSession();
            }
            else {
                //Not new screen ==> SessionId is not null
                currentSession = RegistrationSessionDAO.getBySessionID(currentSession.getId());
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
