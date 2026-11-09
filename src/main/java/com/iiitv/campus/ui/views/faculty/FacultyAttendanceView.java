package com.iiitv.campus.ui.views.faculty;

import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.components.StatusChip;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "faculty/attendance", layout = MainLayout.class)
@PageTitle("Faculty Attendance Marker - IIIT Vadodara Campus Portal")
@RequiresRole({Role.FACULTY, Role.STUDENT, Role.ADMIN})
public class FacultyAttendanceView extends VerticalLayout {

    public enum Mark {
        PRESENT("P", "is-active-p"),
        ABSENT("A", "is-active-a"),
        EXCUSED("E", "is-active-e");

        private final String code;
        private final String cssClass;

        Mark(String code, String cssClass) {
            this.code = code;
            this.cssClass = cssClass;
        }

        public String getCode() {
            return code;
        }

        public String getCssClass() {
            return cssClass;
        }
    }

    public static class RosterItem {
        private final String roll;
        private final String name;
        private final String cumulative;
        private final String statusLabel;
        private final String statusType;
        private Mark currentMark;
        private Div rowElement;
        private Button btnP;
        private Button btnA;
        private Button btnE;

        public RosterItem(String roll, String name, String cumulative, String statusLabel, String statusType, Mark currentMark) {
            this.roll = roll;
            this.name = name;
            this.cumulative = cumulative;
            this.statusLabel = statusLabel;
            this.statusType = statusType;
            this.currentMark = currentMark;
        }

        public String getRoll() { return roll; }
        public String getName() { return name; }
        public String getCumulative() { return cumulative; }
        public String getStatusLabel() { return statusLabel; }
        public String getStatusType() { return statusType; }
        public Mark getCurrentMark() { return currentMark; }
        public void setCurrentMark(Mark currentMark) { this.currentMark = currentMark; }
    }

    private final List<RosterItem> studentList = new ArrayList<>(List.of(
            new RosterItem("202201045", "Raghav A.", "160 / 132 (82.5%)", "SAFE", "SAFE", Mark.PRESENT),
            new RosterItem("202201012", "Ananya Roy", "160 / 146 (91.2%)", "SAFE", "SAFE", Mark.PRESENT),
            new RosterItem("202201078", "Tanmay Sharma", "160 / 118 (73.8%)", "SHORT", "SHORT", Mark.ABSENT),
            new RosterItem("202201095", "Rohan Verma", "160 / 125 (78.1%)", "WATCH", "WATCH", Mark.PRESENT),
            new RosterItem("202201110", "Isha Patel", "160 / 138 (86.2%)", "SAFE", "SAFE", Mark.PRESENT),
            new RosterItem("202201024", "Devansh Gupta", "160 / 130 (81.2%)", "SAFE", "SAFE", Mark.PRESENT),
            new RosterItem("202201033", "Pooja Nair", "160 / 142 (88.7%)", "SAFE", "SAFE", Mark.PRESENT),
            new RosterItem("202201056", "Kavya Joshi", "160 / 116 (72.5%)", "SHORT", "SHORT", Mark.EXCUSED),
            new RosterItem("202201089", "Sameer Khan", "160 / 134 (83.8%)", "SAFE", "SAFE", Mark.PRESENT),
            new RosterItem("202201102", "Aditya Rao", "160 / 127 (79.4%)", "WATCH", "WATCH", Mark.PRESENT)
    ));

    private int selectedIndex = 0;

    // Counter spans
    private final Span countTotalSpan = new Span();
    private final Span countPresentSpan = new Span();
    private final Span countAbsentSpan = new Span();
    private final Span countExcusedSpan = new Span();
    private final Span countRateSpan = new Span();

    public FacultyAttendanceView() {
        addClassName("view-container");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        // Course Selector Header
        ComboBox<String> courseSelect = new ComboBox<>();
        courseSelect.setItems(
                "CS301 · Data Structures (Sec A)",
                "CS301 · Data Structures (Sec B)",
                "CS701 · Advanced Graph Theory"
        );
        courseSelect.setValue("CS301 · Data Structures (Sec A)");
        courseSelect.setAriaLabel("Select course section for attendance");
        courseSelect.setWidth("300px");

        PageHeader header = new PageHeader(
                "Attendance Marker",
                "Record session attendance, toggle marks (P / A / E), and sync with academic records",
                courseSelect
        );

        // 1. Next-Class Card
        Div nextClassCard = createNextClassCard();

        // 2. Live Counters Strip
        Div countersStrip = createLiveCountersStrip();

        // 3. Action Toolbar (Mark All Present, Keyboard Legend, Save)
        HorizontalLayout actionToolbar = createActionToolbar();

        // 4. Roster Table Container
        Div rosterContainer = createRosterTable();

        // Register Keyboard Shortcuts for P, A, E, Arrow Down, Arrow Up
        setupKeyboardShortcuts();

        // Initial Counters & Selection Render
        updateCounters();
        highlightSelectedRow();

        add(header, nextClassCard, countersStrip, actionToolbar, rosterContainer);
    }

