-- -------------------------------------------------------------------------
-- SCRS Database Seed -- MySQL
-- Run this after schema.sql to populate the database with sample data.
-- -------------------------------------------------------------------------

USE scrs_db;

-- -------------------------------------------------------------------------
-- SAMPLE DATA (minimum 3 records per table)
-- -------------------------------------------------------------------------

INSERT IGNORE INTO departments VALUES
('DEPT01', 'Computer Science',       'D001'),
('DEPT02', 'Information Technology', 'D001'),
('DEPT03', 'Software Engineering',   'D001');

INSERT IGNORE INTO deans VALUES
('D001', 'dean@uni.lk', 'dean123', 'DEPT01');

INSERT IGNORE INTO students VALUES
('S001', 'John',  'Silva',       'john@email.com',  'pass123', '1998-03-15', '0771234567', 'CS', 'UG'),
('S002', 'Amara', 'Perera',      'amara@email.com', 'pass456', '2000-07-22', '0769876543', 'IT', 'PG'),
('S003', 'Kasun', 'Jayawardena', 'kasun@email.com', 'pass789', '1999-11-05', '0754321098', 'CS', 'UG');

INSERT IGNORE INTO lecturers VALUES
('L001', 'Nimal',  'Fernando',    'nimal@uni.lk',  'lec123', '1985-05-10', '0712345678', 'CS', 'CS101', 'FT'),
('L002', 'Priya',  'Wickrama',    'priya@uni.lk',  'lec456', '1990-08-20', '0719876543', 'IT', 'IT201', 'PT'),
('L003', 'Roshan', 'Gunawardena', 'roshan@uni.lk', 'lec789', '1982-03-01', '0713456789', 'CS', 'CS102', 'FT');

INSERT IGNORE INTO courses VALUES
('CS101', 'Intro to OOP',     'L001', 'DEPT01', 3, 40, 'CORE'),
('CS102', 'Data Structures',  'L003', 'DEPT01', 3, 35, 'CORE'),
('IT201', 'Web Technologies', 'L002', 'DEPT02', 3, 30, 'ELEC');

INSERT IGNORE INTO enrollments VALUES
('E001', 'S001', 'CS101', '2025-09-01', 'ACTIVE'),
('E002', 'S001', 'CS102', '2025-09-01', 'DROPPED'),
('E003', 'S002', 'IT201', '2025-09-02', 'ACTIVE');

INSERT IGNORE INTO timetable_entries VALUES
('T001', 'CS101', 'L001', 'Monday',    '09:00', 'Room-A1'),
('T002', 'CS102', 'L003', 'Wednesday', '11:00', 'Room-B3'),
('T003', 'IT201', 'L002', 'Friday',    '14:00', 'Room-C2');
