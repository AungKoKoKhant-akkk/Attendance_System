package com.school.attendance_system.service;

import com.school.attendance_system.dto.response.AiRecognizeFaceResponse;
import com.school.attendance_system.dto.response.AiRegisterFaceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AiFaceService {
    AiRegisterFaceResponse registerFace(String studentCode, MultipartFile file);
    AiRecognizeFaceResponse recognizeFace(MultipartFile file);
}
