package com.iiitv.campus.dto;

import java.util.List;

public record WeeklyAttendanceRecord(
        String courseCode,
        String courseName,
        List<DayAttendanceMark> dailyMarks,
        int weekTotal,
        int weekAttended,
        int weekAbsent
) {}
