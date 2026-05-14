/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/model/User.java
 * 🏗  LAYER: Backend — JPA Domain Model (Abstract Base Class)
 * 📋 ROLE: Abstract superclass for ALL human entities in the SCRS
 *          (Student, Lecturer, Dean). It defines shared fields
 *          (name, email, password, phone, DOB) so every subclass
 *          inherits them without duplication.
 *          Demonstrates: Abstraction (abstract class + abstract methods)
 *                        Encapsulation (private fields, controlled access)
 * 🔗 USED BY: Student.java, Lecturer.java, Dean.java (all extend User)
 *    DEPENDS ON: Jakarta Persistence (JPA) for ORM mapping,
 *                Jackson (JsonProperty) for JSON serialization control
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.model;


import com.fasterxml.jackson.annotation.JsonProperty;

// [L1] JPA annotations for mapping Java fields to database columns
import jakarta.persistence.Column;

// [L1] Marks this class as a JPA superclass whose fields are inherited by entity subclasses
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class User {




    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "dob")
    private String dob;

    @Column(name = "phone_number")
    private String phoneNumber;

    public User() {

    }


    public abstract String getRole();
    public abstract String getDashboardPath();




    public String getFirstName()                 { return firstName; }
    public void   setFirstName(String fn)        { this.firstName = fn; }

    public String getLastName()                  { return lastName; }
    public void   setLastName(String ln)         { this.lastName = ln; }

    public String getEmail()                     { return email; }
    public void   setEmail(String email)         { this.email = email; }

    public String getPassword()                  { return password; }
    public void   setPassword(String pw)         { this.password = pw; }

    public String getDob()                       { return dob; }
    public void   setDob(String dob)             { this.dob = dob; }

    public String getPhoneNumber()               { return phoneNumber; }
    public void   setPhoneNumber(String phone)   { this.phoneNumber = phone; }
}