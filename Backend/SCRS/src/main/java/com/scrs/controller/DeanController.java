/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/controller/DeanController.java
 * 🏗  LAYER: Backend — API Layer (REST Controller)
 * 📋 ROLE: Exposes endpoints for Dean management (currently just GET).
 *          Authentication is handled globally in AuthController.
 * 🔗 DEPENDS ON: DeanService
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs.controller;

import com.scrs.model.Dean;
import com.scrs.service.DeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deans")
@CrossOrigin(origins = "*")
public class DeanController {

    @Autowired
    private DeanService deanService;


    @GetMapping("/{id}")
    public ResponseEntity<Dean> getDean(@PathVariable String id) {
        return deanService.getDeanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
