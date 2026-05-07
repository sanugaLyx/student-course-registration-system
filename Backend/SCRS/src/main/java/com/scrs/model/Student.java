/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/Student.java
 * 🏗  LAYER: Backend — JPA Domain Model (Abstract Entity)
 * 📋 ROLE: Represents a student in the system. This is an ABSTRACT
 *          class — you cannot create a "Student" directly. You must
 *          create an UndergraduateStudent (UG) or PostgraduateStudent (PG).
 *          Uses JPA SINGLE_TABLE inheritance: both UG and PG rows live
 *          in the same 'students' table, distinguished by a 'type' column.
 * 🔗 EXTENDS: User (inherits name, email, password, etc.)
 *    EXTENDED BY: UndergraduateStudent, PostgraduateStudent
 *    USED BY: StudentService, StudentController, EnrollmentService, AuthController
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.model; // [L1] Part of the model/domain layer


import com.fasterxml.jackson.annotation.JsonSubTypes; // [L1] Maps JSON "type" values to concrete Java classes
import com.fasterxml.jackson.annotation.JsonTypeInfo; // [L1] Tells Jackson to look for a "type" field in JSON to determine which subclass to create

// [L1] JPA annotations for database mapping and inheritance strategy
import jakarta.persistence.*;

@Entity
@Table(name = "students")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UndergraduateStudent.class, name = "UG"),
        @JsonSubTypes.Type(value = PostgraduateStudent.class, name = "PG")
})
public abstract class Student extends User {

    @Id
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "department")
    private String department;

    public Student() { super(); }


    @Override
    public String getRole()          { return "STUDENT"; }

    @Override
    public String getDashboardPath() { return "dashboard.html"; }


    public abstract int getEnrollmentLimit();

    public String getType() {
        return (this instanceof UndergraduateStudent) ? "UG" : "PG"; // [L1] Returns type based on runtime class
    }


    public String getStudentId()               { return studentId; }
    public void   setStudentId(String id)      { this.studentId = id; }



    public String getUserId()                  { return studentId; }
    public void   setUserId(String id)         { this.studentId = id; }

    public String getDepartment()              { return department; }
    public void   setDepartment(String dept)   { this.department = dept; }
}