package com.lnh.CourseRegistration.UIs.Screens.ClassInfo;

import com.lnh.CourseRegistration.DAOs.ClassInfoDAO;
import com.lnh.CourseRegistration.Entities.ClassInfo;
import com.lnh.CourseRegistration.UIs.Screens.Student.StudentScreen;
import com.lnh.CourseRegistration.Utils.CustomComparator;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ClassInfoScreen extends JFrame implements ActionListener {
    private JScrollPane paneTable;
    private JTable mainTable;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnNew;
    private JButton btnSearch;
    private JPanel mainPanel;
    private JPanel panelTab;
    private static final int CARD_STUDENT = 0;
    private StudentScreen studentScreen = null;

    private JPopupMenu popupMenu;
    private JMenuItem refreshMenuItem, newMenuItem, editMenuItem, deleteMenuItem, searchMenuItem, selectMenuItem;

    private DefaultTableModel tableModel;

    private static final String ObjectName = "Lớp học"; //Name of the current managed object type to display
    //Table related attributes
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_STUDENT = 2;
    private static final int COLUMN_MALE = 3;
    private static final int COLUMN_FEMALE = 4;
    Object[] columnLabels = {"ID", "Lớp học", "Sỉ số", "Sỉ số Nam", "Sỉ số Nữ"};

    public ClassInfoScreen() {
        initStudentScreen();
        initTable();
        initBtnListeners();
    }

    public JPanel getMainPanel() { return this.mainPanel; }
    public void refreshData() {
        refreshClassInfoTable();
    }
    public void removeData() {
        removeTableData();
        studentScreen.removeTableData();
    }

    private void initStudentScreen() {
        studentScreen = new StudentScreen();
        panelTab.add(studentScreen.getMainPanel(), CARD_STUDENT);
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

        sorter.setComparator(COLUMN_ID, new CustomComparator.ComparatorInt());
        sorter.setComparator(COLUMN_NAME, new CustomComparator.ComparatorString());
        sorter.setComparator(COLUMN_STUDENT, new CustomComparator.ComparatorLong());
        sorter.setComparator(COLUMN_MALE, new CustomComparator.ComparatorLong());
        sorter.setComparator(COLUMN_FEMALE, new CustomComparator.ComparatorLong());

        //Sort by ID default
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(COLUMN_NAME, SortOrder.ASCENDING));
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
        mainTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {//Detect double click events
                    viewClassStudents();
                }
            }
        });
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
            addNewClassInfo();
        } else if (source == btnEdit || source == editMenuItem) {
            editSelectedClassInfo();
        } else if (source == btnDelete || source == deleteMenuItem) {
            deleteSelectedClassInfo();
        } else if (source == btnSearch || source == searchMenuItem) {
            search();
        } else if (source == refreshMenuItem) {
            refreshClassInfoTable();
        } else if (source == selectMenuItem) {
            viewClassStudents();
        }
    }

    //Handler method---------------------------------------------
    private void viewClassStudents() {
        int classID = getSelectedClassInfoID();

        if (classID == -1) {
            DialogUtil.showWarningMessage("Vui lòng chọn một " + ObjectName + " để xem danh sách");
            return;
        }

        studentScreen.setClassID(classID);
    }

    private void refreshClassInfoTable() {
        try {
            List<Object[]> allWithInfo = ClassInfoDAO.getAllWithInfo();
            setTableData(allWithInfo);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    /**
     * Set table data to list of
     * @param rows List data of rows to set
     */
    private void setTableData(List<Object[]> rows) {
        removeTableData();

        for (Object[] row: rows) {
            ClassInfo classInfo = (ClassInfo) row[0];
            Number total = (Number) row[1];
            Number totalMale = (Number) row[2];
            Number totalFemale = (Number) row[3];
            Object[] rowData = {
                    classInfo.getId(),
                    classInfo.getClassName(),
                    total,
                    totalMale,
                    totalFemale
            };
            tableModel.addRow(rowData);
        }
    }

    private void removeTableData() {
        tableModel.setRowCount(0);
    }


    private void addNewClassInfo() {
        openClassInfoEditDialog(null);
    }

    private void editSelectedClassInfo() {
        try {
            ClassInfo selected = fromSelectedTableRow();

            if (selected == null) {
                DialogUtil.showWarningMessage("Vui lòng chọn một "+ ObjectName + " để sửa");
                return;
            }

            openClassInfoEditDialog(selected);
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không lấy được thông tin"+ ObjectName +". Lỗi:\n" + ex.getMessage());
        }
    }

    private void deleteSelectedClassInfo() {
        int ClassInfoID = getSelectedClassInfoID();

        if (ClassInfoID == -1) {
            DialogUtil.showWarningMessage("Vui lòng chọn một " + ObjectName + " để xóa");
            return;
        }

        String msg = "Xóa " + ObjectName + " có ID: " + ClassInfoID + "?";
        int option = JOptionPane.showConfirmDialog(this, msg);

        if (option == JOptionPane.YES_OPTION) {
            try {
                ClassInfoDAO.delete(ClassInfoID);
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi xóa thông tin "+ ObjectName +".\n" + ex.getMessage());
            }
        }

        refreshClassInfoTable();
    }

    /**
     * Open ClassInfo edit windows
     * @param aClassInfo ClassInfo to edit (<b>null</b> to add new ClassInfo)
     */
    private void openClassInfoEditDialog(ClassInfo aClassInfo) {
        //Dialog so that it block ClassInfo screen
        new FormEditClassInfo(this, aClassInfo);
        refreshClassInfoTable();
    }

    /**
     * Search for object with conditions
     */
    private void search() {
        String msg = "Nhập tên " + ObjectName + " cần tìm:";

        String searchValue = JOptionPane.showInputDialog(this, msg);

        String value = (searchValue == null) ? null: searchValue.trim();
        if (value != null && value.length() > 0) {
            try {
                List<Object[]> resultList = ClassInfoDAO.searchByNameWithInfo(value);
                setTableData(resultList);
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi tìm kiếm.\n" + ex.getMessage());
            }
        }

    }



    //Get Object Info from table------------------------------------------------------
    /**
     * Get ClassInfo info from first selected table row
     * @return Selected ClassInfo (null if selected none)
     */
    private ClassInfo fromSelectedTableRow() throws Exception {
        ClassInfo classInfo = null;
        int id = getSelectedClassInfoID();

        if (id > -1) {
            classInfo = ClassInfoDAO.getByClassInfoID(id);
        }

        return classInfo;
    }

    /**
     * Get ClassInfoID from first selected table row
     * @return ClassInfoID (-1 if selected none)
     */
    private int getSelectedClassInfoID() {
        int id = -1;
        int index = mainTable.getSelectedRow();

        if (index > -1) {
            id = (Integer) mainTable.getValueAt(index, COLUMN_ID);
        }

        return id;
    }



    //Methods use by children-----------------------------------------------
    /**
     * Save (Insert/Update) classInfo to Database
     * @param classInfo classInfo to be save to Database
     * @throws Exception If insert/update fails
     */
    public void saveClassInfo(ClassInfo classInfo) throws Exception {
        if (ClassInfoDAO.getByClassInfoID(classInfo.getId()) == null) {
            ClassInfoDAO.insert(classInfo);
        }
        else {
            ClassInfoDAO.update(classInfo);
        }

        refreshClassInfoTable();
    }
}
