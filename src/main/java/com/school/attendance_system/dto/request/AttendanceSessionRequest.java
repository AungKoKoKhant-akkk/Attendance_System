package com.school.attendance_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class AttendanceSessionRequest {
    @NotBlank(message = "Class section is required")
    private String classSection;

    @NotBlank(message = "Subject name is required")
    private String subjectName;

    // For now, we manually send teacherId.
    // Later, we will get teacherId from logged-in user.
    private Long teacherId;

    private Integer lateAfterMinutes;
}
