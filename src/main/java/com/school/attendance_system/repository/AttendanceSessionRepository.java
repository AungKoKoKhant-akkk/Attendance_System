package com.school.attendance_system.repository;

import com.school.attendance_system.entity.AttendanceSession;
import com.school.attendance_system.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    List<AttendanceSession> findByStatus(SessionStatus status);

    List<AttendanceSession> findByClassSection(String classSection);
}
