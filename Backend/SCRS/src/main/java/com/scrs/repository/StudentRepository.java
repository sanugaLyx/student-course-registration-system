/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/repository/StudentRepository.java
 * 🏗  LAYER: Backend — Data Access Layer (Repository)
 * 📋 ROLE: Interface for database operations on the 'students' table.
 *          Since Student uses SINGLE_TABLE inheritance, this repository
 *          handles both UndergraduateStudent and PostgraduateStudent.
 * 🔗 USED BY: StudentService
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.repository;

import com.scrs.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, String> {


    Optional<Student> findByEmailAndPassword(String email, String password);


    boolean existsByEmail(String email);
}
