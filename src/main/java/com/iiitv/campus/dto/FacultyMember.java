package com.iiitv.campus.dto;

import java.util.List;

public record FacultyMember(
        String id,
        String name,
        String designation,
        String department,
        String office,
        String email,
        String phone,
        String campusMode, // ICD, GN-Online, GN-Hybrid, External-Online
        String officeHours,
        String liveStatusBadge,
        String liveStatusType, // IN_CLASS, AVAILABLE, OFFICE_HOURS
        ClassOccurrence nextClass,
        List<ClassOccurrence> weeklySchedule,
        List<String> coursesTaught,
        String researchAreas
) {
    public FacultyMember(
            String id,
            String name,
            String designation,
            String department,
            String office,
            String email,
            String officeHours,
            String liveStatusBadge,
            String liveStatusType,
            ClassOccurrence nextClass,
            List<ClassOccurrence> weeklySchedule,
            List<String> coursesTaught,
            String researchAreas
    ) {
        this(id, name, designation, department, office, email, "N/A", "ICD", officeHours, liveStatusBadge, liveStatusType, nextClass, weeklySchedule, coursesTaught, researchAreas);
    }
}
