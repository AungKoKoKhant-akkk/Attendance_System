package com.school.attendance_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiAttendanceResponse {
    private Boolean recognized;
    private String studentCode;
    private Double confidence;
    private Double distance;
    private String message;

    private AttendanceRecordResponse attendanceRecord;
}
