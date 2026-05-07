/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/service/DeanService.java
 * 🏗  LAYER: Backend — Service Layer (Business Logic)
 * 📋 ROLE: Handles business logic for Deans (Admins). Primarily
 *          deals with authentication.
 * 🔗 DEPENDS ON: DeanRepository
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.service;

import com.scrs.model.Dean;
import com.scrs.repository.DeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class DeanService {

    @Autowired
    private DeanRepository deanRepository;

    public Optional<Dean> login(String email, String password) {
        return deanRepository.findByEmailAndPassword(email, password);
    }

    public Optional<Dean> getDeanById(String id) {
        return deanRepository.findById(id);
    }
}