package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.ClassOccurrence;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.EmptyState;
import com.iiitv.campus.ui.components.ScheduleRow;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.*;

@Route(value = "timetable", layout = MainLayout.class)
@PageTitle("My Timetable - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class TimetableView extends VerticalLayout {

    private enum Mode { DAY, WEEK, LIST }

    private final ApiClient apiClient;
    private Mode currentMode = Mode.WEEK;
    private int weekOffset = 0; // 0 = current week
    private String selectedSemester = "Semester V (B.Tech CSE - Active)";
    private String selectedBranch = "All Branches";

    private final Div viewContainer = new Div();
    private final Span dateRangeSpan = new Span();
    private Paragraph subtitle;
    private Button dayBtn;
    private Button weekBtn;
    private Button listBtn;

    // Days Mon–Sat matching the mockup
    private final List<String> days = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat");
    private final Map<String, String> dayDates = Map.of(
            "Mon", "9 Nov",
            "Tue", "10 Nov",
            "Wed", "11 Nov",
            "Thu", "12 Nov",
            "Fri", "13 Nov",
            "Sat", "14 Nov"
    );

    public TimetableView(ApiClient apiClient) {
        this.apiClient = apiClient;

        addClassName("view-container");
        setSizeFull();
        setPadding(false);
        setSpacing(true);

        // Header and Navigation Controls
        add(createHeaderWithControls());

        viewContainer.setWidthFull();
        add(viewContainer);

        renderCurrentView();
    }

    private Component createHeaderWithControls() {
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setPadding(false);
        headerLayout.setSpacing(false);
        headerLayout.setWidthFull();
        headerLayout.addClassNames("timetable-header-row", "neo-timetable-header-row");

        HorizontalLayout topRow = new HorizontalLayout();
        topRow.setWidthFull();
        topRow.setAlignItems(FlexComponent.Alignment.CENTER);
        topRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // Title and Subtitle
        VerticalLayout titleGroup = new VerticalLayout();
        titleGroup.setPadding(false);
        titleGroup.setSpacing(false);

        H1 title = new H1("My Timetable");
        title.addClassNames("page-header-title", "font-ui", "text-navy", "neo-timetable-title");

        subtitle = new Paragraph("Weekly schedule for Semester V · ICD Campus");
        subtitle.addClassNames("page-header-subtitle", "font-ui", "text-muted", "neo-timetable-subtitle");

        titleGroup.add(title, subtitle);

        // Right Controls: Semester Selector + Previous/Next Week + Date Range + View Switcher
        HorizontalLayout controlsGroup = new HorizontalLayout();
        controlsGroup.addClassNames("timetable-controls-group", "neo-timetable-controls");
        controlsGroup.setAlignItems(FlexComponent.Alignment.CENTER);
        controlsGroup.setSpacing(true);

        // Semester Switcher
        Select<String> semesterSelect = new Select<>();
        semesterSelect.setItems(
                "Semester V (B.Tech CSE - Active)",
                "All Branches & Semesters (Master Schedule)",
                "Semester I (B.Tech ICD)",
                "Semester II (B.Tech ICD)",
                "Semester III (B.Tech ICD)",
                "Semester IV (B.Tech ICD)",
                "Semester VI (B.Tech ICD)",
                "Semester VII (B.Tech ICD)",
                "Semester VIII (B.Tech ICD)",
                "MCA (ICD Campus)",
                "MCA - Semester I",
                "MCA - Semester II",
                "MCA - Semester III",
                "MCA - Semester IV",
                "M.Tech (ICD Campus)",
                "M.Tech - Semester I",
                "M.Tech - Semester II",
                "M.Tech - Semester III / IV"
        );
        semesterSelect.setValue("Semester V (B.Tech CSE - Active)");
        semesterSelect.addClassNames("font-ui", "neo-glass-select");
        semesterSelect.setWidth("240px");
        semesterSelect.setAriaLabel("Select semester timetable");
        semesterSelect.addValueChangeListener(e -> {
            this.selectedSemester = e.getValue();
            subtitle.setText("Weekly schedule for " + selectedSemester + " · ICD Campus");
            updateDateRangeLabel();
            renderCurrentView();
        });

        // Branch Filter (Allows student to see all branches or isolate CSE, ECE, AI, IT)
        Select<String> branchSelect = new Select<>();
        branchSelect.setItems("All Branches", "CSE", "ECE", "AI", "CSE(CS) / IT");
        branchSelect.setValue("All Branches");
        branchSelect.addClassNames("font-ui", "neo-glass-select");
        branchSelect.setWidth("150px");
        branchSelect.setAriaLabel("Filter schedule by branch");
        branchSelect.addValueChangeListener(e -> {
            this.selectedBranch = e.getValue();
            renderCurrentView();
        });

        // Date Range Navigator (‹ 9 Nov – 14 Nov 2026 ›)
        HorizontalLayout navBox = new HorizontalLayout();
        navBox.addClassNames("date-navigator", "neo-timetable-date-nav");
        navBox.setAlignItems(FlexComponent.Alignment.CENTER);
        navBox.setSpacing(false);

        Button prevBtn = new Button("‹", e -> changeWeek(-1));
        prevBtn.addClassNames("nav-arrow-btn", "neo-tt-arrow-btn", "font-ui");
        prevBtn.getElement().setAttribute("aria-label", "Previous week");

        dateRangeSpan.addClassNames("date-range-display", "neo-tt-date-pill", "font-mono");
        updateDateRangeLabel();

        Button nextBtn = new Button("›", e -> changeWeek(1));
        nextBtn.addClassNames("nav-arrow-btn", "neo-tt-arrow-btn", "font-ui");
        nextBtn.getElement().setAttribute("aria-label", "Next week");

        navBox.add(prevBtn, dateRangeSpan, nextBtn);

        // View Switcher (Day / Week / List)
        HorizontalLayout switchGroup = new HorizontalLayout();
        switchGroup.addClassNames("view-switch-group", "neo-timetable-view-switch");
        switchGroup.setSpacing(false);

        dayBtn = new Button("Day", e -> setViewMode(Mode.DAY));
        dayBtn.addClassNames("view-switch-btn", "neo-tt-switch-btn", "font-ui");
        dayBtn.getElement().setAttribute("aria-label", "Switch to Day timetable view");

        weekBtn = new Button("Week", e -> setViewMode(Mode.WEEK));
        weekBtn.addClassNames("view-switch-btn", "neo-tt-switch-btn", "font-ui", "active-view");
        weekBtn.getElement().setAttribute("aria-label", "Switch to Week timetable view");

        listBtn = new Button("List", e -> setViewMode(Mode.LIST));
        listBtn.addClassNames("view-switch-btn", "neo-tt-switch-btn", "font-ui");
        listBtn.getElement().setAttribute("aria-label", "Switch to List timetable view");

        switchGroup.add(dayBtn, weekBtn, listBtn);

        controlsGroup.add(semesterSelect, branchSelect, navBox, switchGroup);
        topRow.add(titleGroup, controlsGroup);
        headerLayout.add(topRow);

        return headerLayout;
    }

    private void changeWeek(int delta) {
        weekOffset += delta;
        updateDateRangeLabel();
        renderCurrentView();
    }

    private void updateDateRangeLabel() {
        if (weekOffset == 0) {
            dateRangeSpan.setText("9 Nov – 14 Nov 2026");
        } else if (weekOffset > 0) {
            dateRangeSpan.setText("Week +" + weekOffset + " (Semester V)");
        } else {
            dateRangeSpan.setText("Week " + weekOffset + " (Semester V)");
        }
    }

    private void setViewMode(Mode mode) {
        this.currentMode = mode;

        dayBtn.removeClassName("active-view");
        weekBtn.removeClassName("active-view");
        listBtn.removeClassName("active-view");

        if (mode == Mode.DAY) dayBtn.addClassName("active-view");
        else if (mode == Mode.WEEK) weekBtn.addClassName("active-view");
        else if (mode == Mode.LIST) listBtn.addClassName("active-view");

        renderCurrentView();
    }

    private void renderCurrentView() {
        viewContainer.removeAll();
        String semKey = parseSemesterKey(selectedSemester);
        List<ClassOccurrence> allOccurrences = apiClient.getWeeklyTimetable(semKey);

        if (selectedBranch != null && !"All Branches".equalsIgnoreCase(selectedBranch) && allOccurrences != null) {
            String branchQuery = selectedBranch.toLowerCase();
            allOccurrences = allOccurrences.stream()
                    .filter(occ -> {
                        if (occ.branch() == null) return true;
                        String b = occ.branch().toLowerCase();
                        if (branchQuery.contains("cse(cs)") || branchQuery.contains("it")) {
                            return b.contains("cse(cs)") || b.contains("it") || b.contains("cs");
                        }
                        return b.contains(branchQuery) || b.contains("all");
                    })
                    .toList();
        }

        if (currentMode == Mode.WEEK) {
            // Desktop and tablet week grid + phone fallback day agenda
            Div weekWrapper = new Div();
            weekWrapper.setWidthFull();

            Div desktopGrid = renderWeekGrid(allOccurrences);
            desktopGrid.addClassName("desktop-week-grid");

            Div phoneDayView = renderDayAgenda(allOccurrences, "Tue");
            phoneDayView.addClassName("phone-only-day-view");

            weekWrapper.add(desktopGrid, phoneDayView);
            viewContainer.add(weekWrapper);
        } else if (currentMode == Mode.DAY) {
            viewContainer.add(renderDayAgenda(allOccurrences, "Tue"));
        } else {
            viewContainer.add(renderListView(allOccurrences));
        }
    }

    private Div renderWeekGrid(List<ClassOccurrence> occurrences) {
        Div card = new Div();
        card.addClassNames("timetable-grid-card", "neo-timetable-card", "campus-card");

        Div scrollWrapper = new Div();
        scrollWrapper.addClassName("neo-timetable-scroll-wrapper");

        // CSS Grid Container (100px time column + 6 day columns)
        Div grid = new Div();
        grid.addClassNames("timetable-css-grid", "neo-timetable-grid");

        // Header Row: Time header + Mon-Sat headers
        Div timeColHeader = new Div();
        timeColHeader.setText("Time");
        timeColHeader.addClassNames("timetable-header-cell", "neo-tt-header-cell", "time-col-header", "font-ui");
        grid.add(timeColHeader);

        for (String day : days) {
            Div dayHeader = new Div();
            boolean isToday = "Tue".equalsIgnoreCase(day);
            dayHeader.addClassNames("timetable-header-cell", "neo-tt-header-cell", "font-ui");
            if (isToday) {
                dayHeader.addClassNames("today-col-header", "neo-tt-today-header");
            }

            Div dayTitle = new Div();
            dayTitle.setText(day);
            dayTitle.addClassNames("day-title", "neo-tt-day-title");

            Div dateSub = new Div();
            dateSub.setText(dayDates.getOrDefault(day, ""));
            dateSub.addClassNames("day-date", "neo-tt-day-date", "font-mono");

            dayHeader.add(dayTitle, dateSub);
            grid.add(dayHeader);
        }

        String semKey = parseSemesterKey(selectedSemester);
        List<String> currentSlots = getTimeSlotsForSemester(semKey);

        // Grid Rows for each time slot
        for (String slot : currentSlots) {
            String[] times = slot.split("–");
            String start = times[0];

            // Time Slot cell
            Div timeCell = new Div();
            timeCell.setText(slot);
            timeCell.addClassNames("timetable-time-cell", "neo-tt-time-cell", "font-mono");
            grid.add(timeCell);

            // Special check: Lunch Break (only if NO classes exist in this slot)
            boolean isLunchSlot = "13:15".equals(start) || "12:15".equals(start);
            boolean hasClassesInSlot = occurrences.stream().anyMatch(occ -> {
                int cs = toMinutes(occ.startTime());
                int se = times.length > 1 ? toMinutes(times[1]) : cs + 60;
                return cs >= toMinutes(start) && cs < se
                        && !"Break".equalsIgnoreCase(occ.type())
                        && !"BREAK".equalsIgnoreCase(occ.courseCode());
            });

            if (isLunchSlot && !hasClassesInSlot) {
                Div breakCell = new Div();
                breakCell.setText("Lunch Break");
                breakCell.addClassNames("timetable-break-cell", "neo-tt-break-cell", "font-ui");
                grid.add(breakCell);
                continue;
            }

            // Normal day cells (Mon–Sat)
            for (String day : days) {
                Div cell = new Div();
                boolean isToday = "Tue".equalsIgnoreCase(day);
                cell.addClassNames("timetable-body-cell", "neo-tt-body-cell");
                if (isToday) {
                    cell.addClassName("neo-tt-today-cell");
                }

                List<ClassOccurrence> occs = findOccurrencesForSlot(occurrences, day, slot);

                if (occs.isEmpty()) {
                    Div empty = new Div();
                    empty.setText("—");
                    empty.addClassNames("cell-empty", "font-mono");
                    cell.add(empty);
                } else {
                    for (ClassOccurrence occ : occs) {
                        cell.add(createClassCell(occ));
                    }
                }

                grid.add(cell);
            }
        }

        scrollWrapper.add(grid);
        card.add(scrollWrapper);

        // Legend at bottom right
        card.add(createLegend());

        return card;
    }

    private Component createClassCell(ClassOccurrence occ) {
        Div cell = new Div();
        cell.addClassNames("neo-class-block", "font-ui");

        String typeClass = switch (occ.type().toLowerCase()) {
            case "lab" -> "cell-lab neo-class-lab";
            case "tutorial" -> "cell-tutorial neo-class-tutorial";
            default -> "cell-lecture neo-class-lecture";
        };
        for (String tc : typeClass.split(" ")) {
            cell.addClassName(tc);
        }

        if (occ.isCancelled()) {
            cell.addClassName("cell-cancelled");
        }

        Div code = new Div();
        code.setText(occ.displayCode());
        code.addClassNames("cell-course-code", "neo-class-code", "font-ui");

        if (occ.isCancelled()) {
            code.addClassName("cell-cancelled-line");
        }

        Div room = new Div();
        room.setText(occ.room());
        room.addClassNames("cell-room", "neo-class-room", "font-mono");

        Div timeSpan = new Div();
        timeSpan.setText(occ.startTime() + "–" + occ.endTime());
        timeSpan.addClassNames("cell-sub-time", "neo-class-time", "font-mono");

        cell.add(code, room, timeSpan);

        if (occ.isNextUp()) {
            Span nextChip = new Span(occ.nextUpBadge() != null ? occ.nextUpBadge().toUpperCase() : "IN 30 MIN");
            nextChip.addClassNames("neo-class-next-chip", "font-mono");
            cell.add(nextChip);
        }

        if (occ.isCancelled()) {
            Div reason = new Div();
            reason.setText(occ.cancellationReason() != null ? occ.cancellationReason() : "Cancelled");
            reason.addClassNames("cell-cancellation-reason", "font-ui");
            cell.add(reason);
        }

        if (occ.isExtra()) {
            Span extraBadge = new Span("EXTRA");
            extraBadge.addClassNames("cell-extra-badge", "font-mono");
            cell.add(extraBadge);
        }

        return cell;
    }

    private String parseSemesterKey(String semester) {
        if (semester == null) return "sem-5";
        String lower = semester.toLowerCase();
        if (lower.contains("all") || lower.contains("master")) return "all";
        if (lower.contains("mca")) {
            if (lower.contains("sem 1") || lower.contains("semester i") || lower.contains("mca 1") || lower.contains("mca-1") || lower.contains("mca - semester i")) return "mca-1";
            if (lower.contains("sem 2") || lower.contains("semester ii") || lower.contains("mca 2") || lower.contains("mca-2") || lower.contains("mca - semester ii")) return "mca-2";
            if (lower.contains("sem 3") || lower.contains("semester iii") || lower.contains("mca 3") || lower.contains("mca-3") || lower.contains("mca - semester iii")) return "mca-3";
            if (lower.contains("sem 4") || lower.contains("semester iv") || lower.contains("mca 4") || lower.contains("mca-4") || lower.contains("mca - semester iv")) return "mca-4";
            return "mca";
        }
        if (lower.contains("mtech") || lower.contains("m.tech")) {
            if (lower.contains("sem 1") || lower.contains("semester i") || lower.contains("mtech 1") || lower.contains("mtech-1") || lower.contains("m.tech - semester i")) return "mtech-1";
            if (lower.contains("sem 2") || lower.contains("semester ii") || lower.contains("mtech 2") || lower.contains("mtech-2") || lower.contains("m.tech - semester ii")) return "mtech-2";
            if (lower.contains("sem 3") || lower.contains("sem 4") || lower.contains("semester iii") || lower.contains("semester iv") || lower.contains("mtech - semester iii")) return "mtech-34";
            return "mtech";
        }
        if (lower.contains("sem 8") || lower.contains("semester viii") || lower.contains("sem-8")) return "sem-8";
        if (lower.contains("sem 7") || lower.contains("semester vii") || lower.contains("sem-7")) return "sem-7";
        if (lower.contains("sem 6") || lower.contains("semester vi") || lower.contains("sem-6")) return "sem-6";
        if (lower.contains("sem 5") || lower.contains("semester v") || lower.contains("sem-5")) return "sem-5";
        if (lower.contains("sem 4") || lower.contains("semester iv") || lower.contains("sem-4")) return "sem-4";
        if (lower.contains("sem 3") || lower.contains("semester iii") || lower.contains("sem-3")) return "sem-3";
        if (lower.contains("sem 2") || lower.contains("semester ii") || lower.contains("sem-2")) return "sem-2";
        if (lower.contains("sem 1") || lower.contains("semester i") || lower.contains("sem-1")) return "sem-1";
        return "sem-5";
    }

    private List<String> getTimeSlotsForSemester(String semesterKey) {
        if ("all".equalsIgnoreCase(semesterKey)) {
            return List.of(
                    "08:00–09:30",
                    "09:30–11:00",
                    "11:15–13:15",
                    "13:15–14:00",
                    "13:45–15:15",
                    "15:15–16:45",
                    "16:45–18:15"
            );
        } else if (semesterKey.contains("sem-1")) {
            return List.of("08:00–09:30", "09:30–11:00", "11:15–12:30", "13:15–14:00", "13:30–15:00", "15:15–17:15");
        } else if (semesterKey.contains("sem-2")) {
            return List.of("08:00–09:30", "09:30–11:00", "11:15–12:30", "13:15–14:00", "14:00–16:00");
        } else if (semesterKey.contains("sem-3")) {
            return List.of("08:00–10:00", "11:15–13:15", "13:15–14:00", "13:45–15:15", "15:15–17:15", "17:30–18:30");
        } else if (semesterKey.contains("sem-4")) {
            return List.of("09:30–11:00", "11:15–13:15", "13:15–14:00", "14:00–16:00");
        } else if (semesterKey.contains("sem-6")) {
            return List.of("11:15–12:45", "13:15–14:00", "14:00–15:30", "16:00–18:00");
        } else if (semesterKey.contains("sem-7")) {
            return List.of("11:15–13:15", "13:00–15:00", "13:15–14:00", "15:15–16:45", "16:45–18:15");
        } else if (semesterKey.contains("sem-8")) {
            return List.of("09:30–11:00", "11:15–13:00", "13:15–14:00", "14:00–17:00");
        } else if (semesterKey.contains("mca")) {
            return List.of("08:00–09:30", "09:30–11:00", "11:15–12:30", "13:15–14:00", "13:15–15:15", "15:30–17:00");
        } else if (semesterKey.contains("mtech")) {
            return List.of("09:30–11:00", "11:15–13:15", "13:15–14:00", "14:00–17:00", "15:15–17:15");
        }
        return List.of(
                "09:00–11:00",
                "11:15–13:15",
                "13:15–14:00",
                "15:15–16:45",
                "16:45–18:15"
        );
    }

    private List<ClassOccurrence> findOccurrencesForSlot(List<ClassOccurrence> occurrences, String day, String slot) {
        List<ClassOccurrence> matches = new ArrayList<>();
        String[] parts = slot.split("–");
        int slotStart = toMinutes(parts[0]);
        int slotEnd = parts.length > 1 ? toMinutes(parts[1]) : slotStart + 60;

        for (ClassOccurrence occ : occurrences) {
            if (!day.equalsIgnoreCase(occ.dayOfWeek())) {
                continue;
            }
            if ("Break".equalsIgnoreCase(occ.type()) || "BREAK".equalsIgnoreCase(occ.courseCode())) {
                continue;
            }
            int classStart = toMinutes(occ.startTime());
            if (classStart >= slotStart && classStart < slotEnd) {
                matches.add(occ);
            }
        }
        return matches;
    }

    private int toMinutes(String time) {
        try {
            String[] parts = time.trim().split(":");
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (Exception ex) {
            return 0;
        }
    }

    private Component createLegend() {
        HorizontalLayout legend = new HorizontalLayout();
        legend.addClassNames("timetable-legend-row", "neo-timetable-legend", "font-ui");
        legend.setAlignItems(FlexComponent.Alignment.CENTER);
        legend.setWidthFull();

        legend.add(
                createLegendItem("swatch-lecture", "Lecture"),
                createLegendItem("swatch-lab", "Lab"),
                createLegendItem("swatch-tutorial", "Tutorial"),
                createLegendItem("swatch-break", "Break")
        );

        return legend;
    }

    private Component createLegendItem(String swatchClass, String label) {
        HorizontalLayout item = new HorizontalLayout();
        item.addClassNames("legend-item", "neo-tt-legend-item");
        item.setAlignItems(FlexComponent.Alignment.CENTER);
        item.setSpacing(true);

        Div swatch = new Div();
        swatch.addClassNames("legend-swatch", "neo-tt-legend-swatch", swatchClass);

        Span text = new Span(label);
        text.addClassNames("font-ui", "text-muted", "neo-tt-legend-label");

        item.add(swatch, text);
        return item;
    }

    private Div renderDayAgenda(List<ClassOccurrence> allOccurrences, String targetDay) {
        Div container = new Div();
        container.addClassNames("day-agenda-container", "campus-card", "neo-timetable-card");

        H2 dayHeading = new H2("Tuesday, 10 November 2026 (Today)");
        dayHeading.addClassNames("text-navy", "font-ui");
        container.add(dayHeading);

        List<ClassOccurrence> dayClasses = allOccurrences.stream()
                .filter(occ -> targetDay.equalsIgnoreCase(occ.dayOfWeek()))
                .sorted(Comparator.comparing(ClassOccurrence::startTime))
                .toList();

        if (dayClasses.isEmpty()) {
            container.add(new EmptyState(
                    "No Classes Today",
                    "There are no scheduled lectures or tutorials for this day."
            ));
        } else {
            for (ClassOccurrence occ : dayClasses) {
                if ("Break".equalsIgnoreCase(occ.type())) {
                    Div breakBanner = new Div();
                    breakBanner.addClassNames("cell-break-full", "font-ui");
                    breakBanner.setText(occ.startTime() + "–" + occ.endTime() + " · " + occ.courseName());
                    container.add(breakBanner);
                } else {
                    ScheduleRow row = new ScheduleRow(
                            occ.startTime() + "–" + occ.endTime(),
                            occ.displayCode() + " · " + occ.courseName(),
                            occ.room(),
                            occ.instructor(),
                            occ.isNextUp() ? occ.nextUpBadge() : null
                    );
                    container.add(row);
                }
            }
        }

        return container;
    }

    private Div renderListView(List<ClassOccurrence> allOccurrences) {
        Div container = new Div();
        container.addClassName("list-view-container");

        for (String day : days) {
            List<ClassOccurrence> dayClasses = allOccurrences.stream()
                    .filter(occ -> day.equalsIgnoreCase(occ.dayOfWeek()))
                    .sorted(Comparator.comparing(ClassOccurrence::startTime))
                    .toList();

            Div dayCard = new Div();
            dayCard.addClassNames("campus-card", "list-day-section", "neo-timetable-card");

            H2 dayHeader = new H2(day + " (" + dayDates.getOrDefault(day, "") + ")");
            dayHeader.addClassNames("list-day-header", "font-ui", "text-navy");
            dayCard.add(dayHeader);

            if (dayClasses.isEmpty()) {
                Paragraph empty = new Paragraph("No scheduled classes.");
                empty.addClassNames("font-ui", "text-muted");
                dayCard.add(empty);
            } else {
                for (ClassOccurrence occ : dayClasses) {
                    if ("Break".equalsIgnoreCase(occ.type())) {
                        Div breakBanner = new Div();
                        breakBanner.addClassNames("cell-break-full", "font-ui");
                        breakBanner.setText(occ.startTime() + "–" + occ.endTime() + " · Lunch Break");
                        dayCard.add(breakBanner);
                    } else {
                        ScheduleRow row = new ScheduleRow(
                                occ.startTime() + "–" + occ.endTime(),
                                occ.displayCode() + " · " + occ.courseName(),
                                occ.room(),
                                occ.instructor(),
                                occ.isNextUp() ? occ.nextUpBadge() : null
                        );
                        dayCard.add(row);
                    }
                }
            }

            container.add(dayCard);
        }

        return container;
    }
}
