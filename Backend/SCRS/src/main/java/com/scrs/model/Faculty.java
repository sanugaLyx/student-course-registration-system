
package com.scrs.model;


public class Faculty {

    private String facultyId;
    private String facultyName;

    public Faculty() {}

    public Faculty(String facultyId, String facultyName) {
        this.facultyId   = facultyId;
        this.facultyName = facultyName;
    }


    public String generateReport() {
        return "Faculty Report: " + facultyName;
    }


    public String getFacultyId()               { return facultyId; }
    public void   setFacultyId(String id)      { this.facultyId = id; }

    public String getFacultyName()             { return facultyName; }
    public void   setFacultyName(String name)  { this.facultyName = name; }
}