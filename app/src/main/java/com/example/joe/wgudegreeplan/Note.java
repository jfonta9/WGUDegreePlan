package com.example.joe.wgudegreeplan;

import java.util.ArrayList;

public class Note {
    // fields
    private int nid;
    private String noteContent;




    // constructors
    public Note() {}

    public Note(int id, String noteContent) {
        this.nid = id;
        this.noteContent = noteContent;

    }

    // properties
    public void setId(int id) {
        this.nid = id;
    }

    public int getId() {
        return this.nid;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteContent() {
        return this.noteContent;
    }


}
