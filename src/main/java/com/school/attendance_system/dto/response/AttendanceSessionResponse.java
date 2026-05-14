package com.school.attendance_system.dto.response;

import com.school.attendance_system.enums.SessionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class AttendanceSessionResponse {
    private Long id;
    private String classSection;
    private String subjectName;
    private Long teacherId;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer lateAfterMinutes;
    private SessionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
