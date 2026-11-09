package com.iiitv.campus.dto;

public record StreamPost(
        String id,
        String author,
        String date,
        String postType, // ASSIGNMENT, MATERIAL, ANNOUNCEMENT
        String title,
        String content,
        String attachmentName,
        String attachmentSize,
        int likes,
        int commentsCount
) {}
