package com.smartclinic.service;

import com.smartclinic.api.PrescriptionCreateRequest;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.Prescription;
import com.smartclinic.repo.AppointmentRepository;
import com.smartclinic.repo.PrescriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService {

    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(AppointmentRepository appointmentRepository,
                               PrescriptionRepository prescriptionRepository) {
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public Prescription createPrescription(PrescriptionCreateRequest req) {
        Appointment appt = appointmentRepository.findById(req.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + req.getAppointmentId()));

        Prescription rx = new Prescription();
        rx.setAppointment(appt);
        rx.setNotes(req.getNotes());
        return prescriptionRepository.save(rx);
    }
}
