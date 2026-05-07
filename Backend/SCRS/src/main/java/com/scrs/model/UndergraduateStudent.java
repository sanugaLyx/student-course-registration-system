/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/UndergraduateStudent.java
 * 🏗  LAYER: Backend — JPA Domain Model (Concrete Entity)
 * 📋 ROLE: Concrete subclass of Student for undergraduate students.
 *          Stored in the same 'students' table with type = 'UG'.
 *          Overrides getEnrollmentLimit() to return 6 (OOP Polymorphism).
 * 🔗 EXTENDS: Student → User
 *    USED BY: Jackson deserializer (when JSON has "type": "UG"),
 *             EnrollmentService (to check enrollment limits)
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("UG")
public class UndergraduateStudent extends Student {

    public UndergraduateStudent() { super(); }

    @Override
    public int getEnrollmentLimit() {
        return 6;
    }
}
