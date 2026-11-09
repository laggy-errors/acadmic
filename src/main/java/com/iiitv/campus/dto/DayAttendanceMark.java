package com.iiitv.campus.dto;

public record DayAttendanceMark(
        String dayOfWeek,
        String date,
        String mark,
        String tooltip
) {}
