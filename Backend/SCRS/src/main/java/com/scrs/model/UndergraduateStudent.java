

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
