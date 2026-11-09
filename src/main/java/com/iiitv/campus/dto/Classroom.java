package com.iiitv.campus.dto;

public record Classroom(
        String id,
        String courseCode,
        String courseName,
        String teacher,
        String subtitle,
        String newMaterialsTag,
        String assignmentDueTag,
        int dueSoonCount
) {
    public Classroom(String id, String courseCode, String courseName, String subtitle, String newMaterialsTag, String assignmentDueTag) {
        this(id, courseCode, courseName, extractTeacher(subtitle), subtitle, newMaterialsTag, assignmentDueTag,
                assignmentDueTag != null && assignmentDueTag.contains("due") ? 1 : ("CS301".equals(courseCode) ? 1 : 0));
    }

    private static String extractTeacher(String subtitle) {
        if (subtitle != null && subtitle.contains("·")) {
            String[] parts = subtitle.split("·");
            return parts[parts.length - 1].trim();
        }
        return "Faculty";
    }
}
