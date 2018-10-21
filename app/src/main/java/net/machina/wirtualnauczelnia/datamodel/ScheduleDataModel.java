package net.machina.wirtualnauczelnia.datamodel;

import java.util.Objects;

public class ScheduleDataModel {
    private String date, hourStart, hourEnd, lectureName, lecturer, lecturePlace, lectureBuildingAddr, lectureForm, lecturePassForm;

    public ScheduleDataModel(String date, String hourStart, String hourEnd, String lectureName, String lecturer, String lecturePlace, String lectureBuildingAddr, String lectureForm, String lecturePassForm) {
        this.date = date;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        this.lectureName = lectureName;
        this.lecturer = lecturer;
        this.lecturePlace = lecturePlace;
        this.lectureBuildingAddr = lectureBuildingAddr;
        this.lectureForm = lectureForm;
        this.lecturePassForm = lecturePassForm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHourStart() {
        return hourStart;
    }

    public void setHourStart(String hourStart) {
        this.hourStart = hourStart;
    }

    public String getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(String hourEnd) {
        this.hourEnd = hourEnd;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getLecturePlace() {
        return lecturePlace;
    }

    public void setLecturePlace(String lecturePlace) {
        this.lecturePlace = lecturePlace;
    }

    public String getLectureBuildingAddr() {
        return lectureBuildingAddr;
    }

    public void setLectureBuildingAddr(String lectureBuildingAddr) {
        this.lectureBuildingAddr = lectureBuildingAddr;
    }

    public String getLectureForm() {
        return lectureForm;
    }

    public void setLectureForm(String lectureForm) {
        this.lectureForm = lectureForm;
    }

    public String getLecturePassForm() {
        return lecturePassForm;
    }

    public void setLecturePassForm(String lecturePassForm) {
        this.lecturePassForm = lecturePassForm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleDataModel that = (ScheduleDataModel) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(hourStart, that.hourStart) &&
                Objects.equals(hourEnd, that.hourEnd) &&
                Objects.equals(lectureName, that.lectureName) &&
                Objects.equals(lecturer, that.lecturer) &&
                Objects.equals(lecturePlace, that.lecturePlace) &&
                Objects.equals(lectureBuildingAddr, that.lectureBuildingAddr) &&
                Objects.equals(lectureForm, that.lectureForm) &&
                Objects.equals(lecturePassForm, that.lecturePassForm);
    }

    @Override
    public int hashCode() {

        return Objects.hash(date, hourStart, hourEnd, lectureName, lecturer, lecturePlace, lectureBuildingAddr, lectureForm, lecturePassForm);
    }

    @Override
    public String toString() {
        return "ScheduleDataModel{" +
                "date='" + date + '\'' +
                ", hourStart='" + hourStart + '\'' +
                ", hourEnd='" + hourEnd + '\'' +
                ", lectureName='" + lectureName + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", lecturePlace='" + lecturePlace + '\'' +
                ", lectureBuildingAddr='" + lectureBuildingAddr + '\'' +
                ", lectureForm='" + lectureForm + '\'' +
                ", lecturePassForm='" + lecturePassForm + '\'' +
                '}';
    }
}
