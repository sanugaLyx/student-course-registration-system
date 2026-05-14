/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/PartTimeLecturer.java
 * 🏗  LAYER: Backend — JPA Domain Model (Concrete Entity)
 * 📋 ROLE: Concrete subclass for part-time lecturers. Stored with type = 'PT'.
 *          Overrides getMaxCourses() to return 2 (stricter than full-time).
 * 🔗 EXTENDS: Lecturer → User
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PT")
public class PartTimeLecturer extends Lecturer {

    public PartTimeLecturer() { super(); }

    @Override
    public int getMaxCourses() {
        return 2;
    }
}
