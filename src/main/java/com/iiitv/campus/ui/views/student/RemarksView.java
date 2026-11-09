package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.Remark;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.components.StatusChip;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "remarks", layout = MainLayout.class)
@PageTitle("Faculty Remarks - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class RemarksView extends VerticalLayout {

    private final ApiClient apiClient;
    private final Div remarksList = new Div();
    private String selectedFilter = "ALL"; // ALL, COMMENDATION, ADVISORY, MENTOR_NOTE
    private String searchQuery = "";

    @Autowired
    public RemarksView(ApiClient apiClient) {
        this.apiClient = apiClient;

        addClassNames("view-container", "neo-remarks-view");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        PageHeader header = new PageHeader(
                "Faculty Remarks",
                "Academic progress observations, mentor notes, and official faculty commendations"
        );
        header.addClassName("neo-remarks-header");

        // Academic Standing Metrics Strip - Glass bar with 4 mono stat blocks
        Div metricsStrip = new Div();
        metricsStrip.addClassNames("mess-metrics-strip", "neo-remarks-summary-strip", "neo-glass-panel");

        metricsStrip.add(
                createMetric("Academic Standing", "Good Standing (SAFE)", "stat-safe"),
                createMetric("Commendations", "2 Official Honours", "stat-safe"),
                createMetric("Active Advisories", "1 Action Required (WATCH)", "stat-watch"),
                createMetric("Assigned Mentor", "Dr. A. Mehta (ICD-2113-F)", "stat-info")
        );

        // Filter Tabs
        HorizontalLayout filterBar = new HorizontalLayout();
        filterBar.setSpacing(true);
        filterBar.addClassNames("tab-group", "neo-dept-filter-bar", "neo-remarks-filter-bar");

        Button tabAll = createFilterTab("All Notes", "ALL", true);
        Button tabComm = createFilterTab("Commendations", "COMMENDATION", false);
        Button tabAdv = createFilterTab("Advisories", "ADVISORY", false);
        Button tabMentor = createFilterTab("Mentor Notes", "MENTOR_NOTE", false);

        filterBar.add(tabAll, tabComm, tabAdv, tabMentor);

        // 2-Column Layout
        Div layoutGrid = new Div();
        layoutGrid.addClassNames("remarks-layout-grid", "neo-remarks-layout-grid");

        // LEFT: Search + Remarks Feed
        VerticalLayout leftCol = new VerticalLayout();
        leftCol.setWidthFull();
        leftCol.setSpacing(true);
        leftCol.setPadding(false);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search remarks by course, faculty, or title...");
        searchField.setAriaLabel("Search remarks by course, faculty, or title");
        searchField.setWidthFull();
        searchField.addClassNames("neo-inset-input", "neo-remarks-search", "font-ui");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> {
            searchQuery = e.getValue();
            renderRemarks();
        });

        remarksList.addClassNames("remarks-list", "neo-remarks-list");
        renderRemarks();

        leftCol.add(searchField, remarksList);

        // RIGHT: Mentorship Guidance & Policy
        VerticalLayout rightCol = new VerticalLayout();
        rightCol.setWidthFull();
        rightCol.setSpacing(true);
        rightCol.setPadding(false);

        Div policyCard = new Div();
        policyCard.addClassNames("campus-card", "stream-card", "neo-glass-panel", "neo-remarks-governance-card");

        H3 policyTitle = new H3("Mentorship Governance");
        policyTitle.addClassNames("font-ui", "text-navy", "margin-none", "neo-governance-title");

        Paragraph p1 = new Paragraph("• Official commendations are recorded in your permanent academic transcript dossier and considered for Dean's List and TA nominations.");
        p1.addClassNames("font-ui", "text-main", "neo-governance-bullet");

        Paragraph p2 = new Paragraph("• Attendance and academic advisories serve as proactive guidance before semester registration holds or examination debarment thresholds occur.");
        p2.addClassNames("font-ui", "text-main", "neo-governance-bullet");

        Paragraph p3 = new Paragraph("• Students with advisories are advised to meet their assigned faculty mentor during designated office hours.");
        p3.addClassNames("font-ui", "text-muted", "neo-governance-bullet");

        Button contactMentorBtn = new Button("View Mentor Profile & Schedule");
        contactMentorBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui", "neo-governance-btn");
        contactMentorBtn.setWidthFull();
        contactMentorBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("faculty-directory/dr-a-mehta")));

        policyCard.add(policyTitle, p1, p2, p3, contactMentorBtn);
        rightCol.add(policyCard);

        layoutGrid.add(leftCol, rightCol);

        add(header, metricsStrip, filterBar, layoutGrid);
    }

    private Div createMetric(String label, String value, String colorClass) {
        Div cell = new Div();
        cell.addClassNames("mess-metric-item", "neo-remarks-stat-block");

        Span l = new Span(label);
        l.addClassNames("mess-metric-label", "neo-remarks-stat-label");

        Span v = new Span(value);
        v.addClassNames("mess-metric-val", "neo-remarks-stat-val", "font-mono", colorClass);

        cell.add(l, v);
        return cell;
    }

    private Button createFilterTab(String label, String filterKey, boolean defaultActive) {
        Button btn = new Button(label);
        btn.addClassNames("btn-tab", "neo-dept-pill", "font-ui");
        if (defaultActive) {
            btn.addClassNames("tab-active", "neo-dept-pill-active");
        }
        btn.addClickListener(e -> {
            btn.getParent().ifPresent(parent -> {
                parent.getChildren().forEach(c -> {
                    if (c instanceof Button b) {
                        b.removeClassName("tab-active");
                        b.removeClassName("neo-dept-pill-active");
                    }
                });
            });
            btn.addClassNames("tab-active", "neo-dept-pill-active");
            selectedFilter = filterKey;
            renderRemarks();
        });
        return btn;
    }

    private void renderRemarks() {
        remarksList.removeAll();

        List<Remark> all = apiClient.getFacultyRemarks();

        List<Remark> filtered = all.stream()
                .filter(r -> {
                    if ("ALL".equalsIgnoreCase(selectedFilter)) return true;
                    return r.type().equalsIgnoreCase(selectedFilter);
                })
                .filter(r -> {
                    if (searchQuery == null || searchQuery.isBlank()) return true;
                    String q = searchQuery.toLowerCase().trim();
                    return r.title().toLowerCase().contains(q)
                            || r.content().toLowerCase().contains(q)
                            || r.facultyName().toLowerCase().contains(q)
                            || r.courseCode().toLowerCase().contains(q)
                            || r.courseName().toLowerCase().contains(q);
                })
                .toList();

        if (filtered.isEmpty()) {
            Div empty = new Div();
            empty.addClassNames("campus-card", "empty-state-card", "neo-glass-panel");
            Paragraph p = new Paragraph("No remarks found matching your selected filter.");
            p.addClassNames("font-ui", "text-muted");
            empty.add(p);
            remarksList.add(empty);
            return;
        }

        for (Remark r : filtered) {
            remarksList.add(createRemarkCard(r));
        }
    }

    private Div createRemarkCard(Remark r) {
        Div card = new Div();
        card.addClassNames("remark-card", "neo-glass-card", "neo-remark-card");

        if ("COMMENDATION".equalsIgnoreCase(r.type())) {
            card.addClassName("is-commendation");
        } else if ("ADVISORY".equalsIgnoreCase(r.type())) {
            card.addClassName("is-advisory");
        } else {
            card.addClassName("is-mentor-note");
        }

        // Top Row: Course Code + Course Title + Status Tag + Date
        Div topRow = new Div();
        topRow.addClassNames("remark-top-row", "neo-remark-top-row");

        Div topLeft = new Div();
        topLeft.addClassNames("remark-top-left", "neo-remark-top-left");

        Span codeBadge = new Span(r.courseCode());
        codeBadge.addClassNames("chip", "chip-neutral", "font-mono", "neo-remark-course-chip");

        Span courseTitle = new Span(r.courseName());
        courseTitle.addClassNames("font-ui", "text-muted", "neo-remark-course-title");

        StatusChip statusChip = createStatusChip(r.type(), r.typeLabel());

        topLeft.add(codeBadge, courseTitle, statusChip);

        Span dateSpan = new Span(r.date());
        dateSpan.addClassNames("font-mono", "text-muted", "neo-remark-date");

        topRow.add(topLeft, dateSpan);

        // Faculty Line
        Paragraph facultyLine = new Paragraph("Issued by " + r.facultyName() + " · " + r.facultyRole());
        facultyLine.addClassNames("remark-faculty-line", "neo-remark-faculty-line", "font-ui");

        // Title
        H3 title = new H3(r.title());
        title.addClassNames("remark-title", "neo-remark-title", "font-ui");

        // Content
        Paragraph content = new Paragraph(r.content());
        content.addClassNames("remark-content", "neo-remark-content", "font-ui");

        card.add(topRow, facultyLine, title, content);

        // Action Item Box if present
        if (r.actionItem() != null && !r.actionItem().isBlank()) {
            Div actionBox = new Div();
            actionBox.addClassNames("remark-action-box", "neo-remark-action-box");
            String actionText = r.actionItem().trim();
            if (!actionText.toLowerCase().startsWith("action")) {
                actionText = "Action: " + actionText;
            }
            actionBox.setText(actionText);
            card.add(actionBox);
        }

        // Footer: Acknowledgment
        Div footer = new Div();
        footer.addClassNames("remark-footer", "neo-remark-footer");

        if (r.isAcknowledged()) {
            String ackDate = (r.acknowledgedDate() != null && !r.acknowledgedDate().isBlank()) 
                    ? r.acknowledgedDate().toUpperCase() 
                    : "06 NOV 2026";
            Span ackSpan = new Span("ACKNOWLEDGED ON " + ackDate + " ✓");
            ackSpan.addClassNames("font-mono", "chip", "chip-safe", "neo-chip-ack");
            footer.add(ackSpan);
        } else {
            Button ackBtn = new Button("Acknowledge Note");
            ackBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui", "neo-ack-btn");
            ackBtn.addClickListener(e -> {
                apiClient.acknowledgeRemark(r.id());
                Notification.show("Advisory acknowledged.", 2000, Notification.Position.BOTTOM_END);
                renderRemarks();
            });
            footer.add(ackBtn);
        }

        card.add(footer);
        return card;
    }

    private StatusChip createStatusChip(String type, String label) {
        if ("COMMENDATION".equalsIgnoreCase(type)) {
            return StatusChip.safe(label);
        } else if ("ADVISORY".equalsIgnoreCase(type)) {
            return StatusChip.watch(label);
        } else {
            return StatusChip.neutral(label);
        }
    }
}
