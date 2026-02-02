# MySQL Schema Design (Smart Clinic)

This project uses at least **four** well-defined tables with foreign keys:

## Tables
### `doctors`
- `id` (PK)
- `name`
- `email` (unique)
- `password_hash`
- `specialty`

### `doctor_available_times`
Stores available time slots for each doctor.
- `doctor_id` (FK -> doctors.id)
- `available_time` (TIME)
Primary key: (`doctor_id`, `available_time`)

### `patients`
- `id` (PK)
- `name`
- `email` (unique)
- `phone` (unique)
- `password_hash`

### `appointments`
- `id` (PK)
- `doctor_id` (FK -> doctors.id)
- `patient_id` (FK -> patients.id)
- `appointment_time` (DATETIME)
- `status`

### `prescriptions`
- `id` (PK)
- `appointment_id` (FK -> appointments.id)
- `notes`
- `created_at` (DATETIME)

## Stored Procedures (required by grading)
- `GetDailyAppointmentReportByDoctor`
- `GetDoctorWithMostPatientsByMonth`
- `GetDoctorWithMostPatientsByYear`

See `sql/schema.sql` for full DDL and procedures.
