/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/controller/EnrollmentController.java
 * 🏗  LAYER: Backend — API Layer (REST Controller)
 * 📋 ROLE: Exposes endpoints for Course Enrollments. Converts domain
 *          exceptions into appropriate HTTP error codes.
 * 🔗 DEPENDS ON: EnrollmentService
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.controller;

import com.scrs.model.Enrollment;
import com.scrs.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;


    @GetMapping
    public List<Enrollment> getAll() {
        return enrollmentService.getAllEnrollments();
    }


    @GetMapping("/student/{studentId}")
    public List<Enrollment> getByStudent(@PathVariable String studentId) {
        return enrollmentService.getEnrollmentsByStudent(studentId);
    }


    @GetMapping("/check-seats/{courseId}")
    public ResponseEntity<?> checkSeats(@PathVariable String courseId) {
        boolean available = enrollmentService.checkSeatAvailability(courseId);
        return ResponseEntity.ok(Map.of("available", available));
    }

    @PostMapping
    public ResponseEntity<?> enroll(@RequestBody Map<String, String> body) {
        try {
            Enrollment e = enrollmentService.enroll(body.get("studentId"), body.get("courseId"));
            return ResponseEntity.status(201).body(e);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable String id,
                                          @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(
                    enrollmentService.updateEnrollmentStatus(id, body.get("status")));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.ok(Map.of("message", "Enrollment removed"));
    }
}