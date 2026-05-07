/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/Department.java
 * 🏗  LAYER: Backend — JPA Domain Model (Concrete Entity)
 * 📋 ROLE: Represents an academic department. Maps to 'departments' table.
 *          Extends the non-JPA Faculty class to demonstrate standard
 *          Java inheritance alongside JPA persistence.
 * 🔗 EXTENDS: Faculty
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.model;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department extends Faculty {

    @Id
    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "dean_id")
    private String deanId;

    public Department() { super(); }


    public Department(String departmentId, String departmentName, String deanId) {
        super(departmentId, departmentName);
        this.departmentId   = departmentId;
        this.departmentName = departmentName;
        this.deanId         = deanId;
    }

    @Override
    public String generateReport() {
        return "Department Report: " + departmentName + " | Dean: " + deanId;
    }


    public String getDepartmentId()              { return departmentId; }
    public void   setDepartmentId(String id)     { this.departmentId = id; }

    public String getDepartmentName()            { return departmentName; }
    public void   setDepartmentName(String name) { this.departmentName = name; }

    public String getDeanId()                    { return deanId; }
    public void   setDeanId(String did)          { this.deanId = did; }
}