package com.iiitv.campus.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventDateBadge extends Div {

    private static final Pattern DAY_PATTERN = Pattern.compile("\\b(\\d{1,2})\\b");
    private static final Pattern MONTH_PATTERN = Pattern.compile("(?i)\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\b");
    private static final Pattern WEEKDAY_PATTERN = Pattern.compile("(?i)\\b(Mon|Tue|Wed|Thu|Fri|Sat|Sun)\\b");

    public EventDateBadge(String rawDate, boolean isToday) {
        addClassNames("neo-event-date-badge", "font-mono");

        String weekday = isToday ? "TODAY" : "EVENT";
        String day = "10";
        String month = "NOV";

        if (rawDate != null && !rawDate.isBlank()) {
            if (rawDate.toLowerCase().contains("today")) {
                weekday = "TODAY";
            } else {
                Matcher wm = WEEKDAY_PATTERN.matcher(rawDate);
                if (wm.find()) {
                    weekday = wm.group(1).toUpperCase();
                }
            }

            Matcher dm = DAY_PATTERN.matcher(rawDate);
            if (dm.find()) {
                day = dm.group(1);
            }

            Matcher mm = MONTH_PATTERN.matcher(rawDate);
            if (mm.find()) {
                month = mm.group(1).toUpperCase();
            }
        }

        Span weekdaySpan = new Span(weekday);
        weekdaySpan.addClassNames("neo-badge-weekday", "font-mono");

        Span daySpan = new Span(day);
        daySpan.addClassNames("neo-badge-day", "font-mono");

        Span monthSpan = new Span(month);
        monthSpan.addClassNames("neo-badge-month", "font-mono");

        add(weekdaySpan, daySpan, monthSpan);
    }
}
