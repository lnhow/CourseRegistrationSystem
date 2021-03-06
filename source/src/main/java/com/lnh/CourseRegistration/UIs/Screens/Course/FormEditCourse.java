package com.lnh.CourseRegistration.UIs.Screens.Course;

import com.lnh.CourseRegistration.DAOs.*;
import com.lnh.CourseRegistration.Entities.*;
import com.lnh.CourseRegistration.Entities.SupportEntities.Shift;
import com.lnh.CourseRegistration.Entities.SupportEntities.Weekday;
import com.lnh.CourseRegistration.Utils.AutoCompletion;
import com.lnh.CourseRegistration.Utils.DialogUtil;
import com.lnh.CourseRegistration.Utils.HelperUtils;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

public class FormEditCourse extends JDialog {
    private JPanel mainPanel;
    private JTextField txtTeacher;
    private JTextField txtID;
    private JComboBox<Object> selectSubject;
    private JButton btnSaveInfo;
    private JComboBox<Object> selectClass;
    private JFormattedTextField txtMaxSlot;
    private JComboBox<Object> selectShift;
    private JComboBox<Object> selectWeekday;
    private JTextField txtSemester;
    private JTextField txtRoom;

    private CourseScreen parent;
    private Course currentCourse;

    private Semester currentSemester;
    private List<ClassInfo> listClass;
    private List<Shift> listShift;
    private List<Weekday> listWeekday;
    private List<Subject> listSubject;
    private boolean isNewScreen;

    public FormEditCourse(JFrame parentFrame, Course course) {
        super(parentFrame, true);
        currentCourse = course;

        boolean fetchSuccess = fetchData();
        if (!fetchSuccess) {
            return;
        }

        if (course == null) {
            isNewScreen = true;
            initNewStudent();
        } else {
            currentCourse = course;
            isNewScreen = false;
        }

        initComponents();
        parent = (CourseScreen) parentFrame;
        initWindow();
    }

    private void initNewStudent() {
        currentCourse = new Course();
        currentCourse.setSemester(currentSemester);
        currentCourse.setClassInfo(null);
        currentCourse.setMaxSlot(0);
        currentCourse.setShift(listShift.get(0));
        currentCourse.setWeekday(listWeekday.get(0));
        currentCourse.setRoomName("");
        currentCourse.setSubject(null);
    }

