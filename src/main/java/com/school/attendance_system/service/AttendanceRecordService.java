package com.school.attendance_system.service;

import com.school.attendance_system.dto.request.ManualAttendanceRequest;
import com.school.attendance_system.dto.response.AttendanceRecordResponse;
import com.school.attendance_system.dto.response.AttendanceSummaryResponse;

import java.util.List;

public interface AttendanceRecordService {
    AttendanceRecordResponse markManualAttendance(ManualAttendanceRequest request);

    List<AttendanceRecordResponse> getAttendanceBySession(Long sessionId);

    List<AttendanceRecordResponse> getAttendanceByStudent(Long studentId);

    AttendanceSummaryResponse getAttendanceSummary(Long sessionId);
}
