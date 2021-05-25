package com.lnh.CourseRegistration.Controllers;

import com.lnh.CourseRegistration.DAOs.AccountDAO;
import com.lnh.CourseRegistration.DAOs.StaffDAO;
import com.lnh.CourseRegistration.DAOs.StudentDAO;
import com.lnh.CourseRegistration.Entities.Account;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Entities.Student;
import com.lnh.CourseRegistration.UIs.Screens.ClassInfo.ClassInfoScreen;
import com.lnh.CourseRegistration.UIs.Screens.RegistrationSession.SessionScreen;
import com.lnh.CourseRegistration.UIs.Screens.Semester.SemesterScreen;
import com.lnh.CourseRegistration.UIs.Screens.Staff.StaffScreen;
import com.lnh.CourseRegistration.UIs.Screens.Subject.SubjectScreen;
import com.lnh.CourseRegistration.Utils.HelperUtils;

public class LoginController {
    private static int logInAccountType = Account.ACCOUNT_INVALID;
    private static Staff currentLogInStaff = null;
    private static Student currentLogInStudent = null;

    /**
     * Log in to account with username & password
     * @param username Account username
     * @param password Account password
     * @throws Exception if Error in getting account type
     * */
    public static void logIn(String username, String password) throws Exception {
        Account currentAccount = AccountDAO.getByLoginInfo(username, password);

        if (currentAccount == null) {
            return;
        }

        logInAccountType = currentAccount.getType();

        switch (logInAccountType) {
            case Account.ACCOUNT_STUDENT:
                logInStudent(currentAccount);
                break;
            case Account.ACCOUNT_STAFF:
                logInStaff(currentAccount);
                break;
            default:
                HelperUtils.throwException(
                        "Người dùng không tồn tại(ID Tài khoản: " + currentAccount.getId() + ")"
                );
                logOut();
        }
    }

    /**
     * Log in to a student account
     * @param account Account linked to a student
     * @throws Exception when cannot find an student with account
     * */
    private static void logInStudent(Account account) throws Exception {
        currentLogInStudent = StudentDAO.getByAccountID(account.getId());

        if (currentLogInStudent == null) {
            HelperUtils.throwException(
                    "Người dùng không tồn tại(ID Tài khoản: " +account.getId()+")"
            );
            logOut();
        }
    }

    /**
     * Log in to a staff account
     * @param account Account linked to a staff
     * @throws Exception when cannot find an staff with account
     * */
    private static void logInStaff(Account account) throws Exception {
        currentLogInStaff = StaffDAO.getByAccountID(account.getId());

        if (currentLogInStaff == null) {
            HelperUtils.throwException(
                    "Người dùng không tồn tại(ID Tài khoản: " +account.getId()+")"
            );
            logOut();
        }
    }

    /**
     * Get current logged in staff
     * @return Current Logged in Staff, null if there are no staff logged in
     * */
    public static Staff getLogInStaff() {
        return currentLogInStaff;
    }

    /**
     * Get current logged in student
     * @return Current Logged in Student, null if there are no student logged in
     * */
    public static Student getLogInStudent() {
        return currentLogInStudent;
    }

    /**
     * Get current logged in account type
     * @return Current Logged in Account Type, -1 if no logged in
     * */
    public static int getLogInAccountType() {
        return logInAccountType;
    }

    /**
     * Update current logged account
     * @param staff Staff object with updated staff info
     * */
    public static void updateAccountInfo(Staff staff) throws Exception {
        if (currentLogInStaff == null) {
            HelperUtils.throwException("Không có giáo vụ được đăng nhập");
            return;
        }

        if (currentLogInStaff.getId() != staff.getId()) {
            HelperUtils.throwException("Sai ID Giáo vụ");
            return;
        }

        StaffDAO.update(staff);
        refreshLoggedInAccount();
    }

    /**
     * Update current logged account
     * @param student Student object with updated student info
     * */
    public static void updateAccountInfo(Student student) throws Exception{
        if (currentLogInStudent == null) {
            HelperUtils.throwException("Không có sinh viên được đăng nhập");
            return;
        }

        if (currentLogInStudent.getStudentNo() != student.getStudentNo()) {
            HelperUtils.throwException("Sai ID Sinh viên");
            return;
        }

        StudentDAO.update(student);
        refreshLoggedInAccount();
    }

    /**
     * Update current logged account
     * @param updateAccount Account object with updated student info
     * */
    public static void updateAccount(Account updateAccount) throws Exception{
        if (currentLogInStudent == null && currentLogInStaff == null) {
            HelperUtils.throwException("Không có tài khoản được đăng nhập");
            return;
        }

        if (
                currentLogInStudent != null &&
                currentLogInStudent.getAccount().getId() != updateAccount.getId()
        ) {
            HelperUtils.throwException("Sai ID Tài khoản");
            return;
        } else if (
                currentLogInStaff != null &&
                currentLogInStaff.getAccount().getId() != updateAccount.getId()
        ) {
            HelperUtils.throwException("Sai ID Tài khoản");
            return;
        }

        AccountDAO.update(updateAccount);
        refreshLoggedInAccount();
    }

    /**
     * Refresh current Logged in account info
     * */
    public static void refreshLoggedInAccount() throws Exception {
        if (currentLogInStaff != null) {
            logInStaff(currentLogInStaff.getAccount());
        } else if (currentLogInStudent != null) {
            logInStudent(currentLogInStudent.getAccount());
        }
    }


    /**
     * Log out of account
     * */
    public static void logOut() {
        logInAccountType = Account.ACCOUNT_INVALID;
        currentLogInStudent = null;
        currentLogInStaff = null;
    }
}
