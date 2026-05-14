/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/service/StudentService.java
 * 🏗  LAYER: Backend — Service Layer (Business Logic)
 * 📋 ROLE: Handles business logic for Students. Includes manual cascading
 *          deletes to remove enrollments when a student is deleted.
 *          Supports changing the discriminator type (UG <-> PG) via native SQL.
 * 🔗 DEPENDS ON: StudentRepository, EnrollmentRepository, EntityManager
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.service;

import com.scrs.model.Student;
import com.scrs.repository.StudentRepository;
import com.scrs.repository.EnrollmentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired private StudentRepository studentRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;
    @Autowired private EntityManager entityManager;


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

    @Transactional
    public Student updateStudent(String id, Student updated) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));


        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setDepartment(updated.getDepartment());
        existing.setDob(updated.getDob());
        existing.setPhoneNumber(updated.getPhoneNumber());


        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            existing.setPassword(updated.getPassword());
        }

        studentRepository.save(existing);

        String newType = updated.getType();
        if (newType != null && !newType.equals(existing.getType())) {
            studentRepository.updateType(id, newType);
            entityManager.flush();
            entityManager.clear();
        }


        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found after update: " + id));
    }

    @Transactional
    public void deleteStudent(String id) {

        enrollmentRepository.findByStudentId(id).forEach(e -> enrollmentRepository.delete(e));


        studentRepository.deleteById(id);
    }
}
