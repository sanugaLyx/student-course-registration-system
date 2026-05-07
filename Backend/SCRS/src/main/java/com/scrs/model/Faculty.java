/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/Faculty.java
 * 🏗  LAYER: Backend — Domain Model (Plain Java Base Class)
 * 📋 ROLE: Represents a Faculty conceptually. This is NOT a JPA entity
 *          and is not saved to the database directly. It exists to
 *          demonstrate OOP inheritance (Department extends Faculty).
 * 🔗 EXTENDED BY: Department
 * ─────────────────────────────────────────────────────────────
 */

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