package com.school.attendance_system.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentResponse {
    private Long studentId;
    private String studentCode;
    private String name;
    private String classSection;
    private String faceImagePath;
    private Boolean faceRegistered;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
