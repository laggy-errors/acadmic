package com.iiitv.campus.dto;

public record MealRating(
        String mealName,
        String date,
        int rating, // 1 to 5
        String category,
        String comment
) {}
