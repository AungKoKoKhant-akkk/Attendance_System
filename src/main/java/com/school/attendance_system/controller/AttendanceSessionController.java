package com.school.attendance_system.controller;

import com.school.attendance_system.dto.request.AttendanceSessionRequest;
import com.school.attendance_system.dto.response.AttendanceSessionResponse;
import com.school.attendance_system.service.AttendanceSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class AttendanceSessionController {
    private final AttendanceSessionService attendanceSessionService;

    @PostMapping("/start")
    public AttendanceSessionResponse startSession(@Valid @RequestBody AttendanceSessionRequest request) {
        return attendanceSessionService.startSession(request);
    }

    @PostMapping("/{id}/finish")
    public AttendanceSessionResponse finishSession(@PathVariable Long id) {
        return attendanceSessionService.finishSession(id);
    }

    @GetMapping
    public List<AttendanceSessionResponse> getAllSessions() {
        return attendanceSessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public AttendanceSessionResponse getSessionById(@PathVariable Long id) {
        return attendanceSessionService.getSessionById(id);
    }

    @GetMapping("/active")
    public List<AttendanceSessionResponse> getActiveSessions() {
        return attendanceSessionService.getActiveSessions();
    }
}
