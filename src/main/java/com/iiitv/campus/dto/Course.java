package com.iiitv.campus.dto;

public record Course(
        String code,
        String name,
        String section,
        String instructor,
        String room,
        int credits
) {}
