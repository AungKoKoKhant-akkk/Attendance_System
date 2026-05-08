package com.school.attendance_system.service.impl;

import com.school.attendance_system.dto.request.StudentRequest;
import com.school.attendance_system.dto.response.StudentResponse;
import com.school.attendance_system.entity.Student;
import com.school.attendance_system.repository.StudentRepository;
import com.school.attendance_system.service.StudentService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@Data
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

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .name(student.getName())
                .classSection(student.getClassSection())
                .faceImagePath(student.getFaceImagePath())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
