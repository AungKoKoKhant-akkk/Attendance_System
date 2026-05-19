package com.school.attendance_system.controller;

import com.school.attendance_system.dto.request.StudentRequest;
import com.school.attendance_system.dto.response.FaceUploadResponse;
import com.school.attendance_system.dto.response.StudentResponse;
import com.school.attendance_system.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    @PostMapping
    public StudentResponse createStudent(@Valid @RequestBody StudentRequest request) {
        return studentService.createStudent(request);
    }

    @GetMapping
    public List<StudentResponse> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public StudentResponse getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public StudentResponse updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request
    ) {
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "Student deleted successfully";
    }

    @PostMapping(
            value = "/{studentCode}/face",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public FaceUploadResponse uploadStudentFace(
            @PathVariable String studentCode,
            @RequestParam("file") MultipartFile file
    ) {
        return studentService.uploadStudentFace(studentCode, file);
    }
}
