package com.iiitv.campus.ui.views.faculty;

import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.components.StatusChip;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "faculty/gradebook", layout = MainLayout.class)
@PageTitle("Classroom Grading - IIIT Vadodara Campus Portal")
@RequiresRole({Role.FACULTY, Role.STUDENT, Role.ADMIN})
public class FacultyGradebookView extends VerticalLayout {

    public static class StudentGradeEntry {
        private final String roll;
        private final String name;
        private final String submissionStatus;
        private final String zipFile;
        private int score;
        private final int maxScore;
        private String feedback;
        private TextField scoreField;
        private TextField feedbackField;

        public StudentGradeEntry(String roll, String name, String submissionStatus, String zipFile, int score, int maxScore, String feedback) {
            this.roll = roll;
            this.name = name;
            this.submissionStatus = submissionStatus;
            this.zipFile = zipFile;
            this.score = score;
            this.maxScore = maxScore;
            this.feedback = feedback;
        }

        public String getRoll() { return roll; }
        public String getName() { return name; }
        public String getSubmissionStatus() { return submissionStatus; }
        public String getZipFile() { return zipFile; }
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
        public int getMaxScore() { return maxScore; }
        public String getFeedback() { return feedback; }
        public void setFeedback(String feedback) { this.feedback = feedback; }
    }

    private final List<StudentGradeEntry> gradeEntries = new ArrayList<>(List.of(
            new StudentGradeEntry("202201045", "Raghav A.", "Submitted on time", "Assignment_1_Raghav.zip (380 KB)", 18, 20, "Excellent balanced tree rotation proofs. Clean C++ code."),
            new StudentGradeEntry("202201012", "Ananya Roy", "Submitted on time", "Assignment_1_Ananya.zip (410 KB)", 19, 20, "Outstanding amortized analysis and unit tests."),
            new StudentGradeEntry("202201078", "Tanmay Sharma", "Submitted (1 hr late)", "Assignment_1_Tanmay.zip (320 KB)", 15, 20, "1 pt late penalty applied. Minor memory leak in destructor."),
            new StudentGradeEntry("202201095", "Rohan Verma", "Submitted on time", "Assignment_1_Rohan.zip (390 KB)", 16, 20, "Good implementation of queue and stack conversions."),
            new StudentGradeEntry("202201110", "Isha Patel", "Submitted on time", "Assignment_1_Isha.zip (440 KB)", 17, 20, "Comprehensive edge case handling in tree deletion."),
            new StudentGradeEntry("202201024", "Devansh Gupta", "Submitted on time", "Assignment_1_Devansh.zip (365 KB)", 18, 20, "Clean modular code and well-commented functions."),
            new StudentGradeEntry("202201033", "Pooja Nair", "Submitted on time", "Assignment_1_Pooja.zip (425 KB)", 19, 20, "Perfect score in complexity benchmarks."),
            new StudentGradeEntry("202201056", "Kavya Joshi", "Submitted on time", "Assignment_1_Kavya.zip (350 KB)", 14, 20, "Need clearer variable naming and algorithmic proofs."),
            new StudentGradeEntry("202201089", "Sameer Khan", "Submitted on time", "Assignment_1_Sameer.zip (395 KB)", 17, 20, "Solid implementation of monotonic stacks."),
            new StudentGradeEntry("202201102", "Aditya Rao", "Submitted on time", "Assignment_1_Aditya.zip (370 KB)", 16, 20, "Well structured doubly linked list operations.")
    ));

    private final Span avgScoreSpan = new Span();
    private final Span gradedCountSpan = new Span();

    public FacultyGradebookView() {
        addClassName("view-container");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        ComboBox<String> assessmentSelect = new ComboBox<>();
        assessmentSelect.setItems(
                "CS301 · Assignment 1: Balanced Trees (Max 20)",
                "CS301 · Assignment 2: Graph Traversal & Heaps (Max 20)",
                "CS301 · Mid-Sem Lab Practical Exam (Max 30)"
        );
        assessmentSelect.setValue("CS301 · Assignment 1: Balanced Trees (Max 20)");
        assessmentSelect.setAriaLabel("Select assignment to grade");
        assessmentSelect.setWidth("380px");

        PageHeader header = new PageHeader(
                "Classroom Grading",
                "Evaluate assignment code submissions, grade with rubrics, and publish feedback",
                assessmentSelect
        );

        // Metrics Strip
        Div metricsStrip = new Div();
        metricsStrip.addClassName("mess-metrics-strip");

        metricsStrip.add(
                createMetric("Submissions", "10 / 10 Received"),
                createMetric("Average Score", avgScoreSpan),
                createMetric("Evaluated", gradedCountSpan),
                createMetric("Grading Status", "Ready to Publish (SAFE)")
        );

        // Rubric & Actions Toolbar
        HorizontalLayout rubricBar = createRubricBar();

        // Gradebook Table
        Div tableWrap = createGradebookTable();

        // Footer Actions
        HorizontalLayout footerActions = createFooterActions();

        updateMetrics();

        add(header, metricsStrip, rubricBar, tableWrap, footerActions);
    }

    private Div createMetric(String label, String value) {
        Span v = new Span(value);
        return createMetric(label, v);
    }

