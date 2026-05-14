/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/Lecturer.java
 * 🏗  LAYER: Backend — JPA Domain Model (Abstract Entity)
 * 📋 ROLE: Represents a lecturer. Abstract — must be FullTimeLecturer (FT)
 *          or PartTimeLecturer (PT). Uses SINGLE_TABLE inheritance
 *          (same pattern as Student). Linked to a Course via courseId.
 * 🔗 EXTENDS: User (inherits name, email, password, etc.)
 *    EXTENDED BY: FullTimeLecturer, PartTimeLecturer
 *    USED BY: LecturerService, LecturerController, AuthController
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.model;

import com.fasterxml.jackson.annotation.JsonSubTypes; // [L1] Maps JSON type values to Java subclasses
import com.fasterxml.jackson.annotation.JsonTypeInfo; // [L1] Enables polymorphic JSON deserialization
import jakarta.persistence.*;

@Entity
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
})

    @Id
    private String lecturerId;

    private String department;



    @Override

    @Override


    public abstract int getMaxCourses();


    public String getType() {
        return (this instanceof FullTimeLecturer) ? "FT" : "PT";
    }


    public String getLecturerId()              { return lecturerId; }
    public void   setLecturerId(String id)     { this.lecturerId = id; }

    public String getUserId()                  { return lecturerId; } // [L1] Alias for uniform access across User subclasses
    public void   setUserId(String id)         { this.lecturerId = id; }

    public String getDepartment()              { return department; }
    public void   setDepartment(String dept)   { this.department = dept; }

    public String getCourseId()                { return courseId; }
    public void   setCourseId(String cid)      { this.courseId = cid; }
}