package com.lnh.CourseRegistration.UIs.Screens.Staff;

import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Utils.CustomComparator;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class StaffScreen extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JTable mainTable;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnNew;
    private JButton btnSearch;
    private JScrollPane paneTable;

    private JPopupMenu popupMenu;
    private JMenuItem refreshMenuItem, newMenuItem, editMenuItem, deleteMenuItem, searchMenuItem;

    private DefaultTableModel tableModel;

    private static final String ObjectName = "Giáo vụ"; //Name of the current managed object type to display
    //Table related attributes
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_USERNAME = 2;
    Object[] columnLabels = {"ID", "Họ tên", "Tên đăng nhập"};
    private static final int[] DISABLE_SORT_COLUMN_INDEXES = {COLUMN_NAME, COLUMN_USERNAME};

    public StaffScreen() {
        initTable();
        initBtnListeners();
    }

    public JPanel getMainPanel() { return this.mainPanel; }
    public void refreshData() {
        refreshStaffTable();
    }
    public void removeData() {
        removeTableData();
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

        refreshMenuItem.addActionListener(this);
        searchMenuItem.addActionListener(this);
        newMenuItem.addActionListener(this);
        editMenuItem.addActionListener(this);
        deleteMenuItem.addActionListener(this);

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
            addNewStaff();
        } else if (source == btnEdit || source == editMenuItem) {
            editSelectedStaff();
        } else if (source == btnDelete || source == deleteMenuItem) {
            deleteSelectedStaff();
        } else if (source == btnSearch || source == searchMenuItem) {
            search();
        } else if (source == refreshMenuItem) {
            refreshStaffTable();
        }
    }

    //Handler method---------------------------------------------
    private void refreshStaffTable() {
        try {
            List<Staff> staffList = StaffDAO.getAll();
            setTableData(staffList);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    /**
     * Set table data to list of
     * @param staffList List data of staff to set
     */
    private void setTableData(List<Staff> staffList) {
        removeTableData();

        for (Staff staff: staffList) {
            Object[] rowData = {
                    staff.getId(),
                    staff.getName(),
                    staff.getAccount().getUsername()
            };
            tableModel.addRow(rowData);
        }
    }

    private void removeTableData() {
        tableModel.setRowCount(0);
    }


    private void addNewStaff() {
        openStaffEditDialog(null);
    }

    private void editSelectedStaff() {
        try {
            Staff selected = fromSelectedTableRow();

            if (selected == null) {
                DialogUtil.showWarningMessage("Vui lòng chọn một "+ ObjectName + " để sửa");
                return;
            }

            openStaffEditDialog(selected);
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không lấy được thông tin"+ ObjectName +". Lỗi:\n" + ex.getMessage());
        }
    }

    private void deleteSelectedStaff() {
        long staffID = getSelectedStaffID();

        if (staffID == -1) {
            DialogUtil.showWarningMessage("Vui lòng chọn một " + ObjectName + " để xóa");
            return;
        }

        String msg = "Xóa " + ObjectName + " có ID: " + staffID + "?";
        int option = JOptionPane.showConfirmDialog(this.getMainPanel(), msg);

        if (option == JOptionPane.YES_OPTION) {
            try {
                StaffDAO.delete(staffID);
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi xóa thông tin "+ ObjectName +".\n" + ex.getMessage());
            }
        }

        refreshStaffTable();
    }

    /**
     * Open staff edit windows
     * @param aStaff staff to edit (<b>null</b> to add new staff)
     */
    private void openStaffEditDialog(Staff aStaff) {
        //Dialog so that it block staff screen
        //Have to be this else will break things
        new FormEditStaff(this, aStaff);
        refreshStaffTable();
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
                List<Staff> staffList = StaffDAO.searchByName(value);
                setTableData(staffList);
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
    private Staff fromSelectedTableRow() throws Exception {
        Staff staff = null;
        long id = getSelectedStaffID();

        if (id > -1) {
            staff = StaffDAO.getByStaffID(id);
        }

        return staff;
    }

    /**
     * Get staffID from first selected table row
     * @return staffID (-1 if selected none)
     */
    private long getSelectedStaffID() {
        long id = -1;
        int index = mainTable.getSelectedRow();

        if (index > -1) {
            id = (Long) mainTable.getValueAt(index, COLUMN_ID);
        }

        return id;
    }



    //Methods use by children-----------------------------------------------
    /**
     * Save (Insert/Update) staff to Database
     * @param staff Staff to be save to Database
     * @throws Exception If insert/update fails
     */
    public void saveStaff(Staff staff) throws Exception {
        if (StaffDAO.getByAccountID(staff.getAccount().getId()) == null) {
            StaffDAO.insert(staff);
        }
        else {
            StaffDAO.update(staff);
        }

        refreshStaffTable();
    }
}
