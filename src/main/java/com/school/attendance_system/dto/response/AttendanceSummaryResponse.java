package com.school.attendance_system.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AttendanceSummaryResponse {
    private Long sessionId;
    private String classSection;
    private String subjectName;
    private int totalStudents;
    private int presentCount;
    private int absentCount;
    private int lateCount;
    private List<AttendanceRecordResponse> presentStudents;
    private List<StudentResponse> absentStudents;

}
