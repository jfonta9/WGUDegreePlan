package com.example.joe.wgudegreeplan;

public class Assessment {

    // fields
    private int assessmentId;
    private int courseId;
    private String assessmentTitle;
    private String assessmentType;
    private String assessmentDueDate;

    // constructors
    public Assessment() {}

    public Assessment(int assessmentId, String assessmentTitle, String assessmentType,
                      String assessmentDueDate, int courseId) {
        this.assessmentId = assessmentId;
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentDueDate = assessmentDueDate;
        this.courseId = courseId;
    }

    public Assessment(int assessmentId, String assessmentTitle, String assessmentType,
                      String assessmentDueDate) {
        this.assessmentId = assessmentId;
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentDueDate = assessmentDueDate;

    }

    // properties


    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public int getAssessmentId() { return this.assessmentId; }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }

    public String getAssessmentTitle() {
        return this.assessmentTitle;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getAssessmentType() {
        return this.assessmentType;
    }

    public void setAssessmentDueDate(String assessmentDueDate) {
        this.assessmentDueDate = assessmentDueDate;
    }

    public String getAssessmentDueDate() {
        return this.assessmentDueDate;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return this.courseId;
    }



}