package com.smartclinic.repo;

import com.smartclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Grading: method retrieves patient by email using derived or custom query
    Optional<Patient> findByEmail(String email);

    // Grading: method retrieves patient using either email or phone number
    Optional<Patient> findByEmailOrPhone(String email, String phone);
}
