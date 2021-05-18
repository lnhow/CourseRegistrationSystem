package com.lnh.CourseRegistration.UIs.Screens.Semester;

import com.lnh.CourseRegistration.Controllers.SemesterController;
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
    private JLabel txtCurrentSemester;

    private JPopupMenu popupMenu;
    private JMenuItem refreshMenuItem, newMenuItem, editMenuItem, deleteMenuItem, setCurrentSemesterMenuItem;

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
        refreshTxtCurrentSemester();    //Display current semester text
        initTable();
        initBtnListeners();
        refreshData();
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
        setCurrentSemesterMenuItem = new JMenuItem("Đặt làm Học Kì hiện tại");

        refreshMenuItem.addActionListener(this);
        newMenuItem.addActionListener(this);
        editMenuItem.addActionListener(this);
        deleteMenuItem.addActionListener(this);
        setCurrentSemesterMenuItem.addActionListener(this);

        popupMenu.add(newMenuItem);
        popupMenu.add(editMenuItem);
        popupMenu.add(deleteMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(setCurrentSemesterMenuItem);
        popupMenu.add(refreshMenuItem);

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
            refreshData();
        } else if (source == setCurrentSemesterMenuItem) {
            setCurrentSemesterAction();
        }
    }

    //Handler method---------------------------------------------
    /**
     * Refresh table with data from Database
     */
    private void refreshData() {
        try {
            List<Semester> SemesterList = SemesterDAO.getAll();
            setTableData(SemesterList);

            Semester currentSemester = SemesterController.getCurrentSemester();
            if (currentSemester != null) {
                Semester updatedCurrentSemester = SemesterDAO.getBySemesterID(currentSemester.getId());
                this.setCurrentSemester(updatedCurrentSemester);
            }
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

                //If delete semester successfully & the deleted semester is current semester
                //Then set current semester to null
                Semester currentSemester = SemesterController.getCurrentSemester();
                if (currentSemester != null && currentSemester.getId() == semesterID) {
                    this.setCurrentSemester(null);
                }
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi xóa thông tin "+ ObjectName +".\n" + ex.getMessage());
            }
        }

        refreshData();
    }

    /**
     * Open Semester edit windows
     * @param aSemester Semester to edit (<b>null</b> to add new Semester)
     */
    private void openSemesterEditDialog(Semester aSemester) {
        //Dialog so that it block Semester screen
        new FormEditSemester(this, aSemester);
        refreshData();
    }

    /**
     * Action performer for set Current Semester action listeners
     */
    private void setCurrentSemesterAction() {
        try {
            Semester selected = fromSelectedTableRow();

            if (selected == null) {
                DialogUtil.showWarningMessage("Vui lòng chọn một "+ ObjectName + " để sửa");
                return;
            }

            this.setCurrentSemester(selected);
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không lấy được thông tin"+ ObjectName +". Lỗi:\n" + ex.getMessage());
        }
    }

    /**
     * Wrapper function to set Current Semester to data & update text
     * @param semester semester to set to current semester
     */
    private void setCurrentSemester(Semester semester) {
        SemesterController.setCurrentSemester(semester);
        this.refreshTxtCurrentSemester();
    }

    private void refreshTxtCurrentSemester() {
        Semester currentSemester = SemesterController.getCurrentSemester();
        String msg = "Học kì hiện tại: "
                + ((currentSemester == null) ?
                    "Chưa chọn":currentSemester.getSemesterName() + " Năm " + currentSemester.getSemesterYear()
                );
        txtCurrentSemester.setText(msg);
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
     * Save (Insert/Update) semester to Database
     * @param semester semester to be save to Database
     * @throws Exception If insert/update fails
     */
    public void saveSemester(Semester semester) throws Exception {
        if (SemesterDAO.getBySemesterID(semester.getId()) == null) {
            SemesterDAO.insert(semester);
        }
        else {
            SemesterDAO.update(semester);

            //If update semester successfully & the updated semester is current semester
            //Then set current semester to it
            Semester currentSemester = SemesterController.getCurrentSemester();
            if (currentSemester != null && currentSemester.getId() == semester.getId()) {
                this.setCurrentSemester(semester);
            }
        }

        refreshData();
    }
}
