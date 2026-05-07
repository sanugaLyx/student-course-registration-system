/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/service/StudentService.java
 * 🏗  LAYER: Backend — Service Layer (Business Logic)
 * 📋 ROLE: Handles business logic for Students. Includes manual cascading
 *          deletes to remove enrollments when a student is deleted.
 * 🔗 DEPENDS ON: StudentRepository, EnrollmentRepository
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.service;

import com.scrs.model.Student;
import com.scrs.repository.StudentRepository;
//import com.scrs.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired private StudentRepository studentRepository;
    //@Autowired private EnrollmentRepository enrollmentRepository;


    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }


    public Optional<Student> login(String email, String password) {
        return studentRepository.findByEmailAndPassword(email, password);
    }


    public Student addStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return studentRepository.save(student);
    }


    public Student updateStudent(String id, Student updated) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));


        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setDepartment(updated.getDepartment());
        existing.setDob(updated.getDob());
        existing.setPhoneNumber(updated.getPhoneNumber());
        
        return studentRepository.save(existing);
    }


//    @Transactional
//    public void deleteStudent(String id) {
//
//        enrollmentRepository.findByStudentId(id).forEach(e -> enrollmentRepository.delete(e));
//
//
//        studentRepository.deleteById(id);
//    }
}
