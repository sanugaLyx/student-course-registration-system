

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
