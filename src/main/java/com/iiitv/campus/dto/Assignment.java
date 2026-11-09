package com.iiitv.campus.dto;

public record Assignment(
        String id,
        String title,
        String courseCode,
        String dueDate,
        String statusBadge, // e.g. "5 days left", "Past due", "Graded"
        String score,       // e.g. "18 / 20", or null
        boolean isNextUp,
        AssignmentGroup group,
        String description,
        String attachmentName,
        String attachmentSize,
        String feedback,
        int maxPoints,
        boolean submitted,
        String submittedFileName,
        String submittedAt
) {
    public enum AssignmentGroup {
        UPCOMING,
        PAST_DUE,
        DONE
    }

    public Assignment(String id, String title, String courseCode, String dueDate, String statusBadge, String score, boolean isNextUp) {
        this(id, title, courseCode, dueDate, statusBadge, score, isNextUp,
                score != null ? AssignmentGroup.DONE : (isNextUp ? AssignmentGroup.UPCOMING : AssignmentGroup.DONE),
                "Implement assignment requirements adhering to standard algorithmic complexity and submission specifications.",
                title.contains("2") ? "Assignment_2_Trees.pdf" : "Assignment_1_Arrays.pdf",
                title.contains("2") ? "452 KB" : "380 KB",
                score != null ? "Excellent implementation of balanced tree rotations. Unit tests passed all edge cases." : null,
                20,
                score != null,
                score != null ? title.replace(" ", "_") + "_submission.pdf" : null,
                score != null ? "28 Oct 2026, 21:30" : null
        );
    }
}
