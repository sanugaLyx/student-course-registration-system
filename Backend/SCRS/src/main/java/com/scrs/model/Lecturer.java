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

/*
 * [L2] Same inheritance pattern as Student:
 *   - @Inheritance(SINGLE_TABLE): FT and PT lecturers share the 'lecturers' table
 *   - @DiscriminatorColumn("type"): "FT" or "PT" stored in the type column
 *   - @JsonTypeInfo + @JsonSubTypes: Jackson creates FullTimeLecturer or PartTimeLecturer
 *     based on the "type" field in JSON, avoiding "cannot instantiate abstract class" errors
 */
@Entity
@Table(name = "lecturers") // [L1] Maps to the 'lecturers' database table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // [L1] FT + PT share one table
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING) // [L1] 'type' column: "FT" or "PT"
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FullTimeLecturer.class, name = "FT"), // [L1] JSON "FT" → FullTimeLecturer
        @JsonSubTypes.Type(value = PartTimeLecturer.class, name = "PT")  // [L1] JSON "PT" → PartTimeLecturer
})
public abstract class Lecturer extends User { // [L1] Abstract — cannot be instantiated directly

    @Id
    @Column(name = "lecturer_id") // [L1] Primary key for lecturers table
    private String lecturerId;

    @Column(name = "department") // [L1] Which department this lecturer belongs to
    private String department;

    @Column(name = "course_id") // [L1] The course this lecturer is currently assigned to teach
    private String courseId; // [L1] FK → Course (stored as String, not a JPA relationship)

    public Lecturer() { super(); } // [L1] Required by JPA

    @Override
    public String getRole()          { return "LECTURER"; } // [L1] Used by AuthController for role identification

    @Override
    public String getDashboardPath() { return "dashboard.html"; } // [L1] Lecturers go to the shared dashboard

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