package com.school.attendance_system.dto.response;

import lombok.Data;

@Data
public class AiRecognizeFaceResponse {
    private Boolean recognized;
    private String studentCode;
    private Double confidence;
    private Double distance;
    private String bestMatchStudentCode;
    private String message;
}
