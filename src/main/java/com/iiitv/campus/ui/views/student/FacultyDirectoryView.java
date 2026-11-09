package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.ClassOccurrence;
import com.iiitv.campus.dto.FacultyMember;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.components.StatusChip;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Route(value = "faculty-directory", layout = MainLayout.class)
@PageTitle("Faculty Directory - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class FacultyDirectoryView extends VerticalLayout implements HasUrlParameter<String> {

    private final ApiClient apiClient;
    private final Div contentArea = new Div();

    @Autowired
    public FacultyDirectoryView(ApiClient apiClient) {
        this.apiClient = apiClient;
        addClassNames("view-container", "neo-faculty-dir-view");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        contentArea.setWidthFull();
        contentArea.addClassNames("neo-faculty-content-area");
        add(contentArea);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        contentArea.removeAll();
        if (parameter == null || parameter.trim().isEmpty()) {
            renderDirectoryView();
        } else {
            FacultyMember member = apiClient.getFacultyMember(parameter.trim());
            renderProfileView(member);
        }
    }

    /* =========================================================================
       1. DIRECTORY LIST VIEW
       ========================================================================= */

    private void renderDirectoryView() {
        VerticalLayout dirLayout = new VerticalLayout();
        dirLayout.setWidthFull();
        dirLayout.setSpacing(true);
        dirLayout.setPadding(false);

        List<FacultyMember> allFaculty = apiClient.getFacultyDirectory();

        // Search Field
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search by name, department, or course...");
        searchField.setAriaLabel("Search faculty by name, department, or course");
        searchField.setWidth("340px");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addClassNames("neo-inset-input", "neo-faculty-search", "font-ui");

        PageHeader header = new PageHeader(
                "Faculty Directory",
                "Professors, teaching staff, office hours, and live availability",
                searchField
        );
        header.addClassNames("neo-faculty-header");

        // Department filter buttons
        HorizontalLayout filterBar = new HorizontalLayout();
        filterBar.setSpacing(true);
        filterBar.addClassNames("tab-group", "neo-dept-filter-bar");

        Button btnAll = new Button("All Departments (27)");
        btnAll.addClassNames("btn-tab", "neo-dept-pill", "tab-active");
        Button btnCse = new Button("Computer Science & Engineering");
        btnCse.addClassNames("btn-tab", "neo-dept-pill");
        Button btnEce = new Button("Electronics & Communication");
        btnEce.addClassNames("btn-tab", "neo-dept-pill");
        Button btnSci = new Button("Science, Math & Humanities");
        btnSci.addClassNames("btn-tab", "neo-dept-pill");
        Button btnOnline = new Button("GN & External Online Faculty");
        btnOnline.addClassNames("btn-tab", "neo-dept-pill");

        Div facultyGrid = new Div();
        facultyGrid.addClassNames("faculty-grid", "neo-faculty-grid");

        renderFacultyCards(allFaculty, facultyGrid);

        searchField.addValueChangeListener(e -> {
            String query = e.getValue();
            filterAndDisplay(allFaculty, query, facultyGrid);
        });

        btnAll.addClickListener(e -> {
            setActiveTab(btnAll, btnCse, btnEce, btnSci, btnOnline);
            searchField.clear();
            renderFacultyCards(allFaculty, facultyGrid);
        });

        btnCse.addClickListener(e -> {
            setActiveTab(btnCse, btnAll, btnEce, btnSci, btnOnline);
            searchField.clear();
            List<FacultyMember> filtered = allFaculty.stream()
                    .filter(f -> f.department().toLowerCase().contains("computer")
                            || f.coursesTaught().stream().anyMatch(c -> c.toUpperCase().startsWith("CS") || c.toUpperCase().startsWith("IT")))
                    .toList();
            renderFacultyCards(filtered, facultyGrid);
        });

        btnEce.addClickListener(e -> {
            setActiveTab(btnEce, btnAll, btnCse, btnSci, btnOnline);
            searchField.clear();
            List<FacultyMember> filtered = allFaculty.stream()
                    .filter(f -> f.department().toLowerCase().contains("electronics")
                            || f.coursesTaught().stream().anyMatch(c -> c.toUpperCase().startsWith("EC")))
                    .toList();
            renderFacultyCards(filtered, facultyGrid);
        });

        btnSci.addClickListener(e -> {
            setActiveTab(btnSci, btnAll, btnCse, btnEce, btnOnline);
            searchField.clear();
            List<FacultyMember> filtered = allFaculty.stream()
                    .filter(f -> f.department().toLowerCase().contains("math")
                            || f.department().toLowerCase().contains("physics")
                            || f.department().toLowerCase().contains("humanities")
                            || f.department().toLowerCase().contains("environmental")
                            || f.department().toLowerCase().contains("science"))
                    .toList();
            renderFacultyCards(filtered, facultyGrid);
        });

        btnOnline.addClickListener(e -> {
            setActiveTab(btnOnline, btnAll, btnCse, btnEce, btnSci);
            searchField.clear();
            List<FacultyMember> filtered = allFaculty.stream()
                    .filter(f -> f.campusMode() != null && !f.campusMode().equalsIgnoreCase("ICD"))
                    .toList();
            renderFacultyCards(filtered, facultyGrid);
        });

        filterBar.add(btnAll, btnCse, btnEce, btnSci, btnOnline);
        dirLayout.add(header, filterBar, facultyGrid);
        contentArea.add(dirLayout);
    }

    private void setActiveTab(Button active, Button... others) {
        active.addClassName("tab-active");
        for (Button b : others) {
            b.removeClassName("tab-active");
        }
    }

    private void filterAndDisplay(List<FacultyMember> list, String query, Div grid) {
        if (query == null || query.isBlank()) {
            renderFacultyCards(list, grid);
            return;
        }
        String q = query.toLowerCase().trim();
        List<FacultyMember> filtered = list.stream()
                .filter(f -> f.name().toLowerCase().contains(q)
                        || f.department().toLowerCase().contains(q)
                        || f.office().toLowerCase().contains(q)
                        || (f.phone() != null && f.phone().contains(q))
                        || (f.campusMode() != null && f.campusMode().toLowerCase().contains(q))
                        || f.coursesTaught().stream().anyMatch(c -> c.toLowerCase().contains(q))
                        || (f.researchAreas() != null && f.researchAreas().toLowerCase().contains(q)))
                .toList();
        renderFacultyCards(filtered, grid);
    }

    private void renderFacultyCards(List<FacultyMember> list, Div grid) {
        grid.removeAll();

        if (list.isEmpty()) {
            Div empty = new Div();
            empty.addClassNames("campus-card", "empty-state-card");
            Paragraph p = new Paragraph("No faculty members found matching your search query.");
            p.addClassNames("font-ui", "text-muted");
            empty.add(p);
            grid.add(empty);
            return;
        }

        for (FacultyMember f : list) {
            Div card = new Div();
            card.addClassNames("faculty-directory-card", "neo-faculty-card");

            // Header: Avatar + Name & Designation + Mode Chip
            Div cardHeader = new Div();
            cardHeader.addClassNames("faculty-card-header", "neo-faculty-card-header");

            Div avatar = new Div();
            avatar.addClassNames("faculty-avatar", "neo-faculty-avatar");
            avatar.setText(getInitials(f.name()));

            Div titleGroup = new Div();
            titleGroup.addClassNames("faculty-card-title-group", "neo-faculty-title-group");

            H3 name = new H3(f.name());
            name.addClassNames("faculty-card-name", "neo-faculty-name", "font-ui");

            HorizontalLayout subRow = new HorizontalLayout();
            subRow.setSpacing(true);
            subRow.setAlignItems(FlexComponent.Alignment.CENTER);

            Paragraph sub = new Paragraph(f.designation() + " · " + f.department());
            sub.addClassNames("faculty-card-sub", "neo-faculty-sub", "font-ui");

            Span modeChip = new Span(f.campusMode() != null ? f.campusMode() : "ICD");
            modeChip.addClassNames("chip", "chip-neutral", "font-mono", "neo-campus-chip");

            subRow.add(sub, modeChip);
            titleGroup.add(name, subRow);
            cardHeader.add(avatar, titleGroup);

            // Live status badge
            Div statusBadge = createLiveStatusBadge(f.liveStatusBadge(), f.liveStatusType());

            // Meta grid (Office, Phone, Office Hours) as inset panel in mono
            Div metaGrid = new Div();
            metaGrid.addClassNames("faculty-meta-grid", "neo-faculty-meta-inset");

            Div officeCell = new Div();
            officeCell.addClassNames("faculty-meta-item", "neo-meta-item");
            Span officeLabel = new Span("Office");
            officeLabel.addClassNames("faculty-meta-label", "neo-meta-lbl", "font-mono");
            Span officeVal = new Span(f.office());
            officeVal.addClassNames("faculty-meta-val", "neo-meta-val", "font-mono");
            officeCell.add(officeLabel, officeVal);

            Div phoneCell = new Div();
            phoneCell.addClassNames("faculty-meta-item", "neo-meta-item");
            Span phoneLabel = new Span("Phone");
            phoneLabel.addClassNames("faculty-meta-label", "neo-meta-lbl", "font-mono");
            Span phoneVal = new Span(f.phone() != null ? f.phone() : "N/A");
            phoneVal.addClassNames("faculty-meta-val", "neo-meta-val", "font-mono");
            phoneCell.add(phoneLabel, phoneVal);

            Div hoursCell = new Div();
            hoursCell.addClassNames("faculty-meta-item", "neo-meta-item");
            Span hoursLabel = new Span("Office Hours");
            hoursLabel.addClassNames("faculty-meta-label", "neo-meta-lbl", "font-mono");
            Span hoursVal = new Span(f.officeHours());
            hoursVal.addClassNames("faculty-meta-val", "neo-meta-val", "font-mono");
            hoursCell.add(hoursLabel, hoursVal);

            metaGrid.add(officeCell, phoneCell, hoursCell);

            // Courses preview chips
            if (f.coursesTaught() != null && !f.coursesTaught().isEmpty()) {
                HorizontalLayout coursesWrap = new HorizontalLayout();
                coursesWrap.addClassNames("faculty-courses-preview", "neo-faculty-courses-preview", "font-ui");
                coursesWrap.setSpacing(true);
                for (String c : f.coursesTaught()) {
                    String codeOnly = c.split(" ")[0];
                    Span cChip = new Span(codeOnly);
                    cChip.addClassNames("chip", "chip-safe", "neo-chip-mint", "font-mono");
                    coursesWrap.add(cChip);
                }
                card.add(cardHeader, statusBadge, metaGrid, coursesWrap);
            } else {
                card.add(cardHeader, statusBadge, metaGrid);
            }

            // Next class preview if available
            if (f.nextClass() != null) {
                Div nextClassLine = new Div();
                nextClassLine.addClassNames("schedule-row-layout", "neo-faculty-next-strip");
                HorizontalLayout nextRow = new HorizontalLayout();
                nextRow.setWidthFull();
                nextRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
                nextRow.setAlignItems(FlexComponent.Alignment.CENTER);

                Span nextLabel = new Span("Next: " + f.nextClass().courseCode() + " (" + f.nextClass().room() + ") at " + f.nextClass().startTime());
                nextLabel.addClassNames("font-mono", "neo-faculty-next-label");

                if (f.nextClass().nextUpBadge() != null) {
                    Span countChip = new Span(f.nextClass().nextUpBadge());
                    countChip.addClassNames("chip", "chip-next-up", "neo-chip-gold", "font-mono");
                    nextRow.add(nextLabel, countChip);
                } else {
                    nextRow.add(nextLabel);
                }
                nextClassLine.add(nextRow);
                card.add(nextClassLine);
            }

            // Actions footer: Email = outline button; View Profile = solid maroon
            Div actions = new Div();
            actions.addClassNames("faculty-card-actions", "neo-faculty-card-actions");

            Button emailBtn = new Button("Email");
            emailBtn.addClassNames("btn-secondary", "neo-btn-outline", "font-ui");
            emailBtn.addClickListener(e -> UI.getCurrent().getPage().open("mailto:" + f.email(), "_blank"));

            Button viewProfileBtn = new Button("View Profile & Schedule →");
            viewProfileBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui");
            viewProfileBtn.addClickListener(e -> UI.getCurrent().navigate("faculty-directory/" + f.id()));

            actions.add(emailBtn, viewProfileBtn);
            card.add(actions);

            grid.add(card);
        }
    }

    /* =========================================================================
       2. PROFESSOR PROFILE VIEW
       ========================================================================= */

    private void renderProfileView(FacultyMember faculty) {
        VerticalLayout profileLayout = new VerticalLayout();
        profileLayout.setWidthFull();
        profileLayout.setSpacing(true);
        profileLayout.setPadding(false);

        // Breadcrumb navigation
        HorizontalLayout breadcrumb = new HorizontalLayout();
        breadcrumb.addClassNames("page-header-breadcrumbs", "font-ui", "neo-classroom-breadcrumbs");
        breadcrumb.setAlignItems(FlexComponent.Alignment.CENTER);
        breadcrumb.setSpacing(false);

        Button backBtn = new Button("← Back to Faculty Directory");
        backBtn.addClassNames("btn-secondary", "neo-btn-glass", "font-ui");
        backBtn.addClickListener(e -> UI.getCurrent().navigate("faculty-directory"));

        Span separator = new Span("›");
        separator.addClassNames("breadcrumb-separator", "neo-breadcrumb-sep");

        Span currentProf = new Span(faculty.name());
        currentProf.addClassNames("breadcrumb-current", "neo-breadcrumb-curr");

        breadcrumb.add(backBtn, separator, currentProf);

        // Hero Card (maroon banner)
        Div heroCard = new Div();
        heroCard.addClassNames("professor-hero-card", "neo-professor-hero-banner");

        Div heroTop = new Div();
        heroTop.addClassNames("professor-hero-top", "neo-prof-hero-top");

        Div heroLeft = new Div();
        heroLeft.addClassNames("professor-hero-left", "neo-prof-hero-left");

        Div avatarLg = new Div();
        avatarLg.addClassNames("professor-avatar-lg", "neo-prof-avatar-lg");
        avatarLg.setText(getInitials(faculty.name()));

        Div heroInfo = new Div();
        heroInfo.addClassNames("professor-hero-info", "neo-prof-hero-info");

        H2 profName = new H2(faculty.name());
        profName.addClassNames("professor-hero-name", "neo-prof-hero-name", "font-ui");

        Paragraph profRole = new Paragraph(faculty.designation() + " · " + faculty.department());
        profRole.addClassNames("professor-hero-role", "neo-prof-hero-role", "font-ui");

        // Prominent live status badge + campus mode badge
        HorizontalLayout badgesRow = new HorizontalLayout();
        badgesRow.setSpacing(true);
        badgesRow.setAlignItems(FlexComponent.Alignment.CENTER);

        Div statusBadge = createLiveStatusBadge(faculty.liveStatusBadge(), faculty.liveStatusType());
        Span modeBadge = new Span("Campus Mode: " + (faculty.campusMode() != null ? faculty.campusMode() : "ICD"));
        modeBadge.addClassNames("chip", "chip-neutral", "font-mono", "neo-campus-chip");

        badgesRow.add(statusBadge, modeBadge);

        heroInfo.add(profName, profRole, badgesRow);
        heroLeft.add(avatarLg, heroInfo);

        Button emailActionBtn = new Button("Email Professor");
        emailActionBtn.addClassNames("btn-secondary", "neo-btn-glass", "font-ui");
        emailActionBtn.addClickListener(e -> UI.getCurrent().getPage().open("mailto:" + faculty.email(), "_blank"));

        heroTop.add(heroLeft, emailActionBtn);

        // Contact info strip (Email, Phone, Office, Mode, Hours)
        Div contactStrip = new Div();
        contactStrip.addClassNames("professor-contact-strip", "neo-prof-contact-inset");

        Div emailCell = new Div();
        emailCell.addClassName("professor-contact-cell");
        Span emailLbl = new Span("Email:");
        emailLbl.addClassName("professor-contact-label");
        Span emailVal = new Span(faculty.email());
        emailVal.addClassNames("font-mono", "neo-prof-mono-val");
        emailCell.add(emailLbl, emailVal);

        Div phoneCell = new Div();
        phoneCell.addClassName("professor-contact-cell");
        Span phoneLbl = new Span("Phone:");
        phoneLbl.addClassName("professor-contact-label");
        Span phoneVal = new Span(faculty.phone() != null ? faculty.phone() : "N/A");
        phoneVal.addClassNames("font-mono", "neo-prof-mono-val");
        phoneCell.add(phoneLbl, phoneVal);

        Div officeCell = new Div();
        officeCell.addClassName("professor-contact-cell");
        Span officeLbl = new Span("Office / Room:");
        officeLbl.addClassName("professor-contact-label");
        Span officeVal = new Span(faculty.office());
        officeVal.addClassNames("font-mono", "neo-prof-mono-val");
        officeCell.add(officeLbl, officeVal);

        Div hoursCell = new Div();
        hoursCell.addClassName("professor-contact-cell");
        Span hoursLbl = new Span("Office Hours:");
        hoursLbl.addClassName("professor-contact-label");
        Span hoursVal = new Span(faculty.officeHours());
        hoursVal.addClassNames("font-mono", "neo-prof-mono-val");
        hoursCell.add(hoursLbl, hoursVal);

        contactStrip.add(emailCell, phoneCell, officeCell, hoursCell);
        heroCard.add(heroTop, contactStrip);

        // 2-Column Master Layout
        Div mainGrid = new Div();
        mainGrid.addClassNames("professor-profile-grid", "neo-prof-profile-grid");

        // LEFT COLUMN: Weekly Schedule & Research
        VerticalLayout leftCol = new VerticalLayout();
        leftCol.setWidthFull();
        leftCol.setSpacing(true);
        leftCol.setPadding(false);

        // Weekly Schedule Card
        Div scheduleCard = new Div();
        scheduleCard.addClassNames("campus-card", "stream-card", "neo-glass-panel");

        HorizontalLayout schedHeader = new HorizontalLayout();
        schedHeader.setWidthFull();
        schedHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        schedHeader.setAlignItems(FlexComponent.Alignment.CENTER);

        H3 schedTitle = new H3("Weekly Teaching & Consultation Schedule");
        schedTitle.addClassNames("font-ui", "neo-glass-panel-title", "margin-none");

        // Legend
        HorizontalLayout legend = new HorizontalLayout();
        legend.setSpacing(true);
        Span legLec = new Span("Lecture");
        legLec.addClassNames("schedule-type-badge", "type-lecture");
        Span legLab = new Span("Lab");
        legLab.addClassNames("schedule-type-badge", "type-lab");
        Span legCons = new Span("Consultation");
        legCons.addClassNames("schedule-type-badge", "type-consultation");
        legend.add(legLec, legLab, legCons);

        schedHeader.add(schedTitle, legend);
        scheduleCard.add(schedHeader);

        // Render schedule by day
        Div schedContainer = new Div();
        schedContainer.addClassName("faculty-schedule-container");

        Map<String, List<ClassOccurrence>> dayMap = groupScheduleByDay(faculty.weeklySchedule());

        for (Map.Entry<String, List<ClassOccurrence>> entry : dayMap.entrySet()) {
            String day = entry.getKey();
            List<ClassOccurrence> slots = entry.getValue();

            Div dayGroup = new Div();
            dayGroup.addClassNames("faculty-schedule-day-group", "neo-sched-day-group");

            Div dayHead = new Div();
            dayHead.addClassNames("faculty-schedule-day-header", "neo-sched-day-header");
            Span dayTitle = new Span(day);
            dayTitle.addClassNames("font-ui");
            Span slotCount = new Span(slots.size() + (slots.size() == 1 ? " session" : " sessions"));
            slotCount.addClassNames("font-mono", "text-muted");
            dayHead.add(dayTitle, slotCount);

            dayGroup.add(dayHead);

            for (ClassOccurrence slot : slots) {
                Div slotItem = new Div();
                slotItem.addClassNames("faculty-schedule-slot-item", "neo-sched-slot-item");
                if (slot.isNextUp()) {
                    slotItem.addClassName("is-current");
                }

                Div slotLeft = new Div();
                slotLeft.addClassName("schedule-slot-left");

                Span slotTime = new Span(slot.startTime() + " – " + slot.endTime());
                slotTime.addClassNames("schedule-slot-time", "font-mono");

                Div slotDetails = new Div();
                Span courseName = new Span(slot.courseCode() + " · " + slot.courseName());
                courseName.addClassName("schedule-slot-course");

                Span roomSpan = new Span(" (" + slot.room() + ")");
                roomSpan.addClassNames("schedule-slot-room", "font-mono");

                slotDetails.add(courseName, roomSpan);
                slotLeft.add(slotTime, slotDetails);

                HorizontalLayout slotRight = new HorizontalLayout();
                slotRight.setAlignItems(FlexComponent.Alignment.CENTER);
                slotRight.setSpacing(true);

                Span typeBadge = new Span(slot.type());
                typeBadge.addClassNames("schedule-type-badge", getTypeClass(slot.type()));
                slotRight.add(typeBadge);

                if (slot.nextUpBadge() != null) {
                    Span countChip = new Span(slot.nextUpBadge());
                    countChip.addClassNames("chip", "chip-next-up", "neo-chip-gold", "font-mono");
                    slotRight.add(countChip);
                }

                slotItem.add(slotLeft, slotRight);
                dayGroup.add(slotItem);
            }

            schedContainer.add(dayGroup);
        }

        scheduleCard.add(schedContainer);
        leftCol.add(scheduleCard);

        // Research Areas Card
        Div researchCard = new Div();
        researchCard.addClassNames("campus-card", "stream-card", "neo-glass-panel");

        H3 resTitle = new H3("Research & Academic Interests");
        resTitle.addClassNames("font-ui", "neo-glass-panel-title", "margin-none");

        Div chipsContainer = new Div();
        chipsContainer.addClassNames("research-chips-wrap", "neo-research-chips");

        if (faculty.researchAreas() != null) {
            String[] areas = faculty.researchAreas().split(",");
            for (String area : areas) {
                Span chip = new Span(area.trim());
                chip.addClassName("research-chip");
                chipsContainer.add(chip);
            }
        }

        Paragraph resDesc = new Paragraph("Actively supervising undergraduate B.Tech projects, M.Tech dissertations, and doctoral research in theoretical computer science, algorithmic complexity, and scalable network architectures.");
        resDesc.addClassNames("font-ui", "text-muted");

        researchCard.add(resTitle, chipsContainer, resDesc);
        leftCol.add(researchCard);

        // RIGHT COLUMN: Next-Class Card, Courses Taught, Consultation Policy
        VerticalLayout rightCol = new VerticalLayout();
        rightCol.setWidthFull();
        rightCol.setSpacing(true);
        rightCol.setPadding(false);

        // Next-Class Card
        if (faculty.nextClass() != null) {
            Div nextCard = new Div();
            nextCard.addClassNames("next-class-card", "neo-glass-panel", "neo-next-class-highlight");

            Div nextHead = new Div();
            nextHead.addClassName("next-class-header");

            Span nextTag = new Span("NEXT UPCOMING CLASS");
            nextTag.addClassNames("next-class-tag", "font-mono");

            if (faculty.nextClass().nextUpBadge() != null) {
                Span countChip = new Span(faculty.nextClass().nextUpBadge());
                countChip.addClassNames("chip", "chip-next-up", "neo-chip-gold", "font-mono");
                nextHead.add(nextTag, countChip);
            } else {
                nextHead.add(nextTag);
            }

            H3 nextCode = new H3(faculty.nextClass().courseCode());
            nextCode.addClassNames("next-class-code", "font-mono");

            Paragraph nextTitle = new Paragraph(faculty.nextClass().courseName());
            nextTitle.addClassNames("next-class-title", "font-ui");

            Div nextMeta = new Div();
            nextMeta.addClassNames("next-class-meta", "neo-next-meta");

            Div timeRow = new Div();
            timeRow.addClassName("next-class-meta-row");
            Span timeLbl = new Span("Time");
            timeLbl.addClassNames("text-muted", "font-ui");
            Span timeVal = new Span(faculty.nextClass().startTime() + " – " + faculty.nextClass().endTime());
            timeVal.addClassNames("font-mono", "text-navy");
            timeRow.add(timeLbl, timeVal);

            Div roomRow = new Div();
            roomRow.addClassName("next-class-meta-row");
            Span roomLbl = new Span("Room");
            roomLbl.addClassNames("text-muted", "font-ui");
            Span roomVal = new Span(faculty.nextClass().room());
            roomVal.addClassNames("font-mono", "text-navy");
            roomRow.add(roomLbl, roomVal);

            Div typeRow = new Div();
            typeRow.addClassName("next-class-meta-row");
            Span typeLbl = new Span("Format");
            typeLbl.addClassNames("text-muted", "font-ui");
            Span typeVal = new Span(faculty.nextClass().type());
            typeVal.addClassNames("font-ui", "text-main");
            typeRow.add(typeLbl, typeVal);

            nextMeta.add(timeRow, roomRow, typeRow);

            Button openClassroomBtn = new Button("Open Classroom View →");
            openClassroomBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui");
            openClassroomBtn.addClickListener(e -> UI.getCurrent().navigate("classrooms/" + faculty.nextClass().courseCode()));

            nextCard.add(nextHead, nextCode, nextTitle, nextMeta, openClassroomBtn);
            rightCol.add(nextCard);
        }

        // Courses Taught Card
        Div coursesCard = new Div();
        coursesCard.addClassNames("campus-card", "stream-card", "neo-glass-panel");

        H3 coursesTitle = new H3("Courses Taught");
        coursesTitle.addClassNames("font-ui", "neo-glass-panel-title", "margin-none");

        Div coursesList = new Div();
        coursesList.addClassName("courses-list-group");

        if (faculty.coursesTaught() != null) {
            for (String c : faculty.coursesTaught()) {
                Div cRow = new Div();
                cRow.addClassNames("course-item-row", "neo-course-item-row");
                Span cName = new Span(c);
                cName.addClassNames("font-ui");
                Span codeBadge = new Span("Active");
                codeBadge.addClassNames("chip", "chip-safe", "neo-chip-mint", "font-mono");
                cRow.add(cName, codeBadge);
                coursesList.add(cRow);
            }
        }

        coursesCard.add(coursesTitle, coursesList);
        rightCol.add(coursesCard);

        // Consultation Policy Card
        Div policyCard = new Div();
        policyCard.addClassNames("campus-card", "stream-card", "neo-glass-panel");

        H3 policyTitle = new H3("Office Hours & Appointments");
        policyTitle.addClassNames("font-ui", "neo-glass-panel-title", "margin-none");

        Paragraph policyText = new Paragraph("Walk-ins are welcomed during scheduled office hours (" + faculty.officeHours() + "). For academic discussions outside regular hours, students are requested to send an email at least 24 hours prior.");
        policyText.addClassNames("font-ui", "text-muted");

        Button bookSlotBtn = new Button("Request Appointment");
        bookSlotBtn.addClassNames("btn-secondary", "neo-btn-outline", "font-ui");
        bookSlotBtn.addClickListener(e -> Notification.show("Appointment request drafted for " + faculty.name(), 3000, Notification.Position.BOTTOM_END));

        policyCard.add(policyTitle, policyText, bookSlotBtn);
        rightCol.add(policyCard);

        mainGrid.add(leftCol, rightCol);
        profileLayout.add(breadcrumb, heroCard, mainGrid);
        contentArea.add(profileLayout);
    }

    /* =========================================================================
       HELPER METHODS
       ========================================================================= */

    private Div createLiveStatusBadge(String text, String type) {
        Div badge = new Div();
        badge.addClassNames("faculty-status-badge", "neo-faculty-status-badge");

        Span dot = new Span();
        dot.addClassNames("status-dot", "neo-status-dot");

        Span label = new Span(text != null ? text : "Status Unknown");
        label.addClassNames("neo-status-label", "font-ui");

        if ("IN_CLASS".equalsIgnoreCase(type)) {
            badge.addClassNames("badge-in-class", "neo-badge-in-class");
            dot.addClassNames("dot-amber", "neo-dot-amber");
        } else if ("OFFICE_HOURS".equalsIgnoreCase(type)) {
            badge.addClassNames("badge-office-hours", "neo-badge-office-hours");
            dot.addClassNames("dot-green", "neo-dot-green");
        } else {
            badge.addClassNames("badge-available", "neo-badge-available");
            dot.addClassNames("dot-green", "neo-dot-green");
        }

        badge.add(dot, label);
        return badge;
    }

    private String getInitials(String name) {
        if (name == null || name.isBlank()) return "FC";
        String clean = name.replace("Dr.", "").replace("Prof.", "").trim();
        String[] parts = clean.split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }

    private String getTypeClass(String type) {
        if (type == null) return "type-lecture";
        return switch (type.toLowerCase()) {
            case "lab" -> "type-lab";
            case "consultation", "office hours" -> "type-consultation";
            case "tutorial" -> "type-tutorial";
            default -> "type-lecture";
        };
    }

    private Map<String, List<ClassOccurrence>> groupScheduleByDay(List<ClassOccurrence> schedule) {
        Map<String, List<ClassOccurrence>> map = new LinkedHashMap<>();
        if (schedule == null) return map;

        List<String> order = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat");
        for (String day : order) {
            map.put(day, new ArrayList<>());
        }

        for (ClassOccurrence occ : schedule) {
            String dayKey = occ.dayOfWeek();
            if (dayKey == null) continue;
            // Normalize "Monday" -> "Mon", "Tuesday" -> "Tue", etc.
            for (String standard : order) {
                if (dayKey.toLowerCase().startsWith(standard.toLowerCase())) {
                    dayKey = standard;
                    break;
                }
            }
            map.computeIfAbsent(dayKey, k -> new ArrayList<>()).add(occ);
        }

        // Remove empty days
        map.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        return map;
    }
}
