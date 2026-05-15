package com.school.attendance_system.controller;

import com.school.attendance_system.dto.request.AttendanceCorrectionRequest;
import com.school.attendance_system.dto.request.ManualAttendanceRequest;
import com.school.attendance_system.dto.response.AttendanceRecordResponse;
import com.school.attendance_system.dto.response.AttendanceSummaryResponse;
import com.school.attendance_system.service.AttendanceRecordService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceRecordController {

    private final AttendanceRecordService attendanceRecordService;

    @PostMapping("/mark")
    public AttendanceRecordResponse markManualAttendance(
            @Valid @RequestBody ManualAttendanceRequest request
    ) {
        return attendanceRecordService.markManualAttendance(request);
    }

    @GetMapping("/session/{sessionId}")
    public List<AttendanceRecordResponse> getAttendanceBySession(
            @PathVariable Long sessionId
    ) {
        return attendanceRecordService.getAttendanceBySession(sessionId);
    }

    @GetMapping("/session/{sessionId}/summary")
    public AttendanceSummaryResponse getAttendanceSummary(
            @PathVariable Long sessionId
    ) {
        return attendanceRecordService.getAttendanceSummary(sessionId);
    }

    @GetMapping("/student/{studentId}")
    public List<AttendanceRecordResponse> getAttendanceByStudent(
            @PathVariable Long studentId
    ) {
        return attendanceRecordService.getAttendanceByStudent(studentId);
    }

    @PutMapping("/{recordId}/correct")
    public AttendanceRecordResponse correctAttendance(@PathVariable Long recordId, @Valid @RequestBody AttendanceCorrectionRequest request){
        return attendanceRecordService.correctAttendance(recordId, request);
    }
}