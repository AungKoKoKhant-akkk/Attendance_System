package com.school.attendance_system.dto.response;

import lombok.Data;

@Data
public class AiRegisterFaceResponse {
    private Boolean success;
    private String studentCode;
    private String imagePath;
    private String embeddingPath;
    private Double faceConfidence;
    private String message;
}
