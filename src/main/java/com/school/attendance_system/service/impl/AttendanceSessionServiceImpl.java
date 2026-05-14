package com.school.attendance_system.service.impl;

import com.school.attendance_system.dto.request.AttendanceSessionRequest;
import com.school.attendance_system.dto.response.AttendanceSessionResponse;
import com.school.attendance_system.entity.AttendanceRecord;
import com.school.attendance_system.entity.AttendanceSession;
import com.school.attendance_system.entity.Student;
import com.school.attendance_system.enums.AttendanceStatus;
import com.school.attendance_system.enums.SessionStatus;
import com.school.attendance_system.repository.AttendanceRecordRepository;
import com.school.attendance_system.repository.AttendanceSessionRepository;
import com.school.attendance_system.repository.StudentRepository;
import com.school.attendance_system.service.AttendanceSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceSessionServiceImpl implements AttendanceSessionService {
    private final AttendanceSessionRepository attendanceSessionRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @Override
    public AttendanceSessionResponse startSession(AttendanceSessionRequest request) {

        AttendanceSession session = AttendanceSession.builder()
                .classSection(request.getClassSection())
                .subjectName(request.getSubjectName())
                .teacherId(request.getTeacherId())
                .sessionDate(LocalDate.now())
                .startTime(LocalTime.now())
                .lateAfterMinutes(
                        request.getLateAfterMinutes() != null
                                ? request.getLateAfterMinutes()
                                : 10
                )
                .status(SessionStatus.ACTIVE)
                .build();

        AttendanceSession savedSession = attendanceSessionRepository.save(session);

        return mapToResponse(savedSession);
    }

    @Override
    public AttendanceSessionResponse finishSession(Long sessionId) {

        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Attendance session not found with id: " + sessionId));

        if (session.getStatus() == SessionStatus.FINISHED) {
            throw new RuntimeException("Attendance session is already finished");
        }

        session.setEndTime(LocalTime.now());
        session.setStatus(SessionStatus.FINISHED);

        AttendanceSession updatedSession = attendanceSessionRepository.save(session);

        return mapToResponse(updatedSession);
    }

    @Override
    public List<AttendanceSessionResponse> getAllSessions() {
        return attendanceSessionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AttendanceSessionResponse getSessionById(Long id) {
        AttendanceSession session = attendanceSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance session not found with id: " + id));

        return mapToResponse(session);
    }

    @Override
    public List<AttendanceSessionResponse> getActiveSessions() {
        return attendanceSessionRepository.findByStatus(SessionStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void saveAbsentStudents(AttendanceSession session){
        List<Student> studentsInClass = studentRepository.findByClassSection(session.getClassSection());

        for(Student student : studentsInClass){
            boolean alreadyMarked = attendanceRecordRepository
                    .existsBySessionIdAndStudentId(session.getId(), student.getId());

            if(!alreadyMarked){
                AttendanceRecord absentRecord = AttendanceRecord.builder()
                        .session(session)
                        .student(student)
                        .status(AttendanceStatus.ABSENT)
                        .checkInTime(null)
                        .confidenceScore(null)
                        .markedBy("SYSTEM")
                        .build();
                attendanceRecordRepository.save(absentRecord);
            }
        }
    }

    private AttendanceSessionResponse mapToResponse(AttendanceSession session) {
        return AttendanceSessionResponse.builder()
                .id(session.getId())
                .classSection(session.getClassSection())
                .subjectName(session.getSubjectName())
                .teacherId(session.getTeacherId())
                .sessionDate(session.getSessionDate())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .lateAfterMinutes(session.getLateAfterMinutes())
                .status(session.getStatus())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }
}
