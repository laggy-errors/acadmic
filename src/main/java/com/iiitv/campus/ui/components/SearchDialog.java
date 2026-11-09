package com.iiitv.campus.ui.components;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.*;

public class SearchDialog extends Dialog {

    private final TextField searchField = new TextField();
    private final VerticalLayout resultsContainer = new VerticalLayout();

    private record SearchEntry(String category, String badge, String title, String subtitle, String route) {}

    private final List<SearchEntry> allEntries = List.of(
            // Courses
            new SearchEntry("COURSES", "CS301", "Data Structures", "Dr. Amit Mehta · ICD-2125 · Lab 3", "classrooms"),
            new SearchEntry("COURSES", "CS302", "Operating Systems", "Dr. Rajesh Iyer · ICD-2125 · Sec 1", "classrooms"),
            new SearchEntry("COURSES", "CS303", "Computer Networks", "Dr. Sneha Kulkarni · ICD-2125 · Sec 1", "classrooms"),
            new SearchEntry("COURSES", "CS304", "Database Management Systems", "Dr. Nilesh Patel · ICD-2113 · Sec 2", "classrooms"),

            // Faculty
            new SearchEntry("FACULTY", "FACULTY", "Dr. Amit Mehta", "Associate Professor & Head of CSE · ICD-2125", "faculty-directory"),
            new SearchEntry("FACULTY", "FACULTY", "Dr. Rajesh Iyer", "Assistant Professor (CSE) · ICD-2122", "faculty-directory"),
            new SearchEntry("FACULTY", "FACULTY", "Dr. Sneha Kulkarni", "Assistant Professor (CSE) · ICD-2124", "faculty-directory"),
            new SearchEntry("FACULTY", "FACULTY", "Dr. Nilesh Patel", "Assistant Professor (CSE) · ICD-2113", "faculty-directory"),

            // Events
            new SearchEntry("CAMPUS EVENTS", "EVENT", "Technical Symposium 2026", "Main Auditorium · 12 Nov · 09:30 AM", "events"),
            new SearchEntry("CAMPUS EVENTS", "EVENT", "Annual Inter-IIIT Sports Meet", "Campus Sports Complex · 18 Nov · 08:00 AM", "events"),
            new SearchEntry("CAMPUS EVENTS", "EVENT", "Open Source Hackathon 2026", "Lab 2 & Lab 3 · 22 Nov · 10:00 AM", "events"),

            // Announcements
            new SearchEntry("ANNOUNCEMENTS", "NOTICE", "Mid-Sem Examination Schedule Published", "Academic Office · 20 Nov · Timetable Released", "announcements"),
            new SearchEntry("ANNOUNCEMENTS", "NOTICE", "Library Return Amnesty Period", "Central Library · No late fees till 15 Nov", "announcements"),
            new SearchEntry("ANNOUNCEMENTS", "NOTICE", "Hostel Wi-Fi Maintenance Window", "IT Infrastructure · Sunday 02:00 - 05:00 AM", "announcements"),

            // Navigation
            new SearchEntry("NAVIGATION", "NAV", "My Timetable (Weekly Schedule)", "View lecture slots, lab timings & rooms", "timetable"),
            new SearchEntry("NAVIGATION", "NAV", "Attendance Guidance & Breakdown", "82.5% Safe status · 8 classes buffer", "attendance"),
            new SearchEntry("NAVIGATION", "NAV", "Tasks & Deliverables", "3 Pending assignments · 1 Critical urgency", "tasks"),
            new SearchEntry("NAVIGATION", "NAV", "Mess Menu & Nutrition", "Daily menu, timings and student reviews", "mess-menu"),
            new SearchEntry("NAVIGATION", "NAV", "Student Voice & Suggestions", "Submit proposals & vote on campus matters", "suggestions"),
            new SearchEntry("NAVIGATION", "NAV", "My Student Profile", "Academic record, mentor details, credentials", "profile")
    );

    public SearchDialog() {
        addClassNames("neo-command-palette-dialog", "neo-glass-dialog");
        setWidth("640px");
        setMaxWidth("94vw");
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);
        content.addClassName("neo-command-palette-content");

        // Command input bar
        HorizontalLayout searchBarLayout = new HorizontalLayout();
        searchBarLayout.setWidthFull();
        searchBarLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        searchBarLayout.addClassName("neo-command-search-bar-wrap");

        searchField.setWidthFull();
        searchField.setPlaceholder("Type a course, faculty name, event, notice or page...");
        searchField.setAriaLabel("Search courses, events, faculty, announcements, and navigation");
        searchField.setClearButtonVisible(true);
        searchField.addClassNames("neo-command-search-input", "font-ui");

        Span escBadge = new Span("ESC");
        escBadge.addClassNames("neo-command-esc-chip", "font-mono");

        searchBarLayout.add(searchField, escBadge);
        searchField.addValueChangeListener(e -> updateResults(e.getValue()));

        // Results Container
        resultsContainer.setPadding(false);
        resultsContainer.setSpacing(false);
        resultsContainer.setWidthFull();
        resultsContainer.addClassName("neo-command-results-scroll");

