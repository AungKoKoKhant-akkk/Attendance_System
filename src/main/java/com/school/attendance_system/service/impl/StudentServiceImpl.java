package com.school.attendance_system.service.impl;

import com.school.attendance_system.dto.request.StudentRequest;
import com.school.attendance_system.dto.response.FaceUploadResponse;
import com.school.attendance_system.dto.response.StudentResponse;
import com.school.attendance_system.entity.Student;
import com.school.attendance_system.repository.StudentRepository;
import com.school.attendance_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor

public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;


    @Override
    public StudentResponse createStudent(StudentRequest request) {

        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new RuntimeException("Student code already exists: " + request.getStudentCode());
        }

        Student student = Student.builder()
                .studentCode(request.getStudentCode())
                .name(request.getName())
                .classSection(request.getClassSection())
                .build();

        Student savedStudent = studentRepository.save(student);

        return mapToResponse(savedStudent);
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        return mapToResponse(student);
    }

    @Override
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        if (!student.getStudentCode().equals(request.getStudentCode())
                && studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new RuntimeException("Student code already exists: " + request.getStudentCode());
        }

        student.setStudentCode(request.getStudentCode());
        student.setName(request.getName());
        student.setClassSection(request.getClassSection());

        Student updatedStudent = studentRepository.save(student);

        return mapToResponse(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
    }

    @Value("${app.file.upload-dir}")
    private final String uploadDir;

    @Override
    public FaceUploadResponse uploadStudentFace(String studentCode, MultipartFile file) {
        Student student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new RuntimeException("Student not found with code: " + studentCode));

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Face image file is required");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed");
        }

        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);

            String fileName = student.getStudentCode() + "_" + System.currentTimeMillis() + extension;

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            String savedPath = uploadDir + "/" + fileName;

            student.setFaceImagePath(savedPath);
            student.setFaceRegistered(true);

            Student updatedStudent = studentRepository.save(student);

            return FaceUploadResponse.builder()
                    .studentCode(updatedStudent.getStudentCode())
                    .studentName(updatedStudent.getName())
                    .faceImagePath(updatedStudent.getFaceImagePath())
                    .faceRegistered(updatedStudent.getFaceRegistered())
                    .message("Face image uploaded successfully")
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload face image: " + e.getMessage());
        }
    }

    private String getFileExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }

        return filename.substring(filename.lastIndexOf("."));
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .studentCode(student.getStudentCode())
                .name(student.getName())
                .classSection(student.getClassSection())
                .faceImagePath(student.getFaceImagePath())
                .faceRegistered(student.getFaceRegistered())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
