package com.lnh.CourseRegistration.UIs.Screens.RegistrationScreen;

import com.lnh.CourseRegistration.Controllers.LoginController;
import com.lnh.CourseRegistration.DAOs.CourseDAO;
import com.lnh.CourseRegistration.DAOs.RegistrationInfoDAO;
import com.lnh.CourseRegistration.Entities.Course;
import com.lnh.CourseRegistration.Entities.RegistrationInfo;
import com.lnh.CourseRegistration.Entities.Student;
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

public class StudentRegisterScreen implements ActionListener {
    private JPanel mainPanel;
    private JScrollPane paneRegister;
    private JTable tableRegister;
    private JButton btnRegister;
    private JScrollPane paneRegistered;
    private JTable tableRegistered;
    private JButton btnCancel;
    private JLabel lblLeft;

    DefaultTableModel tableRegisterModel;
    DefaultTableModel tableRegisteredModel;
    JMenuItem refreshRegister, refreshRegistered, registerMenuItem, cancelMenuItem;

    private static final String ObjectName = "Học phần"; //Name of the current managed object type to display
    //Table related attributes
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
    Object[] columnLabels = {"ID", "Mã môn", "Môn học", "Lớp", "Tối đa", "Phòng", "Giảng viên", "Giờ học"};
    private static final int[] DISABLE_SORT_COLUMN_INDEXES = {
            COLUMN_MAX_SLOT, COLUMN_TEACHER, COLUMN_TIME, COLUMN_ROOM
    };
    Object[] columnExtraLabels = {"ID", "Mã môn", "Môn học", "Lớp",
            "Tối đa", "Phòng", "Giảng viên", "Giờ học", "Tình trạng", "TG Đăng ký"};
    private static final int[] DISABLE_SORT_EXTRA_COLUMN_INDEXES = {
            COLUMN_MAX_SLOT, COLUMN_TEACHER, COLUMN_TIME, COLUMN_ROOM, COLUMN_STATUS
    };

    public StudentRegisterScreen() {
        initTable();
        initBtnListeners();
    }

    //Init methods-----------------------------------------------------------------
    private void initTable() {
        tableRegisterModel = initATable(tableRegister);
        tableRegisteredModel = initATableWithExtraInfo(tableRegistered);
        initTablePopupMenu();
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

        //Sort by ID default
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(COLUMN_SUBJECT_SHORT, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        table.setRowSorter(sorter);

        return tableModel;
    }

    /**
     * Initialize a table
     * @param table to initialize
     * @return Table model of initialized table
     * */
    private DefaultTableModel initATableWithExtraInfo(JTable table) {
        DefaultTableModel tableModel = new DefaultTableModel(columnExtraLabels, 0) {
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
        sortKeys.add(new RowSorter.SortKey(COLUMN_SUBJECT_SHORT, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        table.setRowSorter(sorter);

        return tableModel;
    }



    //Popup menu-----------------------------------------------------------------
    private void initTablePopupMenu() {
        initTableRegisterPopupMenu();
        initTableRegisteredPopupMenu();
    }

    private void initTableRegisterPopupMenu() {
        JPopupMenu menuConfirmed = new JPopupMenu();
        refreshRegister = new JMenuItem("Tải lại");
        registerMenuItem = new JMenuItem("Đăng ký");
        refreshRegister.addActionListener(this);
        registerMenuItem.addActionListener(this);

        menuConfirmed.add(refreshRegister);
        menuConfirmed.add(registerMenuItem);

        MouseAdapter adapterConfirmed = new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if(me.isPopupTrigger())
                    menuConfirmed.show(me.getComponent(), me.getX(), me.getY());
            }
        };

        tableRegister.addMouseListener(adapterConfirmed);
        //Make the empty area on table pane (if present) can show popup
        paneRegister.addMouseListener(adapterConfirmed);
    }

    private void initTableRegisteredPopupMenu() {
        JPopupMenu menuWaiting = new JPopupMenu();
        refreshRegistered = new JMenuItem("Tải lại");
        cancelMenuItem = new JMenuItem("Hủy đăng ký");
        refreshRegistered.addActionListener(this);
        cancelMenuItem.addActionListener(this);

        menuWaiting.add(refreshRegistered);
        menuWaiting.add(cancelMenuItem);

        MouseAdapter adapterWaiting = new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if(me.isPopupTrigger())
                    menuWaiting.show(me.getComponent(), me.getX(), me.getY());
            }
        };

        tableRegistered.addMouseListener(adapterWaiting);
        //Make the empty area on table pane (if present) can show popup
        paneRegistered.addMouseListener(adapterWaiting);
    }

    //Popup menu-----------------------------------------------------------------


