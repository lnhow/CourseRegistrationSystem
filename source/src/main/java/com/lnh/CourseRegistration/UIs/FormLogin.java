package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.DAOs.AccountDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormLogin {
    private JFrame AppFrame;
    private JPanel mainPanel;
    private JTextField txtUsername;
    private JButton btnLogin;
    private JPasswordField txtPassword;
    private JCheckBox ckcShowPassword;
    private JLabel txtMessage;

    public FormLogin() {
        initComponents();
        setVisible();
    }

    private void setVisible() {
        AppFrame = new JFrame("Đăng nhập");
        AppFrame.setContentPane(this.mainPanel);
        AppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AppFrame.setLocationRelativeTo(null);

        AppFrame.getRootPane().setDefaultButton(btnLogin);

        Container container = AppFrame.getContentPane();
        container.setPreferredSize(new Dimension(400, 250));
        AppFrame.pack();
        AppFrame.setVisible(true);
    }

    private void initComponents() {
        txtUsername.setText("");
        txtPassword.setText("");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                txtMessage.setText("Đang tải...");
                AppFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (username.equals("")) {
                    txtMessage.setText("Tên đăng nhập không được trống");
                    AppFrame.setCursor(Cursor.getDefaultCursor());
                    return;
                } else if(password.equals("")) {
                    txtMessage.setText("Mật khẩu không được trống");
                    AppFrame.setCursor(Cursor.getDefaultCursor());
                    return;
                }

                Account loginAccount = login(username,password);
                txtMessage.setText("");
                if (loginAccount == null) {
                    txtMessage.setText("Sai Tài khoản hay Mật khẩu");
                    AppFrame.setCursor(Cursor.getDefaultCursor());
                    return;
                }
                processLogin(loginAccount);
                AppFrame.setCursor(Cursor.getDefaultCursor());
            }
        });
        ckcShowPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ckcShowPassword.isSelected()) {
                    txtPassword.setEchoChar((char)0);
                } else {
                    txtPassword.setEchoChar('•');
                }
            }
        });
    }


    private Account login(String username, String password) {
        Account loginAccount = null;
        try {
            loginAccount = AccountDAO.getByLoginInfo(username, password);
        } catch (Exception exception) {
            DialogUtil.showErrorMessage(exception.getMessage());
        }

        return loginAccount;
    }

    private void processLogin(Account account) {
        if (account == null) {
            return;
        }

        switch (account.getType()) {
            case Account.ACCOUNT_STUDENT:
                new MenuStudent(account);
                AppFrame.dispose();
                break;
            case Account.ACCOUNT_STAFF:
                new MenuStaff(account);
                AppFrame.dispose();
                break;
            default:
                DialogUtil.showErrorMessage(
                        "Tài khoản không tồn tại(ID: "+account.getId()+"). Vui lòng liên hệ admin."
                );
                break;
        }
    }
}
