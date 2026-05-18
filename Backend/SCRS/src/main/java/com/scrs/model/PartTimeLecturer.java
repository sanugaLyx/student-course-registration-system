
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
