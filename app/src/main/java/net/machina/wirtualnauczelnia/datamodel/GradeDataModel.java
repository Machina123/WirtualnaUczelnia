package net.machina.wirtualnauczelnia.datamodel;

import java.util.Objects;

public class GradeDataModel {
    private String courseName, courseForm, lecturer, hoursCount, firstTermGrade, secondTermGrade, thirdTermGrade, ectsCount;

    public GradeDataModel(String courseName, String courseForm, String lecturer, String hoursCount, String firstTermGrade, String secondTermGrade, String thirdTermGrade, String ectsCount) {
        this.courseName = courseName;
        this.courseForm = courseForm;
        this.lecturer = lecturer;
        this.hoursCount = hoursCount;
        this.firstTermGrade = firstTermGrade;
        this.secondTermGrade = secondTermGrade;
        this.thirdTermGrade = thirdTermGrade;
        this.ectsCount = ectsCount;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseForm() {
        return courseForm;
    }

    public void setCourseForm(String courseForm) {
        this.courseForm = courseForm;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getHoursCount() {
        return hoursCount;
    }

    public void setHoursCount(String hoursCount) {
        this.hoursCount = hoursCount;
    }

    public String getFirstTermGrade() {
        return firstTermGrade;
    }

    public void setFirstTermGrade(String firstTermGrade) {
        this.firstTermGrade = firstTermGrade;
    }

    public String getSecondTermGrade() {
        return secondTermGrade;
    }

    public void setSecondTermGrade(String secondTermGrade) {
        this.secondTermGrade = secondTermGrade;
    }

    public String getThirdTermGrade() {
        return thirdTermGrade;
    }

    public void setThirdTermGrade(String thirdTermGrade) {
        this.thirdTermGrade = thirdTermGrade;
    }

    public String getEctsCount() {
        return ectsCount;
    }

    public void setEctsCount(String ectsCount) {
        this.ectsCount = ectsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GradeDataModel)) return false;
        GradeDataModel that = (GradeDataModel) o;
        return Objects.equals(getCourseName(), that.getCourseName()) &&
                Objects.equals(getCourseForm(), that.getCourseForm()) &&
                Objects.equals(getLecturer(), that.getLecturer()) &&
                Objects.equals(getHoursCount(), that.getHoursCount()) &&
                Objects.equals(getFirstTermGrade(), that.getFirstTermGrade()) &&
                Objects.equals(getSecondTermGrade(), that.getSecondTermGrade()) &&
                Objects.equals(getThirdTermGrade(), that.getThirdTermGrade()) &&
                Objects.equals(getEctsCount(), that.getEctsCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourseName(), getCourseForm(), getLecturer(), getHoursCount(), getFirstTermGrade(), getSecondTermGrade(), getThirdTermGrade(), getEctsCount());
    }

    @Override
    public String toString() {
        return "GradeDataModel{" +
                "courseName='" + courseName + '\'' +
                ", courseForm='" + courseForm + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", hoursCount='" + hoursCount + '\'' +
                ", firstTermGrade='" + firstTermGrade + '\'' +
                ", secondTermGrade='" + secondTermGrade + '\'' +
                ", thirdTermGrade='" + thirdTermGrade + '\'' +
                ", ectsCount='" + ectsCount + '\'' +
                '}';
    }
}
