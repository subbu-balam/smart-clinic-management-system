package com.smartclinic.repo;

import com.smartclinic.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND DATE(a.appointmentTime) = :date ORDER BY a.appointmentTime")
    List<Appointment> findByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

    List<Appointment> findByPatient_Id(Long patientId);
}
