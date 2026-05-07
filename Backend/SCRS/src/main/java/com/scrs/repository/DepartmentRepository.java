/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/repository/DepartmentRepository.java
 * 🏗  LAYER: Backend — Data Access Layer (Repository)
 * 📋 ROLE: Interface for database operations on the 'departments' table.
 * 🔗 USED BY: DepartmentService
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.repository;

import com.scrs.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
}