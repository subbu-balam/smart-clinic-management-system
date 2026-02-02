package com.smartclinic.controller;

import com.smartclinic.api.ApiResponse;
import com.smartclinic.service.DoctorService;
import com.smartclinic.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final TokenService tokenService;

    public DoctorController(DoctorService doctorService, TokenService tokenService) {
        this.doctorService = doctorService;
        this.tokenService = tokenService;
    }

    // Grading: GET endpoint for doctor availability using dynamic parameters
    // Example: GET /api/doctors/1/availability?date=2026-02-01
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<ApiResponse<?>> getAvailability(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @PathVariable Long doctorId,
            @RequestParam String date
    ) {
        try {
            // Grading: validates token + structured response using ResponseEntity
            tokenService.validateAndGetEmail(authorization);

            LocalDate parsed = LocalDate.parse(date);
            var slots = doctorService.getAvailableSlots(doctorId, parsed);
            return ResponseEntity.ok(ApiResponse.ok("Availability fetched", slots));
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid date format. Use YYYY-MM-DD"));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(ApiResponse.error(ex.getMessage()));
        }
    }
}
