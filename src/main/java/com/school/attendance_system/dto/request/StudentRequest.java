package com.school.attendance_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentRequest {
    @NotBlank(message = "student code is required")
    private String studentCode;
    @NotBlank(message = "student name is required")
    private String name;
    @NotBlank(message = "student class is required")
    private String classSection;


}
