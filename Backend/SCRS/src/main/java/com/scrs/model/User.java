

package com.scrs.model;


import com.fasterxml.jackson.annotation.JsonProperty;


import jakarta.persistence.Column;


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
