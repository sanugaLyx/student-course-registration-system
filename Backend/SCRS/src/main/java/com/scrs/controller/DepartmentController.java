/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/controller/DepartmentController.java
 * 🏗  LAYER: Backend — API Layer (REST Controller)
 * 📋 ROLE: Exposes CRUD endpoints for Departments.
 * 🔗 DEPENDS ON: DepartmentService
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.controller;

import com.scrs.model.Department;
import com.scrs.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    @GetMapping
    public List<Department> getAll() {
        return departmentService.getAllDepartments();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Department> add(@RequestBody Department dept) {
        return ResponseEntity.status(201).body(departmentService.addDepartment(dept));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Department dept) {
        try {
            return ResponseEntity.ok(departmentService.updateDepartment(id, dept));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.ok(Map.of("message", "Department deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
