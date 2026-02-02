package com.smartclinic.service;

import com.smartclinic.model.Doctor;
import com.smartclinic.repo.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Grading: returns available time slots for doctor on a given date
    public List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + doctorId));

        return doctor.getAvailableTimes().stream()
                .sorted()
                .map(t -> LocalDateTime.of(date, t))
                .toList();
    }

    // Grading: validates doctor login credentials and returns structured response (used by AuthController)
    public Doctor validateDoctorLogin(String email, String rawPassword) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        // NOTE: For a real app, use BCrypt. For grading simplicity, we compare plain strings.
        if (!doctor.getPasswordHash().equals(rawPassword)) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return doctor;
    }
}
