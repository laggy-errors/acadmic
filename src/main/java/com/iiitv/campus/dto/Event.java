package com.iiitv.campus.dto;

public record Event(
        String id,
        String title,
        String category,
        String date,           // e.g. "Today, 10 Nov 2026"
        String dateRange,      // e.g. "10 Nov 2026"
        String timeRange,      // e.g. "14:00 – 16:30"
        String location,       // e.g. "Auditorium A-101"
        String organizer,      // e.g. "Coding Club"
        String description,
        Timeframe timeframe,   // TODAY, THIS_WEEK, NEXT_30_DAYS
        boolean hasTimetableClash,
        String clashDetails,   // e.g. "Clashes with CS302 Operating Systems (14:00 – 15:00, Room B-102)"
        boolean isRsvp,
        int rsvpCount,
        int capacity
) {
    public enum Timeframe {
        TODAY,
        THIS_WEEK,
        NEXT_30_DAYS
    }

    // Backwards-compatible constructor for 5-parameter calls
    public Event(String id, String title, String date, String dateRange, String location) {
        this(id, title, "Symposium", date, dateRange, "10:00 – 17:00", location, "Student Affairs",
                "Campus event and technical sessions.", Timeframe.THIS_WEEK, false, null, false, 85, 200);
    }
}
