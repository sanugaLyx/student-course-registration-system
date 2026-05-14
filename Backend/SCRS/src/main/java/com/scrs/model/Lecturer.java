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
@Table(name = "lecturers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FullTimeLecturer.class, name = "FT"),
        @JsonSubTypes.Type(value = PartTimeLecturer.class, name = "PT")
})
public abstract class Lecturer extends User {

    @Id
    @Column(name = "lecturer_id")
    private String lecturerId;

    @Column(name = "department")
    private String department;

    @Column(name = "course_id")
    private String courseId;

    public Lecturer() { super(); }

    @Override
    public String getRole()          { return "LECTURER"; }

    @Override
    public String getDashboardPath() { return "dashboard.html"; }

    /*
     * [L2] getMaxCourses() — Abstract Polymorphic Method
     * FullTimeLecturer → 5 courses max
     * PartTimeLecturer → 2 courses max
     * Currently defined but not enforced in the service layer.
     */
    public abstract int getMaxCourses();

    // [L2] Returns "FT" or "PT" for JSON serialization — mirrors the discriminator value
    public String getType() {
        return (this instanceof FullTimeLecturer) ? "FT" : "PT";
    }

    // ── Getters and Setters ────────────────────────────────────
    public String getLecturerId()              { return lecturerId; }
    public void   setLecturerId(String id)     { this.lecturerId = id; }

    public String getUserId()                  { return lecturerId; } // [L1] Alias for uniform access across User subclasses
    public void   setUserId(String id)         { this.lecturerId = id; }

    public String getDepartment()              { return department; }
    public void   setDepartment(String dept)   { this.department = dept; }

    public String getCourseId()                { return courseId; }
    public void   setCourseId(String cid)      { this.courseId = cid; }
}