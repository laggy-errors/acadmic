package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.Suggestion;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.components.StatusChip;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

@Route(value = "suggestions", layout = MainLayout.class)
@PageTitle("Suggestion Box - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class SuggestionBoxView extends VerticalLayout {

    private final ApiClient apiClient;
    private final Div suggestionsList = new Div();
    private String selectedFilter = "ALL"; // ALL, TOP_VOTED, UNDER_REVIEW, ACCEPTED, RESOLVED
    private String searchQuery = "";

    @Autowired
    public SuggestionBoxView(ApiClient apiClient) {
        this.apiClient = apiClient;

        addClassNames("view-container", "neo-suggestions-view");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        // Header Action: New Suggestion Dialog
        Button newSuggestionBtn = new Button("New Suggestion");
        newSuggestionBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui");
        newSuggestionBtn.addClickListener(e -> openNewSuggestionDialog());

        PageHeader header = new PageHeader(
                "Suggestion Box",
                "Campus improvement proposals, student upvoting, and administration responses",
                newSuggestionBtn
        );
        header.addClassName("neo-suggestions-header");

        // Filter Bar (Tabs)
        HorizontalLayout filterBar = new HorizontalLayout();
        filterBar.setSpacing(true);
        filterBar.addClassNames("tab-group", "neo-dept-filter-bar", "neo-suggestions-filter-bar");

        Button tabAll = createFilterTab("All Proposals", "ALL", true);
        Button tabTop = createFilterTab("Top Voted", "TOP_VOTED", false);
        Button tabUnderReview = createFilterTab("Under Review", "UNDER_REVIEW", false);
        Button tabAccepted = createFilterTab("Accepted & Planned", "ACCEPTED", false);
        Button tabResolved = createFilterTab("Resolved", "RESOLVED", false);

        filterBar.add(tabAll, tabTop, tabUnderReview, tabAccepted, tabResolved);

        // 2-Column Board Grid (Suggestions List left, Guidelines right)
        Div boardGrid = new Div();
        boardGrid.addClassNames("suggestion-board-grid", "neo-suggestions-layout-grid");

        // LEFT COLUMN: Search + List
        VerticalLayout leftCol = new VerticalLayout();
        leftCol.setWidthFull();
        leftCol.setSpacing(true);
        leftCol.setPadding(false);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search proposals by title, category, or response...");
        searchField.setAriaLabel("Search proposals by title, category, or response");
        searchField.setWidthFull();
        searchField.addClassNames("neo-inset-input", "neo-suggestions-search", "font-ui");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> {
            searchQuery = e.getValue();
            renderSuggestions();
        });

        suggestionsList.addClassNames("suggestions-list", "neo-suggestions-list");
        renderSuggestions();

        leftCol.add(searchField, suggestionsList);

        // RIGHT COLUMN: Stats & Guidelines
        VerticalLayout rightCol = new VerticalLayout();
        rightCol.setWidthFull();
        rightCol.setSpacing(true);
        rightCol.setPadding(false);
        rightCol.addClassNames("neo-suggestions-sticky-sidebar");

        // Board Stats Card
        Div statsCard = new Div();
        statsCard.addClassNames("campus-card", "stream-card", "neo-glass-panel", "neo-governance-stats-card");

        H3 statsTitle = new H3("Proposal Governance");
        statsTitle.addClassNames("font-ui", "text-navy", "margin-none", "neo-card-title");

        Div statsList = new Div();
        statsList.addClassNames("courses-list-group", "neo-governance-list");

        statsList.add(
                createStatRow("Total Proposals", "5", "chip-neutral"),
                createStatRow("Senate Review Threshold", "30 votes", "chip-watch"),
                createStatRow("Implementation Rate", "60%", "chip-safe")
        );

        statsCard.add(statsTitle, statsList);

        // Guidelines Card
        Div guidelinesCard = new Div();
        guidelinesCard.addClassNames("campus-card", "stream-card", "neo-glass-panel", "neo-sla-guidelines-card");

        H3 guideTitle = new H3("Student Voice & SLA Guidelines");
        guideTitle.addClassNames("font-ui", "text-navy", "margin-none", "neo-card-title");

        Paragraph p1 = new Paragraph("• Anonymous submissions conceal your name, email, and roll number completely from the public feed.");
        p1.addClassNames("font-ui", "text-main", "neo-guidelines-text");

        Paragraph p2 = new Paragraph("• Any proposal reaching 30+ student upvotes is formally placed on the monthly Student-Faculty Senate agenda.");
        p2.addClassNames("font-ui", "text-main", "neo-guidelines-text");

        Paragraph p3 = new Paragraph("• For urgent facilities breakdowns (e.g. power, water, or network outages), contact Estate Desk directly.");
        p3.addClassNames("font-ui", "text-muted", "neo-guidelines-text");

        guidelinesCard.add(guideTitle, p1, p2, p3);

        rightCol.add(statsCard, guidelinesCard);

        boardGrid.add(leftCol, rightCol);
        add(header, filterBar, boardGrid);
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
            renderSuggestions();
        });
        return btn;
    }

    private Div createStatRow(String label, String value, String chipClass) {
        Div row = new Div();
        row.addClassNames("course-item-row", "neo-governance-row");

        Span l = new Span(label);
        l.addClassNames("font-ui", "text-main", "neo-governance-label");

        Span v = new Span(value);
        v.addClassNames("chip", chipClass, "font-mono", "neo-governance-val");

        row.add(l, v);
        return row;
    }

    private void renderSuggestions() {
        suggestionsList.removeAll();

        List<Suggestion> all = apiClient.getSuggestions();

        List<Suggestion> filtered = all.stream()
                .filter(s -> {
                    if ("ALL".equalsIgnoreCase(selectedFilter)) return true;
                    if ("TOP_VOTED".equalsIgnoreCase(selectedFilter)) return true;
                    if ("UNDER_REVIEW".equalsIgnoreCase(selectedFilter)) return "UNDER_REVIEW".equalsIgnoreCase(s.status());
                    if ("ACCEPTED".equalsIgnoreCase(selectedFilter)) return "ACCEPTED".equalsIgnoreCase(s.status()) || "PLANNED".equalsIgnoreCase(s.status());
                    if ("RESOLVED".equalsIgnoreCase(selectedFilter)) return "RESOLVED".equalsIgnoreCase(s.status());
                    return true;
                })
                .filter(s -> {
                    if (searchQuery == null || searchQuery.isBlank()) return true;
                    String q = searchQuery.toLowerCase().trim();
                    return s.title().toLowerCase().contains(q)
                            || s.description().toLowerCase().contains(q)
                            || s.category().toLowerCase().contains(q)
                            || (s.officialResponse() != null && s.officialResponse().toLowerCase().contains(q));
                })
                .sorted((a, b) -> {
                    if ("TOP_VOTED".equalsIgnoreCase(selectedFilter)) {
                        return Integer.compare(b.upvotes(), a.upvotes());
                    }
                    return 0; // retain default order
                })
                .toList();

        if (filtered.isEmpty()) {
            Div empty = new Div();
            empty.addClassNames("campus-card", "empty-state-card", "neo-glass-panel");
            Paragraph p = new Paragraph("No proposals found for the selected filter.");
            p.addClassNames("font-ui", "text-muted");
            empty.add(p);
            suggestionsList.add(empty);
            return;
        }

        for (Suggestion s : filtered) {
            suggestionsList.add(createSuggestionCard(s));
        }
    }

    private Div createSuggestionCard(Suggestion s) {
        Div card = new Div();
        card.addClassNames("suggestion-card", "neo-glass-card", "neo-suggestion-card");

        // Upvote Column
        Div upvoteCol = new Div();
        upvoteCol.addClassNames("suggestion-upvote-col", "neo-upvote-col");

        Div upvoteBtn = new Div();
        upvoteBtn.addClassNames("btn-upvote", "neo-btn-upvote");
        upvoteBtn.getElement().setAttribute("role", "button");
        upvoteBtn.getElement().setAttribute("tabindex", "0");
        upvoteBtn.getElement().setAttribute("aria-label", "Upvote proposal: " + s.title() + ". Current votes: " + s.upvotes());
        if (s.hasUpvoted()) {
            upvoteBtn.addClassNames("is-upvoted", "neo-upvoted");
        }

        Span upvoteIcon = new Span("▲");
        upvoteIcon.addClassNames("upvote-icon", "neo-upvote-icon");

        Span upvoteCount = new Span(String.valueOf(s.upvotes()));
        upvoteCount.addClassNames("upvote-count", "neo-upvote-count", "font-mono");

        upvoteBtn.add(upvoteIcon, upvoteCount);

        Runnable toggleAction = () -> {
            boolean isNowUpvoted = apiClient.toggleSuggestionUpvote(s.id());
            if (isNowUpvoted) {
                Notification.show("Upvoted proposal: " + s.title(), 2000, Notification.Position.BOTTOM_END);
            } else {
                Notification.show("Upvote removed.", 1500, Notification.Position.BOTTOM_END);
            }
            renderSuggestions();
        };

        upvoteBtn.addClickListener(e -> toggleAction.run());
        upvoteBtn.getElement().addEventListener("keydown", e -> toggleAction.run())
                .setFilter("event.key === 'Enter' || event.key === ' '");

        upvoteCol.add(upvoteBtn);

        // Content Column
        Div contentCol = new Div();
        contentCol.addClassNames("suggestion-content-col", "neo-suggestion-content-col");

        // Top Meta: Category Chip + Status Chip + Date
        Div topMeta = new Div();
        topMeta.addClassNames("suggestion-top-meta", "neo-suggestion-top-meta");

        Div metaLeft = new Div();
        metaLeft.addClassNames("suggestion-meta-left", "neo-suggestion-meta-left");

        Span catChip = new Span(s.category());
        catChip.addClassNames("chip", "chip-neutral", "font-mono", "neo-suggestion-cat-chip");

        StatusChip statusChip = createStatusChip(s.status(), s.statusLabel());

        metaLeft.add(catChip, statusChip);

        Span dateSpan = new Span(s.date());
        dateSpan.addClassNames("font-mono", "text-muted", "neo-suggestion-date");

        topMeta.add(metaLeft, dateSpan);

        // Title
        H3 title = new H3(s.title());
        title.addClassNames("suggestion-title", "neo-suggestion-title", "font-ui");

        // Description
        Paragraph desc = new Paragraph(s.description());
        desc.addClassNames("suggestion-desc", "neo-suggestion-desc", "font-ui");

        // Author line
        Paragraph author = new Paragraph("Proposed by " + s.author());
        author.addClassNames("suggestion-author-line", "neo-suggestion-author", "font-ui");

        contentCol.add(topMeta, title, desc, author);

        // Official Response (inset block)
        if (s.officialResponse() != null && !s.officialResponse().isBlank()) {
            Div respBox = new Div();
            respBox.addClassNames("official-response-box", "neo-official-response-box");

            Span respLabel = new Span("Official Response:");
            respLabel.addClassNames("official-response-label", "neo-official-response-label");

            Span respText = new Span(s.officialResponse());
            respText.addClassName("neo-official-response-text");

            respBox.add(respLabel, respText);
            contentCol.add(respBox);
        }

        card.add(upvoteCol, contentCol);
        return card;
    }

    private StatusChip createStatusChip(String status, String label) {
        if ("ACCEPTED".equalsIgnoreCase(status) || "PLANNED".equalsIgnoreCase(status)) {
            return StatusChip.safe(label);
        } else if ("RESOLVED".equalsIgnoreCase(status)) {
            return StatusChip.neutral(label);
        } else {
            return StatusChip.watch(label);
        }
    }

    /* =========================================================================
       NEW SUGGESTION DIALOG (WITH ANONYMOUS OPTION)
       ========================================================================= */

    private void openNewSuggestionDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Submit a Campus Proposal");
        dialog.addClassNames("neo-glass-dialog", "neo-suggestion-modal");

        VerticalLayout form = new VerticalLayout();
        form.setPadding(false);
        form.setSpacing(true);
        form.setWidth("440px");

        Paragraph desc = new Paragraph("Proposals are visible across campus for student upvoting. Ideas reaching 30+ votes are escalated to the Senate.");
        desc.addClassNames("font-ui", "text-muted");

        TextField titleField = new TextField("Proposal Title");
        titleField.setPlaceholder("Clear, specific summary of the proposed improvement...");
        titleField.setWidthFull();

        ComboBox<String> categorySelect = new ComboBox<>("Category");
        categorySelect.setItems(
                "Academics",
                "Campus IT & Wi-Fi",
                "Hostel & Facilities",
                "Library & Reading Rooms",
                "Mess & Dining",
                "Sports & Recreation"
        );
        categorySelect.setValue("Campus IT & Wi-Fi");
        categorySelect.setWidthFull();

        TextArea detailsArea = new TextArea("Detailed Context & Proposed Solution");
        detailsArea.setPlaceholder("Describe the current issue, affected student groups, and potential steps for implementation...");
        detailsArea.setMinHeight("110px");
        detailsArea.setWidthFull();

        Checkbox anonymousBox = new Checkbox("Submit anonymously (Conceal my identity from public board)");
        anonymousBox.setValue(true); // Default to anonymous for student privacy

        form.add(desc, titleField, categorySelect, detailsArea, anonymousBox);
        dialog.add(form);

        Button submitBtn = new Button("Submit Proposal", e -> {
            String titleVal = titleField.getValue();
            if (titleVal == null || titleVal.trim().isBlank()) {
                Notification.show("Please enter a proposal title.", 2500, Notification.Position.BOTTOM_END);
                return;
            }

            Suggestion created = apiClient.createSuggestion(
                    titleVal.trim(),
                    categorySelect.getValue(),
                    detailsArea.getValue() != null ? detailsArea.getValue().trim() : "",
                    anonymousBox.getValue()
            );

            Notification.show("Proposal submitted! Assigned ticket #" + created.id().toUpperCase(), 3500, Notification.Position.BOTTOM_END);
            dialog.close();
            renderSuggestions();
        });
        submitBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui");

        Button cancelBtn = new Button("Cancel", e -> dialog.close());
        cancelBtn.addClassNames("btn-secondary", "neo-btn-outline", "font-ui");

        HorizontalLayout footer = new HorizontalLayout(cancelBtn, submitBtn);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footer.setWidthFull();

        dialog.getFooter().add(footer);
        dialog.open();
    }
}
