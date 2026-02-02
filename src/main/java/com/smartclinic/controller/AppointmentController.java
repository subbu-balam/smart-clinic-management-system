package com.smartclinic.controller;

import com.smartclinic.api.ApiResponse;
import com.smartclinic.api.AppointmentCreateRequest;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TokenService tokenService;

    public AppointmentController(AppointmentService appointmentService, TokenService tokenService) {
        this.appointmentService = appointmentService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> book(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @Valid @RequestBody AppointmentCreateRequest req
    ) {
        try {
            tokenService.validateAndGetEmail(authorization);
            var appt = appointmentService.bookAppointment(req);
            return ResponseEntity.ok(ApiResponse.ok("Appointment booked", appt.getId()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<?>> getDoctorAppointments(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @PathVariable Long doctorId,
            @RequestParam String date
    ) {
        try {
            tokenService.validateAndGetEmail(authorization);
            LocalDate parsed = LocalDate.parse(date);
            var list = appointmentService.getDoctorAppointmentsByDate(doctorId, parsed);
            return ResponseEntity.ok(ApiResponse.ok("Appointments fetched", list));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<?>> getPatientAppointments(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @PathVariable Long patientId
    ) {
        try {
            tokenService.validateAndGetEmail(authorization);
            var list = appointmentService.getPatientAppointments(patientId);
            return ResponseEntity.ok(ApiResponse.ok("Appointments fetched", list));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }
}
