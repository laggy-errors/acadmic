package com.iiitv.campus.ui.views;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.AttendanceStatus;
import com.iiitv.campus.dto.CourseAttendance;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.*;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "kitchen-sink", layout = MainLayout.class)
@PageTitle("Campus Theme - Reusable Components & Kitchen Sink")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class KitchenSinkView extends VerticalLayout {

    public KitchenSinkView(ApiClient apiClient) {
        addClassName("view-container");
        setSizeFull();
        setSpacing(true);

        // DEMO 1: PageHeader (title, subtitle, primary action, breadcrumbs)
        Button newRequestBtn = new Button("New Request", e -> Notification.show("Primary action triggered"));
        newRequestBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        PageHeader headerDemo = new PageHeader(
                "Reusable Components & Kitchen Sink",
                "Presentation component library implementing campus design tokens with zero API calls",
                newRequestBtn,
                List.of("Portal", "Design System", "Kitchen Sink")
        );
        add(headerDemo);

        // DEMO 2: StatCard (label, big mono value, optional ring)
        add(createStatCardsDemo(apiClient));

        // DEMO 3: AttendanceRing (SVG ring, percentage in mono, label)
        add(createAttendanceRingsDemo());

        // DEMO 4: StatusChip (SAFE / WATCH / SHORT / CRITICAL / HIGH / MEDIUM / NEXT_UP)
        add(createStatusChipsDemo());

        // DEMO 5 & 6: CardSection & ScheduleRow
        add(createCardSectionAndScheduleDemo());

        // DEMO 7: EmptyState (specific written copy, no illustration)
        add(createEmptyStateDemo());

        // DEMO 8: SkeletonBlock for loading states
        add(createSkeletonDemo());

        // Baseline Themed Form Controls & Grid
        add(createFormAndGridDemo(apiClient));
    }

    private Component createStatCardsDemo(ApiClient apiClient) {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);

        H2 title = new H2("2. StatCard (Label, Big Mono Value, Optional Ring)");
        title.addClassNames("text-navy", "font-ui");
        section.add(title);

        HorizontalLayout cardsRow = new HorizontalLayout();
        cardsRow.setWidthFull();
        cardsRow.setSpacing(true);

        // Card 1: Attendance with AttendanceRing
        AttendanceRing miniRing = new AttendanceRing(82.5, "Overall", AttendanceStatus.SAFE, AttendanceRing.Size.SMALL);
        StatCard card1 = new StatCard("Attendance", "82.5%", miniRing);

        // Card 2: Pending Tasks
        StatCard card2 = new StatCard("Pending Tasks", "3");

        // Card 3: Assignments with Due label
        Span dueTag = new Span("Due");
        dueTag.addClassNames("chip", "chip-neutral");
        StatCard card3 = new StatCard("Assignments", "2", null, dueTag);

        // Card 4: Upcoming Classes with orange Next-Up chip
        StatusChip in30Min = StatusChip.nextUp("in 30 min");
        StatCard card4 = new StatCard("Upcoming Classes", "1", null, in30Min);

        cardsRow.add(card1, card2, card3, card4);
        section.add(cardsRow);
        return section;
    }

    private Component createAttendanceRingsDemo() {
        Div card = new Div();
        card.addClassNames("campus-card");

        H2 title = new H2("3. AttendanceRing (SVG Ring, Percentage in Mono, Label)");
        title.addClassNames("text-navy", "font-ui");

        Paragraph desc = new Paragraph("Self-contained SVG circular gauges rendered with mono percentages, clean track, and status color states.");
        desc.addClassNames("font-ui", "text-muted");

        HorizontalLayout ringsRow = new HorizontalLayout();
        ringsRow.setWidthFull();
        ringsRow.setAlignItems(FlexComponent.Alignment.CENTER);
        ringsRow.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);

        // SAFE Ring
        VerticalLayout r1 = new VerticalLayout(
                new AttendanceRing(82.5, "Overall", AttendanceStatus.SAFE, AttendanceRing.Size.LARGE),
                new Span("SAFE Status (82.5%)")
        );
        r1.setAlignItems(FlexComponent.Alignment.CENTER);

        // WATCH Ring
        VerticalLayout r2 = new VerticalLayout(
                new AttendanceRing(74.0, "Overall", AttendanceStatus.WATCH, AttendanceRing.Size.MEDIUM),
                new Span("WATCH Status (74.0%)")
        );
        r2.setAlignItems(FlexComponent.Alignment.CENTER);

        // SHORT Ring
        VerticalLayout r3 = new VerticalLayout(
                new AttendanceRing(65.5, "Overall", AttendanceStatus.SHORT, AttendanceRing.Size.MEDIUM),
                new Span("SHORT Status (65.5%)")
        );
        r3.setAlignItems(FlexComponent.Alignment.CENTER);

        // Small Gauge
        VerticalLayout r4 = new VerticalLayout(
                new AttendanceRing(90.0, "", AttendanceStatus.SAFE, AttendanceRing.Size.SMALL),
                new Span("Small Compact (90.0%)")
        );
        r4.setAlignItems(FlexComponent.Alignment.CENTER);

        ringsRow.add(r1, r2, r3, r4);
        card.add(title, desc, ringsRow);
        return card;
    }

    private Component createStatusChipsDemo() {
        Div card = new Div();
        card.addClassNames("campus-card");

        H2 title = new H2("4. StatusChip (SAFE / WATCH / SHORT / CRITICAL / HIGH / MEDIUM / NEXT_UP)");
        title.addClassNames("text-navy", "font-ui");

        Paragraph desc = new Paragraph("Design rule: Status is never shown by colour alone — always paired with an explicit uppercase text label.");
        desc.addClassNames("font-ui", "text-muted");

        HorizontalLayout chipsRow = new HorizontalLayout();
        chipsRow.setAlignItems(FlexComponent.Alignment.CENTER);
        chipsRow.setSpacing(true);

        chipsRow.add(
                StatusChip.safe("SAFE"),
                StatusChip.watch("WATCH"),
                StatusChip.shortStatus("SHORT"),
                StatusChip.critical("CRITICAL"),
                StatusChip.high("HIGH"),
                StatusChip.medium("MEDIUM"),
                StatusChip.nextUp("NOW 14:35"),
                StatusChip.nextUp("IN 30 MIN")
        );

        card.add(title, desc, chipsRow);
        return card;
    }

    private Component createCardSectionAndScheduleDemo() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setSpacing(true);

        // Left: CardSection wrapping ScheduleRows
        CardSection scheduleSection = new CardSection(
                "Today's Schedule",
                "View Timetable →",
                com.iiitv.campus.ui.views.student.TimetableView.class
        );

        ScheduleRow row1 = new ScheduleRow("13:15–14:00", "Lunch Break", "Dining Hall", "");
        ScheduleRow row2 = new ScheduleRow("15:15–16:45", "Computer Networks", "ICD-2125", "Dr. S. Kulkarni", "in 45 min");
        ScheduleRow row3 = new ScheduleRow("17:00–18:00", "DBMS Consultation", "ICD-2113", "Dr. N. Patel");

        scheduleSection.addContent(row1, row2, row3);

        // Right: CardSection with simple content
        CardSection announcementsSection = new CardSection(
                "Campus Announcements",
                "View All →",
                com.iiitv.campus.ui.views.student.AnnouncementsView.class
        );

        ScheduleRow ann1 = new ScheduleRow("20 Nov 2026", "Mid-Sem Examination Schedule", "Academic", "Controller of Exams");
        ScheduleRow ann2 = new ScheduleRow("18 Nov 2026", "Project Submission Deadline", "Academic", "Faculty Advisor");
        ScheduleRow ann3 = new ScheduleRow("17 Nov 2026", "Guest Lecture on AI", "Auditorium", "ACM Student Chapter");

        announcementsSection.addContent(ann1, ann2, ann3);

        layout.add(scheduleSection, announcementsSection);
        return layout;
    }

    private Component createEmptyStateDemo() {
        Div card = new Div();
        card.addClassNames("campus-card");

        H2 title = new H2("7. EmptyState (Specific Written Copy, No Illustration)");
        title.addClassNames("text-navy", "font-ui");

        Button submitRequestBtn = new Button("Submit Course Request", e -> Notification.show("Action clicked"));
        submitRequestBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        EmptyState emptyState = new EmptyState(
                "No Pending Course Registrations",
                "You are enrolled in all 4 mandatory core courses for Semester V. Elective add/drop window opens on 15 December 2026.",
                submitRequestBtn
        );

        card.add(title, emptyState);
        return card;
    }

    private Component createSkeletonDemo() {
        Div card = new Div();
        card.addClassNames("campus-card");

        H2 title = new H2("8. SkeletonBlock (Loading States, Shimmer Animation, Reduced Motion Aware)");
        title.addClassNames("text-navy", "font-ui");

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);

        HorizontalLayout headerRow = new HorizontalLayout();
        headerRow.setAlignItems(FlexComponent.Alignment.CENTER);
        headerRow.setSpacing(true);

        SkeletonBlock avatarSkeleton = SkeletonBlock.circle("40px");
        VerticalLayout textLines = new VerticalLayout();
        textLines.setPadding(false);
        textLines.setSpacing(false);
        textLines.add(SkeletonBlock.title("200px"), SkeletonBlock.text("140px"));

        headerRow.add(avatarSkeleton, textLines);

        SkeletonBlock rectBlock = new SkeletonBlock("100%", "72px", SkeletonBlock.Variant.RECTANGLE);
        SkeletonBlock textBlock1 = SkeletonBlock.text("100%");
        SkeletonBlock textBlock2 = SkeletonBlock.text("75%");

        layout.add(headerRow, rectBlock, textBlock1, textBlock2);
        card.add(title, layout);
        return card;
    }

    private Component createFormAndGridDemo(ApiClient apiClient) {
        Div card = new Div();
        card.addClassNames("campus-card");

        H2 title = new H2("Form Controls, Tabs & Themed Grid (44px Rows, No Zebra)");
        title.addClassNames("text-navy", "font-ui");

        Tabs tabs = new Tabs(
                new Tab("Course-wise Attendance"),
                new Tab("Weekly View"),
                new Tab("Guidance")
        );
        tabs.setWidthFull();

        Grid<CourseAttendance> grid = new Grid<>(CourseAttendance.class, false);
        grid.setAllRowsVisible(true);

        grid.addColumn(CourseAttendance::courseCode)
                .setHeader("Code")
                .setWidth("100px")
                .setFlexGrow(0)
                .setClassNameGenerator(item -> "font-mono");

        grid.addColumn(CourseAttendance::courseName)
                .setHeader("Course")
                .setFlexGrow(2);

        grid.addColumn(CourseAttendance::total)
                .setHeader("Total")
                .setWidth("80px")
                .setFlexGrow(0)
                .setClassNameGenerator(item -> "font-mono");

        grid.addColumn(CourseAttendance::present)
                .setHeader("Present")
                .setWidth("90px")
                .setFlexGrow(0)
                .setClassNameGenerator(item -> "font-mono");

        grid.addColumn(CourseAttendance::absent)
                .setHeader("Absent")
                .setWidth("90px")
                .setFlexGrow(0)
                .setClassNameGenerator(item -> "font-mono");

        grid.addColumn(CourseAttendance::excused)
                .setHeader("Excused")
                .setWidth("90px")
                .setFlexGrow(0)
                .setClassNameGenerator(item -> "font-mono");

        grid.addColumn(item -> String.format("%.1f%%", item.percentage()))
                .setHeader("Percentage")
                .setWidth("120px")
                .setFlexGrow(0)
                .setClassNameGenerator(item -> "font-mono");

        grid.addColumn(new ComponentRenderer<>(item -> {
            return item.status() == AttendanceStatus.SAFE
                    ? StatusChip.safe("SAFE")
                    : StatusChip.watch("WATCH");
        })).setHeader("Status").setWidth("110px").setFlexGrow(0);

        grid.setItems(apiClient.getCourseAttendanceList());

        card.add(title, tabs, grid);
        return card;
    }
}