    private Div createNextClassCard() {
        Div card = new Div();
        card.addClassName("faculty-next-class-card");

        Div info = new Div();
        info.addClassName("faculty-next-class-info");

        Span sessionTag = new Span("ACTIVE SCHEDULED SESSION · TUE 10 NOV 2026");
        sessionTag.addClassNames("chip", "chip-neutral", "font-mono");

        H3 title = new H3("CS301 Data Structures · Lecture 29 (AVL Rotations & Heaps)");
        title.addClassName("faculty-next-class-title");

        Paragraph meta = new Paragraph("Room ICD-2125 · 15:15 – 16:45 · Section A (60 Enrolled Students)");
        meta.addClassNames("faculty-next-class-meta", "font-mono");

        info.add(sessionTag, title, meta);

        HorizontalLayout rightGroup = new HorizontalLayout();
        rightGroup.setAlignItems(FlexComponent.Alignment.CENTER);
        rightGroup.setSpacing(true);

        StatusChip nextUpChip = StatusChip.nextUp("in 30 min");
        rightGroup.add(nextUpChip);

        card.add(info, rightGroup);
        return card;
    }

    private Div createLiveCountersStrip() {
        Div strip = new Div();
        strip.addClassName("roster-counters-strip");

        strip.add(
                createCounterCell("Enrolled Students", countTotalSpan, ""),
                createCounterCell("Present (P)", countPresentSpan, "val-safe"),
                createCounterCell("Absent (A)", countAbsentSpan, "val-short"),
                createCounterCell("Excused (E)", countExcusedSpan, "val-watch"),
                createCounterCell("Session Projected", countRateSpan, "")
        );

        return strip;
    }

    private Div createCounterCell(String label, Span valSpan, String colorClass) {
        Div cell = new Div();
        cell.addClassName("roster-counter-cell");

        Span l = new Span(label);
        l.addClassName("roster-counter-label");

        valSpan.addClassNames("roster-counter-val", "font-mono");
        if (!colorClass.isBlank()) {
            valSpan.addClassName(colorClass);
        }

        cell.add(l, valSpan);
        return cell;
    }

    private HorizontalLayout createActionToolbar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setWidthFull();
        bar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        bar.setAlignItems(FlexComponent.Alignment.CENTER);

        Span legend = new Span("Keys: [P] Present · [A] Absent · [E] Excused · [↑ / ↓] Navigate");
        legend.addClassName("keyboard-legend-pill");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        Button markAllBtn = new Button("Mark All Present");
        markAllBtn.addClassNames("btn-secondary", "font-ui");
        markAllBtn.addClickListener(e -> {
            for (RosterItem item : studentList) {
                setStudentMark(item, Mark.PRESENT);
            }
            updateCounters();
            Notification.show("Marked all " + studentList.size() + " students as Present.", 2000, Notification.Position.BOTTOM_END);
        });

        Button saveBtn = new Button("Save & Submit Attendance");
        saveBtn.addClassNames("btn-primary", "font-ui");
        saveBtn.addClickListener(e -> {
            long p = studentList.stream().filter(s -> s.getCurrentMark() == Mark.PRESENT).count();
            long a = studentList.stream().filter(s -> s.getCurrentMark() == Mark.ABSENT).count();
            long ex = studentList.stream().filter(s -> s.getCurrentMark() == Mark.EXCUSED).count();
            Notification.show("Attendance locked for CS301 (Lecture 29): " + p + " Present, " + a + " Absent, " + ex + " Excused.", 3500, Notification.Position.BOTTOM_END);
        });

