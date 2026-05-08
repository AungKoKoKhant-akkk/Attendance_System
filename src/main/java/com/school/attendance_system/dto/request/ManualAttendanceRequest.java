package com.school.attendance_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManualAttendanceRequest {
    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotBlank(message = "Student code is required")
    private String studentCode;
}
