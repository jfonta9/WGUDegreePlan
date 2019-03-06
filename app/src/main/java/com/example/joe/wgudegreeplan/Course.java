package com.example.joe.wgudegreeplan;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Course {

    // fields

    private int courseId;
    private String courseName;
    private String courseStart;
    private String courseEnd;
    private String courseStatus;
    private int termId;



    // constructors
    public Course() {}

    public Course(int id, String courseName, String courseStart, String courseEnd,
                  String courseStatus, int termId, ArrayList<Mentor> mentorList,
                  ArrayList<Assessment> assessmentList) {
        this.courseId = id;
        this.courseName = courseName;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus;
        this.termId = termId;
    }

    // properties

    public void setId(int id) {
        this.courseId = id;
    }

    public int getId() {
        return this.courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseStart(String courseStart) {
        this.courseStart = courseStart;
    }

    public String getCourseStart() {
        return this.courseStart;
    }

    public void setCourseEnd(String courseEnd) {
        this.courseEnd = courseEnd;
    }

    public String getCourseEnd() {
        return this.courseEnd;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseStatus() {
        return this.courseStatus;
    }





}
