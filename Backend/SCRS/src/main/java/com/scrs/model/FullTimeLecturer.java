
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