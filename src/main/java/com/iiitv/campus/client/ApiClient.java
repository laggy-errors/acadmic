package com.iiitv.campus.client;

import com.iiitv.campus.dto.*;
import java.util.List;

public interface ApiClient {
    StudentInfo getStudentProfile();
    AttendanceSummary getAttendanceSummary();
    List<CourseAttendance> getCourseAttendanceList();
    List<ClassOccurrence> getTodaySchedule();
    List<ClassOccurrence> getWeeklyTimetable(String weekIdentifier);
    List<Task> getPendingTasks();
    List<Announcement> getAnnouncements();
    void markAllAnnouncementsRead();
    void markAnnouncementRead(String id);
    List<Event> getUpcomingEvents();
    List<Event> getEventsByTimeframe(Event.Timeframe timeframe);
    boolean toggleEventRsvp(String eventId);
    MealMenu getTodayMealMenu();
    List<WeeklyMealDay> getWeeklyMealMenu();
    default List<WeeklyMealDay> getWeeklyMealMenu(String weekIdentifier) {
        return getWeeklyMealMenu();
    }
    boolean submitMealRating(MealRating rating);
    List<Suggestion> getSuggestions();
    boolean toggleSuggestionUpvote(String suggestionId);
    Suggestion createSuggestion(String title, String category, String description, boolean isAnonymous);
    List<Classroom> getClassrooms();
    List<StreamPost> getStreamPosts(String courseCode);
    List<Assignment> getAssignments(String courseCode);
    List<Course> getEnrolledCourses();
    List<WeeklyAttendanceRecord> getWeeklyAttendanceRegister(String semester, String weekIdentifier);
    boolean joinClassroom(String code);
    List<Remark> getFacultyRemarks();
    boolean acknowledgeRemark(String id);
    List<FacultyMember> getFacultyDirectory();
    FacultyMember getFacultyMember(String id);
    DashboardData getDashboardData();
}
