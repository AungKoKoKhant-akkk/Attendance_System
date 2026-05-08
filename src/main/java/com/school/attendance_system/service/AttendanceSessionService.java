package com.school.attendance_system.service;

import com.school.attendance_system.dto.request.AttendanceSessionRequest;
import com.school.attendance_system.dto.response.AttendanceSessionResponse;

import java.util.List;

public interface AttendanceSessionService {
    AttendanceSessionResponse startSession(AttendanceSessionRequest request);

    AttendanceSessionResponse finishSession(Long sessionId);

    List<AttendanceSessionResponse> getAllSessions();

    AttendanceSessionResponse getSessionById(Long id);

    List<AttendanceSessionResponse> getActiveSessions();
}
