package com.school.attendance_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FaceUploadResponse {
    private String studentCode;
    private String studentName;
    private String faceImagePath;
    private Boolean faceRegistered;
    private String message;
}