    private void initComponents() {
        //Enabled auto completion for this comboBox
        AutoCompletion.enable(selectClass);
        AutoCompletion.enable(selectSubject);
        refreshFields();

        NumberFormat format = NumberFormat.getNumberInstance();
        int maxDigit = 4;
        format.setMaximumIntegerDigits(maxDigit);
        txtMaxSlot.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(format)));

        if (isNewScreen) {
            setTitle("Th??m H???c ph???n");
        }
        else {
            setTitle("S???a H???c ph???n");
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
        getRootPane().setDefaultButton(btnSaveInfo);    //Allow btn to trigger if hit Enter
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //Fetch methods---------------------------------------------
    private boolean fetchData() {
        boolean isSuccess = true;
        try {
            currentSemester = fetchCurrentSemester();

            listClass = fetchClassInfo();
            listShift = fetchShift();
            listWeekday = fetchWeekday();
            listSubject = fetchSubject();
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("L???i l???y d??? li???u\n"+ex.getMessage());
            isSuccess = false;
        }
        return isSuccess;
    }

    private Semester fetchCurrentSemester() throws Exception {
        Semester current = SemesterDAO.getCurrentSemester();
        if (current == null) {
            HelperUtils.throwException("Ch??a c?? h???c k??? n??o ???????c ?????t l?? h???c k??? hi???n t???i");
        }
        return current;
    }

    private List<ClassInfo> fetchClassInfo() throws Exception {
        return ClassInfoDAO.getAll();
    }

    private List<Subject> fetchSubject() throws Exception {
        return SubjectDAO.getAll();
    }

    private List<Shift> fetchShift() throws Exception {
        return ShiftDAO.getAll();
    }

    private List<Weekday> fetchWeekday() throws Exception {
        return WeekdayDAO.getAll();
    }
    //Fetch methods---------------------------------------------


    private void refreshFields() {
        selectClass.setModel(new DefaultComboBoxModel<>(listClass.toArray()));
        selectSubject.setModel(new DefaultComboBoxModel<>(listSubject.toArray()));
        selectWeekday.setModel(new DefaultComboBoxModel<>(listWeekday.toArray()));
        selectShift.setModel(new DefaultComboBoxModel<>(listShift.toArray()));

        txtID.setText(Long.toString(currentCourse.getId()));
        txtTeacher.setText(currentCourse.getTeacherName());
        txtSemester.setText(currentCourse.getSemester().getSemesterName());
        txtMaxSlot.setValue(currentCourse.getMaxSlot());
        txtRoom.setText(currentCourse.getRoomName());

        if (currentCourse.getClassInfo() == null) {
            selectClass.setSelectedIndex(0);
        }
        else {
            selectClass.setSelectedItem(currentCourse.getClassInfo());
        }

        if (currentCourse.getSubject() == null) {
            selectSubject.setSelectedIndex(0);
        }
        else {
            selectSubject.setSelectedItem(currentCourse.getSubject());
        }

        selectWeekday.setSelectedItem(currentCourse.getWeekday());
        selectShift.setSelectedItem(currentCourse.getShift());
    }


    //Handler methods----------------------------------------------------------------------------
    private void saveInfo() {
        String teacherName = txtTeacher.getText();
        String roomName = txtRoom.getText();
        Integer maxSlot = txtMaxSlot.getValue() != null ? ((Number) txtMaxSlot.getValue()).intValue(): null;
        ClassInfo classInfo = (ClassInfo) selectClass.getSelectedItem();
        Subject subject = (Subject) selectSubject.getSelectedItem();
        Shift shift = (Shift) selectShift.getSelectedItem();
        Weekday weekday = (Weekday) selectWeekday.getSelectedItem();

        if (maxSlot == null) {
            DialogUtil.showWarningMessage("S??? s??? t???i ??a kh??ng ???????c tr???ng");
            return;
        } else if (classInfo == null) {
            DialogUtil.showWarningMessage("L???p h???c kh??ng ???????c tr???ng");
            return;
        } else if (subject == null) {
            DialogUtil.showWarningMessage("M??n h???c kh??ng ???????c tr???ng");
            return;
        } else if (shift == null) {
            DialogUtil.showWarningMessage("Ca h???c kh??ng ???????c tr???ng");
            return;
        } else if (weekday == null) {
            DialogUtil.showWarningMessage("Th??? kh??ng ???????c tr???ng");
            return;
        }

        currentCourse.setSubject(subject);
        currentCourse.setClassInfo(classInfo);
        currentCourse.setWeekday(weekday);
        currentCourse.setShift(shift);
        currentCourse.setMaxSlot(maxSlot);
        currentCourse.setTeacherName(teacherName);
        currentCourse.setRoomName(roomName);

        String msg = "L??u thay ?????i?";
        int option = JOptionPane.showConfirmDialog(parent, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveCourse();
        }
    }

    private void saveCourse() {
        try {
            parent.saveCourse(currentCourse);

            if (isNewScreen) {
                initNewStudent();
            }
            else {
                //Not new screen ==> ID is not null
                currentCourse = CourseDAO.getByCourseID(currentCourse.getId());
            }

            refreshFields();
        } catch (Exception ex) {
            String errorMessage = "L???i kh??ng l??u ???????c:\n"+ex.getMessage();
            DialogUtil.showErrorMessage(errorMessage);
            return;
        }

        DialogUtil.showInfoMessage("L??u th??nh c??ng!");
    }
}
