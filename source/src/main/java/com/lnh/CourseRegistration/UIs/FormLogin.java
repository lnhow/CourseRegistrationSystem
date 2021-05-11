package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.DAOs.AccountDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormLogin {
    public static JFrame AppFrame;
    private JPanel mainPanel;
    private JTextField txtUsername;
    private JButton btnLogin;
    private JPasswordField txtPassword;
    private JCheckBox ckcShowPassword;
    private JLabel txtMessage;

    FormLogin() {
        txtUsername.setText("");
        txtPassword.setText("");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                if (username.equals("")) {
                    txtMessage.setText("Please input username");
                    return;
                } else if(password.equals("")) {
                    txtMessage.setText("Please input password");
                    return;
                }

                Account loginAccount = login(username,password);
                if (loginAccount == null) {
                    txtMessage.setText("Wrong username or password");
                    return;
                }
                processLogin(loginAccount);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (
                        ClassNotFoundException | InstantiationException
                        | IllegalAccessException | UnsupportedLookAndFeelException e
                    ) {
                    e.printStackTrace();
                }
                AppFrame = new JFrame("Login");
                AppFrame.setContentPane(new FormLogin().mainPanel);
                AppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                DialogUtil.setFrame(AppFrame);

                Container container = AppFrame.getContentPane();
                container.setPreferredSize(new Dimension(400, 250));
                AppFrame.pack();
                AppFrame.setVisible(true);
            }
        });
    }

    private static Account login(String username, String password) {
        Account loginAccount = null;
        try {
            loginAccount = AccountDAO.getByLoginInfo(username, password);
        } catch (Exception exception) {
            DialogUtil.showErrorMessage(exception.getMessage());
        }

        return loginAccount;
    }

    private static void processLogin(Account account) {
        if (account == null) {
            return;
        }

        String msg = "Login successfully"
                +"\nID: " + account.getId()
                +"\nUsername: " + account.getUsername()
                +"\nPassword: " + account.getPassword()
                +"\nAccountType: " + account.getType();
        DialogUtil.showWarningMessage(msg);
    }
}
