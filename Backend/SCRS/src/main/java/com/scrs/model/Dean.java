/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/Dean.java
 * 🏗  LAYER: Backend — JPA Domain Model (Concrete Entity)
 * 📋 ROLE: Represents a Dean (Admin) in the system. Has full access privileges.
 *          Maps to its own 'deans' table, but inherits fields from the 'User' MappedSuperclass.
 * 🔗 EXTENDS: User
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.model;

import jakarta.persistence.*; // [L1] JPA annotations

@Entity
@Table(name = "deans")
public class Dean extends User {

    @Id
    @Column(name = "dean_id")
    private String deanId;

    @Column(name = "department_id")
    private String departmentId;

    public Dean() { super(); }


    public Dean(String deanId, String email, String password, String departmentId) {
        super();
        this.deanId       = deanId;
        this.departmentId = departmentId;
        setEmail(email);
        setPassword(password);
    }


    @Override
    public String getRole()          { return "DEAN"; }

    @Override
    public String getDashboardPath() { return "dean-dashboard.html"; }




    public String approveTimetable(String timeId) {
        return "Timetable " + timeId + " approved by Dean " + deanId;
    }

    public String viewDepartmentReport(String deptId) {
        return "Department report for " + deptId;
    }

    public String assignLecturerToCourse(String lecturerId, String courseId) {
        return "Lecturer " + lecturerId + " assigned to course " + courseId;
    }


    public String getDeanId()                 { return deanId; }
    public void   setDeanId(String id)        { this.deanId = id; }

    public String getUserId()                 { return deanId; }
    public void   setUserId(String id)        { this.deanId = id; }

    public String getDepartmentId()           { return departmentId; }
    public void   setDepartmentId(String did) { this.departmentId = did; }
}