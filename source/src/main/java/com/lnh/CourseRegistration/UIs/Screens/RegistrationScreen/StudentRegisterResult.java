package com.lnh.CourseRegistration.UIs.Screens.RegistrationScreen;

import com.lnh.CourseRegistration.Controllers.LoginController;
import com.lnh.CourseRegistration.DAOs.RegistrationInfoDAO;
import com.lnh.CourseRegistration.Entities.Course;
import com.lnh.CourseRegistration.Entities.RegistrationInfo;
import com.lnh.CourseRegistration.Entities.Student;
import com.lnh.CourseRegistration.Entities.SupportEntities.RegisterStatus;
import com.lnh.CourseRegistration.Entities.SupportEntities.Shift;
import com.lnh.CourseRegistration.Utils.CustomComparator;
import com.lnh.CourseRegistration.Utils.DialogUtil;
import com.lnh.CourseRegistration.Utils.HelperUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class StudentRegisterResult implements ActionListener {
    private JPanel mainPanel;
    private JScrollPane paneCurrent;
    private JTable tableCurrent;
    private JScrollPane paneAll;
    private JTable tableAll;

    private DefaultTableModel tableCurrentModel;
    private DefaultTableModel tableAllModel;

    private JMenuItem refreshCurrent, refreshAll;

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_SUBJECT_SHORT = 1;
    private static final int COLUMN_SUBJECT = 2;
    private static final int COLUMN_CLASS = 3;
    private static final int COLUMN_MAX_SLOT = 4;
    private static final int COLUMN_ROOM = 5;
    private static final int COLUMN_TEACHER = 6;
    private static final int COLUMN_TIME = 7;
    private static final int COLUMN_STATUS = 8;
    private static final int COLUMN_REGISTER_TIME = 9;
    Object[] columnLabels = {
            "ID", "Mã môn", "Môn học", "Lớp", "Tối đa", "Phòng", "Giảng viên", "Giờ học",
            "Tình trạng", "TG Đăng ký"
    };
    private static final int[] DISABLE_SORT_COLUMN_INDEXES = {
            COLUMN_MAX_SLOT, COLUMN_TEACHER, COLUMN_TIME, COLUMN_ROOM, COLUMN_STATUS
    };

    public StudentRegisterResult() {
        initTable();
        initPopupMenu();
    }

    public void refreshData() {
        refreshTableCurrent();
        refreshTableAll();
    }

    public void removeData() {
        removeTableCurrentData();
        removeTableAllData();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    //Init---------------------------------------------------------------------------------------
    //Table
    private void initTable() {
        tableCurrentModel = initATable(tableCurrent);
        tableAllModel = initATable(tableAll);
    }

    /**
     * Initialize a table
     * @param table to initialize
     * @return Table model of initialized table
     * */
    private DefaultTableModel initATable(JTable table) {
        DefaultTableModel tableModel = new DefaultTableModel(columnLabels, 0) {
            @Override
            public boolean isCellEditable(int rowIdx, int colIdx) { return false; }
        };
        table.setModel(tableModel);
        //Sorter-----------------------------------------------------------------------------
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);

        for (int index: DISABLE_SORT_COLUMN_INDEXES) {
            sorter.setSortable(index, false);
        }

        sorter.setComparator(COLUMN_ID, new CustomComparator.ComparatorLong());
        sorter.setComparator(COLUMN_SUBJECT_SHORT, new CustomComparator.ComparatorString());
        sorter.setComparator(COLUMN_SUBJECT, new CustomComparator.ComparatorString());
        sorter.setComparator(COLUMN_CLASS, new CustomComparator.ComparatorString());
        sorter.setComparator(COLUMN_REGISTER_TIME, new CustomComparator.ComparatorLocalDateTime());

        //Sort by ID default
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(COLUMN_TIME, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        table.setRowSorter(sorter);

        return tableModel;
    }
    //Table

    //Popup menu
    private void initPopupMenu() {
        initCurrentPopupMenu();
        initAllPopupMenu();
    }
    private void initCurrentPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        refreshCurrent = new JMenuItem("Tải lại");
        refreshCurrent.addActionListener(this);

        popupMenu.add(refreshCurrent);
        MouseAdapter adapter = new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if(me.isPopupTrigger())
                    popupMenu.show(me.getComponent(), me.getX(), me.getY());
            }
        };

        tableCurrent.addMouseListener(adapter);
        //Make the empty area on table pane (if present) can show popup
        paneCurrent.addMouseListener(adapter);
    }
    private void initAllPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        refreshAll = new JMenuItem("Tải lại");
        refreshAll.addActionListener(this);

        popupMenu.add(refreshAll);
        MouseAdapter adapter = new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if(me.isPopupTrigger())
                    popupMenu.show(me.getComponent(), me.getX(), me.getY());
            }
        };

        tableAll.addMouseListener(adapter);
        //Make the empty area on table pane (if present) can show popup
        paneAll.addMouseListener(adapter);
    }
    //Popup menu


    //Init---------------------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == refreshCurrent) {
            refreshTableCurrent();
        } else if (source == refreshAll) {
            refreshTableAll();
        }
    }

    private void refreshTableCurrent() {
        try {
            Student student = LoginController.getLogInStudent();
            if (student == null) {
                HelperUtils.throwException("Không lấy được thông tin sinh viên");
            }

            List<Object[]> rows = RegistrationInfoDAO.getOfStudentInCurrentSemester(student.getStudentNo());
            setTableCurrentData(rows);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách ĐKHP\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    private void refreshTableAll() {
        try {
            Student student = LoginController.getLogInStudent();
            if (student == null) {
                HelperUtils.throwException("Không lấy được thông tin sinh viên");
            }

            List<Object[]> rows = RegistrationInfoDAO.getOfStudent(
                    student.getStudentNo(), RegisterStatus.STATUS_CONFIRMED);
            setTableAllData(rows);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách ĐKHP\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    private void removeTableCurrentData() {
        tableCurrentModel.setRowCount(0);
    }
    /**
     * Set table data to list of rows
     * @param list List data of row to set
     */
    private void setTableCurrentData(List<Object[]> list) {
        removeTableCurrentData();

        for (Object[] row: list) {
            RegistrationInfo info = (RegistrationInfo) row[0];
            Course course = (Course) row[1];
            Shift courseShift = course.getShift();
            String time = course.getWeekday().getWeekdayName()
                    + " ("+ courseShift.getShiftStart() +" - " + courseShift.getShiftEnd() +")";
            Object[] rowData = {
                    course.getId(),
                    course.getSubject().getShortName(),
                    course.getSubject().getSubjectName(),
                    course.getClassInfo().getClassName(),
                    course.getMaxSlot(),
                    course.getRoomName(),
                    course.getTeacherName(),
                    time,
                    info.getStatus().getStatusDesc(),
                    info.getRegisterTime().toLocalDateTime()
            };
            tableCurrentModel.addRow(rowData);
        }
    }

    private void removeTableAllData() {
        tableAllModel.setRowCount(0);
    }
    /**
     * Set table data to list of rows
     * @param list List data of row to set
     */
    private void setTableAllData(List<Object[]> list) {
        removeTableAllData();

        for (Object[] row: list) {
            RegistrationInfo info = (RegistrationInfo) row[0];
            Course course = (Course) row[1];
            Shift courseShift = course.getShift();
            String time = course.getWeekday().getWeekdayName()
                    + " ("+ courseShift.getShiftStart() +" - " + courseShift.getShiftEnd() +")";
            Object[] rowData = {
                    course.getId(),
                    course.getSubject().getShortName(),
                    course.getSubject().getSubjectName(),
                    course.getClassInfo().getClassName(),
                    course.getMaxSlot(),
                    course.getRoomName(),
                    course.getTeacherName(),
                    time,
                    info.getStatus().getStatusDesc(),
                    info.getRegisterTime().toLocalDateTime()
            };
            tableAllModel.addRow(rowData);
        }
    }
}
