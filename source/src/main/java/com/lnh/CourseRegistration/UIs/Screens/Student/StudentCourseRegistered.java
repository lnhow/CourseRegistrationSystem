package com.lnh.CourseRegistration.UIs.Screens.Student;

import com.lnh.CourseRegistration.Controllers.LoginController;
import com.lnh.CourseRegistration.Controllers.RegistrationInfoController;
import com.lnh.CourseRegistration.DAOs.RegistrationInfoDAO;
import com.lnh.CourseRegistration.DAOs.StudentDAO;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class StudentCourseRegistered extends JDialog implements ActionListener {
    private JPanel mainPanel;
    private JScrollPane paneTable;
    private JTable tableMain;
    private JLabel lblStudent;
    private DefaultTableModel tableModel;

    private JMenuItem refreshMenuItem, confirmMenuItem, cancelMenuItem;

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
    private static final int COLUMN_HIDDEN_STATUS_ID = 9;
    Object[] columnLabels = {
            "ID", "Mã môn", "Môn học", "Lớp", "Tối đa", "Phòng", "Giảng viên", "Giờ học",
            "Tình trạng", "TG Đăng ký"
    };
    private static final int[] DISABLE_SORT_COLUMN_INDEXES = {
            COLUMN_MAX_SLOT, COLUMN_TEACHER, COLUMN_TIME, COLUMN_ROOM, COLUMN_STATUS, COLUMN_HIDDEN_STATUS_ID
    };

    private long studentDBID = -1;
    private Student currentStudent;

    public StudentCourseRegistered(JFrame parent,long studentDBID) {
        super(parent, true);
        initTable();
        initPopupMenu();
        this.studentDBID = studentDBID;
        try {
            refreshStudent();
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Lỗi lấy thông tin SV\n" + ex.getMessage());
            return;
        }
        refreshTable();
        initWindow();
    }

    public void refreshData() {
        //Refresh student first
        try {
            refreshStudent();
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Lỗi lấy thông tin SV\n" + ex.getMessage());
            return;
        }
        refreshTable();
    }

    public void removeData() {
        removeTableData();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    //Init---------------------------------------------------------------------------------------
    private void initWindow() {
        setContentPane(this.mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1280, 560));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //Table
    private void initTable() {
        tableModel = initATable(tableMain);
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
    }
    private void initCurrentPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        refreshMenuItem = new JMenuItem("Tải lại");
        confirmMenuItem = new JMenuItem("Duyệt");
        cancelMenuItem = new JMenuItem("Hủy");
        refreshMenuItem.addActionListener(this);
        confirmMenuItem.addActionListener(this);
        cancelMenuItem.addActionListener(this);

        popupMenu.add(refreshMenuItem);
        popupMenu.add(confirmMenuItem);
        popupMenu.add(cancelMenuItem);
        MouseAdapter adapter = new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if(me.isPopupTrigger()) {
                    popupMenu.show(me.getComponent(), me.getX(), me.getY());
                }
            }
        };

        tableMain.setComponentPopupMenu(popupMenu);
        //Make the empty area on table pane (if present) can show popup
        paneTable.addMouseListener(adapter);
    }
    //Popup menu


    //Init---------------------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == refreshMenuItem) {
            refreshTable();
        } else if (source == confirmMenuItem) {
            confirm();
        } else if (source == cancelMenuItem) {
            cancel();
        }
    }

    private void refreshStudent() throws Exception {
        long id = studentDBID;

        if (id > -1) {
            currentStudent = StudentDAO.getByID(id);
        }

        if (currentStudent == null) {
            HelperUtils.throwException("Không lấy được thông tin SV");
            return;
        }

        studentDBID = currentStudent.getStudentNo();
        String msg = "Sinh viên " + currentStudent.getId() + " - " + currentStudent.getName();
        lblStudent.setText(msg);
    }

    private void refreshTable() {
        try {
            List<Object[]> rows = RegistrationInfoDAO.getOfStudentInCurrentSemester(currentStudent.getStudentNo());
            setTableData(rows);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách ĐKHP\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    private void removeTableData() {
        tableModel.setRowCount(0);
    }
    /**
     * Set table data to list of rows
     * @param list List data of row to set
     */
    private void setTableData(List<Object[]> list) {
        removeTableData();

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
                    info.getRegisterTime().toLocalDateTime(),
                    info.getStatus().getStatusID()
            };
            tableModel.addRow(rowData);
        }
    }

    private void confirm() {
        long dbID = getSelectedID();

        if (dbID == -1) {
            DialogUtil.showInfoMessage("Vui lòng chọn ít nhất 1 (một) học phần để duyệt");
            return;
        }

        String msg = "Xác nhận duyệt các ĐKHP này (Không thể hoàn tác)";
        int option = JOptionPane.showConfirmDialog(
                this.mainPanel, msg, "Xác nhận duyệt", JOptionPane.OK_CANCEL_OPTION
        );

        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            RegistrationInfoController.confirmCourseByStaff(currentStudent.getStudentNo(), dbID);
            refreshTable();
        } catch (Exception ex) {
            DialogUtil.showErrorMessage(
                    "Lỗi duyệt học phần\n" + ex.getMessage()
            );
        }

    }

    private void cancel() {
        long dbID = getSelectedID();

        if (dbID == -1) {
            DialogUtil.showInfoMessage("Vui lòng chọn ít nhất 1 (một) học phần để hủy");
            return;
        }

        String msg = "Xác nhận hủy các ĐKHP này";
        int option = JOptionPane.showConfirmDialog(
                this.mainPanel, msg, "Xác nhận hủy", JOptionPane.OK_CANCEL_OPTION
        );

        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            RegistrationInfoController.cancelCourseByStaff(currentStudent.getStudentNo(), dbID);
            refreshTable();
        } catch (Exception ex) {
            DialogUtil.showErrorMessage(
                    "Lỗi hủy học phần\n" + ex.getMessage()
            );
        }

    }


    /**
     * Get DatabaseID from first selected table row
     * @return DatabaseID (-1 if selected none)
     */
    private long getSelectedID() {
        long id = -1;
        int index = tableMain.getSelectedRow();

        if (index > -1) {
            id = (Long) tableMain.getValueAt(index, COLUMN_ID);
        }

        return id;
    }

    /**
     * Get DatabaseID from first selected table row
     * @return DatabaseID (-1 if selected none)
     */
    private long getSelectedStatusID() {
        int id = -1;
        int index = tableMain.getSelectedRow();

        if (index > -1) {
            id = (Integer) tableMain.getValueAt(index, COLUMN_HIDDEN_STATUS_ID);
        }

        return id;
    }
}
