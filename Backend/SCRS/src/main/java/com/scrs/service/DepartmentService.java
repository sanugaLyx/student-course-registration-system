/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/service/DepartmentService.java
 * 🏗  LAYER: Backend — Service Layer (Business Logic)
 * 📋 ROLE: Manages Departments. Enforces a business rule preventing
 *          the deletion of a department if it still has active courses.
 * 🔗 DEPENDS ON: DepartmentRepository, CourseRepository
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.service;

import com.scrs.model.Department;
import com.scrs.repository.CourseRepository;
import com.scrs.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DepartmentService {

    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private CourseRepository courseRepository;


    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }


    public Optional<Department> getDepartmentById(String id) {
        return departmentRepository.findById(id);
    }


    public Department addDepartment(Department dept) {
        return departmentRepository.save(dept);
    }


    public Department updateDepartment(String id, Department updated) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));
        existing.setDepartmentName(updated.getDepartmentName());
        existing.setDeanId(updated.getDeanId());
        return departmentRepository.save(existing);
    }

    public void deleteDepartment(String id) {

        boolean hasLinkedCourses = courseRepository.findAll().stream()
                .anyMatch(c -> id.equals(c.getDepartmentId())); 
        
        if (hasLinkedCourses)

            throw new RuntimeException("Cannot delete department with active courses"); 
            
        departmentRepository.deleteById(id);
    }
}