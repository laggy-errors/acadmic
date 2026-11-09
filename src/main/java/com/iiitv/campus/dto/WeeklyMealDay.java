package com.iiitv.campus.dto;

public record WeeklyMealDay(
        String day,
        String date,
        boolean isToday,
        String breakfast,
        String lunch,
        String snacks,
        String dinner,
        String specialItem
) {}
