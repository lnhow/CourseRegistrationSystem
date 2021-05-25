package com.lnh.CourseRegistration.Controllers;

import com.lnh.CourseRegistration.DAOs.RegisterStatusDAO;
import com.lnh.CourseRegistration.DAOs.RegistrationInfoDAO;
import com.lnh.CourseRegistration.Entities.RegistrationInfo;
import com.lnh.CourseRegistration.Entities.Staff;
import com.lnh.CourseRegistration.Entities.Student;
import com.lnh.CourseRegistration.Entities.SupportEntities.RegisterStatus;
import com.lnh.CourseRegistration.Utils.HelperUtils;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

public class RegistrationInfoController {
    public static final String STUDENT_INFO_GETTING_ERR_MSG =
            "Lỗi lấy thông tin sinh viên: Không lấy được DBID Sinh viên";
    public static final String COURSE_NOT_REGISTERED_ERR_MSG =
            "Chưa đăng ký khóa học";
    public static final String COURSE_CANNOT_CANCEL_ERR_MSG =
            "Không thể hủy";
    public static final String NO_PERMISSION_ERR_MSG =
            "Không có quyền để hủy";
    public static final String STAFF_INFO_GETTING_ERR_MSG =
            "Lỗi lấy thông tin giáo vụ";
    public static final String COURSE_CANNOT_CONFIRM_ERR_MSG =
            "Không thể duyệt";
    public static final String COURSE_CANNOT_REEVALUATE_ERR_MSG =
            "Không thể hoàn tác";
    public static final String ERR_NO_COURSE_WITH_ID = "Lỗi không tìm thấy Khóa học";
    public static final String ERR_NO_STUDENT_WITH_ID = "Lỗi không tìm thấy Sinh viên có DBID";
    public static final String ERR_MAX_REGISTERED_ALREADY = "Đã đến số học phần tối đa";
    public static final String ERR_REGISTERED_ALREADY = "Đã đăng ký học phần này rồi";
    public static final String ERR_CONFLICT_TIME = "Trùng giờ học";
    public static final String ERR_COURSE_MAXED_SLOT_ALREADY = "Đã đến tối đa số lượt có thể đăng ký";

    public static void registerCourse(long courseID) throws Exception {
        long studentDBID = getLoggedInStudentDBID();
        if (studentDBID == -1) {
            return;
        }

        RegistrationInfoDAO.register(studentDBID, courseID);
    }

    public static void cancelCourseByStudent(long courseID) throws Exception {
        long studentDBID = getLoggedInStudentDBID();
        if (studentDBID == -1) {
            return;
        }

        RegistrationInfo info = RegistrationInfoDAO.getByID(studentDBID, courseID);
        if (info == null) {
            HelperUtils.throwException(COURSE_NOT_REGISTERED_ERR_MSG + ". " + COURSE_CANNOT_CANCEL_ERR_MSG);
        } else {
            info.setStatus(RegisterStatusDAO.getStatus(RegisterStatus.STATUS_CANCELLED_BY_STUDENT));
            RegistrationInfoDAO.update(info);
        }
    }

    public static void confirmCourseByStaff(long studentDBID, long courseID) throws Exception {
        boolean hasPermission = checkPermissionStaff();
        if (!hasPermission) {
            return;
        }

        RegistrationInfo info = RegistrationInfoDAO.getByID(studentDBID, courseID);
        if (info == null) {
            HelperUtils.throwException(COURSE_NOT_REGISTERED_ERR_MSG + ". " + COURSE_CANNOT_CONFIRM_ERR_MSG);
        } else {
            info.setStatus(RegisterStatusDAO.getStatus(RegisterStatus.STATUS_CONFIRMED));
            RegistrationInfoDAO.update(info);
        }
    }

    public static void reevaluateCourseByStaff(long studentDBID, long courseID) throws Exception {
        boolean hasPermission = checkPermissionStaff();
        if (!hasPermission) {
            return;
        }

        RegistrationInfo info = RegistrationInfoDAO.getByID(studentDBID, courseID);
        if (info == null) {
            HelperUtils.throwException(COURSE_NOT_REGISTERED_ERR_MSG + ". " + COURSE_CANNOT_REEVALUATE_ERR_MSG);
        } else {
            info.setStatus(RegisterStatusDAO.getStatus(RegisterStatus.STATUS_WAITING));
            RegistrationInfoDAO.update(info);
        }
    }

    public static void cancelCourseByStaff(long studentDBID, long courseID) throws Exception {
        boolean hasPermission = checkPermissionStaff();
        if (!hasPermission) {
            return;
        }

        RegistrationInfo info = RegistrationInfoDAO.getByID(studentDBID, courseID);
        if (info == null) {
            HelperUtils.throwException(COURSE_NOT_REGISTERED_ERR_MSG + ". " + COURSE_CANNOT_CANCEL_ERR_MSG);
        } else {
            info.setStatus(RegisterStatusDAO.getStatus(RegisterStatus.STATUS_CANCELLED));
            RegistrationInfoDAO.update(info);
        }
    }

    /**
     * Get Logged in student DBID
     * @return studentDBID (-1 if there are not logged in student)
     * */
    private static long getLoggedInStudentDBID() throws Exception {
        Student student = LoginController.getLogInStudent();

        if (student == null) {
            HelperUtils.throwException(STUDENT_INFO_GETTING_ERR_MSG);
            return -1;
        }

        return student.getStudentNo();
    }

    /**
     * Check if current logged in user has permission
     * @return true if has permission, else false
     * */
    private static boolean checkPermissionStaff() throws Exception {
        Staff staff = LoginController.getLogInStaff();

        if (staff == null) {
            HelperUtils.throwException(NO_PERMISSION_ERR_MSG + ". " + STAFF_INFO_GETTING_ERR_MSG);
            return false;
        }

        return true;
    }

}
