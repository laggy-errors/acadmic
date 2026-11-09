package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.AttendanceStatus;
import com.iiitv.campus.dto.AttendanceSummary;
import com.iiitv.campus.dto.CourseAttendance;
import com.iiitv.campus.dto.DayAttendanceMark;
import com.iiitv.campus.dto.WeeklyAttendanceRecord;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.AttendanceRing;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.components.StatusChip;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "attendance", layout = MainLayout.class)
@PageTitle("Attendance - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.ADMIN})
public class AttendanceView extends VerticalLayout {

    private final ApiClient apiClient;

    public AttendanceView(ApiClient apiClient) {
        this.apiClient = apiClient;

        addClassName("view-container");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        // All figures loaded directly from API - UI does zero calculation
        AttendanceSummary summary = apiClient.getAttendanceSummary();
        List<CourseAttendance> courseList = apiClient.getCourseAttendanceList();
        List<WeeklyAttendanceRecord> weeklyRecords = apiClient.getWeeklyAttendanceRegister("Semester V", "Week 10");

        // 1. Header with Semester Selector
        Select<String> semesterSelect = new Select<>();
        semesterSelect.setItems("Semester VIII", "Semester VII", "Semester VI", "Semester V", "Semester IV", "Semester III", "Semester II", "Semester I");
        semesterSelect.setValue("Semester V");
        semesterSelect.setAriaLabel("Select academic semester");
        semesterSelect.addClassNames("neo-glass-select", "font-ui");
        semesterSelect.setWidth("140px");

        PageHeader header = new PageHeader(
                "Attendance",
                "Track your class attendance and progress",
                semesterSelect
        );
        header.addClassNames("neo-attendance-header");

        // 2. Summary Strip (Ring 82.5%, Total Classes 160, Attended 132, Absent 28, Threshold 75%, SAFE box)
        Div summaryStrip = buildSummaryStrip(summary);

        // 3. Main Content Card with Tabs
        Div mainCard = new Div();
        mainCard.addClassNames("campus-card", "neo-attendance-tabs-card");

        Tab courseWiseTab = new Tab("Course-wise");
        Tab weeklyTab = new Tab("Weekly View");
        Tab guidanceTab = new Tab("Attendance Guidance");

        Tabs tabs = new Tabs(courseWiseTab, weeklyTab, guidanceTab);
        tabs.setSelectedTab(courseWiseTab);
        tabs.setWidthFull();
        tabs.addClassNames("neo-maroon-tabs", "font-ui");

        Div tabContentArea = new Div();
        tabContentArea.setWidthFull();

        // 4. Tab 1: Course-wise Grid
        Div courseWisePanel = buildCourseWisePanel(courseList, summary);

        // 5. Tab 2: Weekly View (Mon-Sun Register-style Strip per course)
        Div weeklyPanel = buildWeeklyRegisterPanel(weeklyRecords);
        weeklyPanel.setVisible(false);

        // 6. Tab 3: Attendance Guidance details
        Div guidancePanel = buildGuidanceDetailPanel(summary);
        guidancePanel.setVisible(false);

        tabs.addSelectedChangeListener(e -> {
            Tab selected = tabs.getSelectedTab();
            courseWisePanel.setVisible(selected == courseWiseTab);
            weeklyPanel.setVisible(selected == weeklyTab);
            guidancePanel.setVisible(selected == guidanceTab);
        });

        mainCard.add(tabs, tabContentArea);
        tabContentArea.add(courseWisePanel, weeklyPanel, guidancePanel);

        // 7. Attendance Guidance Bottom Card (Matching Neo on glass mockup)
        Div guidanceBottomCard = buildGuidanceBottomCard(summary);

        add(header, summaryStrip, mainCard, guidanceBottomCard);
    }

    private Div buildSummaryStrip(AttendanceSummary summary) {
        Div card = new Div();
        card.addClassNames("campus-card", "attendance-top-card", "neo-attendance-summary-card");

        Div leftGroup = new Div();
        leftGroup.addClassNames("attendance-top-left", "neo-att-top-left");

        // Circular Ring 82.5%
        AttendanceRing ring = new AttendanceRing(summary.percentage(), "Overall", summary.status(), AttendanceRing.Size.LARGE);
        ring.addClassName("neo-att-ring");
        leftGroup.add(ring);

        // Metrics Group (Total Classes 160, Attended 132, Absent 28, Threshold 75%)
        Div metricsGroup = new Div();
        metricsGroup.addClassNames("attendance-metrics-group", "neo-att-metrics");

        metricsGroup.add(createMetricColumn("Total Classes", String.valueOf(summary.totalClasses()), ""));
        metricsGroup.add(createMetricColumn("Attended", String.valueOf(summary.attendedClasses()), "attended"));
        metricsGroup.add(createMetricColumn("Absent", String.valueOf(summary.absentClasses()), "absent"));
        metricsGroup.add(createMetricColumn("Threshold", String.format("%.0f%%", summary.threshold()), ""));

        leftGroup.add(metricsGroup);

        // Status Box on Right (Mint SAFE, Yellow WATCH, Rose DANGER)
        Div banner = new Div();
        banner.addClassNames("attendance-safe-banner", "neo-attendance-status-banner");
        if (summary.status() == AttendanceStatus.SAFE) {
            banner.addClassNames("status-safe", "neo-att-banner-safe");
        } else if (summary.status() == AttendanceStatus.WATCH) {
            banner.addClassNames("status-watch", "neo-att-banner-watch");
        } else {
            banner.addClassNames("status-short", "neo-att-banner-short");
        }

        Div bannerTitle = new Div();
        bannerTitle.addClassNames("attendance-safe-banner-title", "neo-att-banner-title", "font-ui");
        bannerTitle.setText(summary.status() == AttendanceStatus.SHORT ? "DANGER" : summary.status().name());

        Div bannerSub = new Div();
        bannerSub.addClassNames("attendance-safe-banner-sub", "neo-att-banner-sub", "font-ui");
        bannerSub.setText(summary.status() == AttendanceStatus.SAFE
                ? "You are above the required threshold."
                : (summary.status() == AttendanceStatus.WATCH
                ? "Approaching threshold limit."
                : "Shortage alert: below 75% threshold."));

        banner.add(bannerTitle, bannerSub);

        card.add(leftGroup, banner);
        return card;
    }

    private Div createMetricColumn(String label, String value, String variantClass) {
        Div col = new Div();
        col.addClassNames("attendance-metric-col", "neo-att-metric-col");

        Span labelSpan = new Span(label);
        labelSpan.addClassNames("attendance-metric-label", "neo-att-metric-label", "font-ui");

        Span valSpan = new Span(value);
        valSpan.addClassNames("attendance-metric-num", "neo-att-metric-num", "font-mono");
        if (!variantClass.isBlank()) {
            valSpan.addClassName(variantClass);
        }

        col.add(labelSpan, valSpan);
        return col;
    }

    private Div buildCourseWisePanel(List<CourseAttendance> courseList, AttendanceSummary summary) {
        Div panel = new Div();
        panel.setWidthFull();

        // 1. Course-wise Vaadin Grid with mono header caps and hairline dividers
        Grid<CourseAttendance> grid = new Grid<>(CourseAttendance.class, false);
        grid.setWidthFull();
        grid.setAllRowsVisible(true);
        grid.addClassNames("neo-attendance-grid");

        grid.addComponentColumn(c -> {
            Span s = new Span(c.courseCode() + " – " + c.courseName());
            s.addClassNames("font-ui", "neo-course-name");
            return s;
        }).setHeader(createHeader("Course")).setFlexGrow(3).setAutoWidth(true);

        grid.addComponentColumn(c -> {
            Span s = new Span(String.valueOf(c.total()));
            s.addClassNames("font-mono", "neo-att-mono-cell");
            return s;
        }).setHeader(createHeader("Total")).setFlexGrow(1).setAutoWidth(true);

        grid.addComponentColumn(c -> {
            Span s = new Span(String.valueOf(c.present()));
            s.addClassNames("font-mono", "neo-att-mono-cell");
            return s;
        }).setHeader(createHeader("Present")).setFlexGrow(1).setAutoWidth(true);

        grid.addComponentColumn(c -> {
            Span s = new Span(String.valueOf(c.absent()));
            s.addClassNames("font-mono", "neo-att-mono-cell");
            return s;
        }).setHeader(createHeader("Absent")).setFlexGrow(1).setAutoWidth(true);

        grid.addComponentColumn(c -> {
            Span s = new Span(String.valueOf(c.excused()));
            s.addClassNames("font-mono", "neo-att-mono-cell");
            return s;
        }).setHeader(createHeader("Excused")).setFlexGrow(1).setAutoWidth(true);

        grid.addComponentColumn(c -> {
            Span s = new Span(String.format("%.1f%%", c.percentage()));
            s.addClassNames("font-mono", "neo-att-mono-cell");
            return s;
        }).setHeader(createHeader("Percentage")).setFlexGrow(1).setAutoWidth(true);

        grid.addComponentColumn(c -> switch (c.status()) {
            case SAFE -> new StatusChip("SAFE", StatusChip.Type.SAFE);
            case WATCH -> new StatusChip("WATCH", StatusChip.Type.WATCH);
            case SHORT -> new StatusChip("DANGER", StatusChip.Type.CRITICAL);
        }).setHeader(createHeader("Status")).setFlexGrow(1).setAutoWidth(true);

        grid.setItems(courseList);
        panel.add(grid);

        return panel;
    }

    private Span createHeader(String title) {
        Span h = new Span(title.toUpperCase());
        h.addClassNames("font-mono", "neo-th-caps");
        return h;
    }

    private Div buildWeeklyRegisterPanel(List<WeeklyAttendanceRecord> records) {
        Div panel = new Div();
        panel.setWidthFull();

        // Weekly Header Controls
        HorizontalLayout controls = new HorizontalLayout();
        controls.setWidthFull();
        controls.setJustifyContentMode(JustifyContentMode.BETWEEN);
        controls.setAlignItems(Alignment.CENTER);
        controls.addClassName("neo-weekly-reg-controls");

        H3 weekLabel = new H3("Week 10 (2 Nov – 8 Nov 2026)");
        weekLabel.addClassNames("font-ui", "text-navy", "margin-none", "neo-weekly-label");

        HorizontalLayout weekNav = new HorizontalLayout();
        weekNav.setSpacing(true);

        Button prevBtn = new Button("← Previous Week");
        prevBtn.addClassNames("btn-secondary", "neo-btn-glass", "font-ui");
        prevBtn.getElement().setAttribute("aria-label", "View previous week attendance register");
        prevBtn.addClickListener(e ->
                Notification.show("Showing Week 9 attendance log", 2000, Notification.Position.BOTTOM_END));

        Button nextBtn = new Button("Next Week →");
        nextBtn.addClassNames("btn-secondary", "neo-btn-glass", "font-ui");
        nextBtn.getElement().setAttribute("aria-label", "View next week attendance register");
        nextBtn.addClickListener(e ->
                Notification.show("Showing Week 11 attendance log", 2000, Notification.Position.BOTTOM_END));

        weekNav.add(prevBtn, nextBtn);
        controls.add(weekLabel, weekNav);
        panel.add(controls);

        // Mon-Sun Register Strip Table
        Div tableWrap = new Div();
        tableWrap.addClassNames("attendance-register-wrap", "neo-att-register-wrap", "mb-m");

        StringBuilder html = new StringBuilder();
        html.append("<table class=\"attendance-register-table neo-att-register-table\">");
        html.append("<thead><tr>")
                .append("<th class=\"col-course font-mono\">COURSE</th>")
                .append("<th class=\"font-mono\">MON<br><span class=\"register-date-sub\">2 Nov</span></th>")
                .append("<th class=\"font-mono\">TUE<br><span class=\"register-date-sub\">3 Nov</span></th>")
                .append("<th class=\"font-mono\">WED<br><span class=\"register-date-sub\">4 Nov</span></th>")
                .append("<th class=\"font-mono\">THU<br><span class=\"register-date-sub\">5 Nov</span></th>")
                .append("<th class=\"font-mono\">FRI<br><span class=\"register-date-sub\">6 Nov</span></th>")
                .append("<th class=\"font-mono\">SAT<br><span class=\"register-date-sub\">7 Nov</span></th>")
                .append("<th class=\"font-mono\">SUN<br><span class=\"register-date-sub\">8 Nov</span></th>")
                .append("<th class=\"font-mono\">TOTAL</th>")
                .append("<th class=\"font-mono\">ATTENDED</th>")
                .append("<th class=\"font-mono\">ABSENT</th>")
                .append("</tr></thead><tbody>");

        for (WeeklyAttendanceRecord rec : records) {
            html.append("<tr>");
            html.append("<td class=\"col-course\"><strong class=\"font-mono\">").append(rec.courseCode())
                    .append("</strong> <span class=\"font-ui text-main\">").append(rec.courseName()).append("</span></td>");

            for (DayAttendanceMark d : rec.dailyMarks()) {
                String cellClass;
                if ("P".equals(d.mark())) {
                    cellClass = "cell-present";
                } else if ("A".equals(d.mark())) {
                    cellClass = "cell-absent"; // absent cells tinted rose/red!
                } else if ("E".equals(d.mark())) {
                    cellClass = "cell-excused";
                } else {
                    cellClass = "cell-none";
                }

                html.append("<td class=\"").append(cellClass).append("\" title=\"").append(d.tooltip()).append("\">")
                        .append("<span class=\"register-mark\">").append(d.mark()).append("</span>")
                        .append("</td>");
            }

            html.append("<td class=\"font-mono\">").append(rec.weekTotal()).append("</td>")
                    .append("<td class=\"font-mono text-safe\">").append(rec.weekAttended()).append("</td>")
                    .append("<td class=\"font-mono text-short\">").append(rec.weekAbsent()).append("</td>")
                    .append("</tr>");
        }

        html.append("</tbody></table>");
        tableWrap.getElement().setProperty("innerHTML", html.toString());
        panel.add(tableWrap);

        // Legend Strip
        Div legend = new Div();
        legend.addClassNames("register-legend-strip", "neo-reg-legend-strip", "font-ui");
        legend.getElement().setProperty("innerHTML", """
            <span class="legend-item"><span class="register-mark cell-present legend-mark">P</span> Present</span>
            <span class="legend-item"><span class="register-mark cell-absent legend-mark">A</span> Absent (Tinted Red)</span>
            <span class="legend-item"><span class="register-mark cell-excused legend-mark">E</span> Excused Leave</span>
            <span class="legend-item"><span class="font-mono text-muted legend-dash">-</span> No Class Scheduled</span>
            """);
        panel.add(legend);

        return panel;
    }

    private Div buildGuidanceDetailPanel(AttendanceSummary summary) {
        Div panel = new Div();
        panel.setWidthFull();

        H3 title = new H3("Institute Attendance Policy & Projections");
        title.addClassNames("font-ui", "text-navy", "attendance-guidance-title", "neo-guidance-card-title");

        Paragraph p1 = new Paragraph("According to IIIT Vadodara Academic Regulations, every undergraduate student must maintain a minimum of 75% attendance in each registered course to be eligible to appear for the End-Semester Examination.");
        p1.addClassNames("font-ui", "text-main", "neo-guidance-policy-p");

        Paragraph p2 = new Paragraph("Medical leaves and approved extracurricular representations must be submitted with supporting documents to the Academic Office within 7 working days for consideration under excused category.");
        p2.addClassNames("font-ui", "text-muted", "neo-guidance-policy-p");

        Div row = new Div();
        row.addClassNames("guidance-cards-row", "neo-guidance-tiles-row");

        Div card1 = new Div();
        card1.addClassNames("guidance-box", "guidance-box-safe", "neo-guidance-tile", "neo-guidance-mint");
        card1.getElement().setProperty("innerHTML", """
            <div class="guidance-box-label neo-guidance-label font-ui">Current Safe Buffer</div>
            <div class="guidance-box-content neo-guidance-val-row">
                <span class="guidance-box-bold neo-guidance-bold font-mono">%d classes</span>
                <span class="guidance-box-sub neo-guidance-sub font-ui">can still be missed without falling below 75%%</span>
            </div>
            """.formatted(summary.classesCanMiss()));

        Div card2 = new Div();
        card2.addClassNames("guidance-box", "guidance-box-info", "neo-guidance-tile", "neo-guidance-sky");
        card2.getElement().setProperty("innerHTML", """
            <div class="guidance-box-label neo-guidance-label font-ui">Target Honor Roll (%.0f%%)</div>
            <div class="guidance-box-content neo-guidance-val-row">
                <span class="guidance-box-bold neo-guidance-bold font-mono">%d consecutive</span>
                <span class="guidance-box-sub neo-guidance-sub font-ui">classes required to achieve 85%%</span>
            </div>
            """.formatted(summary.targetPercentage(), summary.classesToAttendForTarget()));

        Div card3 = new Div();
        card3.addClassNames("guidance-box", "guidance-box-watch", "neo-guidance-tile", "neo-guidance-yellow");
        card3.getElement().setProperty("innerHTML", """
            <div class="guidance-box-label neo-guidance-label font-ui">End-Semester Forecast</div>
            <div class="guidance-box-content neo-guidance-val-row">
                <span class="guidance-box-bold neo-guidance-bold font-mono">%.1f%%</span>
                <span class="guidance-box-sub neo-guidance-sub font-ui">estimated at current regular presence rate</span>
            </div>
            """.formatted(summary.projectedPercentage()));

        row.add(card1, card2, card3);
        panel.add(title, p1, p2, row);

        return panel;
    }

    private Div buildGuidanceBottomCard(AttendanceSummary summary) {
        Div guidanceSection = new Div();
        guidanceSection.addClassNames("attendance-guidance-card", "neo-attendance-guidance-card");

        H3 guidanceTitle = new H3("Attendance Guidance");
        guidanceTitle.addClassNames("font-ui", "text-navy", "attendance-guidance-title", "neo-guidance-card-title");

        Div row = new Div();
        row.addClassNames("guidance-cards-row", "neo-guidance-tiles-row");

        // Card 1: Safe ("You can still miss 8 classes") - Mint Inset Tile
        Div card1 = new Div();
        card1.addClassNames("guidance-box", "guidance-box-safe", "neo-guidance-tile", "neo-guidance-mint");
        card1.getElement().setProperty("innerHTML", """
            <div class="guidance-box-label neo-guidance-label font-ui">You can still miss</div>
            <div class="guidance-box-content neo-guidance-val-row">
                <span class="guidance-box-bold neo-guidance-bold font-mono">%d classes</span>
                <span class="guidance-box-sub neo-guidance-sub font-ui">(overall)</span>
            </div>
            """.formatted(summary.classesCanMiss()));

        // Card 2: Info ("To reach 85%, attend 4 consecutive classes") - Sky Inset Tile
        Div card2 = new Div();
        card2.addClassNames("guidance-box", "guidance-box-info", "neo-guidance-tile", "neo-guidance-sky");
        card2.getElement().setProperty("innerHTML", """
            <div class="guidance-box-label neo-guidance-label font-ui">To reach %.0f%%, attend</div>
            <div class="guidance-box-content neo-guidance-val-row">
                <span class="guidance-box-bold neo-guidance-bold font-mono">%d consecutive</span>
                <span class="guidance-box-sub neo-guidance-sub font-ui">classes</span>
            </div>
            """.formatted(summary.targetPercentage(), summary.classesToAttendForTarget()));

        // Card 3: Watch ("Projected end-semester 83.8%") - Yellow Inset Tile
        Div card3 = new Div();
        card3.addClassNames("guidance-box", "guidance-box-watch", "neo-guidance-tile", "neo-guidance-yellow");
        card3.getElement().setProperty("innerHTML", """
            <div class="guidance-box-label neo-guidance-label font-ui">Projected end-semester</div>
            <div class="guidance-box-content neo-guidance-val-row">
                <span class="guidance-box-bold neo-guidance-bold font-mono">%.1f%%</span>
                <span class="guidance-box-sub neo-guidance-sub font-ui">(at current trend)</span>
            </div>
            """.formatted(summary.projectedPercentage()));

        row.add(card1, card2, card3);
        guidanceSection.add(guidanceTitle, row);

        return guidanceSection;
    }
}
