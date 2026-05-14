

package com.scrs.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ELEC")
public class ElectiveCourse extends Course {

    public ElectiveCourse() { super(); }


    @Override
    public boolean isEligible(Student student) {
        return true;   
    }
}
