CREATE DATABASE IF NOT EXISTS smart_clinic;
USE smart_clinic;

-- Doctors
CREATE TABLE IF NOT EXISTS doctors (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  specialty VARCHAR(100) NOT NULL
);

-- Doctor available times (normalized)
CREATE TABLE IF NOT EXISTS doctor_available_times (
  doctor_id BIGINT NOT NULL,
  available_time TIME NOT NULL,
  PRIMARY KEY (doctor_id, available_time),
  CONSTRAINT fk_avail_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

-- Patients
CREATE TABLE IF NOT EXISTS patients (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  phone VARCHAR(30) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL
);

-- Appointments
CREATE TABLE IF NOT EXISTS appointments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  doctor_id BIGINT NOT NULL,
  patient_id BIGINT NOT NULL,
  appointment_time DATETIME NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'BOOKED',
  CONSTRAINT fk_appt_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
  CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- Prescriptions
CREATE TABLE IF NOT EXISTS prescriptions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  appointment_id BIGINT NOT NULL,
  notes TEXT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_rx_appt FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- =========================
-- Stored Procedures
-- =========================

DROP PROCEDURE IF EXISTS GetDailyAppointmentReportByDoctor;
DELIMITER $$
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
  IN pDoctorId BIGINT,
  IN pDate DATE
)
BEGIN
  SELECT a.id AS appointmentId,
         a.appointment_time AS appointmentTime,
         a.status,
         p.id AS patientId,
         p.name AS patientName,
         p.email AS patientEmail
  FROM appointments a
  JOIN patients p ON p.id = a.patient_id
  WHERE a.doctor_id = pDoctorId
    AND DATE(a.appointment_time) = pDate
  ORDER BY a.appointment_time;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByMonth;
DELIMITER $$
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
  IN pYear INT,
  IN pMonth INT
)
BEGIN
  SELECT d.id AS doctorId,
         d.name AS doctorName,
         d.specialty,
         COUNT(DISTINCT a.patient_id) AS uniquePatients
  FROM appointments a
  JOIN doctors d ON d.id = a.doctor_id
  WHERE YEAR(a.appointment_time) = pYear
    AND MONTH(a.appointment_time) = pMonth
  GROUP BY d.id, d.name, d.specialty
  ORDER BY uniquePatients DESC
  LIMIT 1;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByYear;
DELIMITER $$
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
  IN pYear INT
)
BEGIN
  SELECT d.id AS doctorId,
         d.name AS doctorName,
         d.specialty,
         COUNT(DISTINCT a.patient_id) AS uniquePatients
  FROM appointments a
  JOIN doctors d ON d.id = a.doctor_id
  WHERE YEAR(a.appointment_time) = pYear
  GROUP BY d.id, d.name, d.specialty
  ORDER BY uniquePatients DESC
  LIMIT 1;
END $$
DELIMITER ;
