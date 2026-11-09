package com.iiitv.campus.dto;

public record Remark(
        String id,
        String courseCode,
        String courseName,
        String facultyName,
        String facultyRole,
        String date,
        String type, // COMMENDATION, ADVISORY, MENTOR_NOTE
        String typeLabel, // e.g. "SAFE · COMMENDATION", "WATCH · ADVISORY"
        String title,
        String content,
        String actionItem,
        boolean isAcknowledged,
        String acknowledgedDate
) {}
