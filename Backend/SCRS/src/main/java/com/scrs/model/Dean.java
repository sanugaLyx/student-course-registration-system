

package com.scrs.model;

import jakarta.persistence.*;


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