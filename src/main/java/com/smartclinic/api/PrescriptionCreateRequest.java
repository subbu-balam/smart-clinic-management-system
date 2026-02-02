package com.smartclinic.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PrescriptionCreateRequest {
    @NotNull
    private Long appointmentId;

    @NotBlank
    private String notes;

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
