package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.Assignment;
import com.iiitv.campus.dto.Classroom;
import com.iiitv.campus.dto.StreamPost;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.*;

import java.util.ArrayList;
import java.util.List;

@Route(value = "classrooms", layout = MainLayout.class)
@PageTitle("Classrooms - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY})
public class ClassroomsView extends VerticalLayout implements HasUrlParameter<String> {

    private final ApiClient apiClient;
    private final Div contentArea = new Div();
    private String selectedCourseCode = null;
    private Assignment activeAssignment = null;

    public ClassroomsView(ApiClient apiClient) {
        this.apiClient = apiClient;

        addClassNames("view-container", "neo-classrooms-view");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        contentArea.setWidthFull();
        contentArea.addClassNames("neo-classrooms-content-area");
        add(contentArea);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null && !parameter.isBlank()) {
            this.selectedCourseCode = parameter.toUpperCase();
        } else {
            this.selectedCourseCode = null;
        }
        this.activeAssignment = null;
        renderCurrentView();
    }

    private void renderCurrentView() {
        contentArea.removeAll();

        if (selectedCourseCode == null) {
            renderClassroomListView();
        } else if (activeAssignment != null) {
            renderAssignmentDetailPage(activeAssignment);
        } else {
            renderClassroomDetailView(selectedCourseCode);
        }
    }

    // =========================================================================
    // 1. CLASSROOM LIST VIEW (/classrooms)
    // =========================================================================
    private void renderClassroomListView() {
        List<Classroom> classrooms = apiClient.getClassrooms();

        Button joinBtn = new Button("Join classroom");
        joinBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui");
        joinBtn.getElement().setAttribute("theme", "primary");
        joinBtn.addClickListener(e -> openJoinClassroomDialog());

        PageHeader header = new PageHeader(
                "Classrooms",
                "Enrolled courses, streams, and study material",
                joinBtn
        );
        header.addClassNames("neo-classrooms-header");
        contentArea.add(header);

        if (classrooms == null || classrooms.isEmpty()) {
            contentArea.add(new com.iiitv.campus.ui.components.EmptyState(
                    "No Enrolled Classrooms",
                    "You are not enrolled in any active course classrooms for this semester."
            ));
            return;
        }

        Div grid = new Div();
        grid.addClassNames("classrooms-list-grid", "neo-classrooms-grid");

        for (Classroom c : classrooms) {
            Div card = new Div();
            card.addClassNames("classroom-card", "neo-classroom-card");
            card.getElement().setAttribute("tabindex", "0");
            card.getElement().setAttribute("role", "button");
            card.getElement().setAttribute("aria-label", "Enter classroom for " + c.courseCode() + " " + c.courseName());
            card.addClickListener(e -> {
                getUI().ifPresent(ui -> ui.navigate("classrooms/" + c.courseCode()));
            });
            card.getElement().addEventListener("keydown", e -> {
                getUI().ifPresent(ui -> ui.navigate("classrooms/" + c.courseCode()));
            }).setFilter("event.key === 'Enter'");

            // Top: course code in mono + teacher
            Div topRow = new Div();
            topRow.addClassNames("classroom-card-top", "neo-classroom-card-top");

            Span code = new Span(c.courseCode());
            code.addClassNames("classroom-card-code", "font-mono", "neo-classroom-card-code");

            Span teacher = new Span(c.teacher());
            teacher.addClassNames("classroom-card-teacher", "font-ui", "neo-classroom-card-teacher");

            topRow.add(code, teacher);

            // Title & Subtitle
            H3 title = new H3(c.courseName());
            title.addClassNames("classroom-card-title", "font-ui", "neo-classroom-card-title");

            Paragraph sub = new Paragraph(c.subtitle());
            sub.addClassNames("classroom-card-sub", "font-ui", "neo-classroom-card-sub");

            // Footer: due-soon count
            Div footer = new Div();
            footer.addClassNames("classroom-card-footer", "neo-classroom-card-footer");

            Span dueBadge = new Span();
            if (c.dueSoonCount() > 0) {
                dueBadge.setText(c.dueSoonCount() + " due soon");
                dueBadge.addClassNames("chip", "chip-next-up", "font-mono", "neo-classroom-chip", "neo-chip-due");
            } else if (c.newMaterialsTag() != null) {
                dueBadge.setText(c.newMaterialsTag());
                dueBadge.addClassNames("chip", "chip-neutral", "font-mono", "neo-classroom-chip", "neo-chip-materials");
            } else {
                dueBadge.setText("No pending work");
                dueBadge.addClassNames("chip", "chip-neutral", "font-mono", "neo-classroom-chip", "neo-chip-nowork");
            }

            Span openLink = new Span("Enter →");
            openLink.addClassNames("font-ui", "text-blue", "neo-classroom-enter");

            footer.add(dueBadge, openLink);
            card.add(topRow, title, sub, footer);
            grid.add(card);
        }

        contentArea.add(grid);
    }

    private void openJoinClassroomDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Join Classroom");
        dialog.addClassNames("neo-join-dialog");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(true);

        Paragraph desc = new Paragraph("Ask your course instructor for the 6-character class code, then enter it here.");
        desc.addClassNames("font-ui", "text-muted");

        TextField codeField = new TextField("Class Code");
        codeField.setPlaceholder("e.g. CS301X");
        codeField.setMaxLength(6);
        codeField.setWidthFull();
        codeField.addClassNames("font-mono", "neo-inset-input");

        Span errorSpan = new Span();
        errorSpan.addClassNames("font-ui", "text-short");
        errorSpan.setVisible(false);

        dialogLayout.add(desc, codeField, errorSpan);
        dialog.add(dialogLayout);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());
        cancelBtn.addClassNames("btn-secondary", "neo-btn-glass", "font-ui");

        Button joinActionBtn = new Button("Join", e -> {
            String val = codeField.getValue() != null ? codeField.getValue().trim() : "";
            if (val.length() != 6) {
                errorSpan.setText("Class code must be exactly 6 characters.");
                errorSpan.setVisible(true);
            } else {
                boolean success = apiClient.joinClassroom(val);
                if (success) {
                    Notification.show("Successfully joined course with code: " + val.toUpperCase(), 3000, Notification.Position.BOTTOM_END);
                    dialog.close();
                } else {
                    errorSpan.setText("Invalid classroom code. Please verify with instructor.");
                    errorSpan.setVisible(true);
                }
            }
        });
        joinActionBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui");
        joinActionBtn.getElement().setAttribute("theme", "primary");

        dialog.getFooter().add(cancelBtn, joinActionBtn);
        dialog.open();
    }

    // =========================================================================
    // 2. CLASSROOM DETAIL VIEW (/classrooms/{courseCode})
    // =========================================================================
    private void renderClassroomDetailView(String courseCode) {
        List<Classroom> classrooms = apiClient.getClassrooms();
        Classroom current = classrooms.stream()
                .filter(c -> c.courseCode().equalsIgnoreCase(courseCode))
                .findFirst()
                .orElse(classrooms.getFirst());

        List<StreamPost> streamPosts = apiClient.getStreamPosts(current.courseCode());
        List<Assignment> assignments = apiClient.getAssignments(current.courseCode());

        // 1. Breadcrumb: Classrooms › CS301 – Data Structures
        HorizontalLayout breadcrumbs = new HorizontalLayout();
        breadcrumbs.addClassNames("page-header-breadcrumbs", "font-ui", "neo-classroom-breadcrumbs");
        breadcrumbs.setSpacing(false);
        breadcrumbs.setAlignItems(FlexComponent.Alignment.CENTER);

        Span rootCrumb = new Span("Classrooms");
        rootCrumb.addClassNames("breadcrumb-parent", "neo-breadcrumb-link");
        rootCrumb.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("classrooms"));
        });

        Span separator = new Span("›");
        separator.addClassNames("breadcrumb-separator", "neo-breadcrumb-sep");

        Span currentCrumb = new Span(current.courseCode() + " – " + current.courseName());
        currentCrumb.addClassNames("breadcrumb-current", "neo-breadcrumb-curr");

        breadcrumbs.add(rootCrumb, separator, currentCrumb);
        contentArea.add(breadcrumbs);

        // 2. Maroon Header Banner (Code, Title, "B.Tech (CSE) · Sem V · Section A · Dr. A. Mehta")
        Div heroCard = new Div();
        heroCard.addClassNames("classroom-hero-card", "neo-classroom-hero-banner");

        Div codeBadge = new Div();
        codeBadge.addClassNames("classroom-badge", "font-mono", "neo-classroom-code-square");
        codeBadge.setText(current.courseCode());

        Div heroText = new Div();
        heroText.addClassNames("neo-classroom-hero-text");
        H2 heroTitle = new H2(current.courseCode() + " – " + current.courseName());
        heroTitle.addClassNames("classroom-hero-title", "font-ui", "neo-classroom-hero-title");

        Paragraph heroSub = new Paragraph(current.subtitle());
        heroSub.addClassNames("classroom-hero-sub", "font-ui", "neo-classroom-hero-sub");

        heroText.add(heroTitle, heroSub);
        heroCard.add(codeBadge, heroText);
        contentArea.add(heroCard);

        // 3. Tabs: Stream / Classwork / People
        Tab streamTab = new Tab("Stream");
        Tab classworkTab = new Tab("Classwork");
        Tab peopleTab = new Tab("People");

        Tabs tabs = new Tabs(streamTab, classworkTab, peopleTab);
        tabs.setSelectedTab(streamTab);
        tabs.setWidthFull();
        tabs.addClassNames("neo-maroon-tabs", "neo-classroom-tabs", "font-ui");
        contentArea.add(tabs);

        // Tab Content Panels
        Div bodyContainer = new Div();
        bodyContainer.setWidthFull();
        bodyContainer.addClassNames("neo-classroom-body-container");

        Div streamView = buildStreamTabPanel(streamPosts, assignments);
        Div classworkView = buildClassworkTabPanel(assignments);
        classworkView.setVisible(false);
        Div peopleView = buildPeopleTabPanel(current);
        peopleView.setVisible(false);

        tabs.addSelectedChangeListener(e -> {
            Tab sel = tabs.getSelectedTab();
            streamView.setVisible(sel == streamTab);
            classworkView.setVisible(sel == classworkTab);
            peopleView.setVisible(sel == peopleTab);
        });

        bodyContainer.add(streamView, classworkView, peopleView);
        contentArea.add(bodyContainer);
    }

    // -------------------------------------------------------------------------
    // STREAM TAB
    // -------------------------------------------------------------------------
    private Div buildStreamTabPanel(List<StreamPost> streamPosts, List<Assignment> assignments) {
        Div grid = new Div();
        grid.addClassNames("classroom-body-grid", "neo-classroom-stream-grid");

        // Left Column (Post Composer + StreamPost Cards)
        Div streamCol = new Div();
        streamCol.addClassNames("classroom-stream-column", "neo-classroom-stream-col");

        // Composer Card
        Div composerCard = new Div();
        composerCard.addClassNames("classroom-composer-card", "neo-classroom-composer-card");

        TextField composerField = new TextField();
        composerField.setPlaceholder("Share an announcement or material...");
        composerField.setAriaLabel("Share an announcement or material with the class");
        composerField.addClassNames("classroom-composer-field", "neo-classroom-composer-input", "font-ui");

        Button postBtn = new Button("Post");
        postBtn.addClassNames("btn-primary", "neo-btn-maroon", "neo-composer-post-btn", "font-ui");
        postBtn.getElement().setAttribute("theme", "primary");
        postBtn.addClickListener(e -> {
            if (composerField.getValue() != null && !composerField.getValue().isBlank()) {
                Notification.show("Post shared to course stream.", 3000, Notification.Position.BOTTOM_END);
                composerField.clear();
            }
        });

        composerCard.add(composerField, postBtn);
        streamCol.add(composerCard);

        // StreamPost Cards
        for (StreamPost post : streamPosts) {
            Div postCard = new Div();
            postCard.addClassNames("stream-card", "neo-stream-card");

            // Header (Author + Date on left, Kind chip on right)
            Div postTop = new Div();
            postTop.addClassNames("stream-card-top", "neo-stream-card-top");

            Div authorGroup = new Div();
            H4 authorName = new H4(post.author());
            authorName.addClassNames("stream-author-title", "neo-stream-author-title", "font-ui");

            Paragraph postDate = new Paragraph(post.date());
            postDate.addClassNames("stream-author-date", "neo-stream-author-date", "font-ui");

            authorGroup.add(authorName, postDate);

            // Kind Chip (ASSIGNMENT yellow, MATERIAL mint)
            Span kindChip = new Span(post.postType());
            if ("ASSIGNMENT".equalsIgnoreCase(post.postType())) {
                kindChip.addClassNames("chip", "neo-chip-assignment", "font-mono");
            } else if ("MATERIAL".equalsIgnoreCase(post.postType())) {
                kindChip.addClassNames("chip", "neo-chip-material", "font-mono");
            } else {
                kindChip.addClassNames("chip", "chip-neutral", "font-mono");
            }

            postTop.add(authorGroup, kindChip);
            postCard.add(postTop);

            // Title
            H3 postTitle = new H3(post.title());
            postTitle.addClassNames("stream-post-title", "neo-stream-post-title", "font-ui");
            postCard.add(postTitle);

            // Body
            Paragraph postBody = new Paragraph(post.content());
            postBody.addClassNames("stream-post-body", "neo-stream-post-body", "font-ui");
            postCard.add(postBody);

            // Attachment Chip (if present)
            if (post.attachmentName() != null && !post.attachmentName().isBlank()) {
                Div attachment = new Div();
                attachment.addClassNames("stream-attachment-btn", "neo-stream-attachment-pill");

                Span fileName = new Span(post.attachmentName());
                fileName.addClassNames("stream-attachment-name", "neo-attachment-filename", "font-mono");

                Span fileSize = new Span("(" + post.attachmentSize() + ")");
                fileSize.addClassNames("stream-attachment-size", "neo-attachment-filesize", "font-ui");

                attachment.add(fileName, fileSize);
                attachment.addClickListener(e ->
                        Notification.show("Downloading " + post.attachmentName(), 2500, Notification.Position.BOTTOM_END));
                postCard.add(attachment);
            }

            // Footer (Likes count & Comments count)
            Div footer = new Div();
            footer.addClassNames("stream-card-footer", "neo-stream-card-footer", "font-ui");
            footer.setText(post.likes() + " likes · " + post.commentsCount() + " comments");
            postCard.add(footer);

            streamCol.add(postCard);
        }

        // Right Column: "Upcoming / To Do" and "Recent Materials"
        Div sidebarCol = new Div();
        sidebarCol.addClassNames("classroom-sidebar-column", "neo-classroom-sidebar-col");

        // Card 1: Upcoming / To Do
        Div upcomingCard = new Div();
        upcomingCard.addClassNames("classroom-sidebar-card", "campus-card", "neo-classroom-sidebar-card");

        H3 upcomingTitle = new H3("Upcoming / To Do");
        upcomingTitle.addClassNames("classroom-sidebar-title", "neo-classroom-sidebar-heading", "font-ui");
        upcomingCard.add(upcomingTitle);

        Div tasksList = new Div();
        tasksList.setWidthFull();

        for (Assignment a : assignments) {
            Div taskRow = new Div();
            taskRow.addClassNames("classroom-task-row", "neo-sidebar-task-row");

            Div info = new Div();
            info.addClassNames("classroom-task-info", "neo-sidebar-task-info");

            Span title = new Span(a.title());
            title.addClassNames("classroom-task-title", "neo-sidebar-task-title", "font-ui");

            Span due = new Span(a.dueDate());
            due.addClassNames("classroom-task-date", "neo-sidebar-task-date", "font-ui");

            info.add(title, due);

            // Status Badge: 5 days left (gold) or 18 / 20 (mint)
            Span chip = new Span();
            if (a.isNextUp()) {
                chip.setText(a.statusBadge());
                chip.addClassNames("chip", "neo-chip-gold", "font-mono");
            } else if (a.score() != null) {
                chip.setText(a.score());
                chip.addClassNames("chip", "neo-chip-mint", "font-mono");
            } else {
                chip.setText(a.statusBadge());
                chip.addClassNames("chip", "chip-neutral", "font-mono");
            }

            taskRow.add(info, chip);
            tasksList.add(taskRow);
        }

        upcomingCard.add(tasksList);
        sidebarCol.add(upcomingCard);

        // Card 2: Recent Materials
        Div materialsCard = new Div();
        materialsCard.addClassNames("classroom-sidebar-card", "campus-card", "neo-classroom-sidebar-card");

        H3 materialsTitle = new H3("Recent Materials");
        materialsTitle.addClassNames("classroom-sidebar-title", "neo-classroom-sidebar-heading", "font-ui");
        materialsCard.add(materialsTitle);

        Div materialsList = new Div();
        materialsList.addClassNames("materials-list", "neo-materials-list");

        List<String> materials = List.of(
                "Hashing_Slides.pdf",
                "Trees_Notes.pdf",
                "Graph_Intro.pdf"
        );

        for (String m : materials) {
            Anchor link = new Anchor("#", m);
            link.addClassNames("classroom-material-link", "neo-recent-material-item", "font-mono");
            link.getElement().addEventListener("click", e ->
                    Notification.show("Accessing " + m, 2500, Notification.Position.BOTTOM_END));
            materialsList.add(link);
        }

        materialsCard.add(materialsList);
        sidebarCol.add(materialsCard);

        grid.add(streamCol, sidebarCol);
        return grid;
    }

    // -------------------------------------------------------------------------
    // CLASSWORK TAB (Groups assignments as Upcoming, Past due and Done)
    // -------------------------------------------------------------------------
    private Div buildClassworkTabPanel(List<Assignment> assignments) {
        Div wrap = new Div();
        wrap.addClassNames("campus-card", "neo-classwork-card");

        List<Assignment> upcoming = assignments.stream()
                .filter(a -> a.group() == Assignment.AssignmentGroup.UPCOMING)
                .toList();

        List<Assignment> pastDue = assignments.stream()
                .filter(a -> a.group() == Assignment.AssignmentGroup.PAST_DUE)
                .toList();

        List<Assignment> done = assignments.stream()
                .filter(a -> a.group() == Assignment.AssignmentGroup.DONE)
                .toList();

        wrap.add(buildClassworkGroup("Upcoming", upcoming, "chip-next-up"));
        wrap.add(buildClassworkGroup("Past due", pastDue, "chip-short"));
        wrap.add(buildClassworkGroup("Done", done, "chip-safe"));

        return wrap;
    }

    private Div buildClassworkGroup(String groupName, List<Assignment> list, String defaultChipClass) {
        Div groupContainer = new Div();
        groupContainer.addClassNames("classwork-group", "neo-classwork-group");

        Div header = new Div();
        header.addClassNames("classwork-group-header", "neo-classwork-group-header");

        H3 title = new H3(groupName + " (" + list.size() + ")");
        title.addClassNames("classwork-group-title", "neo-classwork-group-title", "font-ui");

        header.add(title);
        groupContainer.add(header);

        if (list.isEmpty()) {
            Paragraph empty = new Paragraph("No assignments in this section.");
            empty.addClassNames("font-ui", "text-muted");
            groupContainer.add(empty);
            return groupContainer;
        }

        for (Assignment a : list) {
            Div itemCard = new Div();
            itemCard.addClassNames("classwork-item-card", "neo-classwork-item-card");
            itemCard.addClickListener(e -> {
                this.activeAssignment = a;
                renderCurrentView();
            });

            Div left = new Div();
            left.addClassName("classwork-item-left");

            Div icon = new Div();
            icon.addClassNames("classwork-icon-badge", "neo-classwork-icon-badge", "font-mono");
            icon.setText("AS");

            Div info = new Div();
            info.addClassName("classwork-item-info");

            H4 itemTitle = new H4(a.title());
            itemTitle.addClassNames("classwork-item-title", "neo-classwork-item-title", "font-ui");

            Paragraph itemDue = new Paragraph(a.dueDate() + " · " + a.maxPoints() + " points");
            itemDue.addClassNames("classwork-item-due", "neo-classwork-item-due", "font-ui");

            info.add(itemTitle, itemDue);
            left.add(icon, info);

            // Right status chip
            Span chip = new Span();
            if (a.score() != null) {
                chip.setText(a.score());
                chip.addClassNames("chip", "chip-safe", "neo-chip-mint", "font-mono");
            } else {
                chip.setText(a.statusBadge());
                if (a.group() == Assignment.AssignmentGroup.UPCOMING) {
                    chip.addClassNames("chip", "neo-chip-gold", "font-mono");
                } else if (a.group() == Assignment.AssignmentGroup.PAST_DUE) {
                    chip.addClassNames("chip", "chip-short", "font-mono");
                } else {
                    chip.addClassNames("chip", defaultChipClass, "font-mono");
                }
            }

            itemCard.add(left, chip);
            groupContainer.add(itemCard);
        }

        return groupContainer;
    }

    // -------------------------------------------------------------------------
    // 3. ASSIGNMENT PAGE WITH SUBMISSION PANEL
    // -------------------------------------------------------------------------
    private void renderAssignmentDetailPage(Assignment assignment) {
        // Breadcrumb: Classrooms › CS301 – Data Structures › Classwork › Assignment Name
        HorizontalLayout breadcrumbs = new HorizontalLayout();
        breadcrumbs.addClassNames("page-header-breadcrumbs", "font-ui", "neo-classroom-breadcrumbs");
        breadcrumbs.setSpacing(false);
        breadcrumbs.setAlignItems(FlexComponent.Alignment.CENTER);

        Span rootCrumb = new Span("Classrooms");
        rootCrumb.addClassNames("breadcrumb-parent", "neo-breadcrumb-link");
        rootCrumb.addClickListener(e -> {
            this.selectedCourseCode = null;
            this.activeAssignment = null;
            renderCurrentView();
        });

        Span sep1 = new Span("›");
        sep1.addClassNames("breadcrumb-separator", "neo-breadcrumb-sep");

        Span courseCrumb = new Span(assignment.courseCode());
        courseCrumb.addClassNames("breadcrumb-parent", "neo-breadcrumb-link");
        courseCrumb.addClickListener(e -> {
            this.activeAssignment = null;
            renderCurrentView();
        });

        Span sep2 = new Span("›");
        sep2.addClassNames("breadcrumb-separator", "neo-breadcrumb-sep");

        Span classworkCrumb = new Span("Classwork");
        classworkCrumb.addClassNames("breadcrumb-parent", "neo-breadcrumb-link");
        classworkCrumb.addClickListener(e -> {
            this.activeAssignment = null;
            renderCurrentView();
        });

        Span sep3 = new Span("›");
        sep3.addClassNames("breadcrumb-separator", "neo-breadcrumb-sep");

        Span currentCrumb = new Span(assignment.title());
        currentCrumb.addClassNames("breadcrumb-current", "neo-breadcrumb-curr");

        breadcrumbs.add(rootCrumb, sep1, courseCrumb, sep2, classworkCrumb, sep3, currentCrumb);
        contentArea.add(breadcrumbs);

        // 2-column Layout: Assignment Details on Left, SubmissionPanel on Right
        Div layout = new Div();
        layout.addClassNames("assignment-detail-layout");

        // Left Section: Assignment Specs
        Div detailCol = new Div();
        detailCol.addClassNames("campus-card", "neo-assignment-detail-card");

        H2 assTitle = new H2(assignment.title());
        assTitle.addClassNames("font-ui", "text-navy", "margin-none");

        HorizontalLayout metaRow = new HorizontalLayout();
        metaRow.setSpacing(true);
        metaRow.setAlignItems(FlexComponent.Alignment.CENTER);

        Span dueSpan = new Span(assignment.dueDate());
        dueSpan.addClassNames("font-mono", "text-muted");

        Span pointsSpan = new Span(assignment.maxPoints() + " points");
        pointsSpan.addClassNames("font-mono", "text-navy");

        metaRow.add(dueSpan, pointsSpan);

        Paragraph desc = new Paragraph(assignment.description());
        desc.addClassNames("font-ui", "text-main");

        detailCol.add(assTitle, metaRow, desc);

        // Attachment if present
        if (assignment.attachmentName() != null) {
            Div attachmentBox = new Div();
            attachmentBox.addClassNames("stream-attachment-btn", "neo-stream-attachment-pill");

            Span fn = new Span(assignment.attachmentName());
            fn.addClassNames("stream-attachment-name", "neo-attachment-filename", "font-mono");

            Span sz = new Span("(" + assignment.attachmentSize() + ")");
            sz.addClassNames("stream-attachment-size", "neo-attachment-filesize", "font-ui");

            attachmentBox.add(fn, sz);
            attachmentBox.addClickListener(e ->
                    Notification.show("Downloading " + assignment.attachmentName(), 2500, Notification.Position.BOTTOM_END));
            detailCol.add(attachmentBox);
        }

        layout.add(detailCol);

        // Right Section: SubmissionPanel (upload, Submit, Unsubmit, marks and feedback)
        Div submissionPanel = buildSubmissionPanel(assignment);
        layout.add(submissionPanel);

        contentArea.add(layout);
    }

    private Div buildSubmissionPanel(Assignment assignment) {
        Div panel = new Div();
        panel.addClassNames("submission-panel-card", "neo-submission-panel-card");

        // Title row: "Your Work" + Status Chip
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        top.setAlignItems(FlexComponent.Alignment.CENTER);

        H3 panelTitle = new H3("Your Work");
        panelTitle.addClassNames("submission-panel-title", "font-ui");

        Span statusChip = new Span();
        if (assignment.score() != null) {
            statusChip.setText("Graded (" + assignment.score() + ")");
            statusChip.addClassNames("chip", "chip-safe", "neo-chip-mint", "font-mono");
        } else if (assignment.submitted()) {
            statusChip.setText("Submitted");
            statusChip.addClassNames("chip", "chip-safe", "neo-chip-mint", "font-mono");
        } else if (assignment.group() == Assignment.AssignmentGroup.PAST_DUE) {
            statusChip.setText("Missing");
            statusChip.addClassNames("chip", "chip-short", "font-mono");
        } else {
            statusChip.setText("Assigned");
            statusChip.addClassNames("chip", "chip-next-up", "neo-chip-gold", "font-mono");
        }

        top.add(panelTitle, statusChip);
        panel.add(top);

        // Upload and State Container
        Div stateContainer = new Div();
        stateContainer.setWidthFull();

        final boolean[] isSubmitted = {assignment.submitted()};
        final String[] uploadedFile = {assignment.submittedFileName()};
        final List<String> pendingUploads = new ArrayList<>();

        // Self-reloading runner mechanism
        Runnable[] runner = new Runnable[1];
        runner[0] = () -> {
            stateContainer.removeAll();

            if (assignment.score() != null) {
                Div scoreBox = new Div();
                scoreBox.addClassName("submission-feedback-box");

                Span scoreLabel = new Span("Evaluation & Grade");
                scoreLabel.addClassNames("font-ui", "text-safe");

                Div scoreVal = new Div();
                scoreVal.addClassNames("submission-score-line", "font-mono");
                scoreVal.setText(assignment.score());

                Paragraph feedbackText = new Paragraph("Faculty Feedback: " + assignment.feedback());
                feedbackText.addClassNames("submission-feedback-text", "font-ui");

                scoreBox.add(scoreLabel, scoreVal, feedbackText);

                Div fileBox = new Div();
                fileBox.addClassName("submission-file-box");
                Span nameSpan = new Span("📄 " + assignment.submittedFileName());
                nameSpan.addClassNames("font-mono");
                fileBox.add(nameSpan);

                stateContainer.add(scoreBox, fileBox);
                return;
            }

            if (isSubmitted[0]) {
                Div fileBox = new Div();
                fileBox.addClassName("submission-file-box");
                Span nameSpan = new Span("📄 " + (uploadedFile[0] != null ? uploadedFile[0] : "submission_files.zip"));
                nameSpan.addClassNames("font-mono");
                fileBox.add(nameSpan);

                Paragraph submitDate = new Paragraph("Submitted on " + (assignment.submittedAt() != null ? assignment.submittedAt() : "Today"));
                submitDate.addClassNames("font-ui", "text-muted", "margin-none");

                Button unsubmitBtn = new Button("Unsubmit");
                unsubmitBtn.addClassNames("btn-secondary", "neo-btn-glass", "font-ui");
                unsubmitBtn.setWidthFull();
                unsubmitBtn.addClickListener(e -> {
                    isSubmitted[0] = false;
                    Notification.show("Assignment unsubmitted. You may re-upload files before the deadline.", 3000, Notification.Position.BOTTOM_END);
                    runner[0].run();
                });

                stateContainer.add(fileBox, submitDate, unsubmitBtn);
                return;
            }

            MemoryBuffer buffer = new MemoryBuffer();
            Upload upload = new Upload(buffer);
            upload.setAcceptedFileTypes("application/pdf", ".pdf", "application/zip", ".zip", ".java", ".cpp");
            upload.setMaxFileSize(10 * 1024 * 1024);

            Div fileListContainer = new Div();
            fileListContainer.setWidthFull();

            upload.addSucceededListener(event -> {
                pendingUploads.add(event.getFileName());
                Div fileRow = new Div();
                fileRow.addClassName("submission-file-box");
                fileRow.add(new Span("📄 " + event.getFileName()));
                fileListContainer.add(fileRow);
                Notification.show("Uploaded " + event.getFileName(), 2500, Notification.Position.BOTTOM_END);
            });

            upload.addFileRejectedListener(event -> {
                Notification.show("File upload rejected: " + event.getErrorMessage(), 3000, Notification.Position.BOTTOM_END);
            });

            Button submitBtn = new Button("Submit");
            submitBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui");
            submitBtn.getElement().setAttribute("theme", "primary");
            submitBtn.setWidthFull();
            submitBtn.addClickListener(e -> {
                if (pendingUploads.isEmpty()) {
                    Notification.show("Please upload your work before submitting.", 2500, Notification.Position.BOTTOM_END);
                } else {
                    isSubmitted[0] = true;
                    uploadedFile[0] = pendingUploads.getLast();
                    Notification.show("Assignment submitted successfully!", 3000, Notification.Position.BOTTOM_END);
                    runner[0].run();
                }
            });

            stateContainer.add(upload, fileListContainer, submitBtn);
        };

        runner[0].run();
        panel.add(stateContainer);
        return panel;
    }

    // -------------------------------------------------------------------------
    // PEOPLE TAB
    // -------------------------------------------------------------------------
    private Div buildPeopleTabPanel(Classroom classroom) {
        Div wrap = new Div();
        wrap.addClassNames("campus-card", "neo-people-card");

        H3 title = new H3("Instructors & Teaching Assistants");
        title.addClassNames("font-ui", "text-navy", "margin-none");

        Div teacherCard = new Div();
        teacherCard.addClassNames("classroom-task-row", "neo-people-teacher-card");

        Div teacherInfo = new Div();
        teacherInfo.addClassNames("classroom-task-info");

        Span teacherName = new Span(classroom.teacher());
        teacherName.addClassNames("classroom-task-title", "font-ui");

        Span teacherRole = new Span("Course Lead Instructor · Office ICD-2113-F");
        teacherRole.addClassNames("classroom-task-date", "font-ui");

        teacherInfo.add(teacherName, teacherRole);

        Span emailChip = new Span("a.mehta@iiitvadodara.ac.in");
        emailChip.addClassNames("chip", "chip-neutral", "font-mono");

        teacherCard.add(teacherInfo, emailChip);

        H3 studentHeader = new H3("Classmates");
        studentHeader.addClassNames("font-ui", "text-navy", "mb-s");

        Paragraph count = new Paragraph("64 enrolled students (Semester V, Section A)");
        count.addClassNames("font-ui", "text-muted");

        Div roster = new Div();
        roster.setWidthFull();
        roster.addClassNames("neo-people-roster");

        List<String[]> students = List.of(
                new String[]{"202201012", "Ananya Roy"},
                new String[]{"202201045", "Raghav A. (You)"},
                new String[]{"202201078", "Tanmay Sharma"},
                new String[]{"202201095", "Rohan Verma"},
                new String[]{"202201110", "Isha Patel"}
        );

        for (String[] s : students) {
            Div sRow = new Div();
            sRow.addClassNames("classroom-task-row", "neo-people-row");

            Span rBadge = new Span(s[0]);
            rBadge.addClassNames("chip", "chip-neutral", "font-mono");

            Span sName = new Span(s[1]);
            sName.addClassNames("font-ui", "text-navy");

            sRow.add(rBadge, sName);
            roster.add(sRow);
        }

        wrap.add(title, teacherCard, studentHeader, count, roster);
        return wrap;
    }
}
