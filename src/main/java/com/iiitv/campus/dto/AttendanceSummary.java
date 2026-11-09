package com.iiitv.campus.dto;

public record AttendanceSummary(
        int totalClasses,
        int attendedClasses,
        int absentClasses,
        int excusedClasses,
        double percentage,
        double threshold,
        AttendanceStatus status,
        int classesCanMiss,
        int classesToAttendForTarget,
        double targetPercentage,
        double projectedPercentage
) {}
