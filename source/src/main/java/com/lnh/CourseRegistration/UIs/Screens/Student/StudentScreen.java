package com.lnh.CourseRegistration.UIs.Screens.Student;

import com.lnh.CourseRegistration.DAOs.ClassInfoDAO;
import com.lnh.CourseRegistration.DAOs.StudentDAO;
import com.lnh.CourseRegistration.Entities.ClassInfo;
import com.lnh.CourseRegistration.Entities.Student;
import com.lnh.CourseRegistration.Utils.CustomComparator;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class StudentScreen extends JFrame implements ActionListener{
    private JPanel mainPanel;
    private JScrollPane paneTable;
    private JTable mainTable;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnNew;
    private JButton btnSearch;
    private JLabel lblClass;

    private JPopupMenu popupMenu;
    private JMenuItem refreshMenuItem, newMenuItem, editMenuItem, deleteMenuItem, searchMenuItem, listCourseMenuItem;

    private int currentClassID = -1;
    private ClassInfo currentClassInfo;
    private DefaultTableModel tableModel;

    private static final String ObjectName = "Sinh viên"; //Name of the current managed object type to display
    //Table related attributes
    private static final int COLUMN_NO = 0;
    private static final int COLUMN_ID = 1;
    private static final int COLUMN_NAME = 2;
    private static final int COLUMN_GENDER = 3;
    private static final int COLUMN_CLASS = 4;
    private static final int COLUMN_USERNAME = 5;
    Object[] columnLabels = {"ID", "Mã SV", "Họ tên", "Giới tính", "Lớp", "Tên đăng nhập"};
    private static final int[] DISABLE_SORT_COLUMN_INDEXES = {COLUMN_NO, COLUMN_NAME, COLUMN_GENDER, COLUMN_USERNAME};

    public StudentScreen() {
        initTable();
        initBtnListeners();
    }

    //Init methods-----------------------------------------------------------------
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

        sorter.setComparator(COLUMN_ID, new CustomComparator.ComparatorString());
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
        listCourseMenuItem = new JMenuItem("DS Lớp đăng ký");

        refreshMenuItem.addActionListener(this);
        searchMenuItem.addActionListener(this);
        newMenuItem.addActionListener(this);
        editMenuItem.addActionListener(this);
        deleteMenuItem.addActionListener(this);
        listCourseMenuItem.addActionListener(this);

        popupMenu.add(searchMenuItem);
        popupMenu.add(refreshMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(newMenuItem);
        popupMenu.add(editMenuItem);
        popupMenu.add(deleteMenuItem);
        popupMenu.add(listCourseMenuItem);
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
    //Init methods-----------------------------------------------------------------


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnNew || source == newMenuItem) {
            addNew();
        } else if (source == btnEdit || source == editMenuItem) {
            editSelected();
        } else if (source == btnDelete || source == deleteMenuItem) {
            deleteSelected();
        } else if (source == btnSearch || source == searchMenuItem) {
            search();
        } else if (source == refreshMenuItem) {
            refresh();
        } else if (source == listCourseMenuItem) {
            viewCourseDialog();
        }
    }


    //Handler method---------------------------------------------
    public JPanel getMainPanel() { return this.mainPanel; }

    public void setClassID(int classID) {
        currentClassID = classID;
        refresh();
    }

    //Refreshers-----------------------------------------------------------------------------
    private void refresh() {
        refreshClassInfo();
        refreshTable();
    }

    private void refreshClassInfo() {
        ClassInfo classInfo = null;
        try {
            classInfo = ClassInfoDAO.getByClassInfoID(currentClassID);
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không thể lấy chi tiết lớp học\n" + ex.getMessage());
            return;
        }
        currentClassInfo = classInfo;
        currentClassID = classInfo.getId();
        lblClass.setText("Danh sách lớp " + classInfo.getClassName());
    }

    private void refreshTable() {
        try {
            List<Student> rows = StudentDAO.getByClassID(currentClassID);
            setTableData(rows);
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
        tableModel.setRowCount(0);
    }

    /**
     * Set table data to list of rows
     * @param list List data of row to set
     */
    private void setTableData(List<Student> list) {
        removeTableData();

        for (Student row: list) {
            Object[] rowData = {
                    row.getStudentNo(),
                    row.getId(),
                    row.getName(),
                    row.isMale() ? "Nam":"Nữ",
                    row.getClassInfo().getClassName(),
                    row.getAccount().getUsername()
            };
            tableModel.addRow(rowData);
        }
    }


    private void addNew() {
        openEditDialog(null);
    }

    private void editSelected() {
        try {
            Student selected = fromSelectedTableRow();

            if (selected == null) {
                DialogUtil.showWarningMessage("Vui lòng chọn một "+ ObjectName + " để sửa");
                return;
            }

            openEditDialog(selected);
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không lấy được thông tin"+ ObjectName +". Lỗi:\n" + ex.getMessage());
        }
    }

    private void deleteSelected() {
        long ID = getSelectedID();

        if (ID == -1) {
            DialogUtil.showWarningMessage("Vui lòng chọn một " + ObjectName + " để xóa");
            return;
        }

        String msg = "Xóa " + ObjectName + " có ID: " + ID + "?";
        int option = JOptionPane.showConfirmDialog(this, msg);

        if (option == JOptionPane.YES_OPTION) {
            try {
                StudentDAO.delete(ID);
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi xóa thông tin "+ ObjectName +".\n" + ex.getMessage());
            }
        }

        refreshTable();
    }

    /**
     * Open student course windows
     */
    private void viewCourseDialog() {
        long DBID = getSelectedID();

        if (DBID == -1) {
            DialogUtil.showWarningMessage("Vui lòng chọn một " + ObjectName + " để xem");
            return;
        }

        //Dialog so that it block staff screen
        new StudentCourseRegistered(this, DBID);
    }

    /**
     * Open student edit windows
     * @param student student to edit (<b>null</b> to add new staff)
     */
    private void openEditDialog(Student student) {
        //Dialog so that it block screen
        new FormEditStudent(this, student, currentClassInfo);
    }

    /**
     * Search for object with conditions
     */
    private void search() {
        String msg = "Nhập tên " + ObjectName + " cần tìm:";
        String value = JOptionPane.showInputDialog(
                this,
                msg,
                "Tìm kiếm",
                JOptionPane.INFORMATION_MESSAGE
        );

        if (value != null && value.length() > 0) {
            try {
                List<Student> list = StudentDAO.searchByName(value, currentClassID);
                setTableData(list);
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi tìm kiếm.\n" + ex.getMessage());
            }
        }
    }



    //Get Object Info from table------------------------------------------------------
    /**
     * Get Staff info from first selected table row
     * @return Selected Staff (null if selected none)
     */
    private Student fromSelectedTableRow() throws Exception {
        Student student = null;
        long id = getSelectedID();

        if (id > -1) {
            student = StudentDAO.getByID(id);
        }

        return student;
    }

    /**
     * Get DatabaseID from first selected table row
     * @return DatabaseID (-1 if selected none)
     */
    private long getSelectedID() {
        long id = -1;
        int index = mainTable.getSelectedRow();

        if (index > -1) {
            id = (Long) mainTable.getValueAt(index, COLUMN_NO);
        }

        return id;
    }



    //Methods use by children-----------------------------------------------
    /**
     * Save (Insert/Update) student to Database
     * @param student Student to be save to Database
     * @throws Exception If insert/update fails
     */
    public void saveStudent(Student student) throws Exception {
        if (StudentDAO.getByAccountID(student.getAccount().getId()) == null) {
            StudentDAO.insert(student);
        }
        else {
            StudentDAO.update(student);
        }

        refreshTable();
    }
}
