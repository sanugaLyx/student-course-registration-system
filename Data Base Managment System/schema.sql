-- -------------------------------------------------------------------------
-- SCRS Database Schema -- MySQL
-- Run once before starting the Spring Boot application.
-- -------------------------------------------------------------------------

CREATE DATABASE IF NOT EXISTS scrs_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE scrs_db;

-- Departments table (created first -- other tables reference it)
CREATE TABLE IF NOT EXISTS departments (
    department_id   VARCHAR(20)  PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    dean_id         VARCHAR(20)
);

-- Deans table
CREATE TABLE IF NOT EXISTS deans (
    dean_id         VARCHAR(20)  PRIMARY KEY,
    email           VARCHAR(100) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    department_id   VARCHAR(20),
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id      VARCHAR(20)  PRIMARY KEY,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    dob             VARCHAR(20),
    phone_number    VARCHAR(20),
    department      VARCHAR(50),
    type            VARCHAR(5)   NOT NULL DEFAULT 'UG'   -- UG or PG
);

-- Lecturers table
CREATE TABLE IF NOT EXISTS lecturers (
    lecturer_id     VARCHAR(20)  PRIMARY KEY,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    dob             VARCHAR(20),
    phone_number    VARCHAR(20),
    department      VARCHAR(50),
    course_id       VARCHAR(20),
    type            VARCHAR(5)   NOT NULL DEFAULT 'FT'   -- FT or PT
);

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
    course_id       VARCHAR(20)  PRIMARY KEY,
    course_name     VARCHAR(100) NOT NULL,
    lecturer_id     VARCHAR(20),
    department_id   VARCHAR(20),
    credits         INT          NOT NULL DEFAULT 3,
    max_capacity    INT          NOT NULL DEFAULT 30,
    type            VARCHAR(10)  NOT NULL DEFAULT 'CORE', -- CORE or ELEC
    FOREIGN KEY (lecturer_id)   REFERENCES lecturers(lecturer_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

-- Enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id   VARCHAR(20)  PRIMARY KEY,
    student_id      VARCHAR(20)  NOT NULL,
    course_id       VARCHAR(20)  NOT NULL,
    enrollment_date VARCHAR(20)  NOT NULL,
    status          VARCHAR(15)  NOT NULL DEFAULT 'ACTIVE',  -- ACTIVE | DROPPED | COMPLETED
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_id)  REFERENCES courses(course_id)
);

-- Timetable entries table
CREATE TABLE IF NOT EXISTS timetable_entries (
    time_id         VARCHAR(20)  PRIMARY KEY,
    course_id       VARCHAR(20)  NOT NULL,
    lecturer_id     VARCHAR(20)  NOT NULL,
    day             VARCHAR(15)  NOT NULL,
    time_slot       VARCHAR(10)  NOT NULL,
    classroom       VARCHAR(30)  NOT NULL,
    FOREIGN KEY (course_id)   REFERENCES courses(course_id),
    FOREIGN KEY (lecturer_id) REFERENCES lecturers(lecturer_id)
);

