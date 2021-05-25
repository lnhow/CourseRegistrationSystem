package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.Controllers.LoginController;
import com.lnh.CourseRegistration.DAOs.RegistrationSessionDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.RegistrationSession;
import com.lnh.CourseRegistration.UIs.Screens.RegistrationScreen.StudentRegisterResult;
import com.lnh.CourseRegistration.UIs.Screens.RegistrationScreen.StudentRegisterScreen;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuStudent implements ActionListener {
    private JPanel mainPanel;
    private JButton btnLogOut;
    private JButton btnInfo;
    private JLabel txtName;
    private JButton btnResult;
    private JButton btnRegister;
    private JLabel lblRegister;
    private JButton btnHolder;

    private CardLayout cardLayout;
    private JPanel paneCard;
    private JPanel cardRegisterHolder;
    private JLabel lblTime;
    private StudentRegisterResult cardResult;
    private StudentRegisterScreen cardRegister;

    private static final String CARD_REGISTER_RESULT = "cardRegisterResult";
    private static final String CARD_REGISTER_HOLDER = "cardRegisterHolder";
    private static final String CARD_REGISTER_SCREEN = "cardRegisterScreen";

    private JFrame AppFrame;

    public MenuStudent() {
        initComponents();
        setVisible();

        boolean isStudentNotExist =
                LoginController.getLogInAccountType() != Account.ACCOUNT_STUDENT
                || LoginController.getLogInStudent() == null;
        if (isStudentNotExist) {
            DialogUtil.showErrorMessage("Lỗi đăng nhập");
            return;
        }

        setAccountText();
    }

    private void setVisible() {
        AppFrame = new JFrame("Sinh viên");
        AppFrame.setContentPane(this.mainPanel);
        AppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AppFrame.pack();
        AppFrame.setVisible(true);
        AppFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        btnInfo.addActionListener(this);
        btnLogOut.addActionListener(this);
        btnResult.addActionListener(this);
        btnRegister.addActionListener(this);
        btnHolder.addActionListener(this);

        cardResult = new StudentRegisterResult();
        paneCard.add(cardResult.getMainPanel(), CARD_REGISTER_RESULT);
        cardRegister = new StudentRegisterScreen();
        paneCard.add(cardRegister.getMainPanel(), CARD_REGISTER_SCREEN);

        cardLayout = (CardLayout) paneCard.getLayout();
        showResultScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnInfo) {
            openAccountForm();
        } else if (source == btnLogOut) {
            logOut();
        } else if (source == btnResult) {
            showResultScreen();
        } else if (source == btnRegister) {
            showHolderScreen();
        } else if (source == btnHolder) {
            showRegisterScreen();
        }
    }

    //Handling methods-------------------------------------------------------------------------------------------------
    private void setAccountText() {
        txtName.setText("Xin chào, " + LoginController.getLogInStudent().getName());
    }

    private void logOut() {
        LoginController.logOut();
        new FormLogin();
        AppFrame.dispose();
    }

    private void openAccountForm() {
        JDialog dialog = new JDialog(this.AppFrame);
        new FormAccount(dialog);
        setAccountText();
    }

    private void showResultScreen() {
        removeAllExceptScreen(CARD_REGISTER_RESULT);
        cardResult.refreshData();
        cardLayout.show(paneCard, CARD_REGISTER_RESULT);
    }

    private void showHolderScreen() {
        removeAllExceptScreen(CARD_REGISTER_HOLDER);
        btnHolder.setEnabled(false);
        fetchCurrentRegisterSession();
        cardLayout.show(paneCard, CARD_REGISTER_HOLDER);
    }

    private void showRegisterScreen() {
        removeAllExceptScreen(CARD_REGISTER_SCREEN);
        cardRegister.refreshData();
        cardLayout.show(paneCard, CARD_REGISTER_SCREEN);
    }

    private void fetchCurrentRegisterSession() {
        try {
            RegistrationSession session =
                    RegistrationSessionDAO.getOpenSession(new Timestamp(System.currentTimeMillis()));
            if (session != null) {
                String msg = "Kỳ ĐKHP "
                        + session.getSemester().getSemesterName() + " | " + session.getSemester().getSemesterYear();
                lblRegister.setText(msg);
                LocalDateTime sessionStart = session.getSessionStart().toLocalDateTime();
                LocalDateTime sessionEnd = session.getSessionStart().toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                lblTime.setText(
                        "Bắt đầu: " + sessionStart.format(formatter) + " --- Kết thúc: " + sessionEnd.format(formatter)
                );
                btnHolder.setEnabled(true);
            }
        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Không lấy được thông tin kỳ ĐKHP\n"+ex.getMessage());
        }
    }

    private void removeAllExceptScreen(String screenName) {
        System.out.println("Remove except " + screenName);
        if (!screenName.equals(CARD_REGISTER_RESULT)) {
            cardResult.removeData();
        }
        if (!screenName.equals(CARD_REGISTER_SCREEN)) {
            cardRegister.removeData();
        }
    }
}
