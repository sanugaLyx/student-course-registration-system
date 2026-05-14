
package com.scrs.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CORE")
public class CoreCourse extends Course {

    public CoreCourse() { super(); }


    @Override
    public boolean isEligible(Student student) {
        return "PG".equals(student.getType());
    }
}