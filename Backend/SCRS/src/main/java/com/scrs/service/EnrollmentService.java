/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/service/EnrollmentService.java
 * 🏗  LAYER: Backend — Service Layer (Business Logic)
 * 📋 ROLE: Highly complex service managing the many-to-many relationship
 *          between Students and Courses. Implements strict business rules
 *          for capacities, duplicate checks, and polymorphic limits.
 * 🔗 DEPENDS ON: EnrollmentRepository, StudentRepository, CourseRepository
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.service;

import com.scrs.model.Course;
import com.scrs.model.Enrollment;
import com.scrs.model.Student;
import com.scrs.repository.CourseRepository;
import com.scrs.repository.EnrollmentRepository;
import com.scrs.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class EnrollmentService {

    @Autowired private EnrollmentRepository enrollmentRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CourseRepository courseRepository;


    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public boolean checkSeatAvailability(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        long activeCount = enrollmentRepository.countByCourseIdAndStatus(courseId, "ACTIVE");
        return activeCount < course.getMaxCapacity();
    }


    public Enrollment enroll(String studentId, String courseId) {


        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));


        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        if (!course.isEligible(student)) {
            throw new RuntimeException("Student not eligible for this course type");
        }

        long activeEnrollments = enrollmentRepository.countByStudentIdAndStatus(studentId, "ACTIVE");
        if (activeEnrollments >= student.getEnrollmentLimit()) {
            throw new RuntimeException("Student reached maximum enrollment limit (" + student.getEnrollmentLimit() + ")");
        }


        long currentCourseCount = enrollmentRepository.countByCourseIdAndStatus(courseId, "ACTIVE");
        if (currentCourseCount >= course.getMaxCapacity()) {
            throw new RuntimeException("Course capacity reached");
        }


        String newId = "ENR" + System.currentTimeMillis();
        String today = LocalDate.now().toString();
        Enrollment e = new Enrollment(newId, studentId, courseId, today, "ACTIVE");
        return enrollmentRepository.save(e);
    }

    public Enrollment updateEnrollmentStatus(String enrollmentId, String newStatus) {
        Enrollment existing = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        existing.setStatus(newStatus.toUpperCase());
        return enrollmentRepository.save(existing);
    }


    public void deleteEnrollment(String id) {
        enrollmentRepository.deleteById(id);
    }
}