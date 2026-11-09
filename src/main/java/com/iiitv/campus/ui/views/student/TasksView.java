package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.Task;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.components.StatusChip;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route(value = "tasks", layout = MainLayout.class)
@PageTitle("Tasks - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT})
public class TasksView extends VerticalLayout {

    private final ApiClient apiClient;
    private final List<Task> tasksList = new ArrayList<>();
    private final Set<String> completedSubtaskKeys = new HashSet<>();
    private Task selectedTask;
    private String activeFilter = "ALL";

    private final Div statsRow = new Div();
    private final Div rankedListContainer = new Div();
    private final Div detailPanelContainer = new Div();

    public TasksView(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.tasksList.addAll(apiClient.getPendingTasks());
        if (!tasksList.isEmpty()) {
            this.selectedTask = tasksList.getFirst();
        }

        addClassName("view-container");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        PageHeader header = new PageHeader("Tasks & Deliverables", "Prioritized task queue, subtask breakdown, and deadlines");
        header.addClassNames("neo-tasks-header");

        // Stat Overview Row (3 Cards: Pending, Critical Urgency tinted rose, Total Est. Effort)
        statsRow.addClassNames("dashboard-row-3", "neo-tasks-stats-row");
        updateStats();

        // 2-Column Master-Detail Layout: Ranked List Left, Detail Panel Right
        Div masterDetailGrid = new Div();
        masterDetailGrid.addClassNames("tasks-master-detail-grid", "neo-tasks-grid");

        // LEFT COLUMN: Quick-Add Row + Filter Tabs + Ranked List
        Div leftColumn = new Div();
        leftColumn.addClassNames("tasks-left-column");

        Div quickAddCard = buildQuickAddCard();

        Tab allTab = new Tab("All Tasks");
        Tab pendingTab = new Tab("Pending");
        Tab completedTab = new Tab("Completed");

        Tabs tabs = new Tabs(allTab, pendingTab, completedTab);
        tabs.setSelectedTab(allTab);
        tabs.setWidthFull();
        tabs.addClassNames("neo-maroon-tabs", "font-ui");

        tabs.addSelectedChangeListener(e -> {
            Tab sel = tabs.getSelectedTab();
            if (sel == allTab) {
                activeFilter = "ALL";
            } else if (sel == pendingTab) {
                activeFilter = "PENDING";
            } else {
                activeFilter = "COMPLETED";
            }
            renderRankedList();
        });

        rankedListContainer.setWidthFull();
        rankedListContainer.addClassNames("classroom-stream-column", "neo-tasks-list-col");
        renderRankedList();

        leftColumn.add(quickAddCard, tabs, rankedListContainer);

        // RIGHT COLUMN: Detail Panel for selected task
        detailPanelContainer.setWidthFull();
        renderDetailPanel();

        masterDetailGrid.add(leftColumn, detailPanelContainer);
        add(header, statsRow, masterDetailGrid);
    }

    private void updateStats() {
        statsRow.removeAll();

        long pendingCount = tasksList.stream().filter(t -> !t.completed()).count();
        long criticalCount = tasksList.stream().filter(t -> !t.completed() && "CRITICAL".equalsIgnoreCase(t.priority())).count();
        int totalHours = tasksList.stream().filter(t -> !t.completed()).mapToInt(t -> parseHours(t.estimatedHours())).sum();
        if (totalHours == 0) totalHours = 12;

        statsRow.add(createMiniStat("Pending Tasks", String.valueOf(pendingCount), "Active deliverables in queue", false));
        statsRow.add(createMiniStat("Critical Urgency", String.valueOf(criticalCount), "Due within next 24 hours", true));
        statsRow.add(createMiniStat("Total Est. Effort", totalHours + " h", "Allocated completion time", false));
    }

    private int parseHours(String est) {
        if (est == null) return 0;
        try {
            String cleaned = est.toLowerCase().replaceAll("[^0-9]", "");
            return cleaned.isEmpty() ? 0 : Integer.parseInt(cleaned);
        } catch (Exception e) {
            return 0;
        }
    }

    private Div createMiniStat(String label, String value, String sub, boolean isRose) {
        Div card = new Div();
        card.addClassNames("campus-card", "neo-tasks-stat-card");
        if (isRose) {
            card.addClassName("neo-tasks-stat-rose");
        }

        Span l = new Span(label);
        l.addClassNames("stat-card-label", "neo-tasks-stat-label", "font-ui");

        Div v = new Div();
        v.addClassNames("stat-card-value", "neo-tasks-stat-val", "font-mono");
        v.setText(value);

        Paragraph s = new Paragraph(sub);
        s.addClassNames("font-ui", "text-muted", "stat-card-sub", "neo-tasks-stat-sub");

        card.add(l, v, s);
        return card;
    }

    // =========================================================================
    // QUICK-ADD ROW
    // =========================================================================
    private Div buildQuickAddCard() {
        Div card = new Div();
        card.addClassNames("quick-add-card", "neo-tasks-quick-add");

        H3 header = new H3("Quick Add Task");
        header.addClassNames("quick-add-header", "neo-quick-add-header", "font-ui");

        Div formRow = new Div();
        formRow.addClassNames("quick-add-form-row", "neo-quick-add-row");

        TextField titleField = new TextField();
        titleField.setPlaceholder("Task title or deliverable...");
        titleField.setAriaLabel("Task title or deliverable");
        titleField.addClassNames("quick-add-input-title", "neo-inset-input", "font-ui");

        Select<String> courseSelect = new Select<>();
        courseSelect.setItems("CS301", "CS302", "CS303", "CS304");
        courseSelect.setValue("CS301");
        courseSelect.setAriaLabel("Select course code");
        courseSelect.addClassNames("quick-add-select-course", "neo-inset-input", "font-mono");

        TextField dueField = new TextField();
        dueField.setPlaceholder("Due: e.g. Due Friday 18:00");
        dueField.setAriaLabel("Task due date and time");
        dueField.addClassNames("quick-add-input-due", "neo-inset-input", "font-ui");

        Select<String> prioritySelect = new Select<>();
        prioritySelect.setItems("CRITICAL", "HIGH", "MEDIUM", "LOW");
        prioritySelect.setValue("HIGH");
        prioritySelect.setAriaLabel("Select task priority");
        prioritySelect.addClassNames("quick-add-select-priority", "neo-inset-input", "font-mono");

        TextField hoursField = new TextField();
        hoursField.setPlaceholder("Est: 2 h");
        hoursField.setAriaLabel("Estimated effort in hours");
        hoursField.setWidth("90px");
        hoursField.addClassNames("neo-inset-input", "font-mono");

        Button addBtn = new Button("Add Task");
        addBtn.addClassNames("btn-primary", "neo-btn-maroon-raised", "font-ui");
        addBtn.getElement().setAttribute("theme", "primary");
        addBtn.addClickListener(e -> {
            String title = titleField.getValue() != null ? titleField.getValue().trim() : "";
            if (title.isBlank()) {
                Notification.show("Please enter a task title.", 2500, Notification.Position.BOTTOM_END);
                return;
            }

            int newRank = tasksList.size() + 1;
            String due = dueField.getValue() != null && !dueField.getValue().isBlank() ? dueField.getValue().trim() : "Due in 3 days";
            String est = hoursField.getValue() != null && !hoursField.getValue().isBlank() ? hoursField.getValue().trim() : "2 h";

            Task newTask = new Task(
                    "t" + System.currentTimeMillis(),
                    title,
                    courseSelect.getValue(),
                    due,
                    est,
                    prioritySelect.getValue(),
                    false,
                    newRank,
                    "Created via quick-add row.",
                    List.of("Review assignment details", "Draft initial implementation", "Verify test cases")
            );

            tasksList.add(newTask);
            selectedTask = newTask;
            titleField.clear();
            dueField.clear();
            hoursField.clear();

            updateStats();
            renderRankedList();
            renderDetailPanel();

            Notification.show("Task added to queue: " + title, 2500, Notification.Position.BOTTOM_END);
        });

        formRow.add(titleField, courseSelect, dueField, prioritySelect, hoursField, addBtn);
        card.add(header, formRow);
        return card;
    }

    // =========================================================================
    // RANKED LIST (LEFT COLUMN)
    // =========================================================================
    private void renderRankedList() {
        rankedListContainer.removeAll();

        List<Task> filtered = tasksList.stream()
                .filter(t -> {
                    if ("PENDING".equals(activeFilter)) return !t.completed();
                    if ("COMPLETED".equals(activeFilter)) return t.completed();
                    return true;
                })
                .sorted((t1, t2) -> {
                    // Critical first, then by rank
                    int p1 = "CRITICAL".equalsIgnoreCase(t1.priority()) ? 0 : ("HIGH".equalsIgnoreCase(t1.priority()) ? 1 : 2);
                    int p2 = "CRITICAL".equalsIgnoreCase(t2.priority()) ? 0 : ("HIGH".equalsIgnoreCase(t2.priority()) ? 1 : 2);
                    if (p1 != p2) return Integer.compare(p1, p2);
                    return Integer.compare(t1.rank(), t2.rank());
                })
                .toList();

        if (filtered.isEmpty()) {
            Div emptyBox = new Div();
            emptyBox.addClassNames("empty-state-box", "font-ui");
            emptyBox.setText("No tasks in this category.");
            rankedListContainer.add(emptyBox);
            return;
        }

        int displayRank = 1;
        for (Task t : filtered) {
            final int rankNum = displayRank++;
            Div card = new Div();
            card.addClassNames("task-ranked-card", "neo-task-ranked-card");
            if (selectedTask != null && selectedTask.id().equals(t.id())) {
                card.addClassName("selected");
            }

            card.addClickListener(e -> {
                this.selectedTask = t;
                renderRankedList();
                renderDetailPanel();
            });

            // Left group: Rank badge + Checkbox + Course chip + Info
            Div left = new Div();
            left.addClassName("task-ranked-left");

            Span rankBadge = new Span("#" + rankNum);
            rankBadge.addClassNames("task-rank-badge", "neo-task-rank-badge", "font-mono");

            Checkbox doneBox = new Checkbox();
            doneBox.setValue(t.completed());
            doneBox.addValueChangeListener(ev -> {
                int idx = tasksList.indexOf(t);
                if (idx >= 0) {
                    boolean isDone = ev.getValue();
                    Task updated = new Task(
                            t.id(), t.title(), t.courseCode(), t.dueDate(), t.estimatedHours(),
                            t.priority(), isDone, t.rank(), t.notes(), t.subtasks()
                    );
                    tasksList.set(idx, updated);
                    if (selectedTask != null && selectedTask.id().equals(t.id())) {
                        selectedTask = updated;
                    }
                    if (isDone && t.subtasks() != null) {
                        for (int i = 0; i < t.subtasks().size(); i++) {
                            completedSubtaskKeys.remove("uncheck_" + t.id() + "_" + i);
                            completedSubtaskKeys.add(t.id() + "_" + i);
                        }
                    } else if (!isDone && t.subtasks() != null) {
                        for (int i = 0; i < t.subtasks().size(); i++) {
                            completedSubtaskKeys.remove(t.id() + "_" + i);
                        }
                    }
                    updateStats();
                    renderRankedList();
                    renderDetailPanel();
                    Notification.show(isDone ? "Task marked completed" : "Task marked pending", 2000, Notification.Position.BOTTOM_END);
                }
            });

            Span courseChip = new Span(t.courseCode());
            courseChip.addClassNames("chip", "chip-neutral", "neo-task-course-chip", "font-mono");

            Div info = new Div();
            info.addClassNames("task-ranked-info", "neo-task-ranked-info");

            H4 title = new H4(t.title());
            title.addClassNames("task-ranked-title", "neo-task-ranked-title", "font-ui");

            Span meta = new Span(t.dueDate() + " · Est: " + t.estimatedHours());
            meta.addClassNames("task-ranked-meta", "neo-task-ranked-meta", "font-mono");

            info.add(title, meta);
            left.add(rankBadge, doneBox, courseChip, info);

            // Right: Priority Chip (CRITICAL rose, HIGH yellow, MEDIUM sky, LOW mint)
            Component priorityChip = createPriorityChip(t.priority());

            card.add(left, priorityChip);
            rankedListContainer.add(card);
        }
    }

    private Component createPriorityChip(String priority) {
        String p = priority != null ? priority.toUpperCase() : "MEDIUM";
        Span chip = new Span(p);
        chip.addClassNames("neo-task-priority-chip", "font-mono");
        switch (p) {
            case "CRITICAL" -> chip.addClassName("priority-critical");
            case "HIGH" -> chip.addClassName("priority-high");
            case "LOW" -> chip.addClassName("priority-low");
            default -> chip.addClassName("priority-medium");
        }
        return chip;
    }

    // =========================================================================
    // DETAIL PANEL (RIGHT COLUMN)
    // =========================================================================
    private void renderDetailPanel() {
        detailPanelContainer.removeAll();

        if (selectedTask == null) {
            Div placeholder = new Div();
            placeholder.addClassNames("task-detail-panel", "neo-task-detail-panel");
            Paragraph p = new Paragraph("Select a task from the list to view deliverables and checklist.");
            p.addClassNames("font-ui", "text-muted");
            placeholder.add(p);
            detailPanelContainer.add(placeholder);
            return;
        }

        Div panel = new Div();
        panel.addClassNames("task-detail-panel", "neo-task-detail-panel");

        // Top Row: Course Chip + Priority Chip + Rank
        Div topRow = new Div();
        topRow.addClassNames("task-detail-top", "neo-task-detail-top");

        HorizontalLayout leftTags = new HorizontalLayout();
        leftTags.setSpacing(true);
        leftTags.setAlignItems(FlexComponent.Alignment.CENTER);

        Span courseBadge = new Span(selectedTask.courseCode());
        courseBadge.addClassNames("chip", "chip-neutral", "neo-task-course-chip", "font-mono");

        Component priorityBadge = createPriorityChip(selectedTask.priority());

        leftTags.add(courseBadge, priorityBadge);

        Span rankLabel = new Span("Rank #" + selectedTask.rank());
        rankLabel.addClassNames("font-mono", "neo-task-rank-label");

        topRow.add(leftTags, rankLabel);

        // Task Title
        H3 title = new H3(selectedTask.title());
        title.addClassNames("task-detail-title", "neo-task-detail-title", "font-ui");

        // Meta Box (Key/Value in Mono)
        Div metaGroup = new Div();
        metaGroup.addClassNames("task-detail-meta-group", "neo-task-meta-box");

        metaGroup.add(createMetaLine("Deadline", selectedTask.dueDate()));
        metaGroup.add(createMetaLine("Estimated Effort", selectedTask.estimatedHours()));
        metaGroup.add(createMetaLine("Associated Course", selectedTask.courseCode() + " – Core Curriculum"));
        metaGroup.add(createMetaLine("Status", selectedTask.completed() ? "Completed" : "Pending Action"));

        // Instructions & Scope
        Div notesGroup = new Div();
        notesGroup.addClassName("neo-task-notes-group");

        H4 notesHeader = new H4("Instructions & Scope");
        notesHeader.addClassNames("font-ui", "neo-task-section-heading");

        Paragraph notesText = new Paragraph(selectedTask.notes());
        notesText.addClassNames("font-ui", "neo-task-notes-body");

        notesGroup.add(notesHeader, notesText);

        // Subtasks Checklist with Green Progress Bar
        Div subtasksGroup = new Div();
        subtasksGroup.addClassName("neo-subtasks-group");

        List<String> subtasks = selectedTask.subtasks();
        int totalSubtasks = subtasks != null ? subtasks.size() : 0;
        long completedCount = 0;
        if (subtasks != null) {
            for (int i = 0; i < subtasks.size(); i++) {
                String subKey = selectedTask.id() + "_" + i;
                boolean isDone = completedSubtaskKeys.contains(subKey) || (selectedTask.completed() && !completedSubtaskKeys.contains("uncheck_" + subKey));
                if (isDone) completedCount++;
            }
        }
        int percent = totalSubtasks > 0 ? (int) Math.round((completedCount * 100.0) / totalSubtasks) : 0;

        HorizontalLayout subHeaderRow = new HorizontalLayout();
        subHeaderRow.setWidthFull();
        subHeaderRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        subHeaderRow.setAlignItems(FlexComponent.Alignment.BASELINE);

        H4 subHeader = new H4("Deliverable Checklist (" + totalSubtasks + ")");
        subHeader.addClassNames("font-ui", "neo-task-section-heading");

        Span progressLabel = new Span(completedCount + " / " + totalSubtasks + " completed (" + percent + "%)");
        progressLabel.addClassNames("font-mono", "neo-subtask-progress-label");

        subHeaderRow.add(subHeader, progressLabel);

        // Green Progress Bar
        Div progressTrack = new Div();
        progressTrack.addClassName("neo-subtask-progress-track");

        Div progressBar = new Div();
        progressBar.addClassName("neo-subtask-progress-bar");
        progressBar.getStyle().set("width", percent + "%");
        progressTrack.add(progressBar);

        Div checklist = new Div();
        checklist.addClassNames("subtasks-checklist", "neo-subtasks-checklist");

        if (subtasks != null) {
            for (int i = 0; i < subtasks.size(); i++) {
                final int subIndex = i;
                final String subtask = subtasks.get(i);
                final String subKey = selectedTask.id() + "_" + subIndex;
                boolean isDone = completedSubtaskKeys.contains(subKey) || (selectedTask.completed() && !completedSubtaskKeys.contains("uncheck_" + subKey));

                Div subRow = new Div();
                subRow.addClassNames("subtask-item", "neo-subtask-item");

                Checkbox cb = new Checkbox();
                cb.setValue(isDone);
                cb.addValueChangeListener(ev -> {
                    boolean checked = ev.getValue();
                    if (checked) {
                        completedSubtaskKeys.add(subKey);
                        completedSubtaskKeys.remove("uncheck_" + subKey);
                    } else {
                        completedSubtaskKeys.remove(subKey);
                        completedSubtaskKeys.add("uncheck_" + subKey);
                    }
                    renderDetailPanel();
                });

                Span stText = new Span(subtask);
                stText.addClassNames("font-ui", "neo-subtask-text");
                if (isDone) {
                    stText.addClassName("subtask-done");
                }

                subRow.add(cb, stText);
                checklist.add(subRow);
            }
        }

        subtasksGroup.add(subHeaderRow, progressTrack, checklist);

        // Action Buttons Row
        HorizontalLayout actions = new HorizontalLayout();
        actions.setWidthFull();
        actions.setSpacing(true);
        actions.addClassName("neo-task-actions-row");

        Button toggleDoneBtn = new Button(selectedTask.completed() ? "Mark Pending" : "Mark as Done");
        toggleDoneBtn.addClassNames(selectedTask.completed() ? "neo-btn-glass" : "neo-btn-maroon-raised", "font-ui");
        if (!selectedTask.completed()) {
            toggleDoneBtn.getElement().setAttribute("theme", "primary");
        }
        toggleDoneBtn.addClickListener(e -> {
            int idx = tasksList.indexOf(selectedTask);
            if (idx >= 0) {
                boolean newCompleted = !selectedTask.completed();
                Task updated = new Task(
                        selectedTask.id(), selectedTask.title(), selectedTask.courseCode(), selectedTask.dueDate(),
                        selectedTask.estimatedHours(), selectedTask.priority(), newCompleted,
                        selectedTask.rank(), selectedTask.notes(), selectedTask.subtasks()
                );
                tasksList.set(idx, updated);
                selectedTask = updated;
                if (newCompleted && selectedTask.subtasks() != null) {
                    for (int i = 0; i < selectedTask.subtasks().size(); i++) {
                        completedSubtaskKeys.remove("uncheck_" + selectedTask.id() + "_" + i);
                        completedSubtaskKeys.add(selectedTask.id() + "_" + i);
                    }
                } else if (!newCompleted && selectedTask.subtasks() != null) {
                    for (int i = 0; i < selectedTask.subtasks().size(); i++) {
                        completedSubtaskKeys.remove(selectedTask.id() + "_" + i);
                    }
                }
                updateStats();
                renderRankedList();
                renderDetailPanel();
                Notification.show(updated.completed() ? "Task marked completed" : "Task marked pending", 2000, Notification.Position.BOTTOM_END);
            }
        });

        Button openClassroomBtn = new Button("Open Course →");
        openClassroomBtn.addClassNames("neo-btn-glass", "font-ui");
        openClassroomBtn.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("classrooms/" + selectedTask.courseCode()));
        });

        Button deleteBtn = new Button("Delete");
        deleteBtn.addClassNames("neo-btn-danger-glass", "font-ui");
        deleteBtn.addClickListener(e -> {
            tasksList.remove(selectedTask);
            selectedTask = tasksList.isEmpty() ? null : tasksList.getFirst();
            updateStats();
            renderRankedList();
            renderDetailPanel();
            Notification.show("Task removed from queue", 2500, Notification.Position.BOTTOM_END);
        });

        actions.add(toggleDoneBtn, openClassroomBtn, deleteBtn);

        panel.add(topRow, title, metaGroup, notesGroup, subtasksGroup, actions);
        detailPanelContainer.add(panel);
    }

    private Div createMetaLine(String label, String value) {
        Div line = new Div();
        line.addClassNames("task-detail-meta-line", "neo-task-meta-line");

        Span l = new Span(label);
        l.addClassNames("font-ui", "text-muted", "neo-meta-label");

        Span v = new Span(value);
        v.addClassNames("font-mono", "neo-meta-value");

        line.add(l, v);
        return line;
    }
}
