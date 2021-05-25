package com.lnh.CourseRegistration.UIs.Screens.Semester;

import com.github.lgooddatepicker.components.DatePicker;
import com.lnh.CourseRegistration.DAOs.SemesterDAO;
import com.lnh.CourseRegistration.Entities.Semester;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.IntStream;

public class FormEditSemester extends JDialog {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtID;
    private JButton btnSave;
    private DatePicker pickerStart;
    private DatePicker pickerEnd;
    private JComboBox<Integer> pickerYear;

    private SemesterScreen parent;
    private Semester currentSemester;   //Current Semester to edit
    private boolean isNewScreen;

    public FormEditSemester(JFrame parentFrame, Semester semester) {
        super(parentFrame, true);
        if (semester == null) {
            isNewScreen = true;
            initNewSemester();
        } else {
            currentSemester = semester;
            isNewScreen = false;
        }

        initComponents();
        parent = (SemesterScreen) parentFrame;
        initWindow();
    }

    private void initNewSemester() {
        currentSemester = new Semester();
        currentSemester.setSemesterName("");
        currentSemester.setSemesterYear(Year.now().getValue());
        currentSemester.setSemesterStart(new Date());
        currentSemester.setSemesterEnd(new Date());
    }

    private void initComponents() {
        initYearPicker();
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

    private void initYearPicker() {
        int limit = 10; //Limit of years before and after current year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Integer[] yearList = IntStream.rangeClosed(
                currentYear - limit, currentYear + limit
        ).boxed().toArray(Integer[]::new);  //Boxed to wrapper class & to array with wrapper array constructor
        pickerYear.setModel(new DefaultComboBoxModel<Integer>(yearList));
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
        txtID.setText(Integer.toString(currentSemester.getId()));
        txtName.setText(currentSemester.getSemesterName());
        pickerYear.setSelectedItem(currentSemester.getSemesterYear());

        pickerStart.setDate(new java.sql.Date(currentSemester.getSemesterStart().getTime()).toLocalDate());
        pickerEnd.setDate(new java.sql.Date(currentSemester.getSemesterEnd().getTime()).toLocalDate());
    }


    //Handler methods----------------------------------------------------------------------------
    private void saveInfo() {
        String name = txtName.getText();
        Integer year = (Integer) pickerYear.getSelectedItem();


        LocalDate dateStart = pickerStart.getDate();
        LocalDate dateEnd = pickerEnd.getDate();

        if (dateStart == null) {
            DialogUtil.showWarningMessage("Ngày bắt đầu không được để trống");
            return;
        } else if (dateEnd == null) {
            DialogUtil.showWarningMessage("Ngày kết thúc không được để trống");
            return;
        } else if (name.equals("")) {
            DialogUtil.showWarningMessage("Tên Học kỳ không được để trống");
            return;
        } else if (year == null) {
            DialogUtil.showWarningMessage("Năm học kỳ không được để trống");
            return;
        }

        currentSemester.setSemesterName(name);
        currentSemester.setSemesterYear(year);
        currentSemester.setSemesterStart(java.sql.Date.valueOf(dateStart));
        currentSemester.setSemesterEnd(java.sql.Date.valueOf(dateEnd));

        String msg = "Lưu thay đổi?";
        int option = JOptionPane.showConfirmDialog(parent, msg);

        if (option == JOptionPane.YES_OPTION) {
            saveSemester();
        }
    }

    private void saveSemester() {
        try {
            parent.saveSemester(currentSemester);

            if (isNewScreen) {
                initNewSemester();
            }
            else {
                //Not new screen ==> SemesterId is not null
                currentSemester = SemesterDAO.getBySemesterID(currentSemester.getId());
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