        buttons.add(markAllBtn, saveBtn);
        bar.add(legend, buttons);
        return bar;
    }

    private Div createRosterTable() {
        Div tableContainer = new Div();
        tableContainer.addClassName("roster-table-container");

        for (int i = 0; i < studentList.size(); i++) {
            final int index = i;
            RosterItem item = studentList.get(i);

            Div row = new Div();
            row.addClassName("roster-row");
            item.rowElement = row;

            row.addClickListener(e -> {
                selectedIndex = index;
                highlightSelectedRow();
            });

            // Student Info (Left)
            Div info = new Div();
            info.addClassName("roster-student-info");

            Span rollSpan = new Span(item.getRoll());
            rollSpan.addClassNames("chip", "chip-neutral", "font-mono");

            Span nameSpan = new Span(item.getName());
            nameSpan.addClassName("roster-student-name");

            Span prevSpan = new Span("Term: " + item.getCumulative());
            prevSpan.addClassNames("font-mono", "text-muted");

            StatusChip statusChip = createStatusChip(item.getStatusType(), item.getStatusLabel());

            info.add(rollSpan, nameSpan, prevSpan, statusChip);

            // 3-way toggle buttons (Right)
            Div toggleGroup = new Div();
            toggleGroup.addClassName("roster-toggle-group");

            Button btnP = new Button("P");
            btnP.addClassName("roster-toggle-btn");
            btnP.getElement().setAttribute("aria-label", "Mark " + item.getName() + " Present (Key P)");
            btnP.addClickListener(e -> {
                selectedIndex = index;
                setStudentMark(item, Mark.PRESENT);
                advanceToNext();
            });

            Button btnA = new Button("A");
            btnA.addClassName("roster-toggle-btn");
            btnA.getElement().setAttribute("aria-label", "Mark " + item.getName() + " Absent (Key A)");
            btnA.addClickListener(e -> {
                selectedIndex = index;
                setStudentMark(item, Mark.ABSENT);
                advanceToNext();
            });

            Button btnE = new Button("E");
            btnE.addClassName("roster-toggle-btn");
            btnE.getElement().setAttribute("aria-label", "Mark " + item.getName() + " Excused (Key E)");
            btnE.addClickListener(e -> {
                selectedIndex = index;
                setStudentMark(item, Mark.EXCUSED);
                advanceToNext();
            });

            item.btnP = btnP;
            item.btnA = btnA;
            item.btnE = btnE;

            refreshButtonStyles(item);

            toggleGroup.add(btnP, btnA, btnE);
            row.add(info, toggleGroup);

            tableContainer.add(row);
        }

        return tableContainer;
    }

    private void setStudentMark(RosterItem item, Mark mark) {
        item.setCurrentMark(mark);
        refreshButtonStyles(item);
        updateCounters();
    }

    private void refreshButtonStyles(RosterItem item) {
        item.btnP.removeClassName("is-active-p");
        item.btnA.removeClassName("is-active-a");
        item.btnE.removeClassName("is-active-e");

        if (item.getCurrentMark() == Mark.PRESENT) {
            item.btnP.addClassName("is-active-p");
        } else if (item.getCurrentMark() == Mark.ABSENT) {
            item.btnA.addClassName("is-active-a");
        } else if (item.getCurrentMark() == Mark.EXCUSED) {
            item.btnE.addClassName("is-active-e");
        }
    }

    private void highlightSelectedRow() {
        for (int i = 0; i < studentList.size(); i++) {
            RosterItem item = studentList.get(i);
            if (i == selectedIndex) {
                item.rowElement.addClassName("is-selected");
            } else {
                item.rowElement.removeClassName("is-selected");
            }
        }
    }

    private void advanceToNext() {
        if (selectedIndex < studentList.size() - 1) {
            selectedIndex++;
        }
        highlightSelectedRow();
    }

    private void moveUp() {
        if (selectedIndex > 0) {
            selectedIndex--;
        }
        highlightSelectedRow();
    }

    private void setupKeyboardShortcuts() {
        Shortcuts.addShortcutListener(this, () -> {
            if (selectedIndex >= 0 && selectedIndex < studentList.size()) {
                setStudentMark(studentList.get(selectedIndex), Mark.PRESENT);
                advanceToNext();
            }
        }, Key.KEY_P);

        Shortcuts.addShortcutListener(this, () -> {
            if (selectedIndex >= 0 && selectedIndex < studentList.size()) {
                setStudentMark(studentList.get(selectedIndex), Mark.ABSENT);
                advanceToNext();
            }
        }, Key.KEY_A);

        Shortcuts.addShortcutListener(this, () -> {
            if (selectedIndex >= 0 && selectedIndex < studentList.size()) {
                setStudentMark(studentList.get(selectedIndex), Mark.EXCUSED);
                advanceToNext();
            }
        }, Key.KEY_E);

        Shortcuts.addShortcutListener(this, this::advanceToNext, Key.ARROW_DOWN);
        Shortcuts.addShortcutListener(this, this::moveUp, Key.ARROW_UP);
    }

    private void updateCounters() {
        int total = studentList.size();
        long present = studentList.stream().filter(s -> s.getCurrentMark() == Mark.PRESENT).count();
        long absent = studentList.stream().filter(s -> s.getCurrentMark() == Mark.ABSENT).count();
        long excused = studentList.stream().filter(s -> s.getCurrentMark() == Mark.EXCUSED).count();

        double pct = total > 0 ? (present * 100.0) / total : 0.0;

        countTotalSpan.setText(String.valueOf(total));
        countPresentSpan.setText(String.valueOf(present));
        countAbsentSpan.setText(String.valueOf(absent));
        countExcusedSpan.setText(String.valueOf(excused));
        countRateSpan.setText(String.format("%.1f%%", pct));
    }

    private StatusChip createStatusChip(String type, String label) {
        if ("SAFE".equalsIgnoreCase(type)) {
            return StatusChip.safe(label);
        } else if ("WATCH".equalsIgnoreCase(type)) {
            return StatusChip.watch(label);
        } else {
            return StatusChip.shortStatus(label);
        }
    }
}
