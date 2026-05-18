
package com.scrs.controller;

import com.scrs.model.*;
import com.scrs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private StudentService  studentService;
    @Autowired private LecturerService lecturerService;
    @Autowired private DeanService     deanService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");


        Optional<Student> student = studentService.login(email, password);
        if (student.isPresent()) {
            Map<String, Object> res = new HashMap<>();
            res.put("user", student.get());
            res.put("role", "STUDENT");
            res.put("redirect", "dashboard.html");
            return ResponseEntity.ok(res);
        }


        Optional<Lecturer> lecturer = lecturerService.login(email, password);
        if (lecturer.isPresent()) {
            Map<String, Object> res = new HashMap<>();
            res.put("user", lecturer.get());
            res.put("role", "LECTURER");
            res.put("redirect", "dashboard.html");
            return ResponseEntity.ok(res);
        }


        Optional<Dean> dean = deanService.login(email, password);
        if (dean.isPresent()) {
            Map<String, Object> res = new HashMap<>();
            res.put("user", dean.get());
            res.put("role", "DEAN");
            res.put("redirect", "dean-dashboard.html");
            return ResponseEntity.ok(res);
        }


        return ResponseEntity.status(401)
                .body(Map.of("message", "Invalid email or password"));
    }
}