    private Div createMetric(String label, Span valSpan) {
        Div cell = new Div();
        cell.addClassName("mess-metric-item");

        Span l = new Span(label);
        l.addClassName("mess-metric-label");

        valSpan.addClassNames("mess-metric-val", "font-mono");
        cell.add(l, valSpan);
        return cell;
    }

    private HorizontalLayout createRubricBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setWidthFull();
        bar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        bar.setAlignItems(FlexComponent.Alignment.CENTER);

        Span rubric = new Span("Rubric: Correctness & Proofs (10 pts) · Time Complexity (6 pts) · Clean Code & Tests (4 pts)");
        rubric.addClassNames("keyboard-legend-pill", "font-ui");

        Button downloadZipBtn = new Button("Download All Submissions (.ZIP)");
        downloadZipBtn.addClassNames("btn-secondary", "font-ui");
        downloadZipBtn.addClickListener(e ->
                Notification.show("Exporting batch archive for CS301 Assignment 1...", 2500, Notification.Position.BOTTOM_END));

        bar.add(rubric, downloadZipBtn);
        return bar;
    }

    private Div createGradebookTable() {
        Div wrap = new Div();
        wrap.addClassName("gradebook-grid-wrap");

        // Custom HTML Table with reactive Vaadin controls in cells
        Div container = new Div();
        container.setWidthFull();

        for (StudentGradeEntry entry : gradeEntries) {
            Div row = new Div();
            row.addClassName("classroom-task-row");

            // Left: Roll, Name, Submission file
            HorizontalLayout left = new HorizontalLayout();
            left.setAlignItems(FlexComponent.Alignment.CENTER);
            left.setSpacing(true);

            Span roll = new Span(entry.getRoll());
            roll.addClassNames("chip", "chip-neutral", "font-mono");

            Span name = new Span(entry.getName());
            name.addClassNames("font-ui", "text-navy");

            Span zip = new Span(entry.getZipFile());
            zip.addClassNames("font-mono", "text-muted");

            left.add(roll, name, zip);

            // Right: Feedback input + Score input + / Max + Status Chip
            HorizontalLayout right = new HorizontalLayout();
            right.setAlignItems(FlexComponent.Alignment.CENTER);
            right.setSpacing(true);

            TextField feedbackField = new TextField();
            feedbackField.setPlaceholder("Faculty remarks & feedback...");
            feedbackField.setAriaLabel("Feedback for " + entry.getName());
            feedbackField.setValue(entry.getFeedback());
            feedbackField.setWidth("300px");
            feedbackField.addValueChangeListener(e -> entry.setFeedback(e.getValue()));
            entry.feedbackField = feedbackField;

            TextField scoreField = new TextField();
            scoreField.setValue(String.valueOf(entry.getScore()));
            scoreField.setAriaLabel("Score out of " + entry.getMaxScore() + " for " + entry.getName());
            scoreField.addClassName("gradebook-input");
            scoreField.addValueChangeListener(e -> {
                try {
                    int val = Integer.parseInt(e.getValue().trim());
                    entry.setScore(Math.min(entry.getMaxScore(), Math.max(0, val)));
                    updateMetrics();
                } catch (NumberFormatException ignored) {}
            });
            entry.scoreField = scoreField;

            Span max = new Span("/ " + entry.getMaxScore());
            max.addClassNames("font-mono", "text-muted");

            StatusChip statusChip = StatusChip.safe("SAFE · GRADED");

            right.add(feedbackField, scoreField, max, statusChip);
            row.add(left, right);
            container.add(row);
        }

        wrap.add(container);
        return wrap;
    }

    private HorizontalLayout createFooterActions() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        footer.setAlignItems(FlexComponent.Alignment.CENTER);

        Button exportCsvBtn = new Button("Export Grade Sheet (CSV)");
        exportCsvBtn.addClassNames("btn-secondary", "font-ui");
        exportCsvBtn.addClickListener(e ->
                Notification.show("Grade sheet exported to CS301_Assignment1_Grades.csv", 2500, Notification.Position.BOTTOM_END));

        HorizontalLayout rightButtons = new HorizontalLayout();
        rightButtons.setSpacing(true);

        Button saveDraftBtn = new Button("Save Draft");
        saveDraftBtn.addClassNames("btn-secondary", "font-ui");
        saveDraftBtn.addClickListener(e ->
                Notification.show("Draft evaluations saved successfully.", 2000, Notification.Position.BOTTOM_END));

        Button publishBtn = new Button("Publish Grades & Release Feedback");
        publishBtn.addClassNames("btn-primary", "font-ui");
        publishBtn.addClickListener(e ->
                Notification.show("Grades and rubrics published to all 60 enrolled students in CS301.", 3500, Notification.Position.BOTTOM_END));

        rightButtons.add(saveDraftBtn, publishBtn);
        footer.add(exportCsvBtn, rightButtons);
        return footer;
    }

    private void updateMetrics() {
        int total = gradeEntries.size();
        double sum = 0;
        for (StudentGradeEntry e : gradeEntries) {
            sum += e.getScore();
        }
        double avg = total > 0 ? sum / total : 0.0;

        avgScoreSpan.setText(String.format("%.1f / 20 (%.1f%%)", avg, (avg / 20.0) * 100.0));
        gradedCountSpan.setText(total + " / " + total + " Evaluated");
    }
}
