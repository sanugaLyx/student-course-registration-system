/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/FullTimeLecturer.java
 * 🏗  LAYER: Backend — JPA Domain Model (Concrete Entity)
 * 📋 ROLE: Concrete subclass for full-time lecturers. Stored with type = 'FT'.
 *          Overrides getMaxCourses() to return 5 (Polymorphism).
 * 🔗 EXTENDS: Lecturer → User
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("FT")
public class FullTimeLecturer extends Lecturer {

    public FullTimeLecturer() { super(); }


    @Override
    public int getMaxCourses() {
        return 5;
    }
}