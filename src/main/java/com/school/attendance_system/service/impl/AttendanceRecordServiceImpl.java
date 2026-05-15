package com.school.attendance_system.service.impl;

import com.school.attendance_system.dto.request.AttendanceCorrectionRequest;
import com.school.attendance_system.dto.request.ManualAttendanceRequest;
import com.school.attendance_system.dto.response.AttendanceRecordResponse;
import com.school.attendance_system.dto.response.AttendanceSummaryResponse;
import com.school.attendance_system.dto.response.StudentResponse;
import com.school.attendance_system.entity.AttendanceRecord;
import com.school.attendance_system.entity.AttendanceSession;
import com.school.attendance_system.entity.Student;
import com.school.attendance_system.enums.AttendanceStatus;
import com.school.attendance_system.enums.SessionStatus;
import com.school.attendance_system.repository.AttendanceRecordRepository;
import com.school.attendance_system.repository.AttendanceSessionRepository;
import com.school.attendance_system.repository.StudentRepository;
import com.school.attendance_system.service.AttendanceRecordService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class AttendanceRecordServiceImpl implements AttendanceRecordService {
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final AttendanceSessionRepository attendanceSessionRepository;
    private final StudentRepository studentRepository;

    @Override
    public AttendanceRecordResponse markManualAttendance(ManualAttendanceRequest request) {

        AttendanceSession session = attendanceSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Attendance session not found with id: " + request.getSessionId()));

        if (session.getStatus() == SessionStatus.FINISHED) {
            throw new RuntimeException("Cannot mark attendance. Session is already finished.");
        }

        Student student = studentRepository.findByStudentCode(request.getStudentCode())
                .orElseThrow(() -> new RuntimeException("Student not found with code: " + request.getStudentCode()));

        if (attendanceRecordRepository.existsBySessionIdAndStudentId(session.getId(), student.getId())) {
            throw new RuntimeException("Attendance already marked for student: " + student.getStudentCode());
        }

        LocalTime checkInTime = LocalTime.now();

        AttendanceStatus status = calculateAttendanceStatus(session, checkInTime);

        AttendanceRecord attendanceRecord = AttendanceRecord.builder()
                .session(session)
                .student(student)
                .status(status)
                .checkInTime(checkInTime)
                .confidenceScore(null)
                .markedBy("MANUAL")
                .build();

        AttendanceRecord savedRecord = attendanceRecordRepository.save(attendanceRecord);

        return mapToResponse(savedRecord);
    }

    @Override
    public List<AttendanceRecordResponse> getAttendanceBySession(Long sessionId) {
        return attendanceRecordRepository.findBySessionId(sessionId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AttendanceRecordResponse> getAttendanceByStudent(Long studentId) {
        return attendanceRecordRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AttendanceRecordResponse correctAttendance(Long recordId, AttendanceCorrectionRequest request) {
        AttendanceRecord record = attendanceRecordRepository.findById(recordId)
                .orElseThrow(()-> new RuntimeException("Attendance record not found with id: " + recordId));
        record.setStatus(request.getStatus());
        record.setMarkedBy("TEACHER_CORRECTION");
        record.setCorrectionReason(request.getReason());
        record.setCorrectedBy(request.getCorrectedBy() !=null && !request.getCorrectedBy().isBlank()
        ? request.getCorrectedBy() : "Teacher");

        if((request.getStatus() == AttendanceStatus.PRESENT || request.getStatus() == AttendanceStatus.LATE) && record.getCheckInTime() == null){
            record.setCheckInTime(LocalTime.now());
        }

        // If teacher changes to ABSENT or EXCUSED, check-in time should usually be empty.
        if (request.getStatus() == AttendanceStatus.ABSENT || request.getStatus() == AttendanceStatus.EXCUSED) {
            record.setCheckInTime(null);
        }

        AttendanceRecord updatedRecord = attendanceRecordRepository.save(record);

        return mapToResponse(updatedRecord);
    }

    @Override
    public AttendanceSummaryResponse getAttendanceSummary(Long sessionId) {

        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Attendance session not found with id: " + sessionId));

        List<Student> allStudentsInClass = studentRepository.findByClassSection(session.getClassSection());

        List<AttendanceRecord> allRecords = attendanceRecordRepository.findBySessionId(sessionId);

        List<AttendanceRecord> presentRecords = allRecords.stream()
                .filter(record ->
                        record.getStatus() == AttendanceStatus.PRESENT
                                || record.getStatus() == AttendanceStatus.LATE
                )
                .toList();

        List<AttendanceRecord> absentRecords = allRecords.stream()
                .filter(record -> record.getStatus() == AttendanceStatus.ABSENT)
                .toList();

        List<AttendanceRecord> lateRecords = allRecords.stream()
                .filter(record -> record.getStatus() == AttendanceStatus.LATE)
                .toList();

        List<AttendanceRecordResponse> presentStudentResponses = presentRecords.stream()
                .map(this::mapToResponse)
                .toList();

        List<StudentResponse> absentStudentResponses;

        if (!absentRecords.isEmpty()) {
            absentStudentResponses = absentRecords.stream()
                    .map(record -> mapStudentToResponse(record.getStudent()))
                    .toList();
        } else {
            Set<Long> presentStudentIds = presentRecords.stream()
                    .map(record -> record.getStudent().getId())
                    .collect(Collectors.toSet());

            absentStudentResponses = allStudentsInClass.stream()
                    .filter(student -> !presentStudentIds.contains(student.getId()))
                    .map(this::mapStudentToResponse)
                    .toList();
        }



        return AttendanceSummaryResponse.builder()
                .sessionId(session.getId())
                .classSection(session.getClassSection())
                .subjectName(session.getSubjectName())
                .totalStudents(allStudentsInClass.size())
                .presentCount(presentStudentResponses.size())
                .absentCount(absentStudentResponses.size())
                .presentStudents(presentStudentResponses)
                .absentStudents(absentStudentResponses)
                .lateCount(lateRecords.size())
                .build();
    }

    private AttendanceRecordResponse mapToResponse(AttendanceRecord record) {
        Student student = record.getStudent();

        return AttendanceRecordResponse.builder()
                .id(record.getId())
                .sessionId(record.getSession().getId())
                .studentId(student.getId())
                .studentCode(student.getStudentCode())
                .studentName(student.getName())
                .classSection(student.getClassSection())
                .status(record.getStatus())
                .checkInTime(record.getCheckInTime())
                .confidenceScore(record.getConfidenceScore())
                .markedBy(record.getMarkedBy())
                .correctedBy(record.getCorrectedBy())
                .correctionReason(record.getCorrectionReason())
                .correctedAt(record.getCorrectedAt())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }

    private StudentResponse mapStudentToResponse(Student student) {
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

    private AttendanceStatus calculateAttendanceStatus(AttendanceSession session, LocalTime checkInTime) {

        Integer lateAfterMinutes = session.getLateAfterMinutes();

        if (lateAfterMinutes == null) {
            lateAfterMinutes = 10;
        }

        LocalTime lateLimitTime = session.getStartTime().plusMinutes(lateAfterMinutes);

        if (checkInTime.isAfter(lateLimitTime)) {
            return AttendanceStatus.LATE;
        }

        return AttendanceStatus.PRESENT;
    }

}