        populateDefaultSuggestions();

        // Footer hint
        HorizontalLayout footerHint = new HorizontalLayout();
        footerHint.setWidthFull();
        footerHint.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        footerHint.setAlignItems(FlexComponent.Alignment.CENTER);
        footerHint.addClassName("neo-command-palette-footer");

        Paragraph hintLeft = new Paragraph("Tip: Use ↑ ↓ to navigate, ↵ to open destination");
        hintLeft.addClassNames("neo-command-hint-text", "font-ui");

        Paragraph hintRight = new Paragraph("Global Search · Ctrl+K");
        hintRight.addClassNames("neo-command-shortcut-hint", "font-mono");

        footerHint.add(hintLeft, hintRight);

        content.add(searchBarLayout, resultsContainer, footerHint);
        add(content);

        addOpenedChangeListener(e -> {
            if (e.isOpened()) {
                searchField.clear();
                populateDefaultSuggestions();
                searchField.focus();
            }
        });
    }

    private void populateDefaultSuggestions() {
        resultsContainer.removeAll();
        renderGroupedEntries(allEntries);
    }

    private void updateResults(String query) {
        resultsContainer.removeAll();
        if (query == null || query.isBlank()) {
            populateDefaultSuggestions();
            return;
        }

        String q = query.trim().toLowerCase();
        List<SearchEntry> matched = new ArrayList<>();

        for (SearchEntry entry : allEntries) {
            if (entry.badge().toLowerCase().contains(q)
                    || entry.title().toLowerCase().contains(q)
                    || entry.subtitle().toLowerCase().contains(q)
                    || entry.category().toLowerCase().contains(q)) {
                matched.add(entry);
            }
        }

        if (matched.isEmpty()) {
            Div empty = new Div();
            empty.addClassNames("neo-command-no-results", "font-ui");

            H4 emptyTitle = new H4("No results found");
            emptyTitle.addClassNames("font-ui", "neo-command-empty-title");

            Paragraph emptyMsg = new Paragraph("No matching items found for \"" + query + "\". Try searching for CS301, Mehta, Symposium, Timetable, or Attendance.");
            emptyMsg.addClassNames("font-ui", "neo-command-empty-desc");

            empty.add(emptyTitle, emptyMsg);
            resultsContainer.add(empty);
        } else {
            renderGroupedEntries(matched);
        }
    }

    private void renderGroupedEntries(List<SearchEntry> entries) {
        // Group entries preserving category order
        Map<String, List<SearchEntry>> grouped = new LinkedHashMap<>();
        for (SearchEntry entry : entries) {
            grouped.computeIfAbsent(entry.category(), k -> new ArrayList<>()).add(entry);
        }

        for (Map.Entry<String, List<SearchEntry>> group : grouped.entrySet()) {
            Div groupHeader = new Div();
            groupHeader.addClassNames("neo-command-group-header", "font-mono");

            Span groupTitle = new Span(group.getKey());
            Span countBadge = new Span(String.valueOf(group.getValue().size()));
            countBadge.addClassNames("neo-command-group-count", "font-mono");

            groupHeader.add(groupTitle, countBadge);
            resultsContainer.add(groupHeader);

            for (SearchEntry entry : group.getValue()) {
                resultsContainer.add(createResultItem(entry));
            }
        }
    }

    private Div createResultItem(SearchEntry entry) {
        Div row = new Div();
        row.addClassName("neo-command-item");
        row.getElement().setAttribute("role", "button");
        row.getElement().setAttribute("tabindex", "0");

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        HorizontalLayout left = new HorizontalLayout();
        left.setAlignItems(FlexComponent.Alignment.CENTER);
        left.setSpacing(true);

        Span badgeSpan = new Span(entry.badge());
        badgeSpan.addClassNames("font-mono", "neo-command-badge", getBadgeClass(entry.category()));

        Div textGroup = new Div();
        Span titleSpan = new Span(entry.title());
        titleSpan.addClassNames("neo-command-title", "font-ui");

        Paragraph subSpan = new Paragraph(entry.subtitle());
        subSpan.addClassNames("neo-command-sub", "font-ui");

        textGroup.add(titleSpan, subSpan);
        left.add(badgeSpan, textGroup);

        Span enterHint = new Span("↵");
        enterHint.addClassNames("neo-command-enter-icon", "font-mono");

        layout.add(left, enterHint);
        row.add(layout);

        row.addClickListener(e -> {
            close();
            getUI().ifPresent(ui -> ui.navigate(entry.route()));
        });

        return row;
    }

    private String getBadgeClass(String category) {
        return switch (category) {
            case "COURSES" -> "neo-command-badge-sky";
            case "FACULTY" -> "neo-command-badge-violet";
            case "CAMPUS EVENTS" -> "neo-command-badge-amber";
            case "ANNOUNCEMENTS" -> "neo-command-badge-rose";
            default -> "neo-command-badge-mint";
        };
    }
}
