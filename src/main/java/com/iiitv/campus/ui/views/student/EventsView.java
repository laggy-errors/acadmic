package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.Event;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "events", layout = MainLayout.class)
@PageTitle("Campus Events - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class EventsView extends VerticalLayout {

    private final ApiClient apiClient;
    private final Div grid = new Div();
    private Event.Timeframe selectedTimeframe = null; // null means ALL
    private String selectedCategory = "ALL";
    private String searchQuery = "";

    @Autowired
    public EventsView(ApiClient apiClient) {
        this.apiClient = apiClient;

        addClassNames("view-container", "neo-events-view");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        // Search Field
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search events by title, venue, or club...");
        searchField.setAriaLabel("Search events by title, venue, or club");
        searchField.setWidth("320px");
        searchField.addClassNames("neo-inset-input", "neo-events-search", "font-ui");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        PageHeader header = new PageHeader(
                "Campus Events",
                "Workshops, symposiums, hackathons, and extracurricular calendar",
                searchField
        );
        header.addClassName("neo-events-header");

        // Timeframe filter tabs: All, Today, This Week, Next 30 Days
        HorizontalLayout timeframeBar = new HorizontalLayout();
        timeframeBar.setSpacing(true);
        timeframeBar.addClassNames("tab-group", "neo-dept-filter-bar", "neo-events-filter-bar");

        Button tabAll = new Button("All Events");
        tabAll.addClassNames("btn-tab", "neo-dept-pill", "font-ui", "tab-active", "neo-dept-pill-active");
        Button tabToday = new Button("Today");
        tabToday.addClassNames("btn-tab", "neo-dept-pill", "font-ui");
        Button tabWeek = new Button("This Week");
        tabWeek.addClassNames("btn-tab", "neo-dept-pill", "font-ui");
        Button tabMonth = new Button("Next 30 Days");
        tabMonth.addClassNames("btn-tab", "neo-dept-pill", "font-ui");

        tabAll.addClickListener(e -> {
            setActiveTab(tabAll, tabToday, tabWeek, tabMonth);
            selectedTimeframe = null;
            refreshEvents();
        });

        tabToday.addClickListener(e -> {
            setActiveTab(tabToday, tabAll, tabWeek, tabMonth);
            selectedTimeframe = Event.Timeframe.TODAY;
            refreshEvents();
        });

        tabWeek.addClickListener(e -> {
            setActiveTab(tabWeek, tabAll, tabToday, tabMonth);
            selectedTimeframe = Event.Timeframe.THIS_WEEK;
            refreshEvents();
        });

        tabMonth.addClickListener(e -> {
            setActiveTab(tabMonth, tabAll, tabToday, tabWeek);
            selectedTimeframe = Event.Timeframe.NEXT_30_DAYS;
            refreshEvents();
        });

        timeframeBar.add(tabAll, tabToday, tabWeek, tabMonth);

        searchField.addValueChangeListener(e -> {
            searchQuery = e.getValue();
            refreshEvents();
        });

        grid.addClassNames("events-grid", "neo-events-grid");
        refreshEvents();

        // Guidelines card at bottom
        Div guidelinesCard = new Div();
        guidelinesCard.addClassNames("campus-card", "stream-card", "neo-glass-panel", "neo-event-guidelines-card");

        H3 guideTitle = new H3("Event Guidelines & Venue Reservations");
        guideTitle.addClassNames("font-ui", "text-navy", "margin-none", "neo-card-title");

        Paragraph guideText = new Paragraph("Registered student bodies and technical clubs can reserve the Auditorium, Seminar Halls, and Labs via the Student Affairs Office at least 14 days in advance. Attendance at official academic symposiums is counted towards institutional extracurricular credits.");
        guideText.addClassNames("font-ui", "text-muted", "neo-guidelines-text");

        guidelinesCard.add(guideTitle, guideText);

        add(header, timeframeBar, grid, guidelinesCard);
    }

    private void setActiveTab(Button active, Button... others) {
        active.addClassNames("tab-active", "neo-dept-pill-active");
        for (Button b : others) {
            b.removeClassName("tab-active");
            b.removeClassName("neo-dept-pill-active");
        }
    }

    private void refreshEvents() {
        grid.removeAll();

        List<Event> events = apiClient.getUpcomingEvents();

        List<Event> filtered = events.stream()
                .filter(e -> selectedTimeframe == null || e.timeframe() == selectedTimeframe)
                .filter(e -> "ALL".equalsIgnoreCase(selectedCategory) || e.category().equalsIgnoreCase(selectedCategory))
                .filter(e -> {
                    if (searchQuery == null || searchQuery.isBlank()) return true;
                    String q = searchQuery.toLowerCase().trim();
                    return e.title().toLowerCase().contains(q)
                            || e.location().toLowerCase().contains(q)
                            || e.organizer().toLowerCase().contains(q)
                            || e.category().toLowerCase().contains(q)
                            || e.description().toLowerCase().contains(q);
                })
                .toList();

        if (filtered.isEmpty()) {
            Div empty = new Div();
            empty.addClassNames("campus-card", "empty-state-card", "neo-glass-panel");
            Paragraph p = new Paragraph("No campus events scheduled for the selected filter.");
            p.addClassNames("font-ui", "text-muted");
            empty.add(p);
            grid.add(empty);
            return;
        }

        for (Event event : filtered) {
            grid.add(createEventCard(event));
        }
    }

    private Div createEventCard(Event event) {
        Div card = new Div();
        card.addClassNames("event-card", "neo-glass-card", "neo-event-card");

        // Top Row: Category chip & Timeframe chip
        Div topRow = new Div();
        topRow.addClassNames("event-card-top", "neo-event-top-row");

        Div titleGroup = new Div();
        titleGroup.addClassNames("event-title-group", "neo-event-title-group");

        H3 title = new H3(event.title());
        title.addClassNames("event-card-title", "neo-event-card-title", "font-ui");

        Paragraph organizer = new Paragraph("Organized by " + event.organizer() + " · " + event.category());
        organizer.addClassNames("event-card-organizer", "neo-event-card-organizer", "font-ui");

        titleGroup.add(title, organizer);

        Span timeframeTag = new Span(event.dateRange());
        timeframeTag.addClassNames("chip", "chip-neutral", "font-mono", "neo-event-date-pill");

        topRow.add(titleGroup, timeframeTag);

        // Meta Box: Date & Time, Venue (Inset panel)
        Div metaBox = new Div();
        metaBox.addClassNames("event-meta-box", "neo-event-meta-inset");

        Div timeCol = new Div();
        timeCol.addClassNames("event-meta-col", "neo-event-meta-col");
        Span timeLabel = new Span("Date & Time");
        timeLabel.addClassNames("event-meta-label", "neo-meta-lbl");
        Span timeVal = new Span(event.date() + " · " + event.timeRange());
        timeVal.addClassNames("event-meta-val", "neo-meta-val", "font-mono");
        timeCol.add(timeLabel, timeVal);

        Div venueCol = new Div();
        venueCol.addClassNames("event-meta-col", "neo-event-meta-col");
        Span venueLabel = new Span("Location / Venue");
        venueLabel.addClassNames("event-meta-label", "neo-meta-lbl");
        Span venueVal = new Span(event.location());
        venueVal.addClassNames("event-meta-val", "neo-meta-val", "font-mono");
        venueCol.add(venueLabel, venueVal);

        metaBox.add(timeCol, venueCol);

        // Description
        Paragraph desc = new Paragraph(event.description());
        desc.addClassNames("event-desc", "neo-event-desc", "font-ui");

        card.add(topRow, metaBox, desc);

        // Clash Marker Box (amber glass strip if timetable clash exists)
        if (event.hasTimetableClash() && event.clashDetails() != null) {
            Div clashBox = new Div();
            clashBox.addClassNames("clash-marker-box", "neo-clash-strip");

            Span clashHeader = new Span("WATCH · TIMETABLE CLASH");
            clashHeader.addClassNames("clash-marker-header", "neo-clash-header", "font-mono");

            Span clashBody = new Span(event.clashDetails());
            clashBody.addClassNames("clash-marker-body", "neo-clash-body", "font-ui");

            clashBox.add(clashHeader, clashBody);
            card.add(clashBox);
        }

        // Footer / RSVP Action Row
        Div footer = new Div();
        footer.addClassNames("event-card-footer", "neo-event-card-footer");

        Div rsvpCounter = new Div();
        rsvpCounter.addClassNames("rsvp-counter-group", "neo-rsvp-counter-group");

        Span countVal = new Span(event.rsvpCount() + " attending");
        countVal.addClassNames("rsvp-counter-val", "neo-rsvp-count", "font-mono");

        Span capVal = new Span("Capacity: " + event.capacity() + " seats");
        capVal.addClassNames("rsvp-counter-cap", "neo-rsvp-cap", "font-mono");

        rsvpCounter.add(countVal, capVal);

        Button rsvpBtn = new Button();
        if (event.isRsvp()) {
            rsvpBtn.setText("RSVP'd ✓");
            rsvpBtn.addClassNames("btn-rsvp-active", "neo-btn-rsvp-mint", "font-ui");
        } else {
            rsvpBtn.setText("RSVP to Attend");
            rsvpBtn.addClassNames("btn-primary", "neo-btn-outline", "font-ui");
        }

        rsvpBtn.addClickListener(e -> {
            boolean newState = apiClient.toggleEventRsvp(event.id());
            if (newState) {
                Notification.show("RSVP confirmed! Seat reserved for " + event.title(), 3000, Notification.Position.BOTTOM_END);
            } else {
                Notification.show("RSVP cancelled for " + event.title(), 3000, Notification.Position.BOTTOM_END);
            }
            refreshEvents();
        });

        footer.add(rsvpCounter, rsvpBtn);
        card.add(footer);

        return card;
    }
}
