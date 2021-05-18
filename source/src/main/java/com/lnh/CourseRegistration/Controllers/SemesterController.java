package com.lnh.CourseRegistration.Controllers;

import com.lnh.CourseRegistration.Entities.Semester;

public class SemesterController {
    static Semester currentSemester = null;

    public static void setCurrentSemester(Semester semester) {
        currentSemester = semester;
    }

    public static Semester getCurrentSemester() {
        return currentSemester;
    }
}
