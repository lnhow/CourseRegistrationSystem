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

    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (
//                ClassNotFoundException | InstantiationException
//                        | IllegalAccessException | UnsupportedLookAndFeelException e
//        ) {
//            e.printStackTrace();
//        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormLogin();
            }
        });
    }


    FormLogin() {
        initComponents();
        setVisible();
    }

    private void setVisible() {
        AppFrame = new JFrame("Login");
        AppFrame.setContentPane(this.mainPanel);
        AppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

                txtMessage.setText("Loading...");
                AppFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (username.equals("")) {
                    txtMessage.setText("Please input username");
                    AppFrame.setCursor(Cursor.getDefaultCursor());
                    return;
                } else if(password.equals("")) {
                    txtMessage.setText("Please input password");
                    AppFrame.setCursor(Cursor.getDefaultCursor());
                    return;
                }

                Account loginAccount = login(username,password);
                txtMessage.setText("");
                if (loginAccount == null) {
                    txtMessage.setText("Wrong username or password");
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
                String msg = "Login successfully"
                        +"\nID: " + account.getId()
                        +"\nUsername: " + account.getUsername()
                        +"\nPassword: " + account.getPassword()
                        +"\nAccountType: " + account.getType();
                DialogUtil.showWarningMessage(msg);
                AppFrame.dispose();
                break;
            case Account.ACCOUNT_STAFF:
                new FormStaff(account);
                AppFrame.dispose();
                break;
            default:
                DialogUtil.showErrorMessage(
                        "Account type invalid("+account.getId()+"). Please contact administrator."
                );
                break;
        }
    }
}
