package com.lnh.CourseRegistration.UIs.Screens.Course;

import com.lnh.CourseRegistration.DAOs.CourseDAO;
import com.lnh.CourseRegistration.DAOs.SemesterDAO;
import com.lnh.CourseRegistration.DAOs.SubjectDAO;
import com.lnh.CourseRegistration.Entities.Course;
import com.lnh.CourseRegistration.Entities.Semester;
import com.lnh.CourseRegistration.Entities.Subject;
import com.lnh.CourseRegistration.Entities.SupportEntities.Shift;
import com.lnh.CourseRegistration.UIs.Screens.RegistrationScreen.CourseStudentScreen;
import com.lnh.CourseRegistration.UIs.Screens.Student.StudentScreen;
import com.lnh.CourseRegistration.Utils.CustomComparator;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CourseScreen extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JScrollPane paneTable;
    private JTable mainTable;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnNew;
    private JButton btnSearch;
    private JLabel txtCurrentSemester;
    private JPanel panelTab;
    private static final int CARD_STUDENT = 0;
    private CourseStudentScreen courseStudentScreen = null;

    private JPopupMenu popupMenu;
    private JMenuItem refreshMenuItem, newMenuItem, editMenuItem, deleteMenuItem, searchMenuItem, selectMenuItem;

    private static JFrame AppFrame;
    private DefaultTableModel tableModel;

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
    Object[] columnLabels = {"ID", "Mã môn", "Môn học", "Lớp", "Tối đa", "Phòng", "Giảng viên", "Giờ học"};
    private static final int[] DISABLE_SORT_COLUMN_INDEXES = {
            COLUMN_MAX_SLOT, COLUMN_TEACHER, COLUMN_TIME, COLUMN_ROOM
    };

    private static CourseScreen instance;

    public static CourseScreen getInstance() {
        if (instance == null) {
            instance = new CourseScreen();
        }
        return instance;
    }

    public static void destroyInstance() {
        if (AppFrame != null) {
            AppFrame.dispose();
        }
        if (instance != null) {
            instance.dispose();
            instance = null;
        }
    }

    public CourseScreen() {
        initStudentScreen();
        initTable();
        initBtnListeners();
    }

    public JPanel getMainPanel() { return this.mainPanel; }
    public void removeData() {
        courseStudentScreen.removeTableData();
        removeTableData();
    }

    public void openInNewWindow() {
        //Allow only one screen instance at a time
        if (AppFrame == null) {
            AppFrame = new JFrame("Quản lý " + ObjectName);
            AppFrame.setContentPane(this.mainPanel);
            AppFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            AppFrame.addWindowListener(new WindowListener() {
                @Override public void windowClosed(WindowEvent e) {
                    instance = null;    //Free up space used by Screen instance
                }
                //Required methods
                @Override public void windowOpened(WindowEvent e) {}
                @Override public void windowClosing(WindowEvent e) {}
                @Override public void windowIconified(WindowEvent e) {}
                @Override public void windowDeiconified(WindowEvent e) {}
                @Override public void windowActivated(WindowEvent e) {}
                @Override public void windowDeactivated(WindowEvent e) {}
            });
            AppFrame.pack();
            AppFrame.setLocationRelativeTo(null);
        }
        AppFrame.setVisible(true);
        AppFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initStudentScreen() {
        courseStudentScreen = new CourseStudentScreen();
        panelTab.add(courseStudentScreen.getMainPanel(), CARD_STUDENT);
    }

    private void initTable() {
        tableModel = new DefaultTableModel(columnLabels, 0) {
            @Override
            public boolean isCellEditable(int rowIdx, int colIdx) { return false; }
        };
        mainTable.setModel(tableModel);
        initTableSorter();
        initTablePopupMenu();
    }

    private void initTableSorter() {
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
        sortKeys.add(new RowSorter.SortKey(COLUMN_ID, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        mainTable.setRowSorter(sorter);
    }


    private void initTablePopupMenu() {
        popupMenu = new JPopupMenu();
        refreshMenuItem = new JMenuItem("Tải lại danh sách");
        newMenuItem = new JMenuItem("Thêm mới");
        editMenuItem = new JMenuItem("Chỉnh sửa");
        deleteMenuItem = new JMenuItem("Xóa");
        searchMenuItem = new JMenuItem("Tìm kiếm");
        selectMenuItem = new JMenuItem("Xem danh sách lớp");

        selectMenuItem.addActionListener(this);
        refreshMenuItem.addActionListener(this);
        searchMenuItem.addActionListener(this);
        newMenuItem.addActionListener(this);
        editMenuItem.addActionListener(this);
        deleteMenuItem.addActionListener(this);

        popupMenu.add(selectMenuItem);
        popupMenu.add(searchMenuItem);
        popupMenu.add(refreshMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(newMenuItem);
        popupMenu.add(editMenuItem);
        popupMenu.add(deleteMenuItem);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mouseReleased(MouseEvent me) {
                if(me.isPopupTrigger())
                    popupMenu.show(me.getComponent(), me.getX(), me.getY());
            }
        };
        mainTable.addMouseListener(mouseAdapter);
        //Make the empty area on table pane (if present) can show popup
        paneTable.addMouseListener(mouseAdapter);
    }

    private void initBtnListeners() {
        btnNew.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        btnSearch.addActionListener(this);
    }





    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnNew || source == newMenuItem) {
            addNewCourse();
        } else if (source == btnEdit || source == editMenuItem) {
            editSelectedCourse();
        } else if (source == btnDelete || source == deleteMenuItem) {
            deleteSelectedCourse();
        } else if (source == btnSearch || source == searchMenuItem) {
            search();
        } else if (source == refreshMenuItem) {
            refreshData();
        } else if (source == selectMenuItem) {
            viewClassStudents();
        }
    }

    //Handler method---------------------------------------------
    private void viewClassStudents() {
        long courseID = getSelectedCourseID();

        if (courseID == -1) {
            DialogUtil.showWarningMessage("Vui lòng chọn một " + ObjectName + " để xem danh sách");
            return;
        }

        courseStudentScreen.setCourseID(courseID);
    }

    public void refreshData() {
        refreshCourseTable();
        refreshCurrentSemester();
    }

    private void refreshCourseTable() {
        try {
            List<Course> list = CourseDAO.getAllInCurrentSemester();
            setTableData(list);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    private void refreshCurrentSemester() {
        try {
            Semester semester = SemesterDAO.getCurrentSemester();
            if (semester == null) {
                DialogUtil.showWarningMessage("Chưa có học kì nào được đặt làm học kỳ hiện tại");
                return;
            }
            txtCurrentSemester.setText(semester.getSemesterYear() + " - " + semester.getSemesterName());
        } catch (Exception ex) {
            String msg = "Lỗi lấy thông tin Học Kỳ hiện tại\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }


    /**
     * Set table data to list of
     * @param rows List data of rows to set
     */
    private void setTableData(List<Course> rows) {
        removeTableData();

        for (Course row: rows) {
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
            tableModel.addRow(rowData);
        }
    }

    private void removeTableData() {
        tableModel.setRowCount(0);
    }


    private void addNewCourse() {
        openCourseEditDialog(null);
    }

    private void editSelectedCourse() {
        try {
            Course selected = fromSelectedTableRow();

            if (selected == null) {
                DialogUtil.showWarningMessage("Vui lòng chọn một "+ ObjectName + " để sửa");
                return;
            }

            openCourseEditDialog(selected);
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không lấy được thông tin "+ ObjectName +". Lỗi:\n" + ex.getMessage());
        }
    }

    private void deleteSelectedCourse() {
        long CourseID = getSelectedCourseID();

        if (CourseID == -1) {
            DialogUtil.showWarningMessage("Vui lòng chọn một " + ObjectName + " để xóa");
            return;
        }

        String msg = "Xóa " + ObjectName + " có ID: " + CourseID + "?";
        int option = JOptionPane.showConfirmDialog(this, msg);

        if (option == JOptionPane.YES_OPTION) {
            try {
                CourseDAO.delete(CourseID);
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi xóa thông tin "+ ObjectName +".\n" + ex.getMessage());
            }
        }

        refreshCourseTable();
    }

    /**
     * Open Course edit windows
     * @param aCourse Course to edit (<b>null</b> to add new Course)
     */
    private void openCourseEditDialog(Course aCourse) {
        //Dialog so that it block Course screen
        new FormEditCourse(this, aCourse);
        refreshCourseTable();
    }

    /**
     * Search for object with conditions
     */
    private void search() {
        String msg = "Nhập tên " + ObjectName + " cần tìm:";

        //Init Search dialog--------------------------------------
        JPanel panel = new JPanel(new GridLayout(3,1));
        JLabel msgLabel = new JLabel(msg);
        JTextField txtSearchValue = new JTextField();
        JComboBox<String> comboBox = new JComboBox<>(
                new String[] {"Tìm theo Tên Môn học", "Tìm theo Mã Môn học"}
        );
        panel.add(msgLabel);
        panel.add(comboBox);
        panel.add(txtSearchValue);
        //Init Search dialog--------------------------------------
        int option = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Tìm kiếm",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            String searchValue = txtSearchValue.getText();
            String value = (searchValue == null) ? null: searchValue.trim();
            if (value != null && value.length() > 0) {
                try {
                    List<Course> list;
                    if (comboBox.getSelectedIndex() == 0) {
                        list = CourseDAO.searchInCurrentSemesterByName(value);
                    }
                    else {
                        list = CourseDAO.searchInCurrentSemesterByShortName(value);
                    }

                    setTableData(list);
                } catch (Exception ex) {
                    DialogUtil.showErrorMessage("Lỗi tìm kiếm.\n" + ex.getMessage());
                }
            }
        }

    }



    //Get Object Info from table------------------------------------------------------
    /**
     * Get Course info from first selected table row
     * @return Selected Course (null if selected none)
     */
    private Course fromSelectedTableRow() throws Exception {
        Course classInfo = null;
        long id = getSelectedCourseID();

        if (id > -1) {
            classInfo = CourseDAO.getByCourseID(id);
        }

        return classInfo;
    }

    /**
     * Get CourseID from first selected table row
     * @return CourseID (-1 if selected none)
     */
    private long getSelectedCourseID() {
        long id = -1;
        int index = mainTable.getSelectedRow();

        if (index > -1) {
            id = (Long) mainTable.getValueAt(index, COLUMN_ID);
        }

        return id;
    }



    //Methods use by children-----------------------------------------------
    /**
     * Save (Insert/Update) classInfo to Database
     * @param classInfo classInfo to be save to Database
     * @throws Exception If insert/update fails
     */
    public void saveCourse(Course classInfo) throws Exception {
        if (CourseDAO.getByCourseID(classInfo.getId()) == null) {
            CourseDAO.insert(classInfo);
        }
        else {
            CourseDAO.update(classInfo);
        }

        refreshCourseTable();
    }
    
}
