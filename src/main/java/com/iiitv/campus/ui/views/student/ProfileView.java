package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.StudentInfo;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class ProfileView extends VerticalLayout {

    private final ApiClient apiClient;

    private String phoneVal = "+91 98765 43210";
    private String emailVal = "raghav.agarwal.personal@gmail.com";
    private String emergencyVal = "S. K. Agarwal (+91 98765 11223) · Father";

    private Span phoneSpan;
    private Span emailSpan;
    private Span emergencySpan;

    public ProfileView(ApiClient apiClient) {
        this.apiClient = apiClient;

        addClassName("view-container");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        StudentInfo profile = apiClient.getStudentProfile();

        PageHeader header = new PageHeader("My Profile", "Account identity, program details, mentor advisory, and security settings");

        // 1. Profile Hero Glass Banner
        Div heroCard = new Div();
        heroCard.addClassNames("neo-glass-card", "neo-profile-hero-card");

        HorizontalLayout heroLayout = new HorizontalLayout();
        heroLayout.setWidthFull();
        heroLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        heroLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        HorizontalLayout leftHero = new HorizontalLayout();
        leftHero.setAlignItems(FlexComponent.Alignment.CENTER);
        leftHero.setSpacing(true);

        Div avatar = new Div();
        avatar.addClassNames("neo-profile-avatar", "font-mono");
        avatar.setText("R");

        Div heroInfo = new Div();
        H2 nameHeading = new H2(profile.name());
        nameHeading.addClassNames("neo-profile-name", "font-ui");

        Paragraph meta = new Paragraph(profile.program() + " (Honours) · " + profile.semester() + " (Fall 2026) · " + profile.section() + " · Roll: " + profile.rollNumber());
        meta.addClassNames("neo-profile-meta", "font-mono");

        HorizontalLayout chipsRow = new HorizontalLayout();
        chipsRow.setSpacing(true);
        chipsRow.addClassName("neo-profile-chips-row");

        Span safeChip = new Span("GOOD STANDING (SAFE)");
        safeChip.addClassNames("neo-chip-mint", "font-mono");

        Span campusChip = new Span("ICD GANDHINAGAR");
        campusChip.addClassNames("neo-chip-sky", "font-mono");

        Span roleChip = new Span("ROLE: STUDENT");
        roleChip.addClassNames("neo-chip-gold", "font-mono");

        chipsRow.add(safeChip, campusChip, roleChip);
        heroInfo.add(nameHeading, meta, chipsRow);
        leftHero.add(avatar, heroInfo);

        Button editContactBtn = new Button("Edit Contact Info", e -> openEditProfileDialog());
        editContactBtn.addClassNames("neo-btn-maroon", "font-ui");

        heroLayout.add(leftHero, editContactBtn);
        heroCard.add(heroLayout);

        // 2. 4 Glass Cards Grid (Personal Info, Academic Info, Mentor Advisory, Settings)
        Div grid = new Div();
        grid.addClassNames("neo-profile-grid");

        // Card 1: Personal Information
        Div personalCard = new Div();
        personalCard.addClassNames("neo-glass-card", "neo-profile-section-card");

        H3 personalTitle = new H3("Personal Information");
        personalTitle.addClassNames("neo-card-header-title", "font-ui");
        personalCard.add(personalTitle);

        personalCard.add(createDetailRow("Full Legal Name", profile.name()));
        personalCard.add(createDetailRow("Date of Birth", "14 Aug 2005"));

        phoneSpan = new Span(phoneVal);
        phoneSpan.addClassNames("font-mono", "neo-detail-val");
        personalCard.add(createDetailRowWithSpan("Contact Phone", phoneSpan));

        emailSpan = new Span(emailVal);
        emailSpan.addClassNames("font-mono", "neo-detail-val");
        personalCard.add(createDetailRowWithSpan("Personal Email", emailSpan));

        personalCard.add(createDetailRow("Permanent Address", "B-402, Shivalik Residency, Gandhinagar, Gujarat 382007"));

        emergencySpan = new Span(emergencyVal);
        emergencySpan.addClassNames("font-mono", "neo-detail-val");
        personalCard.add(createDetailRowWithSpan("Emergency Contact", emergencySpan));

        personalCard.add(createDetailRow("Blood Group", "O+ (Positive)"));

        Button editDetailsBtn = new Button("Update Contact Details", e -> openEditProfileDialog());
        editDetailsBtn.addClassNames("neo-btn-outline", "font-ui", "neo-card-action-btn");
        personalCard.add(editDetailsBtn);

        // Card 2: Academic Record & Standing
        Div academicCard = new Div();
        academicCard.addClassNames("neo-glass-card", "neo-profile-section-card");

        H3 academicTitle = new H3("Academic Record");
        academicTitle.addClassNames("neo-card-header-title", "font-ui");
        academicCard.add(academicTitle);

        academicCard.add(createDetailRow("Degree Program", profile.program() + " (Honours)"));
        academicCard.add(createDetailRow("Current Term", profile.semester() + " (Fall 2026)"));
        academicCard.add(createDetailRow("Section & Batch", profile.section()));
        academicCard.add(createDetailRow("Student ID / Roll", profile.rollNumber()));
        academicCard.add(createDetailRow("Cumulative GPA", "8.42 / 10.00"));
        academicCard.add(createDetailRow("Academic Standing", "Good Standing (SAFE)"));
        academicCard.add(createDetailRow("Credits Completed", "88 / 160 Total"));
        academicCard.add(createDetailRow("Class Standing", "Rank #14 / 142"));

        Button viewGradesBtn = new Button("View Remarks & Guidance", e ->
                getUI().ifPresent(ui -> ui.navigate("remarks")));
        viewGradesBtn.addClassNames("neo-btn-outline", "font-ui", "neo-card-action-btn");
        academicCard.add(viewGradesBtn);

        // Card 3: Faculty Mentor & Advisory
        Div mentorCard = new Div();
        mentorCard.addClassNames("neo-glass-card", "neo-profile-section-card");

        H3 mentorTitle = new H3("Faculty Mentor");
        mentorTitle.addClassNames("neo-card-header-title", "font-ui");
        mentorCard.add(mentorTitle);

        mentorCard.add(createDetailRow("Assigned Mentor", "Dr. Amit Mehta"));
        mentorCard.add(createDetailRow("Designation", "Associate Professor & Head of CSE"));
        mentorCard.add(createDetailRow("Office Location", "ICD-2125 (2nd Floor, Academic Block)"));
        mentorCard.add(createDetailRow("Office Hours", "Tue, Thu 15:00 – 16:30"));
        mentorCard.add(createDetailRow("Direct Email", "amit.mehta@iiitvadodara.ac.in"));
        mentorCard.add(createDetailRow("Active Advisory", "Commendation: Outstanding project work in CS301"));

        HorizontalLayout mentorActions = new HorizontalLayout();
        mentorActions.setWidthFull();
        mentorActions.setSpacing(true);

        Button mentorProfileBtn = new Button("View Mentor Profile ->", e ->
                getUI().ifPresent(ui -> ui.navigate("faculty-directory")));
        mentorProfileBtn.addClassNames("neo-btn-maroon", "font-ui");

        Button reqAdvisoryBtn = new Button("Book Advisory", e ->
                Notification.show("Advisory session requested with Dr. Amit Mehta.", 3000, Notification.Position.BOTTOM_END));
        reqAdvisoryBtn.addClassNames("neo-btn-outline", "font-ui");

        mentorActions.add(mentorProfileBtn, reqAdvisoryBtn);
        mentorCard.add(mentorActions);

        // Card 4: Settings & Institute Credentials
        Div settingsCard = new Div();
        settingsCard.addClassNames("neo-glass-card", "neo-profile-section-card");

        H3 settingsTitle = new H3("Credentials & Settings");
        settingsTitle.addClassNames("neo-card-header-title", "font-ui");
        settingsCard.add(settingsTitle);

        settingsCard.add(createDetailRow("Institute Email", "raghav_" + profile.rollNumber() + "@iiitvadodara.ac.in"));
        settingsCard.add(createDetailRow("Library Card ID", "LIB-" + profile.rollNumber()));
        settingsCard.add(createDetailRow("RFID Smart Card", "Active (Valid through Jul 2026)"));
        settingsCard.add(createDetailRow("2-Factor Authentication", "Enabled (TOTP Mobile)"));
        settingsCard.add(createDetailRow("Notification Digest", "Daily Email Summary (08:00 AM)"));
        settingsCard.add(createDetailRow("Theme Language", "Neo on Glass · English (IN)"));

        HorizontalLayout settingsActions = new HorizontalLayout();
        settingsActions.setWidthFull();
        settingsActions.setSpacing(true);

        Button resetPwdBtn = new Button("Change Password", e ->
                Notification.show("Password reset link sent to institute email.", 3000, Notification.Position.BOTTOM_END));
        resetPwdBtn.addClassNames("neo-btn-maroon", "font-ui");

        Button updatePrefsBtn = new Button("Save Settings", e ->
                Notification.show("Account preferences saved successfully.", 3000, Notification.Position.BOTTOM_END));
        updatePrefsBtn.addClassNames("neo-btn-outline", "font-ui");

        settingsActions.add(resetPwdBtn, updatePrefsBtn);
        settingsCard.add(settingsActions);

        grid.add(personalCard, academicCard, mentorCard, settingsCard);
        add(header, heroCard, grid);
    }

    private Div createDetailRow(String label, String value) {
        Div row = new Div();
        row.addClassName("neo-detail-row");

        Span l = new Span(label);
        l.addClassNames("font-ui", "neo-detail-label");

        Span v = new Span(value);
        v.addClassNames("font-mono", "neo-detail-val");

        row.add(l, v);
        return row;
    }

    private Div createDetailRowWithSpan(String label, Span valSpan) {
        Div row = new Div();
        row.addClassName("neo-detail-row");

        Span l = new Span(label);
        l.addClassNames("font-ui", "neo-detail-label");

        row.add(l, valSpan);
        return row;
    }

    private void openEditProfileDialog() {
        Dialog dialog = new Dialog();
        dialog.addClassNames("neo-glass-dialog", "neo-profile-edit-dialog");
        dialog.setHeaderTitle("Edit Personal Contact Information");
        dialog.setWidth("480px");
        dialog.setMaxWidth("92vw");

        VerticalLayout form = new VerticalLayout();
        form.setPadding(false);
        form.setSpacing(true);

        TextField phoneField = new TextField("Contact Phone Number");
        phoneField.setValue(phoneVal);
        phoneField.setWidthFull();
        phoneField.addClassNames("neo-inset-input", "font-mono");

        TextField emailField = new TextField("Personal Email Address");
        emailField.setValue(emailVal);
        emailField.setWidthFull();
        emailField.addClassNames("neo-inset-input", "font-ui");

        TextField emergencyField = new TextField("Emergency Contact & Relationship");
        emergencyField.setValue(emergencyVal);
        emergencyField.setWidthFull();
        emergencyField.addClassNames("neo-inset-input", "font-ui");

        Checkbox smsAlerts = new Checkbox("Enable urgent timetable clash & exam SMS alerts", true);
        smsAlerts.addClassName("font-ui");

        form.add(phoneField, emailField, emergencyField, smsAlerts);
        dialog.add(form);

        Button cancelBtn = new Button("Cancel", e -> dialog.close());
        cancelBtn.addClassNames("neo-btn-outline", "font-ui");

        Button saveBtn = new Button("Save Changes", e -> {
            phoneVal = phoneField.getValue();
            emailVal = emailField.getValue();
            emergencyVal = emergencyField.getValue();

            phoneSpan.setText(phoneVal);
            emailSpan.setText(emailVal);
            emergencySpan.setText(emergencyVal);

            dialog.close();
            Notification.show("Personal contact details updated successfully.", 3000, Notification.Position.BOTTOM_END);
        });
        saveBtn.addClassNames("neo-btn-maroon", "font-ui");

        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }
}
