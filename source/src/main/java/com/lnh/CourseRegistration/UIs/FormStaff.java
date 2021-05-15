package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

public class FormStaff implements ActionListener {
    private JFrame AppFrame;
    private Staff currentLoggedIn;

    private JPanel mainPanel;
    private JButton btnLogOut;
    private JButton btnInfo;
    private JLabel txtStaff;

    private JButton btnStaff;
    private JButton btnStudent;
    private JButton btnCourse;
    private JButton btnClass;
    private JTable tableMain;
    private JButton btnNew;
    private JButton btnSearch;

    private static final int SCREEN_STAFF = 0;
    private static final int SCREEN_STUDENT = 1;
    private static final int SCREEN_CLASS = 2;
    private static final int SCREEN_COURSE = 3;
    private DefaultTableModel tableModel;
    private List<Staff> staffList;


    public FormStaff(Account account) {
        initComponents();
        setVisible();
        fetchStaff(account);

        boolean isStaffNotExist = currentLoggedIn == null;
        if (isStaffNotExist) {
            DialogUtil.showErrorMessage(
                    "Account invalid(ID: "+account.getId()+"). Please contact administrator."
            );
            logOut();
            return;
        }

        setAccountText();
    }

    private void setVisible() {
        AppFrame = new JFrame("Staff Dashboard");
        AppFrame.setContentPane(this.mainPanel);
        AppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AppFrame.pack();
        AppFrame.setVisible(true);
        AppFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        btnInfo.addActionListener(this);
        btnLogOut.addActionListener(this);
        btnStaff.addActionListener(this);
        btnStudent.addActionListener(this);
        btnClass.addActionListener(this);
        btnCourse.addActionListener(this);

        btnNew.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnInfo) {
            openAccountForm();
        } else if (source == btnLogOut) {
            logOut();
        } else if (source == btnStaff) {
            showStaffScreen();
        } else if (source == btnStudent) {

        } else if (source == btnClass) {

        } else if (source == btnCourse) {

        } else if (source == btnNew) {
            openStaffEditDialog(null);
        }
    }

    //Handling methods-------------------------------------------------------------------------------------------------
    private void setAccountText() {
        txtStaff.setText("Welcome, " + currentLoggedIn.getName());
    }

    private void fetchStaff(Account account) {
        try {
            currentLoggedIn = StaffDAO.getByAccountID(account.getId());
        } catch (Exception exception) {
            DialogUtil.showErrorMessage(exception.getMessage());
        }
    }

    private void logOut() {
        currentLoggedIn = null;
        new FormLogin();
        AppFrame.dispose();
    }

    private void openAccountForm() {
        JDialog dialog = new JDialog(this.AppFrame);
        new FormAccount(dialog, currentLoggedIn);
        fetchStaff(currentLoggedIn.getAccount());
        setAccountText();
    }

    private void showStaffScreen() {
        initStaffTable();
        refreshStaffTable();
    }

    private void initStaffTable() {
        Object[] columnLabels = {"ID", "Name", "Username"};
        tableModel = new DefaultTableModel(columnLabels, 0) {
            @Override
            public boolean isCellEditable(int rowIdx, int colIdx) { return false; }
        };
        tableMain.setModel(tableModel);
    }

    private void refreshStaffTable() {
        try {
            staffList = StaffDAO.getAll();
            tableModel.setRowCount(0);

            for (Staff staff: staffList) {
                Object[] rowData = {
                        staff.getId(),
                        staff.getName(),
                        staff.getAccount().getUsername()
                };
                tableModel.addRow(rowData);
            }
        } catch (Exception ex) {
            DialogUtil.showErrorMessage(ex.getMessage());
        }
    }

    private void openStaffEditDialog(Staff aStaff) {
        JDialog dialog = new JDialog(this.AppFrame);
        new FormEditStaff(dialog, aStaff);
        refreshStaffTable();
    }
}
