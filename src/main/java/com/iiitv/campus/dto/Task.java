package com.iiitv.campus.dto;

import java.util.List;

public record Task(
        String id,
        String title,
        String courseCode,
        String dueDate,
        String estimatedHours,
        String priority, // CRITICAL, HIGH, NORMAL
        boolean completed,
        int rank,
        String notes,
        List<String> subtasks
) {
    public Task(String id, String title, String courseCode, String dueDate, String estimatedHours, String priority, boolean completed) {
        this(id, title, courseCode, dueDate, estimatedHours, priority, completed, 1,
                "Follow course guidelines and rubric specifications in Classroom.",
                List.of(
                        "Review problem statement and constraints",
                        "Implement core algorithm / logic",
                        "Run test cases against edge conditions",
                        "Prepare final submission deliverables"
                )
        );
    }
}
