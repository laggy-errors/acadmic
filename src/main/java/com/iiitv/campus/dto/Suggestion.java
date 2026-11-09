package com.iiitv.campus.dto;

public record Suggestion(
        String id,
        String title,
        String category,
        String description,
        String author,
        boolean isAnonymous,
        String date,
        int upvotes,
        boolean hasUpvoted,
        String status, // UNDER_REVIEW, ACCEPTED, RESOLVED, PLANNED
        String statusLabel, // "WATCH · UNDER REVIEW", "SAFE · ACCEPTED", "SAFE · RESOLVED", "PLANNED"
        String officialResponse
) {}
