/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/controller/LecturerController.java
 * 🏗  LAYER: Backend — API Layer (REST Controller)
 * 📋 ROLE: Exposes CRUD endpoints for Lecturers.
 * 🔗 DEPENDS ON: LecturerService
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.controller;

import com.scrs.model.Lecturer;
import com.scrs.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/lecturers")
@CrossOrigin(origins = "*")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;


    @GetMapping
    public List<Lecturer> getAll() {
        return lecturerService.getAllLecturers();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        return lecturerService.getLecturerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> add(@RequestBody Lecturer lecturer) {
        try {
            return ResponseEntity.status(201).body(lecturerService.addLecturer(lecturer));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Lecturer lecturer) {
        try {
            return ResponseEntity.ok(lecturerService.updateLecturer(id, lecturer));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        lecturerService.deleteLecturer(id);
        return ResponseEntity.ok(Map.of("message", "Lecturer deleted"));
    }
}
