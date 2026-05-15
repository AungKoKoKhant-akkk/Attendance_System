package com.school.attendance_system.dto.response;

import com.school.attendance_system.enums.AttendanceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class AttendanceRecordResponse {
    private Long id;

    private Long sessionId;

    private Long studentId;
    private String studentCode;
    private String studentName;
    private String classSection;

    private AttendanceStatus status;
    private LocalTime checkInTime;
    private Double confidenceScore;
    private String markedBy;

    private String correctedBy;
    private String correctionReason;
    private LocalDateTime correctedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
