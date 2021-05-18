package com.lnh.CourseRegistration.UIs.Screens.ClassInfo;

import com.lnh.CourseRegistration.DAOs.ClassInfoDAO;
import com.lnh.CourseRegistration.Entities.ClassInfo;
import com.lnh.CourseRegistration.Utils.CustomComparator;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

//TODO: Add info about Num of Male, Female & Total Student
public class ClassInfoScreen extends JFrame implements ActionListener {
    private JScrollPane paneTable;
    private JTable mainTable;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnNew;
    private JButton btnSearch;
    private JPanel mainPanel;


    private JPopupMenu popupMenu;
    private JMenuItem refreshMenuItem, newMenuItem, editMenuItem, deleteMenuItem, searchMenuItem;

    private static JFrame AppFrame;
    private DefaultTableModel tableModel;

    private static final String ObjectName = "Lớp học"; //Name of the current managed object type to display
    //Table related attributes
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_NAME = 1;
    Object[] columnLabels = {"ID", "Lớp học"};

    private static ClassInfoScreen instance;

    public static ClassInfoScreen getInstance() {
        if (instance == null) {
            instance = new ClassInfoScreen();
        }
        return instance;
    }

    private ClassInfoScreen() {
        initTable();
        initBtnListeners();
        refreshClassInfoTable();
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
            addNewClassInfo();
        } else if (source == btnEdit || source == editMenuItem) {
            editSelectedClassInfo();
        } else if (source == btnDelete || source == deleteMenuItem) {
            deleteSelectedClassInfo();
        } else if (source == btnSearch || source == searchMenuItem) {
            search();
        } else if (source == refreshMenuItem) {
            refreshClassInfoTable();
        }
    }

    //Handler method---------------------------------------------
    private void refreshClassInfoTable() {
        try {
            List<ClassInfo> ClassInfoList = ClassInfoDAO.getAll();
            setTableData(ClassInfoList);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    /**
     * Set table data to list of
     * @param ClassInfoList List data of ClassInfo to set
     */
    private void setTableData(List<ClassInfo> ClassInfoList) {
        tableModel.setRowCount(0);

        for (ClassInfo classInfo: ClassInfoList) {
            Object[] rowData = {
                    classInfo.getId(),
                    classInfo.getClassName()
            };
            tableModel.addRow(rowData);
        }
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

        String value = searchValue.trim();
        if (value != null && value.length() > 0) {
            try {
                List<ClassInfo> classInfoList = ClassInfoDAO.searchByName(value);
                setTableData(classInfoList);
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