    private void initBtnListeners() {
        btnRegister.addActionListener(this);
        btnCancel.addActionListener(this);
    }
    //Init methods-----------------------------------------------------------------


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnRegister || source == registerMenuItem) {

        } else if (source == btnCancel || source == cancelMenuItem) {

        } else if (source == refreshRegister) {
            refreshTableRegister();
        } else if (source == refreshRegistered) {
            refreshTableRegistered();
        }
    }


    //Handler method---------------------------------------------
    public JPanel getMainPanel() { return this.mainPanel; }


    //Refreshers-----------------------------------------------------------------------------
    public void refreshData() {
        refreshTableRegister();
        refreshTableRegistered();
    }

    private void refreshTableRegister() {
        try {
            List<Course> rows = CourseDAO.getAllInCurrentSemester();
            setTableRegisterData(rows);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    private void refreshTableRegistered() {
        try {
            Student student = LoginController.getLogInStudent();
            if (student == null) {
                HelperUtils.throwException("Không lấy được thông tin sinh viên");
            }

            List<Object[]> rows = RegistrationInfoDAO.getOfStudentInCurrentSemester(student.getStudentNo());
            setTableRegisteredData(rows);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }



    //Refreshers-----------------------------------------------------------------------------
    /**
     * Remove current datas
     * */
    public void removeData() {
        removeTableRegisterData();
        removeTableRegisteredData();
    }

    private void removeTableRegisterData() {
        tableRegisterModel.setRowCount(0);
    }
    /**
     * Set table data to list of rows
     * @param list List data of row to set
     */
    private void setTableRegisterData(List<Course> list) {
        removeTableRegisterData();

        for (Course row: list) {
            Shift courseShift = row.getShift();
            String time = row.getWeekday().getWeekdayName()
                    + " ("+ courseShift.getShiftStart() +" - " + courseShift.getShiftEnd() +")";
            Object[] rowData = {
                    row.getId(),
                    row.getSubject().getShortName(),
                    row.getSubject().getSubjectName(),
                    row.getClassInfo().getClassName(),
                    row.getMaxSlot(),
                    row.getRoomName(),
                    row.getTeacherName(),
                    time
            };
            tableRegisterModel.addRow(rowData);
        }
    }

    private void removeTableRegisteredData() {
        tableRegisteredModel.setRowCount(0);
    }
    /**
     * Set table data to list of rows
     * @param list List data of row to set
     */
    private void setTableRegisteredData(List<Object[]> list) {
        removeTableRegisteredData();

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
            tableRegisteredModel.addRow(rowData);
        }
    }

    //Handling methods--------------------------------------------------------------------


//    private void confirm() {
//        List<Long> dbIDs = getSelectedIDsFromWaiting();
//
//        if (dbIDs.size() > 0) {
//            String msg = "Xác nhận duyệt các ĐKHP này (Không thể hoàn tác)";
//            int option = JOptionPane.showConfirmDialog(
//                    this.mainPanel, msg, "Xác nhận duyệt", JOptionPane.OK_CANCEL_OPTION
//            );
//
//            if (option != JOptionPane.YES_OPTION) {
//                return;
//            }
//
//            for (Long dbID: dbIDs) {
//                try {
//                    RegistrationInfoController.confirmCourseByStaff(dbID, currentCourseID);
//                    refreshTableWaiting();
//                    refreshTableConfirmed();
//                } catch (Exception ex) {
//                    try {
//                        Student student = StudentDAO.getByID(dbID);
//                        DialogUtil.showErrorMessage(
//                                "Lỗi duyệt " + ObjectName + " " + student.getId() + "\n" + ex.getMessage()
//                        );
//                    } catch (Exception exception) {
//                        DialogUtil.showErrorMessage(
//                                "Lỗi duyệt "+ ObjectName + " có dbID " + dbID + "\n" + ex.getMessage());
//                    }
//                }
//            }
//        } else {
//            DialogUtil.showInfoMessage("Vui lòng chọn ít nhất 1 (một) " + ObjectName + " để duyệt");
//        }
//    }
//
//    private void cancel() {
//
//        String msg = "Xác nhận hủy các ĐKHP này";
//        int option = JOptionPane.showConfirmDialog(
//                this.mainPanel, msg, "Xác nhận hủy", JOptionPane.OK_CANCEL_OPTION
//        );
//
//        if (option != JOptionPane.YES_OPTION) {
//            return;
//        }
//
//        if (dbIDs.size() > 0) {
//            for (Long dbID: dbIDs) {
//                try {
//
//                } catch (Exception ex) {
//                    try {
//                        Student student = StudentDAO.getByID(dbID);
//                        DialogUtil.showErrorMessage(
//                                "Lỗi hủy " + ObjectName + " " + student.getId() + "\n" + ex.getMessage()
//                        );
//                    } catch (Exception exception) {
//                        DialogUtil.showErrorMessage(
//                                "Lỗi hủy "+ ObjectName + " có dbID " + dbID + "\n" + ex.getMessage());
//                    }
//                }
//            }
//        } else {
//            DialogUtil.showInfoMessage("Vui lòng chọn ít nhất 1 (một) " + ObjectName + " để duyệt");
//        }
//    }

    //Table selects..........................................................
    /**
     * Get ID from selected table register row
     * @return List of course DatabaseID
     */
    private long getSelectedIDFromRegister() {
        int index = tableRegister.getSelectedRow();
        return (Long) tableRegister.getValueAt(index, COLUMN_ID);
    }

    /**
     * Get IDs from selected table cancelled rows
     * @return List of student DatabaseID
     */
    private long getSelectedIDFromRegistered() {
        int index = tableRegistered.getSelectedRow();
        return (Long) tableRegistered.getValueAt(index, COLUMN_ID);
    }
}
