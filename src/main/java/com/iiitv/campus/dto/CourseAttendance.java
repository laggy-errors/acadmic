package com.iiitv.campus.dto;

public record CourseAttendance(
        String courseCode,
        String courseName,
        int total,
        int present,
        int absent,
        int excused,
        double percentage,
        AttendanceStatus status
) {}
