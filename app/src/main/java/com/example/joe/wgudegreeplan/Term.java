package com.example.joe.wgudegreeplan;

import java.util.ArrayList;

public class Term {

    // fields
    private int tid;
    private String termName;
    private String termStart;
    private String termEnd;




    // constructors
    public Term() {}

    public Term(int id, String termName, String termStart, String termEnd,
                ArrayList<Course> courseArrayList) {
        this.tid = id;
        this.termName = termName;
        this.termStart = termStart;
        this.termEnd = termEnd;
    }

    // properties
    public void setId(int id) {
        this.tid = id;
    }

    public int getId() {
        return this.tid;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermName() {
        return this.termName;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }

    public String getTermStart() {
        return this.termStart;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    public String getTermEnd() {
        return this.termEnd;
    }

}
