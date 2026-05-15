/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/repository/EnrollmentRepository.java
 * 🏗  LAYER: Backend — Data Access Layer (Repository)
 * 📋 ROLE: Interface for database operations on the 'enrollments' table.
 * 🔗 USED BY: EnrollmentService, StudentService, CourseService
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.repository;

import com.scrs.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {


    List<Enrollment> findByStudentId(String studentId);


    List<Enrollment> findByCourseId(String courseId);

    long countByCourseIdAndStatus(String courseId, String status);

    long countByStudentIdAndStatus(String studentId, String status);


    boolean existsByStudentIdAndCourseId(String studentId, String courseId);
}