/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/repository/DeanRepository.java
 * 🏗  LAYER: Backend — Data Access Layer (Repository)
 * 📋 ROLE: Interface for database operations on the 'deans' table.
 * 🔗 USED BY: DeanService
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.repository;

import com.scrs.model.Dean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DeanRepository extends JpaRepository<Dean, String> {

    Optional<Dean> findByEmailAndPassword(String email, String password);
}