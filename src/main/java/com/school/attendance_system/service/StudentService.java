package com.school.attendance_system.service;

import com.school.attendance_system.dto.request.StudentRequest;
import com.school.attendance_system.dto.response.FaceUploadResponse;
import com.school.attendance_system.dto.response.StudentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    StudentResponse createStudent(StudentRequest request);

    List<StudentResponse> getAllStudents();

    StudentResponse getStudentById(Long id);

    StudentResponse updateStudent(Long id, StudentRequest request);

    void deleteStudent(Long id);

    FaceUploadResponse uploadStudentFace(String studentCode, MultipartFile file);
}
