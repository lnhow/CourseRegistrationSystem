package com.lnh.CourseRegistration.UIs.Screens.RegistrationSession;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.lnh.CourseRegistration.Controllers.LoginController;
import com.lnh.CourseRegistration.DAOs.RegistrationSessionDAO;
import com.lnh.CourseRegistration.DAOs.SemesterDAO;
import com.lnh.CourseRegistration.Entities.RegistrationSession;
import com.lnh.CourseRegistration.Entities.Semester;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class FormEditSession extends JDialog {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtID;
    private JButton btnSave;
    private JTextField txtYear;
    private DateTimePicker pickerStart;
    private DateTimePicker pickerEnd;

    private SessionScreen parent;
    Semester current = null;
    private RegistrationSession currentSession;   //Current Session to edit
    private boolean isNewScreen;

    public FormEditSession(JFrame parentFrame, RegistrationSession session) {
        super(parentFrame, true);
        if (session == null) {
            try {
                current = SemesterDAO.getCurrentSemester();
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi. Không lấy dữ liệu học kì hiện tại được\n" +ex.getMessage());
                return;
            }

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
        currentSession = new RegistrationSession(current, LoginController.getLogInStaff());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        currentSession.setSessionStart(currentTime);
        currentSession.setSessionEnd(currentTime);
    }

    private void initComponents() {
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
        pickerStart.setDateTimeStrict(currentSession.getSessionStart().toLocalDateTime());
        pickerEnd.setDateTimeStrict(currentSession.getSessionEnd().toLocalDateTime());
    }


    //Handler methods----------------------------------------------------------------------------
    private void saveInfo() {
        LocalDateTime timeStart = pickerStart.getDateTimeStrict();
        LocalDateTime timeEnd = pickerEnd.getDateTimeStrict();

        if (timeStart == null) {
            DialogUtil.showWarningMessage("Thời gian bắt đầu không được để trống");
            return;
        }  else if (timeEnd == null) {
            DialogUtil.showWarningMessage("Thời gian kết thúc không được để trống");
            return;
        }

        currentSession.setSessionStart(Timestamp.valueOf(timeStart));
        currentSession.setSessionEnd(Timestamp.valueOf(timeEnd));

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
