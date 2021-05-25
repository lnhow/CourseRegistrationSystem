package com.lnh.CourseRegistration.UIs.Screens.Subject;

import com.lnh.CourseRegistration.DAOs.SubjectDAO;
import com.lnh.CourseRegistration.Entities.Subject;
import com.lnh.CourseRegistration.Utils.CustomComparator;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectScreen extends JFrame implements ActionListener {
    private JScrollPane paneTable;
    private JTable mainTable;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnNew;
    private JButton btnSearch;
    private JPanel mainPanel;

    private JPopupMenu popupMenu;
    private JMenuItem refreshMenuItem, newMenuItem, editMenuItem, deleteMenuItem, searchMenuItem;

    private DefaultTableModel tableModel;

    private static final String ObjectName = "Môn học"; //Name of the current managed object type to display
    //Table related attributes
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_SHORTNAME = 1;
    private static final int COLUMN_NAME = 2;
    private static final int COLUMN_CREDIT = 3;
    Object[] columnLabels = {"ID", "Mã Môn học", "Tên Môn học", "Số Tín chỉ"};
    private static final int[] DISABLE_SORT_COLUMN_INDEXES = {COLUMN_NAME};

    public SubjectScreen() {
        initTable();
        initBtnListeners();

    }

    public JPanel getMainPanel() { return this.mainPanel; }
    public void refreshData() {
        refreshSubjectTable();
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

        sorter.setComparator(COLUMN_ID, new CustomComparator.ComparatorInt());
        sorter.setComparator(COLUMN_SHORTNAME, new CustomComparator.ComparatorString());
        sorter.setComparator(COLUMN_CREDIT, new CustomComparator.ComparatorInt());

        //Sort by ID default
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(COLUMN_SHORTNAME, SortOrder.ASCENDING));
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
            addNewSubject();
        } else if (source == btnEdit || source == editMenuItem) {
            editSelectedSubject();
        } else if (source == btnDelete || source == deleteMenuItem) {
            deleteSelectedSubject();
        } else if (source == btnSearch || source == searchMenuItem) {
            search();
        } else if (source == refreshMenuItem) {
            refreshSubjectTable();
        }
    }

    //Handler method---------------------------------------------
    private void refreshSubjectTable() {
        try {
            List<Subject> SubjectList = SubjectDAO.getAll();
            setTableData(SubjectList);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    /**
     * Set table data to list of
     * @param SubjectList List data of Subject to set
     */
    private void setTableData(List<Subject> SubjectList) {
        removeTableData();

        for (Subject Subject: SubjectList) {
            Object[] rowData = {
                    Subject.getId(),
                    Subject.getShortName(),
                    Subject.getSubjectName(),
                    Subject.getNumCredit()
            };
            tableModel.addRow(rowData);
        }
    }

    private void removeTableData() {
        tableModel.setRowCount(0);
    }


    private void addNewSubject() {
        openSubjectEditDialog(null);
    }

    private void editSelectedSubject() {
        try {
            Subject selected = fromSelectedTableRow();

            if (selected == null) {
                DialogUtil.showWarningMessage("Vui lòng chọn một "+ ObjectName + " để sửa");
                return;
            }

            openSubjectEditDialog(selected);
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không lấy được thông tin"+ ObjectName +". Lỗi:\n" + ex.getMessage());
        }
    }

    private void deleteSelectedSubject() {
        int SubjectID = getSelectedSubjectID();

        if (SubjectID == -1) {
            DialogUtil.showWarningMessage("Vui lòng chọn một " + ObjectName + " để xóa");
            return;
        }

        String msg = "Xóa " + ObjectName + " có ID: " + SubjectID + "?";
        int option = JOptionPane.showConfirmDialog(this, msg);

        if (option == JOptionPane.YES_OPTION) {
            try {
                SubjectDAO.delete(SubjectID);
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi xóa thông tin "+ ObjectName +".\n" + ex.getMessage());
            }
        }

        refreshSubjectTable();
    }

    /**
     * Open Subject edit windows
     * @param aSubject Subject to edit (<b>null</b> to add new Subject)
     */
    private void openSubjectEditDialog(Subject aSubject) {
        //Dialog so that it block Subject screen
        new FormEditSubject(this, aSubject);
        refreshSubjectTable();
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
            String value = searchValue == null ? null:searchValue.trim();
            if (value != null && value.length() > 0) {
                try {
                    List<Subject> subjectList;
                    if (comboBox.getSelectedIndex() == 0) {
                        subjectList = SubjectDAO.searchByName(value);
                    }
                    else {
                        subjectList = SubjectDAO.searchByShortName(value);
                    }

                    setTableData(subjectList);
                } catch (Exception ex) {
                    DialogUtil.showErrorMessage("Lỗi tìm kiếm.\n" + ex.getMessage());
                }
            }
        }
    }



    //Get Object Info from table------------------------------------------------------
    /**
     * Get Subject info from first selected table row
     * @return Selected Subject (null if selected none)
     */
    private Subject fromSelectedTableRow() throws Exception {
        Subject Subject = null;
        int id = getSelectedSubjectID();

        if (id > -1) {
            Subject = SubjectDAO.getBySubjectID(id);
        }

        return Subject;
    }

    /**
     * Get SubjectID from first selected table row
     * @return SubjectID (-1 if selected none)
     */
    private int getSelectedSubjectID() {
        int id = -1;
        int index = mainTable.getSelectedRow();

        if (index > -1) {
            id = (Integer) mainTable.getValueAt(index, COLUMN_ID);
        }

        return id;
    }



    //Methods use by children-----------------------------------------------
    /**
     * Save (Insert/Update) Subject to Database
     * @param Subject Subject to be save to Database
     * @throws Exception If insert/update fails
     */
    public void saveSubject(Subject Subject) throws Exception {
        if (SubjectDAO.getBySubjectID(Subject.getId()) == null) {
            SubjectDAO.insert(Subject);
        }
        else {
            SubjectDAO.update(Subject);
        }

        refreshSubjectTable();
    }
}
