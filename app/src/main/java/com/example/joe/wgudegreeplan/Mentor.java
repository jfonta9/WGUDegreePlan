package com.example.joe.wgudegreeplan;

public class Mentor {

    // fields
    private String mentorFname;
    private String mentorLname;
    private String mentorPhone;
    private String mentorEmail;
    private int  mentorId;


    // constructors
    public Mentor() {}

    public Mentor(String mentorFname, String mentorLname, String mentorPhone,
                  String mentorEmail) {

        this.mentorFname = mentorFname;
        this.mentorLname = mentorLname;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;

    }


    public Mentor(int id, String mentorFname, String mentorLname, String mentorPhone,
                  String mentorEmail) {
        this.mentorId = id;
        this.mentorFname = mentorFname;
        this.mentorLname = mentorLname;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;

    }

    // properties


    public void setMentorFname(String mentorFname) {
        this.mentorFname = mentorFname;
    }

    public String getMentorFname() {
        return this.mentorFname;
    }

    public void setMentorLname(String mentorLname) {
        this.mentorLname = mentorLname;
    }

    public String getMentorLname() {
        return this.mentorLname;
    }

    public void setMentorPhone(String mentorPhone) {
        this.mentorPhone = mentorPhone;
    }

    public String getMentorPhone() {
        return this.mentorPhone;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public int getMentorId() {
        return this.mentorId;
    }

    public void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
    }

    public String getMentorEmail() {
        return this.mentorEmail;
    }


}