package com.lnh.CourseRegistration.UIs.Screens.Semester;

import com.lnh.CourseRegistration.DAOs.SemesterDAO;
import com.lnh.CourseRegistration.Entities.Semester;
import com.lnh.CourseRegistration.Utils.CustomComparator;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SemesterScreen extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JScrollPane paneTable;
    private JTable mainTable;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnNew;

    private JPopupMenu popupMenu;
    private JMenuItem refreshMenuItem, newMenuItem, editMenuItem, deleteMenuItem;

    private static JFrame AppFrame;
    private DefaultTableModel tableModel;

    private static final String ObjectName = "Học kì"; //Name of the current managed object type to display
    //Table related attributes
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_YEAR = 2;
    private static final int COLUMN_DATE_START = 3;
    private static final int COLUMN_DATE_END = 4;
    Object[] columnLabels = {"ID", "Học kì", "Năm", "Ngày bắt đầu", "Ngày kết thúc"};
    private static final int[] DISABLE_SORT_COLUMN_INDEXES = {COLUMN_NAME, COLUMN_DATE_START, COLUMN_DATE_END};

    private static SemesterScreen instance;

    public static SemesterScreen getInstance() {
        if (instance == null) {
            instance = new SemesterScreen();
        }
        return instance;
    }

    private SemesterScreen() {
        initTable();
        initBtnListeners();
        refreshTable();
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

        for (int index: DISABLE_SORT_COLUMN_INDEXES) {
            sorter.setSortable(index, false);
        }

        sorter.setComparator(COLUMN_ID, new CustomComparator.ComparatorInt());
        sorter.setComparator(COLUMN_YEAR, new CustomComparator.ComparatorInt());

        //Sort by ID default
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(COLUMN_YEAR, SortOrder.DESCENDING));
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

        refreshMenuItem.addActionListener(this);
        newMenuItem.addActionListener(this);
        editMenuItem.addActionListener(this);
        deleteMenuItem.addActionListener(this);

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
    }





    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnNew || source == newMenuItem) {
            addNewSemester();
        } else if (source == btnEdit || source == editMenuItem) {
            editSelectedSemester();
        } else if (source == btnDelete || source == deleteMenuItem) {
            deleteSelectedSemester();
        } else if (source == refreshMenuItem) {
            refreshTable();
        }
    }

    //Handler method---------------------------------------------
    private void refreshTable() {
        try {
            List<Semester> SemesterList = SemesterDAO.getAll();
            setTableData(SemesterList);
        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    /**
     * Set table data to list of
     * @param SemesterList List data of Semester to set
     */
    private void setTableData(List<Semester> SemesterList) {
        tableModel.setRowCount(0);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        for (Semester Semester: SemesterList) {
            Object[] rowData = {
                    Semester.getId(),
                    Semester.getSemesterName(),
                    Semester.getSemesterYear(),
                    formatter.format(Semester.getSemesterStart()),
                    formatter.format(Semester.getSemesterEnd())
            };
            tableModel.addRow(rowData);
        }
    }


    private void addNewSemester() {
        openSemesterEditDialog(null);
    }

    private void editSelectedSemester() {
        try {
            Semester selected = fromSelectedTableRow();

            if (selected == null) {
                DialogUtil.showWarningMessage("Vui lòng chọn một "+ ObjectName + " để sửa");
                return;
            }

            openSemesterEditDialog(selected);
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không lấy được thông tin"+ ObjectName +". Lỗi:\n" + ex.getMessage());
        }
    }

    private void deleteSelectedSemester() {
        int semesterID = getSelectedSemesterID();

        if (semesterID == -1) {
            DialogUtil.showWarningMessage("Vui lòng chọn một " + ObjectName + " để xóa");
            return;
        }

        String msg = "Xóa " + ObjectName + " có ID: " + semesterID + "?";
        int option = JOptionPane.showConfirmDialog(this, msg);

        if (option == JOptionPane.YES_OPTION) {
            try {
                SemesterDAO.delete(semesterID);
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi xóa thông tin "+ ObjectName +".\n" + ex.getMessage());
            }
        }

        refreshTable();
    }

    /**
     * Open Semester edit windows
     * @param aSemester Semester to edit (<b>null</b> to add new Semester)
     */
    private void openSemesterEditDialog(Semester aSemester) {
        //Dialog so that it block Semester screen
        new FormEditSemester(this, aSemester);
        refreshTable();
    }



    //Get Object Info from table------------------------------------------------------
    /**
     * Get Semester info from first selected table row
     * @return Selected Semester (null if selected none)
     */
    private Semester fromSelectedTableRow() throws Exception {
        Semester semester = null;
        int id = getSelectedSemesterID();

        if (id > -1) {
            semester = SemesterDAO.getBySemesterID(id);
        }

        return semester;
    }

    /**
     * Get SemesterID from first selected table row
     * @return SemesterID (-1 if selected none)
     */
    private int getSelectedSemesterID() {
        int id = -1;
        int index = mainTable.getSelectedRow();

        if (index > -1) {
            id = (Integer) mainTable.getValueAt(index, COLUMN_ID);
        }

        return id;
    }




    //Methods use by children-----------------------------------------------
    /**
     * Save (Insert/Update) Semester to Database
     * @param Semester Semester to be save to Database
     * @throws Exception If insert/update fails
     */
    public void saveSemester(Semester Semester) throws Exception {
        if (SemesterDAO.getBySemesterID(Semester.getId()) == null) {
            SemesterDAO.insert(Semester);
        }
        else {
            SemesterDAO.update(Semester);
        }

        refreshTable();
    }
}
