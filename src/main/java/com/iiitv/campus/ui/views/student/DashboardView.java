package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.*;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.*;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "dashboard", layout = MainLayout.class)
@PageTitle("Student Dashboard - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class DashboardView extends VerticalLayout {

    private final ApiClient apiClient;
    private final Div contentContainer = new Div();

    public DashboardView(ApiClient apiClient) {
        this.apiClient = apiClient;

        addClassName("view-container");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        contentContainer.setWidthFull();
        add(contentContainer);

        // Load all data through one call
        loadDashboard();
    }

    private void loadDashboard() {
        // Fetch everything in one single API gateway call
        DashboardData data = apiClient.getDashboardData();
        renderDashboard(data);
    }

    private void renderDashboard(DashboardData data) {
        contentContainer.removeAll();

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setWidthFull();

        // Top: Small Greeting Row above the hero
        layout.add(createGreetingRow(data));

        // Horizontal Hero Carousel (12 feature tiles on glass)
        HeroCarousel heroCarousel = new HeroCarousel(apiClient, data);
        layout.add(heroCarousel);

        // ROW 1: 3 columns (Today ~1.6fr, Attendance 1fr, Tasks 1fr)
        Div row1 = new Div();
        row1.addClassName("neo-dash-row-1");
        renderTodayTimelineCard(row1, data.todaySchedule());
        renderAttendanceOverviewCard(row1, data.attendanceSummary());
        renderTasksCard(row1, data.pendingTasks());
        layout.add(row1);

        // ROW 2: 3 columns (Announcements 1fr, Events 1fr, Mess 1fr)
        Div row2 = new Div();
        row2.addClassName("neo-dash-row-2");
        renderAnnouncementsCard(row2, data.announcements());
        renderEventsCard(row2, data.upcomingEvents());
        renderMessMenuCard(row2, data.todayMealMenu());
        layout.add(row2);

        // ROW 3: My Classrooms quick cards
        Div row3 = new Div();
        row3.addClassName("neo-dash-row-3");
        renderClassroomsCard(row3, data.classrooms());
        layout.add(row3);

        contentContainer.add(layout);
    }

    private Component createGreetingRow(DashboardData data) {
        HorizontalLayout row = new HorizontalLayout();
        row.addClassName("dashboard-header-row");
        row.setWidthFull();
        row.setAlignItems(FlexComponent.Alignment.CENTER);
        row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        VerticalLayout leftGroup = new VerticalLayout();
        leftGroup.setPadding(false);
        leftGroup.setSpacing(false);

        H1 greetingTitle = new H1("Welcome back, " + data.student().name().split(" ")[0] + "!");
        greetingTitle.addClassNames("dashboard-greeting-title", "font-ui", "text-navy");

        Paragraph classLine = new Paragraph(data.student().program() + " · " + data.student().semester());
        classLine.addClassNames("dashboard-greeting-subtitle", "font-ui", "text-muted");

        leftGroup.add(greetingTitle, classLine);

        // Right Date Card
        Div dateCard = new Div();
        dateCard.addClassName("dashboard-date-card");

        Div dateText = new Div();
        dateText.addClassNames("dashboard-date-text", "font-ui");
        dateText.setText(data.dateString());

        Div weekText = new Div();
        weekText.addClassNames("dashboard-week-text", "font-ui");
        weekText.setText(data.weekString());

        dateCard.add(dateText, weekText);

        row.add(leftGroup, dateCard);
        return row;
    }

    // =========================================================================
    // ROW 1: TODAY TIMELINE, ATTENDANCE, TASKS
    // =========================================================================

    private void renderTodayTimelineCard(Div container, List<ClassOccurrence> schedule) {
        CardSection section = new CardSection("Today", "Full timetable →", TimetableView.class);
        section.addClassName("neo-card-today");

        Select<String> semesterSelect = new Select<>();
        semesterSelect.setItems(
                "Semester V (B.Tech)",
                "Semester I (B.Tech)",
                "Semester II (B.Tech)",
                "Semester III (B.Tech)",
                "Semester IV (B.Tech)",
                "Semester VI (B.Tech)",
                "Semester VII (B.Tech)",
                "Semester VIII (B.Tech)",
                "MCA",
                "M.Tech",
                "Master Schedule (All)"
        );
        semesterSelect.setValue("Semester V (B.Tech)");
        semesterSelect.addClassNames("font-ui", "dashboard-sem-select", "neo-glass-select");
        semesterSelect.setWidthFull();
        semesterSelect.setAriaLabel("Select semester for today's schedule");

        Div timelineContainer = new Div();
        timelineContainer.addClassName("neo-today-timeline");
        timelineContainer.setWidthFull();

        renderTimelineItems(timelineContainer, schedule);

        semesterSelect.addValueChangeListener(e -> {
            String semKey = e.getValue().toLowerCase();
            List<ClassOccurrence> all = apiClient.getWeeklyTimetable(semKey);
            List<ClassOccurrence> todayClasses = all.stream()
                    .filter(occ -> "Tue".equalsIgnoreCase(occ.dayOfWeek()))
                    .toList();
            renderTimelineItems(timelineContainer, todayClasses);
        });

        section.addContent(semesterSelect, timelineContainer);
        container.add(section);
    }

    private void renderTimelineItems(Div container, List<ClassOccurrence> schedule) {
        container.removeAll();
        if (schedule == null || schedule.isEmpty()) {
            container.add(new EmptyState(
                    "No Classes Scheduled Today",
                    "There are no lectures, tutorials, or laboratory sessions on your schedule for today."
            ));
            return;
        }

        boolean nextUpFound = false;
        for (ClassOccurrence occ : schedule) {
            boolean isBreak = "Break".equalsIgnoreCase(occ.type()) || "BREAK".equalsIgnoreCase(occ.courseCode());
            boolean isNext = occ.isNextUp();
            if (isNext) nextUpFound = true;

            Div item = new Div();
            item.addClassName("neo-timeline-item");

            // Left Time Column in mono
            Div timeCol = new Div();
            timeCol.addClassNames("neo-timeline-time", "font-mono");
            timeCol.setText(occ.startTime() + "–" + occ.endTime());

            // Center Track with Dot
            Div track = new Div();
            track.addClassName("neo-timeline-track");
            Div dot = new Div();
            dot.addClassName("neo-timeline-dot");

            // Right Content Column
            Div contentCol = new Div();
            contentCol.addClassName("neo-timeline-content");

            if (isBreak) {
                dot.addClassName("is-now");
                item.addClassName("is-now");

                HorizontalLayout titleRow = new HorizontalLayout();
                titleRow.setWidthFull();
                titleRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
                titleRow.setAlignItems(FlexComponent.Alignment.CENTER);

                Span title = new Span(occ.courseName() + " · " + occ.room());
                title.addClassNames("neo-timeline-break-title", "font-ui");

                Span chipNow = new Span("NOW " + occ.startTime());
                chipNow.addClassNames("neo-timeline-chip", "neo-chip-now", "font-mono");

                titleRow.add(title, chipNow);
                contentCol.add(titleRow);
            } else if (isNext) {
                dot.addClassName("is-next");
                item.addClassName("is-next");

                // Next class is a highlighted card: yellow-tinted glass, gold left border, time in mono, bold course name, room, faculty, "in 30 min"
                Div highlightCard = new Div();
                highlightCard.addClassName("neo-timeline-highlight-card");

                HorizontalLayout topRow = new HorizontalLayout();
                topRow.setWidthFull();
                topRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
                topRow.setAlignItems(FlexComponent.Alignment.CENTER);

                Span courseName = new Span(occ.displayCode() + " · " + occ.courseName());
                courseName.addClassNames("neo-timeline-course-title", "font-ui");

                Span chipNext = new Span(occ.nextUpBadge() != null ? occ.nextUpBadge() : "in 30 min");
                chipNext.addClassNames("neo-timeline-chip", "neo-chip-gold", "font-mono");

                topRow.add(courseName, chipNext);

                Paragraph meta = new Paragraph(occ.room() + " · " + occ.instructor());
                meta.addClassNames("neo-timeline-meta", "font-ui");

                highlightCard.add(topRow, meta);
                contentCol.add(highlightCard);
            } else if (!nextUpFound) {
                // Past item: grey dot and mono "DONE" chip
                dot.addClassName("is-done");
                item.addClassName("is-done");

                HorizontalLayout titleRow = new HorizontalLayout();
                titleRow.setWidthFull();
                titleRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
                titleRow.setAlignItems(FlexComponent.Alignment.CENTER);

                Span title = new Span(occ.displayCode() + " · " + occ.courseName());
                title.addClassNames("neo-timeline-title", "font-ui");

                Span chipDone = new Span("DONE");
                chipDone.addClassNames("neo-timeline-chip", "neo-chip-done", "font-mono");

                titleRow.add(title, chipDone);
                Paragraph meta = new Paragraph(occ.room() + " · " + occ.instructor());
                meta.addClassNames("neo-timeline-meta", "font-ui");
                contentCol.add(titleRow, meta);
            } else {
                // Upcoming class item
                dot.addClassName("is-future");
                item.addClassName("is-future");

                Div title = new Div();
                title.addClassNames("neo-timeline-title", "font-ui");
                title.setText(occ.displayCode() + " · " + occ.courseName());

                Paragraph meta = new Paragraph(occ.room() + " · " + occ.instructor());
                meta.addClassNames("neo-timeline-meta", "font-ui");

                contentCol.add(title, meta);
            }

            track.add(dot);
            item.add(timeCol, track, contentCol);
            container.add(item);
        }
    }

    private void renderAttendanceOverviewCard(Div container, AttendanceSummary attendance) {
        CardSection section = new CardSection("Attendance", "View Details →", AttendanceView.class);
        section.addClassName("neo-card-attendance");

        // SAFE chip in header
        Span safeChip = new Span(attendance.status() != null ? attendance.status().name() : "SAFE");
        safeChip.addClassNames("neo-card-header-chip", "font-mono", "neo-chip-" + (attendance.status() != null ? attendance.status().name().toLowerCase() : "safe"));
        section.getElement().getChild(0).appendChild(safeChip.getElement());

        VerticalLayout body = new VerticalLayout();
        body.addClassName("attendance-overview-body");
        body.setPadding(false);
        body.setSpacing(true);

        HorizontalLayout topRow = new HorizontalLayout();
        topRow.addClassName("attendance-overview-top");
        topRow.setAlignItems(FlexComponent.Alignment.CENTER);
        topRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // SVG Ring: green #198754 stroke, track light grey, big mono % in the middle
        AttendanceRing ring = new AttendanceRing(
                attendance.percentage(),
                "Overall",
                attendance.status(),
                AttendanceRing.Size.MEDIUM
        );

        // Stats Lines
        VerticalLayout statsList = new VerticalLayout();
        statsList.addClassName("attendance-stats-list");
        statsList.setPadding(false);
        statsList.setSpacing(false);

        statsList.add(
                createStatLine("Total Classes", String.valueOf(attendance.totalClasses()), "text-navy"),
                createStatLine("Attended", String.valueOf(attendance.attendedClasses()), "text-navy"),
                createStatLine("Absent", String.valueOf(attendance.absentClasses()), "text-navy")
        );

        topRow.add(ring, statsList);
        body.add(topRow);

        // Green Guidance Banner ("You can still miss 8 classes. (Threshold: 75%)")
        Div banner = new Div();
        banner.addClassNames("attendance-guidance-banner", "neo-mint-glass-banner", "font-ui");
        banner.setText("You can still miss " + attendance.classesCanMiss() + " classes. (Threshold: " +
                String.format("%.0f", attendance.threshold()) + "%)");

        body.add(banner);
        section.addContent(body);
        container.add(section);
    }

    private HorizontalLayout createStatLine(String label, String value, String valueColorClass) {
        HorizontalLayout line = new HorizontalLayout();
        line.addClassName("attendance-stat-line");
        line.setWidthFull();
        line.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        Span l = new Span(label);
        l.addClassNames("font-ui", "text-muted");

        Span v = new Span(value);
        v.addClassNames("font-mono", valueColorClass);

        line.add(l, v);
        return line;
    }

    private void renderTasksCard(Div container, List<Task> tasks) {
        CardSection section = new CardSection("Tasks", "View All →", TasksView.class);
        section.addClassName("neo-card-tasks");

        int pendingCount = (tasks != null) ? tasks.size() : 3;
        Span pendingChip = new Span(pendingCount + " PENDING");
        pendingChip.addClassNames("neo-card-header-chip", "font-mono", "neo-chip-gold");
        section.getElement().getChild(0).appendChild(pendingChip.getElement());

        VerticalLayout body = new VerticalLayout();
        body.addClassName("neo-tasks-body");
        body.setPadding(false);
        body.setSpacing(false);
        body.setWidthFull();

        if (tasks == null || tasks.isEmpty()) {
            body.add(new EmptyState("All Tasks Completed", "You have no pending assignments, quizzes, or lab deliverables."));
        } else {
            for (Task t : tasks) {
                Div row = new Div();
                row.addClassName("neo-task-row");

                HorizontalLayout rowLayout = new HorizontalLayout();
                rowLayout.setWidthFull();
                rowLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
                rowLayout.setAlignItems(FlexComponent.Alignment.CENTER);

                HorizontalLayout left = new HorizontalLayout();
                left.setAlignItems(FlexComponent.Alignment.CENTER);
                left.setSpacing(true);

                Span circle = new Span("○");
                circle.addClassNames("neo-task-circle", "font-mono");

                VerticalLayout info = new VerticalLayout();
                info.setPadding(false);
                info.setSpacing(false);

                Span title = new Span(t.title());
                title.addClassNames("neo-task-title", "font-ui");

                Span meta = new Span(t.courseCode() + " · due " + t.dueDate());
                meta.addClassNames("neo-task-meta", "font-ui");

                info.add(title, meta);
                left.add(circle, info);

                String priority = t.priority() != null ? t.priority().toUpperCase() : "NORMAL";
                String pStatus = priority.contains("CRIT") ? "critical" : (priority.contains("HIGH") ? "watch" : "neutral");
                Span pChip = new Span(priority);
                pChip.addClassNames("neo-hero-chip", "font-mono", "neo-chip-" + pStatus);

                rowLayout.add(left, pChip);
                row.add(rowLayout);
                body.add(row);
            }
        }

        section.addContent(body);
        container.add(section);
    }

    // =========================================================================
    // ROW 2: ANNOUNCEMENTS, EVENTS, MESS MENU
    // =========================================================================

    private void renderAnnouncementsCard(Div container, List<Announcement> announcements) {
        CardSection section = new CardSection("Announcements", "View All →", AnnouncementsView.class);
        section.addClassName("neo-card-announcements");

        if (announcements == null || announcements.isEmpty()) {
            section.addContent(new EmptyState(
                    "No Announcements",
                    "There are no active official announcements or circulars at this time."
            ));
        } else {
            List<Announcement> limited = announcements.stream().limit(5).toList();
            int total = limited.size();

            // Toolbar with Prev / "n of N" / Next buttons
            HorizontalLayout toolbar = new HorizontalLayout();
            toolbar.addClassName("neo-announcements-toolbar");
            toolbar.setWidthFull();
            toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            toolbar.setAlignItems(FlexComponent.Alignment.CENTER);

            Button prevBtn = new Button("‹");
            prevBtn.addClassNames("neo-announcements-nav-btn", "font-mono");
            prevBtn.getElement().setAttribute("aria-label", "Previous announcement");

            Span counter = new Span("1 of " + total);
            counter.addClassNames("neo-announcements-counter", "font-mono");

            Button nextBtn = new Button("›");
            nextBtn.addClassNames("neo-announcements-nav-btn", "font-mono");
            nextBtn.getElement().setAttribute("aria-label", "Next announcement");

            toolbar.add(prevBtn, counter, nextBtn);

            // Horizontal mini-carousel of cards
            Div carousel = new Div();
            carousel.addClassName("neo-announcements-mini-carousel");
            carousel.getElement().setAttribute("tabindex", "0");
            carousel.getElement().setAttribute("role", "region");
            carousel.getElement().setAttribute("aria-label", "Official Announcements Strip");

            for (Announcement ann : limited) {
                Div card = new Div();
                card.addClassName("neo-announcement-mini-card");

                HorizontalLayout top = new HorizontalLayout();
                top.setWidthFull();
                top.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
                top.setAlignItems(FlexComponent.Alignment.CENTER);

                String cat = ann.category() != null ? ann.category().toUpperCase() : "ACADEMIC";
                String chipClass = cat.contains("EXAM") ? "critical" : (cat.contains("ACADEMIC") ? "info" : "neutral");
                Span catChip = new Span(cat);
                catChip.addClassNames("neo-hero-chip", "font-mono", "neo-chip-" + chipClass);

                Span date = new Span(ann.date());
                date.addClassNames("neo-announcement-date", "font-mono");

                top.add(catChip, date);

                H3 title = new H3(ann.title());
                title.addClassNames("neo-announcement-title", "font-ui");

                Paragraph summary = new Paragraph(ann.summary());
                summary.addClassNames("neo-announcement-summary", "font-ui");

                card.add(top, title, summary);
                carousel.add(card);
            }

            // 6th card: "View All" card
            Div viewAllCard = new Div();
            viewAllCard.addClassNames("neo-announcement-mini-card", "neo-announcement-view-all-card");
            viewAllCard.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("announcements")));

            Span viewAllHeading = new Span("ARCHIVE");
            viewAllHeading.addClassNames("neo-hero-chip", "neo-chip-mint", "font-mono");

            H3 viewAllTitle = new H3("View All " + announcements.size() + " Notices →");
            viewAllTitle.addClassNames("neo-announcement-title", "font-ui");

            Paragraph viewAllDesc = new Paragraph("Browse all official circulars, exam schedules, and hostel notices.");
            viewAllDesc.addClassNames("neo-announcement-summary", "font-ui");

            viewAllCard.add(viewAllHeading, viewAllTitle, viewAllDesc);
            carousel.add(viewAllCard);

            // Wire up prev/next and keyboard scroll in JS
            prevBtn.addClickListener(e -> {
                carousel.getElement().executeJs("this.scrollBy({ left: -312, behavior: 'smooth' });");
            });

            nextBtn.addClickListener(e -> {
                carousel.getElement().executeJs("this.scrollBy({ left: 312, behavior: 'smooth' });");
            });

            carousel.getElement().executeJs(
                "const c = this; const counter = $0; const total = $1;" +
                "c.addEventListener('scroll', () => {" +
                "  const card = c.querySelector('.neo-announcement-mini-card');" +
                "  const w = card ? card.offsetWidth + 12 : 312;" +
                "  const idx = Math.min(total, Math.max(1, Math.round(c.scrollLeft / w) + 1));" +
                "  counter.textContent = idx + ' of ' + total;" +
                "});" +
                "c.addEventListener('keydown', (e) => {" +
                "  if (e.key === 'ArrowRight') { c.scrollBy({ left: 312, behavior: 'smooth' }); e.preventDefault(); }" +
                "  else if (e.key === 'ArrowLeft') { c.scrollBy({ left: -312, behavior: 'smooth' }); e.preventDefault(); }" +
                "});",
                counter.getElement(), total
            );

            section.addContent(toolbar, carousel);
        }

        container.add(section);
    }

    private void renderEventsCard(Div container, List<Event> events) {
        CardSection section = new CardSection("Events", "View All →", EventsView.class);
        section.addClassName("neo-card-events");

        if (events == null || events.isEmpty()) {
            section.addContent(new EmptyState(
                    "No Upcoming Events",
                    "There are no scheduled campus symposiums or athletic meets on the calendar."
            ));
        } else {
            VerticalLayout list = new VerticalLayout();
            list.addClassName("neo-events-list");
            list.setPadding(false);
            list.setSpacing(false);
            list.setWidthFull();

            List<Event> limited = events.stream().limit(4).toList();

            for (Event ev : limited) {
                Div row = new Div();
                row.addClassName("neo-event-row-grid");

                // 1. EventDateBadge (64x64, neumorphic)
                EventDateBadge dateBadge = new EventDateBadge(ev.date(), ev.timeframe() == Event.Timeframe.TODAY);

                // 2. Details Column (Title clamped to 2 lines, Meta)
                VerticalLayout details = new VerticalLayout();
                details.addClassName("neo-event-details");
                details.setPadding(false);
                details.setSpacing(false);

                H3 title = new H3(ev.title());
                title.addClassNames("neo-event-title", "font-ui");

                Paragraph meta = new Paragraph(ev.dateRange() + " · " + ev.location());
                meta.addClassNames("neo-event-meta", "font-ui");

                details.add(title, meta);

                // 3. Status Chip (CLASH or RSVP OPEN)
                Span chip;
                if (ev.hasTimetableClash()) {
                    chip = new Span("CLASH");
                    chip.addClassNames("neo-hero-chip", "font-mono", "neo-chip-critical", "neo-event-chip");
                } else {
                    chip = new Span("RSVP OPEN");
                    chip.addClassNames("neo-hero-chip", "font-mono", "neo-chip-safe", "neo-event-chip");
                }

                row.add(dateBadge, details, chip);
                list.add(row);
            }

            section.addContent(list);
        }

        container.add(section);
    }

    private void renderMessMenuCard(Div container, MealMenu menu) {
        CardSection section = new CardSection("Mess", "View Full Menu →", MessMenuView.class);
        section.addClassName("neo-card-mess");

        if (menu == null) {
            section.addContent(new EmptyState(
                    "Menu Not Published",
                    "Dining hall schedule for today has not yet been announced."
            ));
        } else {
            VerticalLayout body = new VerticalLayout();
            body.setPadding(false);
            body.setSpacing(true);
            body.setWidthFull();

            // Segmented control (Lunch / Snacks / Dinner) as a neumorphic pill track
            HorizontalLayout segControl = new HorizontalLayout();
            segControl.addClassName("neo-mess-segmented-control");
            segControl.setSpacing(false);

            Button btnLunch = new Button("Lunch");
            btnLunch.addClassNames("neo-mess-seg-btn", "active", "font-ui");

            Button btnSnacks = new Button("Snacks");
            btnSnacks.addClassNames("neo-mess-seg-btn", "font-ui");

            Button btnDinner = new Button("Dinner");
            btnDinner.addClassNames("neo-mess-seg-btn", "font-ui");

            Button btnWeek = new Button("Week");
            btnWeek.addClassNames("neo-mess-seg-btn", "font-ui");

            segControl.add(btnLunch, btnSnacks, btnDinner, btnWeek);

            Div contentArea = new Div();
            contentArea.setWidthFull();

            // Mint glass strip "NOW · Rice, Dal, Paneer Curry, ..."
            Div mintStrip = new Div();
            mintStrip.addClassNames("neo-mess-mint-strip", "font-ui");
            Span nowBadge = new Span("NOW · ");
            nowBadge.addClassNames("font-mono", "neo-mess-now-bold");
            Span itemsSpan = new Span(menu.items() != null ? menu.items() : "Rice, Dal, Paneer Curry, Chapati, Salad");
            itemsSpan.addClassName("neo-mess-items-text");
            mintStrip.add(nowBadge, itemsSpan);

            HorizontalLayout metaRow = new HorizontalLayout();
            metaRow.addClassNames("neo-mess-meta-row");
            metaRow.setWidthFull();
            metaRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
            metaRow.setAlignItems(FlexComponent.Alignment.CENTER);

            Span timings = new Span("12:30 – 14:30 · Dining Hall");
            timings.addClassNames("neo-mess-timings", "font-mono");

            Span rating = new Span("4.2/5 ★");
            rating.addClassNames("neo-hero-chip", "neo-chip-gold", "font-mono");

            metaRow.add(timings, rating);

            Div todayView = new Div(mintStrip, metaRow);
            todayView.setWidthFull();

            Div weekView = createDashboardWeekMessPanel();
            weekView.setVisible(false);

            contentArea.add(todayView, weekView);

            btnLunch.addClickListener(e -> {
                setActiveMessTab(btnLunch, btnSnacks, btnDinner, btnWeek);
                todayView.setVisible(true);
                weekView.setVisible(false);
                nowBadge.setText("NOW · ");
                itemsSpan.setText(menu.items() != null ? menu.items() : "Rice, Dal, Paneer Curry, Chapati, Salad");
                timings.setText("12:30 – 14:30 · Dining Hall");
            });

            btnSnacks.addClickListener(e -> {
                setActiveMessTab(btnSnacks, btnLunch, btnDinner, btnWeek);
                todayView.setVisible(true);
                weekView.setVisible(false);
                nowBadge.setText("EVENING · ");
                itemsSpan.setText("Samosa, Veg Cutlet, Masala Chai, Filter Coffee");
                timings.setText("17:00 – 18:00 · Cafeteria");
            });

            btnDinner.addClickListener(e -> {
                setActiveMessTab(btnDinner, btnLunch, btnSnacks, btnWeek);
                todayView.setVisible(true);
                weekView.setVisible(false);
                nowBadge.setText("TONIGHT · ");
                itemsSpan.setText("Kadhai Paneer, Mix Veg Handi, Phulka, Gulab Jamun");
                timings.setText("19:30 – 21:30 · Dining Hall");
            });

            btnWeek.addClickListener(e -> {
                setActiveMessTab(btnWeek, btnLunch, btnSnacks, btnDinner);
                todayView.setVisible(false);
                weekView.setVisible(true);
            });

            body.add(segControl, contentArea);
            section.addContent(body);
        }

        container.add(section);
    }

    private void setActiveMessTab(Button activeBtn, Button... otherBtns) {
        activeBtn.addClassName("active");
        for (Button b : otherBtns) {
            b.removeClassName("active");
        }
    }

    private Div createDashboardWeekMessPanel() {
        Div panel = new Div();
        panel.addClassName("dashboard-week-list");

        List<WeeklyMealDay> weekDays = apiClient.getWeeklyMealMenu("week 2");
        for (WeeklyMealDay day : weekDays) {
            Div item = new Div();
            item.addClassName("dashboard-week-item");
            if (day.isToday()) {
                item.addClassName("is-today");
            }

            Div header = new Div();
            header.addClassName("dashboard-week-item-header");

            Span daySpan = new Span(day.day() + " (" + day.date() + ")");
            daySpan.addClassNames("dashboard-week-day-title", "font-ui");
            header.add(daySpan);

            if (day.isToday()) {
                Span todayChip = new Span("TODAY");
                todayChip.addClassNames("chip", "chip-neutral", "font-mono");
                header.add(todayChip);
            }

            Div meals = new Div();
            meals.addClassNames("dashboard-week-meals", "font-ui");
            meals.setText("Lunch: " + day.lunch() + " · Dinner: " + day.dinner());

            item.add(header, meals);
            panel.add(item);
        }

        return panel;
    }

    // =========================================================================
    // ROW 3: MY CLASSROOMS
    // =========================================================================

    private void renderClassroomsCard(Div container, List<Classroom> classrooms) {
        CardSection section = new CardSection("My Classrooms", "View All →", ClassroomsView.class);
        section.addClassName("neo-card-classrooms");

        if (classrooms == null || classrooms.isEmpty()) {
            section.addContent(new EmptyState(
                    "No Classrooms Enrolled",
                    "You are not currently enrolled in any active course rooms for this semester."
            ));
        } else {
            Div grid = new Div();
            grid.addClassName("neo-classrooms-grid");

            for (Classroom c : classrooms) {
                Div card = new Div();
                card.addClassName("neo-classroom-quick-card");
                card.addClickListener(e -> UI.getCurrent().navigate("classrooms"));

                HorizontalLayout top = new HorizontalLayout();
                top.setWidthFull();
                top.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

                Span code = new Span(c.courseCode());
                code.addClassNames("neo-classroom-code", "font-mono");

                Span teacher = new Span(c.teacher());
                teacher.addClassNames("neo-classroom-teacher", "font-ui");

                top.add(code, teacher);

                Paragraph name = new Paragraph(c.courseName());
                name.addClassNames("neo-classroom-name", "font-ui");

                Paragraph tag = new Paragraph(c.assignmentDueTag() != null ? c.assignmentDueTag() : (c.newMaterialsTag() != null ? c.newMaterialsTag() : "Up to date"));
                tag.addClassNames("neo-classroom-tag", "font-ui");

                card.add(top, name, tag);
                grid.add(card);
            }

            section.addContent(grid);
        }

        container.add(section);
    }
}
