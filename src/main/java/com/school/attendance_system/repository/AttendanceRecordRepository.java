package com.school.attendance_system.repository;

import com.school.attendance_system.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord,Long> {
    boolean existsBySession_IdAndStudent_Id(Long sessionId, Long studentId);

    Optional<AttendanceRecord> findBySession_IdAndStudent_Id(Long sessionId, Long studentId);

    List<AttendanceRecord> findBySessionId(Long sessionId);

    List<AttendanceRecord> findByStudentId(Long studentId);
}

