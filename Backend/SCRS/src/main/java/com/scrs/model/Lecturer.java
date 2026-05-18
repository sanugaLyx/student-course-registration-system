
package com.scrs.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;


@Entity
@Table(name = "lecturers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FullTimeLecturer.class, name = "FT"),
        @JsonSubTypes.Type(value = PartTimeLecturer.class, name = "PT")
})
public abstract class Lecturer extends User {

    @Id
    @Column(name = "lecturer_id")
    private String lecturerId;

    @Column(name = "department")
    private String department;

    @Column(name = "course_id")
    private String courseId;

    public Lecturer() { super(); }

    @Override
    public String getRole()          { return "LECTURER"; }

    @Override
    public String getDashboardPath() { return "dashboard.html"; }


    public abstract int getMaxCourses();


    public String getType() {
        return (this instanceof FullTimeLecturer) ? "FT" : "PT";
    }


    public String getLecturerId()
    { return lecturerId; }
    public void   setLecturerId(String id)
    { this.lecturerId = id; }

    public String getUserId()
    { return lecturerId; }
    public void   setUserId(String id)
    { this.lecturerId = id; }

    public String getDepartment()
    { return department; }
    public void   setDepartment(String dept)
    { this.department = dept; }

    public String getCourseId()
    { return courseId; }
    public void   setCourseId(String cid)
    { this.courseId = cid; }
}