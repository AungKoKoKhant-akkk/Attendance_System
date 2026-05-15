package com.school.attendance_system.dto.request;

import com.school.attendance_system.enums.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class AttendanceCorrectionRequest {
    @NotNull(message = "Attendance status is required")
    private AttendanceStatus status;

    private String reason;

    private String correctedBy;
}
