package com.iiitv.campus.dto;

public record MealMenu(
        String date,
        String mealType,
        boolean isCurrent,
        String items,
        String nextMealTime
) {}
