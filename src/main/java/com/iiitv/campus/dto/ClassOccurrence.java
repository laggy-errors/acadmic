package com.iiitv.campus.dto;

public record ClassOccurrence(
        String id,
        String courseCode,
        String courseName,
        String room,
        String instructor,
        String dayOfWeek,
        String startTime,
        String endTime,
        String type, // Lecture, Lab, Tutorial, Break
        boolean isNextUp,
        String nextUpBadge, // e.g. "in 30 min", "NOW 14:35"
        boolean isCancelled,
        String cancellationReason,
        boolean isExtra,
        String branch
) {
    public ClassOccurrence(
            String id,
            String courseCode,
            String courseName,
            String room,
            String instructor,
            String dayOfWeek,
            String startTime,
            String endTime,
            String type,
            boolean isNextUp,
            String nextUpBadge
    ) {
        this(id, courseCode, courseName, room, instructor, dayOfWeek, startTime, endTime, type, isNextUp, nextUpBadge, false, null, false, null);
    }

    public ClassOccurrence(
            String id,
            String courseCode,
            String courseName,
            String room,
            String instructor,
            String dayOfWeek,
            String startTime,
            String endTime,
            String type,
            boolean isNextUp,
            String nextUpBadge,
            String branch
    ) {
        this(id, courseCode, courseName, room, instructor, dayOfWeek, startTime, endTime, type, isNextUp, nextUpBadge, false, null, false, branch);
    }

    public ClassOccurrence(
            String id,
            String courseCode,
            String courseName,
            String room,
            String instructor,
            String dayOfWeek,
            String startTime,
            String endTime,
            String type,
            boolean isNextUp,
            String nextUpBadge,
            boolean isCancelled,
            String cancellationReason,
            boolean isExtra
    ) {
        this(id, courseCode, courseName, room, instructor, dayOfWeek, startTime, endTime, type, isNextUp, nextUpBadge, isCancelled, cancellationReason, isExtra, null);
    }

    public String displayCode() {
        if ("BREAK".equalsIgnoreCase(courseCode)) {
            return courseCode;
        }
        if (courseCode.contains("(") && courseCode.contains(")")) {
            return courseCode;
        }
        if (branch != null && !branch.isBlank()) {
            return courseCode + " (" + branch + ")";
        }
        if (courseCode.startsWith("CS3") || courseCode.startsWith("CS1") || courseCode.startsWith("CS2")) {
            return courseCode + " (CSE)";
        } else if (courseCode.startsWith("EC")) {
            return courseCode + " (ECE)";
        } else if (courseCode.startsWith("AI")) {
            return courseCode + " (AI)";
        } else if (courseCode.startsWith("IT")) {
            return courseCode + " (IT)";
        } else if (courseCode.startsWith("MS") || courseCode.startsWith("CS5") || courseCode.startsWith("HS5")) {
            return courseCode + " (MCA)";
        } else if (courseCode.startsWith("CS6") || courseCode.startsWith("MA6")) {
            return courseCode + " (M.Tech)";
        }
        return courseCode;
    }
}
