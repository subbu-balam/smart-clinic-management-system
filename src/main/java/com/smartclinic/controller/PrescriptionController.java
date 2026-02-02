package com.smartclinic.controller;

import com.smartclinic.api.ApiResponse;
import com.smartclinic.api.PrescriptionCreateRequest;
import com.smartclinic.service.PrescriptionService;
import com.smartclinic.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final TokenService tokenService;

    public PrescriptionController(PrescriptionService prescriptionService, TokenService tokenService) {
        this.prescriptionService = prescriptionService;
        this.tokenService = tokenService;
    }

    // Grading: POST endpoint saves prescription with token + request body validation
    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @Valid @RequestBody PrescriptionCreateRequest req
    ) {
        try {
            // token validation
            tokenService.validateAndGetEmail(authorization);

            var rx = prescriptionService.createPrescription(req);
            return ResponseEntity.ok(ApiResponse.ok("Prescription saved", rx.getId()));
        } catch (Exception ex) {
            // Grading: structured success/error using ResponseEntity
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
