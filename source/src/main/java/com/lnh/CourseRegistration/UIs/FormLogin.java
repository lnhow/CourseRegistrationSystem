package com.lnh.CourseRegistration.UIs;

import com.lnh.CourseRegistration.Controllers.LoginController;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

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

                //Optimize logging in first time performance
                Thread thread = new Thread(() -> {
                    try {
                        LoginController.logIn(username, password);

                    } catch (Exception ex) {
                        DialogUtil.showWarningMessage(ex.getMessage());
                        try {
                            SwingUtilities.invokeAndWait(() -> {
                                txtMessage.setText("");
                                AppFrame.setCursor(Cursor.getDefaultCursor());
                            });
                        } catch (InterruptedException | InvocationTargetException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        return;
                    }  catch (ExceptionInInitializerError ex) {
                        DialogUtil.showWarningMessage("Lỗi kết nối CSDL\n"+ ex.getMessage());
                        try {
                            SwingUtilities.invokeAndWait(() -> {
                                txtMessage.setText("");
                                AppFrame.setCursor(Cursor.getDefaultCursor());
                            });
                        } catch (InterruptedException | InvocationTargetException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        return;
                    }

                    try {
                        SwingUtilities.invokeAndWait(() -> {
                            txtMessage.setText("");
                            AppFrame.setCursor(Cursor.getDefaultCursor());
                            processLogin();
                        });
                    } catch (InterruptedException | InvocationTargetException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                });
                thread.start();
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

    private void processLogin() {
        int loginAccountType = LoginController.getLogInAccountType();
        txtMessage.setText("");
        if (loginAccountType == Account.ACCOUNT_INVALID) {
            txtMessage.setText("Sai Tài khoản hay Mật khẩu");
            AppFrame.setCursor(Cursor.getDefaultCursor());
            return;
        }

        switch (loginAccountType) {
            case Account.ACCOUNT_STUDENT -> {
                new MenuStudent();
                AppFrame.dispose();
            }
            case Account.ACCOUNT_STAFF -> {
                new MenuStaff();
                AppFrame.dispose();
            }
            default -> DialogUtil.showErrorMessage(
                    "Người dùng không liên kết với tài khoản"
            );
        }
    }
}
