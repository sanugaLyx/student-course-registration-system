
package com.scrs.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

@Entity
@Table(name = "courses")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CoreCourse.class, name = "CORE"),
        @JsonSubTypes.Type(value = ElectiveCourse.class, name = "ELEC")
})
public abstract class Course {

    @Id
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "lecturer_id")
    private String lecturerId;

    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "credits")
    private int credits;

    @Column(name = "max_capacity")
    private int maxCapacity;

    public Course() {}


    public boolean isEligible(Student student) {
        return true;
    }


    public String getType() {
        return (this instanceof CoreCourse) ? "CORE" : "ELEC";
    }

    
    public String getCourseId()               { return courseId; }
    public void   setCourseId(String id)      { this.courseId = id; }

    public String getCourseName()             { return courseName; }
    public void   setCourseName(String name)  { this.courseName = name; }

    public String getLecturerId()             { return lecturerId; }
    public void   setLecturerId(String lid)   { this.lecturerId = lid; }

    public String getDepartmentId()           { return departmentId; }
    public void   setDepartmentId(String did) { this.departmentId = did; }

    public int    getCredits()                { return credits; }
    public void   setCredits(int credits)     { this.credits = credits; }

    public int    getMaxCapacity()            { return maxCapacity; }
    public void   setMaxCapacity(int cap)     { this.maxCapacity = cap; }
}