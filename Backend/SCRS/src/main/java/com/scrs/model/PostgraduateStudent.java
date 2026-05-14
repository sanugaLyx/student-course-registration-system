/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/PostgraduateStudent.java
 * 🏗  LAYER: Backend — JPA Domain Model (Concrete Entity)
 * 📋 ROLE: Concrete subclass of Student for postgraduate students.
 *          Stored in the same 'students' table with type = 'PG'.
 *          Overrides getEnrollmentLimit() to return 4 (stricter limit
 *          than undergraduates, reflecting heavier coursework at PG level).
 * 🔗 EXTENDS: Student → User
 *    USED BY: Jackson deserializer (when JSON has "type": "PG"),
 *             EnrollmentService (to check enrollment limits)
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PG")
public class PostgraduateStudent extends Student {

    public PostgraduateStudent() { super(); }

    @Override
    public int getEnrollmentLimit() {
        return 4;
    }
}