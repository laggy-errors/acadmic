package com.iiitv.campus.dto;

import java.util.List;

public record DashboardData(
        StudentInfo student,
        String dateString,
        String weekString,
        AttendanceSummary attendanceSummary,
        List<ClassOccurrence> todaySchedule,
        List<Task> pendingTasks,
        int pendingAssignmentsCount,
        int upcomingClassesCount,
        List<Announcement> announcements,
        List<Classroom> classrooms,
        List<Event> upcomingEvents,
        MealMenu todayMealMenu
) {}
