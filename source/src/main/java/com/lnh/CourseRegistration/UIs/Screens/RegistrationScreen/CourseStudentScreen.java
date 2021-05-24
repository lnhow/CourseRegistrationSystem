package com.lnh.CourseRegistration.UIs.Screens.RegistrationScreen;

import com.lnh.CourseRegistration.Controllers.RegistrationInfoController;
import com.lnh.CourseRegistration.DAOs.CourseDAO;
import com.lnh.CourseRegistration.DAOs.RegistrationInfoDAO;
import com.lnh.CourseRegistration.DAOs.StudentDAO;
import com.lnh.CourseRegistration.Entities.Course;
import com.lnh.CourseRegistration.Entities.RegistrationInfo;
import com.lnh.CourseRegistration.Entities.Student;
import com.lnh.CourseRegistration.Entities.SupportEntities.RegisterStatus;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseStudentScreen implements ActionListener {
    private JPanel mainPanel;
    private JScrollPane paneConfirmed;
    private JTable tableConfirmed;
    private JButton btnSearch;
    private JLabel lblClass;
    private JScrollPane paneWaiting;
    private JButton btnReEvaluate;
    private JButton btnConfirm;
    private JButton btnCancel;
    private JTable tableWaiting;
    private JScrollPane paneCancelled;
    private JTable tableCancelled;
    private JLabel lblTeacher;
    private JLabel lblTime;

    private JMenuItem refreshConfirmed, refreshWaiting, refreshCancelled,
            searchMenuItem, confirmMenuItem, cancelMenuItem, reEvaluateMenuItem;

    public static final long NOT_VALID_COURSE_ID = -1;
    private long currentCourseID = NOT_VALID_COURSE_ID;
    private DefaultTableModel tableConfirmedModel;
    private DefaultTableModel tableWaitingModel;
    private DefaultTableModel tableCancelledModel;

    private static final String ObjectName = "ĐKHP Sinh viên"; //Name of the current managed object type to display

    //Table related attributes
    private static final int COLUMN_DBID = 0;
    private static final int COLUMN_ID = 1;
    private static final int COLUMN_NAME = 2;
    private static final int COLUMN_TIME = 3;
    Object[] columnLabels = {"ID", "Mã SV", "Họ tên", "TG Đăng ký"};
    private static final int[] DISABLE_SORT_COLUMN_INDEXES = { COLUMN_NAME };

    public CourseStudentScreen() {
        initTable();
        initBtnListeners();
    }

    //Init methods-----------------------------------------------------------------
    private void initTable() {
        tableConfirmedModel = initATable(tableConfirmed);
        tableWaitingModel = initATable(tableWaiting);
        tableCancelledModel = initATable(tableCancelled);

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

        sorter.setComparator(COLUMN_DBID, new CustomComparator.ComparatorLong());
        sorter.setComparator(COLUMN_ID, new CustomComparator.ComparatorString());
        sorter.setComparator(COLUMN_TIME, new CustomComparator.ComparatorLocalDateTime());

        //Sort by ID default
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(COLUMN_ID, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        table.setRowSorter(sorter);

        return tableModel;
    }



    //Popup menu-----------------------------------------------------------------
    private void initTablePopupMenu() {
        initTableConfirmedPopupMenu();
        initTableWaitingPopupMenu();
        initTableCancelledPopupMenu();
    }

    private void initTableConfirmedPopupMenu() {
        JPopupMenu menuConfirmed = new JPopupMenu();
        refreshConfirmed = new JMenuItem("Tải lại");
        searchMenuItem = new JMenuItem("Tìm kiếm");
        refreshConfirmed.addActionListener(this);
        searchMenuItem.addActionListener(this);

        menuConfirmed.add(refreshConfirmed);
        menuConfirmed.add(searchMenuItem);

        MouseAdapter adapterConfirmed = new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if(me.isPopupTrigger())
                    menuConfirmed.show(me.getComponent(), me.getX(), me.getY());
            }
        };

        tableConfirmed.addMouseListener(adapterConfirmed);
        //Make the empty area on table pane (if present) can show popup
        paneConfirmed.addMouseListener(adapterConfirmed);
    }

    private void initTableWaitingPopupMenu() {
        JPopupMenu menuWaiting = new JPopupMenu();
        refreshWaiting = new JMenuItem("Tải lại");
        confirmMenuItem = new JMenuItem("Duyệt");
        cancelMenuItem = new JMenuItem("Hủy");
        refreshWaiting.addActionListener(this);
        confirmMenuItem.addActionListener(this);
        cancelMenuItem.addActionListener(this);

        menuWaiting.add(refreshWaiting);
        menuWaiting.add(confirmMenuItem);
        menuWaiting.add(cancelMenuItem);

        MouseAdapter adapterWaiting = new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if(me.isPopupTrigger())
                    menuWaiting.show(me.getComponent(), me.getX(), me.getY());
            }
        };

        tableWaiting.addMouseListener(adapterWaiting);
        //Make the empty area on table pane (if present) can show popup
        paneWaiting.addMouseListener(adapterWaiting);
    }

    private void initTableCancelledPopupMenu() {
        JPopupMenu menuCancelled = new JPopupMenu();
        refreshCancelled = new JMenuItem("Tải lại");
        reEvaluateMenuItem = new JMenuItem("Hoàn tác hủy");
        refreshCancelled.addActionListener(this);
        reEvaluateMenuItem.addActionListener(this);

        menuCancelled.add(refreshCancelled);
        menuCancelled.add(reEvaluateMenuItem);

        MouseAdapter adapterCancelled = new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if(me.isPopupTrigger())
                    menuCancelled.show(me.getComponent(), me.getX(), me.getY());
            }
        };

        tableCancelled.addMouseListener(adapterCancelled);
        //Make the empty area on table pane (if present) can show popup
        paneCancelled.addMouseListener(adapterCancelled);
    }
    //Popup menu-----------------------------------------------------------------


    private void initBtnListeners() {
        btnSearch.addActionListener(this);
        btnConfirm.addActionListener(this);
        btnCancel.addActionListener(this);
        btnReEvaluate.addActionListener(this);
    }
    //Init methods-----------------------------------------------------------------


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (currentCourseID == NOT_VALID_COURSE_ID) {
            DialogUtil.showWarningMessage("Vui lòng chọn 1 (một) học phần trước khi thực hiện hành động này");
        }

        if (source == btnSearch || source == searchMenuItem) {
            search();
        } else if (source == btnConfirm || source == confirmMenuItem) {
            confirm();
        } else if (source == btnCancel || source == cancelMenuItem) {
            cancel();
        } else if (source == btnReEvaluate || source == reEvaluateMenuItem) {
            reEvaluate();
        } else if (source == refreshConfirmed) {
            refreshTableConfirmed();
        } else if (source == refreshCancelled) {
            refreshTableCancelled();
        } else if (source == refreshWaiting) {
            refreshTableWaiting();
        }
    }


    //Handler method---------------------------------------------
    public JPanel getMainPanel() { return this.mainPanel; }

    public void setCourseID(long courseID) {
        currentCourseID = courseID;
        refreshData();
    }

    //Refreshers-----------------------------------------------------------------------------
    private void refreshData() {
        refreshCourse();
        refreshTableConfirmed();
        refreshTableWaiting();
        refreshTableCancelled();
    }

    private void refreshCourse() {
        Course course;
        try {
            course = CourseDAO.getByCourseID(currentCourseID);
            if (course == null) {
                HelperUtils.throwException("Không tìm được học phần (Thông tin rỗng)");
            }
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không thể lấy chi tiết học phần\n" + ex.getMessage());
            currentCourseID = NOT_VALID_COURSE_ID;
            return;
        }
        Course currentCourse = course;
        currentCourseID = course.getId();
        String classMsg = "Danh sách lớp " + currentCourse.getSubject().getShortName()
                + " - " + currentCourse.getSubject().getSubjectName()
                + " - " + currentCourse.getClassInfo().getClassName()
                + " - " + currentCourse.getSemester().getSemesterYear()
                + " | " + currentCourse.getSemester().getSemesterName();
        lblClass.setText(classMsg);
        lblTeacher.setText("Giảng viên lý thuyết: " + currentCourse.getTeacherName());
        lblTime.setText("Thời gian học: " + currentCourse.getWeekday() + " Ca " + currentCourse.getShift().toString());
    }

    private void refreshTableConfirmed() {
        try {
            List<Object[]> rows = RegistrationInfoDAO.getInCourse(currentCourseID, RegisterStatus.STATUS_CONFIRMED);
            setTableConfirmedData(rows);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    private void refreshTableWaiting() {
        try {
            List<Object[]> rows = RegistrationInfoDAO.getInCourse(currentCourseID, RegisterStatus.STATUS_WAITING);
            setTableWaitingData(rows);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    private void refreshTableCancelled() {
        try {
            List<Object[]> rows = RegistrationInfoDAO.getInCourse(currentCourseID, RegisterStatus.STATUS_CANCELLED);
            setTableCancelledData(rows);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }


    //Refreshers-----------------------------------------------------------------------------
    /**
     * Remove current data in table
     * */
    public void removeTableData() {
        removeTableConfirmedData();
        removeTableWaitingData();
        removeTableCancelledData();
    }

    private void removeTableConfirmedData() {
        tableConfirmedModel.setRowCount(0);
    }
    /**
     * Set table data to list of rows
     * @param list List data of row to set
     */
    private void setTableConfirmedData(List<Object[]> list) {
        removeTableConfirmedData();

        for (Object[] row: list) {
            RegistrationInfo info = (RegistrationInfo) row[0];
            Student student = (Student) row[1];
            Object[] rowData = {
                    student.getStudentNo(),
                    student.getId(),
                    student.getName(),
                    info.getRegisterTime().toLocalDateTime()
            };
            tableConfirmedModel.addRow(rowData);
        }
    }

    private void removeTableWaitingData() {
        tableWaitingModel.setRowCount(0);
    }
    /**
     * Set table data to list of rows
     * @param list List data of row to set
     */
    private void setTableWaitingData(List<Object[]> list) {
        removeTableWaitingData();

        for (Object[] row: list) {
            RegistrationInfo info = (RegistrationInfo) row[0];
            Student student = (Student) row[1];
            Object[] rowData = {
                    student.getStudentNo(),
                    student.getId(),
                    student.getName(),
                    info.getRegisterTime().toLocalDateTime()
            };
            tableWaitingModel.addRow(rowData);
        }
    }

    private void removeTableCancelledData() {
        tableCancelledModel.setRowCount(0);
    }
    /**
     * Set table data to list of rows
     * @param list List data of row to set
     */
    private void setTableCancelledData(List<Object[]> list) {
        removeTableCancelledData();

        for (Object[] row: list) {
            RegistrationInfo info = (RegistrationInfo) row[0];
            Student student = (Student) row[1];
            Object[] rowData = {
                    student.getStudentNo(),
                    student.getId(),
                    student.getName(),
                    info.getRegisterTime().toLocalDateTime()
            };
            tableCancelledModel.addRow(rowData);
        }
    }

    //Handling methods--------------------------------------------------------------------
    /**
     * Search for object with conditions
     */
    private void search() {
        String msg = "Nhập tên " + ObjectName + " cần tìm:";
        String value = JOptionPane.showInputDialog(
                this.getMainPanel(),
                msg,
                "Tìm kiếm",
                JOptionPane.INFORMATION_MESSAGE
        );

        if (value != null && value.length() > 0) {
            try {
                List<Object[]> rows = RegistrationInfoDAO.searchInCourse(
                        currentCourseID, value, RegisterStatus.STATUS_CONFIRMED
                );
                setTableConfirmedData(rows);
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi tìm kiếm.\n" + ex.getMessage());
            }
        }
    }

    private void confirm() {
        List<Long> dbIDs = getSelectedIDsFromWaiting();

        if (dbIDs.size() > 0) {
            String msg = "Xác nhận duyệt các ĐKHP này (Không thể hoàn tác)";
            int option = JOptionPane.showConfirmDialog(
                    this.mainPanel, msg, "Xác nhận duyệt", JOptionPane.OK_CANCEL_OPTION
            );

            if (option != JOptionPane.YES_OPTION) {
                return;
            }

            for (Long dbID: dbIDs) {
                try {
                    RegistrationInfoController.confirmCourseByStaff(dbID, currentCourseID);
                    refreshTableWaiting();
                    refreshTableConfirmed();
                } catch (Exception ex) {
                    try {
                        Student student = StudentDAO.getByID(dbID);
                        DialogUtil.showErrorMessage(
                                "Lỗi duyệt " + ObjectName + " " + student.getId() + "\n" + ex.getMessage()
                        );
                    } catch (Exception exception) {
                        DialogUtil.showErrorMessage(
                                "Lỗi duyệt "+ ObjectName + " có dbID " + dbID + "\n" + ex.getMessage());
                    }
                }
            }
        } else {
            DialogUtil.showInfoMessage("Vui lòng chọn ít nhất 1 (một) " + ObjectName + " để duyệt");
        }
    }

    private void cancel() {
        List<Long> dbIDs = getSelectedIDsFromWaiting();
        String msg = "Xác nhận hủy các ĐKHP này";
        int option = JOptionPane.showConfirmDialog(
                this.mainPanel, msg, "Xác nhận hủy", JOptionPane.OK_CANCEL_OPTION
        );

        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        if (dbIDs.size() > 0) {
            for (Long dbID: dbIDs) {
                try {
                    RegistrationInfoController.cancelCourseByStaff(dbID, currentCourseID);
                    refreshTableWaiting();
                    refreshTableCancelled();
                } catch (Exception ex) {
                    try {
                        Student student = StudentDAO.getByID(dbID);
                        DialogUtil.showErrorMessage(
                                "Lỗi hủy " + ObjectName + " " + student.getId() + "\n" + ex.getMessage()
                        );
                    } catch (Exception exception) {
                        DialogUtil.showErrorMessage(
                                "Lỗi hủy "+ ObjectName + " có dbID " + dbID + "\n" + ex.getMessage());
                    }
                }
            }
        } else {
            DialogUtil.showInfoMessage("Vui lòng chọn ít nhất 1 (một) " + ObjectName + " để duyệt");
        }
    }

    private void reEvaluate() {
        List<Long> dbIDs = getSelectedIDsFromCancelled();

        if (dbIDs.size() > 0) {
            for (Long dbID: dbIDs) {
                try {
                    RegistrationInfoController.reevaluateCourseByStaff(dbID, currentCourseID);
                    refreshTableCancelled();
                    refreshTableWaiting();
                } catch (Exception ex) {
                    try {
                        Student student = StudentDAO.getByID(dbID);
                        DialogUtil.showErrorMessage(
                                "Lỗi hủy " + ObjectName + " " + student.getId() + "\n" + ex.getMessage()
                        );
                    } catch (Exception exception) {
                        DialogUtil.showErrorMessage(
                                "Lỗi hủy "+ ObjectName + " có dbID " + dbID + "\n" + ex.getMessage());
                    }
                }
            }
        } else {
            DialogUtil.showInfoMessage("Vui lòng chọn ít nhất 1 (một) " + ObjectName + " để duyệt");
        }
    }

    //Table selects..........................................................
    /**
     * Get IDs from selected table waiting rows
     * @return List of student DatabaseID
     */
    private List<Long> getSelectedIDsFromWaiting() {
        List<Long> selected = new ArrayList<>();
        int[] indexes = tableWaiting.getSelectedRows();

        for (int index: indexes) {
            selected.add((Long) tableWaiting.getValueAt(index, COLUMN_DBID));
        }

        return selected;
    }

    /**
     * Get IDs from selected table cancelled rows
     * @return List of student DatabaseID
     */
    private List<Long> getSelectedIDsFromCancelled() {
        List<Long> selected = new ArrayList<>();
        int[] indexes = tableCancelled.getSelectedRows();

        for (int index: indexes) {
            selected.add((Long) tableCancelled.getValueAt(index, COLUMN_DBID));
        }

        return selected;
    }
}
