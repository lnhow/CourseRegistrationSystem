package com.lnh.CourseRegistration.UIs.Screens.RegistrationSession;

import com.lnh.CourseRegistration.DAOs.RegistrationSessionDAO;
import com.lnh.CourseRegistration.DAOs.SemesterDAO;
import com.lnh.CourseRegistration.Entities.RegistrationSession;
import com.lnh.CourseRegistration.Entities.Semester;
import com.lnh.CourseRegistration.Utils.CustomComparator;
import com.lnh.CourseRegistration.Utils.DialogUtil;
import com.lnh.CourseRegistration.Utils.HelperUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SessionScreen extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JScrollPane paneTable;
    private JTable mainTable;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnNew;
    private JLabel txtCurrentSemester;

    private JPopupMenu popupMenu;
    private JMenuItem refreshMenuItem, newMenuItem, editMenuItem, deleteMenuItem;

    private static JFrame AppFrame;
    private DefaultTableModel tableModel;

    private static final String ObjectName = "Kỳ Đăng ký Học phần"; //Name of the current managed object type to display
    //Table related attributes
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_SEMESTER = 1;
    private static final int COLUMN_YEAR = 2;
    private static final int COLUMN_DATE_START = 3;
    private static final int COLUMN_DATE_END = 4;
    Object[] columnLabels = {"ID", "Học kì", "Năm", "Ngày bắt đầu", "Ngày kết thúc"};
    private static final int[] DISABLE_SORT_COLUMN_INDEXES = {COLUMN_SEMESTER, COLUMN_DATE_START, COLUMN_DATE_END};

    private static SessionScreen instance;

    //Allow only one screen at a time
    public static SessionScreen getInstance() {
        if (instance == null) {
            instance = new SessionScreen();
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

    public SessionScreen() {
        refreshTxtCurrentSemester();    //Display current semester text
        initTable();
        initBtnListeners();
    }
    public JPanel getMainPanel() { return this.mainPanel; }
    public void removeData() {
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

        popupMenu.add(newMenuItem);
        popupMenu.add(editMenuItem);
        popupMenu.add(deleteMenuItem);
        popupMenu.addSeparator();
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
            addNewRegistrationSession();
        } else if (source == btnEdit || source == editMenuItem) {
            editSelectedRegistrationSession();
        } else if (source == btnDelete || source == deleteMenuItem) {
            deleteSelectedRegistrationSession();
        } else if (source == refreshMenuItem) {
            refreshData();
        }
    }

    //Handler method---------------------------------------------
    /**
     * Refresh table with data from Database
     */
    public void refreshData() {
        refreshTxtCurrentSemester();
        try {
            List<RegistrationSession> RegistrationSessionList = RegistrationSessionDAO.getAll();
            setTableData(RegistrationSessionList);

        } catch (Exception ex) {
            String msg = "Lỗi không lấy được danh sách " + ObjectName +"\n";
            DialogUtil.showErrorMessage(msg + ex.getMessage());
        }
    }

    /**
     * Set table data to list of
     * @param registrationSessions List data of RegistrationSession to set
     */
    private void setTableData(List<RegistrationSession> registrationSessions) {
        removeTableData();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        for (RegistrationSession session: registrationSessions) {
            Object[] rowData = {
                    session.getId(),
                    session.getSemester().getSemesterName(),
                    session.getSemester().getSemesterYear(),
                    formatter.format(session.getSessionStart()),
                    formatter.format(session.getSessionEnd())
            };
            tableModel.addRow(rowData);
        }
    }

    private void removeTableData() {
        tableModel.setRowCount(0);
    }


    private void addNewRegistrationSession() {
        openRegistrationSessionEditDialog(null);
    }

    private void editSelectedRegistrationSession() {
        try {
            RegistrationSession selected = fromSelectedTableRow();

            if (selected == null) {
                DialogUtil.showWarningMessage("Vui lòng chọn một "+ ObjectName + " để sửa");
                return;
            }

            openRegistrationSessionEditDialog(selected);
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không lấy được thông tin"+ ObjectName +". Lỗi:\n" + ex.getMessage());
        }
    }

    private void deleteSelectedRegistrationSession() {
        long semesterID = getSelectedRegistrationSessionID();

        if (semesterID == -1) {
            DialogUtil.showWarningMessage("Vui lòng chọn một " + ObjectName + " để xóa");
            return;
        }

        String msg = "Xóa " + ObjectName + " có ID: " + semesterID + "?";
        int option = JOptionPane.showConfirmDialog(this, msg);

        if (option == JOptionPane.YES_OPTION) {
            try {
                RegistrationSessionDAO.delete(semesterID);
            } catch (Exception ex) {
                DialogUtil.showErrorMessage("Lỗi xóa thông tin "+ ObjectName +".\n" + ex.getMessage());
            }
        }

        refreshData();
    }

    /**
     * Open RegistrationSession edit windows
     * @param aSession RegistrationSession to edit (<b>null</b> to add new RegistrationSession)
     */
    private void openRegistrationSessionEditDialog(RegistrationSession aSession) {
        if (aSession == null) {
            Semester currentSemester = fetchCurrentSemester();
            if (currentSemester == null)  {
                DialogUtil.showWarningMessage(
                        "Vui lòng đặt Học Kỳ hiện tại trước khi tạo một "
                                + ObjectName
                                + "\nMenu chính > Học kỳ > Chọn một Học kỳ"
                );
                return;
            }
        }

        //Dialog so that it block RegistrationSession screen
        new FormEditSession(this, aSession);
        refreshData();

    }

    private Semester fetchCurrentSemester() {
        Semester current = null;
        try {
            current = SemesterDAO.getCurrentSemester();
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Lỗi không lấy được thông tin học kì hiện tại\n" + ex.getMessage());
        }
        return current;
    }


    private void refreshTxtCurrentSemester() {
        Semester currentSemester = fetchCurrentSemester();
        String msg = "Học kì hiện tại: "
                + ((currentSemester == null) ?
                "Chưa chọn":currentSemester.getSemesterName() + " Năm " + currentSemester.getSemesterYear()
        );
        txtCurrentSemester.setText(msg);
    }



    //Get Object Info from table------------------------------------------------------
    /**
     * Get RegistrationSession info from first selected table row
     * @return Selected RegistrationSession (null if selected none)
     */
    private RegistrationSession fromSelectedTableRow() throws Exception {
        RegistrationSession session = null;
        long id = getSelectedRegistrationSessionID();

        if (id > -1) {
            session = RegistrationSessionDAO.getBySessionID(id);
        }

        return session;
    }

    /**
     * Get RegistrationSessionID from first selected table row
     * @return RegistrationSessionID (-1 if selected none)
     */
    private long getSelectedRegistrationSessionID() {
        long id = -1;
        int index = mainTable.getSelectedRow();

        if (index > -1) {
            id = (Long) mainTable.getValueAt(index, COLUMN_ID);
        }

        return id;
    }




    //Methods use by children-----------------------------------------------
    /**
     * Save (Insert/Update) session to Database
     * @param session session to be save to Database
     * @throws Exception If insert/update fails
     */
    public void saveSession(RegistrationSession session) throws Exception {
        if (session == null) {
            HelperUtils.throwException("Session is null");
            return;
        }

        if (RegistrationSessionDAO.getBySessionID(session.getId()) == null) {
            RegistrationSessionDAO.insert(session);
        }
        else {
            RegistrationSessionDAO.update(session);
        }

        refreshData();
    }
}
