/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/Enrollment.java
 * 🏗  LAYER: Backend — JPA Domain Model (Concrete Entity)
 * 📋 ROLE: Represents the many-to-many relationship bridge between a
 *          Student and a Course. Tracks the status of the enrollment
 *          (ACTIVE, DROPPED, COMPLETED).
 * 🔗 USED BY: EnrollmentService, EnrollmentController, StudentService
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.model;

import jakarta.persistence.*;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @Column(name = "enrollment_id")
    private String enrollmentId;




    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "course_id", nullable = false)
    private String courseId;

    @Column(name = "enrollment_date", nullable = false)
    private String enrollmentDate;

    @Column(name = "status", nullable = false)
    private String status;

    public Enrollment() {}


    public Enrollment(String enrollmentId, String studentId, String courseId,
                      String enrollmentDate, String status) {
        this.enrollmentId   = enrollmentId;
        this.studentId      = studentId;
        this.courseId       = courseId;
        this.enrollmentDate = enrollmentDate;
        this.status         = status;
    }

    // ── Getters and Setters ────────────────────────────────────
    public String getEnrollmentId()              { return enrollmentId; }
    public void   setEnrollmentId(String id)     { this.enrollmentId = id; }

    public String getStudentId()                 { return studentId; }
    public void   setStudentId(String sid)       { this.studentId = sid; }

    public String getCourseId()                  { return courseId; }
    public void   setCourseId(String cid)        { this.courseId = cid; }

    public String getEnrollmentDate()            { return enrollmentDate; }
    public void   setEnrollmentDate(String date) { this.enrollmentDate = date; }

    public String getStatus()                    { return status; }
    public void   setStatus(String status)       { this.status = status; }
}