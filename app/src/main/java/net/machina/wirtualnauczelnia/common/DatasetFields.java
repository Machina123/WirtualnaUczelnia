package net.machina.wirtualnauczelnia.common;

public class DatasetFields {
    public static final String DS_TERMINFO_TERM = "currentTerm";
    public static final String DS_TERMINFO_YEAR = "currentYear";
    public static final String[] DS_GRADE_DETAILS = {
            "courseName",
            "courseForm",
            "lecturer",
            "courseHours",
            "gradeFirst",
            "gradeSecond",
            "gradeComission",
            "ectsCount"
    };

    public static final String DS_SCHEDULE_CURR_WEEK = "currentWeek";
    public static final String[] DS_SCHEDULE_DETAILS = {
            "date",
            "hourStart",
            "hourEnd",
            "lectureName",
            "lecturer",
            "lecturePlace",
            "lectureBuildingAddr",
            "lectureForm",
            "lecturePassForm"
    };
}
