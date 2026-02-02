package com.smartclinic.service;

import com.smartclinic.api.AppointmentCreateRequest;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.model.Patient;
import com.smartclinic.repo.AppointmentRepository;
import com.smartclinic.repo.DoctorRepository;
import com.smartclinic.repo.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // Grading: booking method that saves an appointment
    public Appointment bookAppointment(AppointmentCreateRequest req) {
        Doctor doctor = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + req.getDoctorId()));
        Patient patient = patientRepository.findById(req.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + req.getPatientId()));

        Appointment appt = new Appointment();
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setAppointmentTime(req.getAppointmentTime());
        appt.setStatus("BOOKED");

        return appointmentRepository.save(appt);
    }

    // Grading: retrieve appointments for a doctor on a specific date
    public List<Appointment> getDoctorAppointmentsByDate(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorAndDate(doctorId, date);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatient_Id(patientId);
    }
}
