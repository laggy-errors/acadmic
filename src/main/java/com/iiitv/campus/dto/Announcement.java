package com.iiitv.campus.dto;

public record Announcement(
        String id,
        String title,
        String category, // Academic, Exams, Administrative, Placement, Events
        String date,
        String publisher,
        String summary,
        boolean isPinned,
        boolean isRead,
        String attachmentName,
        String attachmentSize
) {
    // Backwards-compatible constructor for 5-argument callers
    public Announcement(String id, String title, String category, String date, String summary) {
        this(id, title, category, date, "Academic Affairs Office", summary, false, false, null, null);
    }
}
