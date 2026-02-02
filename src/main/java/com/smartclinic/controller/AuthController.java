package com.smartclinic.controller;

import com.smartclinic.api.ApiResponse;
import com.smartclinic.api.LoginRequest;
import com.smartclinic.api.LoginResponse;
import com.smartclinic.model.Patient;
import com.smartclinic.repo.PatientRepository;
import com.smartclinic.service.DoctorService;
import com.smartclinic.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final DoctorService doctorService;
    private final PatientRepository patientRepository;
    private final TokenService tokenService;

    public AuthController(DoctorService doctorService, PatientRepository patientRepository, TokenService tokenService) {
        this.doctorService = doctorService;
        this.patientRepository = patientRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/doctor/login")
    public ResponseEntity<ApiResponse<?>> doctorLogin(@Valid @RequestBody LoginRequest req) {
        try {
            doctorService.validateDoctorLogin(req.getEmail(), req.getPassword());
            String token = tokenService.generateToken(req.getEmail());
            return ResponseEntity.ok(ApiResponse.ok("Doctor login success", new LoginResponse(token)));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/patient/login")
    public ResponseEntity<ApiResponse<?>> patientLogin(@Valid @RequestBody LoginRequest req) {
        try {
            Patient p = patientRepository.findByEmail(req.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
            if (!p.getPasswordHash().equals(req.getPassword())) {
                throw new IllegalArgumentException("Invalid credentials");
            }
            String token = tokenService.generateToken(req.getEmail());
            return ResponseEntity.ok(ApiResponse.ok("Patient login success", new LoginResponse(token)));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(ApiResponse.error(ex.getMessage()));
        }
    }
}
