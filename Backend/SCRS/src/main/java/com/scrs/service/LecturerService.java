/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/service/LecturerService.java
 * 🏗  LAYER: Backend — Service Layer (Business Logic)
 * 📋 ROLE: Handles business logic for Lecturers.
 *          Supports changing the discriminator type (FT <-> PT) via native SQL.
 * 🔗 DEPENDS ON: LecturerRepository, EntityManager
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.service;

import com.scrs.model.Lecturer;
import com.scrs.repository.LecturerRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LecturerService {

    @Autowired private LecturerRepository lecturerRepository;
    @Autowired private EntityManager entityManager;


    public List<Lecturer> getAllLecturers() {
        return lecturerRepository.findAll();
    }


    public Optional<Lecturer> getLecturerById(String id) {
        return lecturerRepository.findById(id);
    }


    public Optional<Lecturer> login(String email, String password) {
        return lecturerRepository.findByEmailAndPassword(email, password);
    }


    public Lecturer addLecturer(Lecturer lecturer) {
        if (lecturerRepository.existsByEmail(lecturer.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return lecturerRepository.save(lecturer);
    }


    @Transactional
    public Lecturer updateLecturer(String id, Lecturer updated) {
        Lecturer existing = lecturerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecturer not found: " + id));
        

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setDepartment(updated.getDepartment());
        existing.setCourseId(updated.getCourseId());
        existing.setDob(updated.getDob());
        existing.setPhoneNumber(updated.getPhoneNumber());


        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            existing.setPassword(updated.getPassword());
        }

        lecturerRepository.save(existing);


        String newType = updated.getType();
        if (newType != null && !newType.equals(existing.getType())) {
            lecturerRepository.updateType(id, newType);
            entityManager.flush();
            entityManager.clear();
        }


        return lecturerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecturer not found after update: " + id));
    }


    @Transactional
    public void deleteLecturer(String id) {

        lecturerRepository.deleteById(id);
    }
}