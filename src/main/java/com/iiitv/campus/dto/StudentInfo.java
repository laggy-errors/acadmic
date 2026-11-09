package com.iiitv.campus.dto;

public record StudentInfo(
        String name,
        String program,
        String semester,
        String section,
        String rollNumber,
        int unreadNotifications
) {}
