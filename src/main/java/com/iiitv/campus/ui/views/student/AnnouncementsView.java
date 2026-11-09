package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.Announcement;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

@Route(value = "announcements", layout = MainLayout.class)
@PageTitle("Announcements - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class AnnouncementsView extends VerticalLayout {

    private final ApiClient apiClient;
    private final Div announcementsContainer = new Div();
    private final Button markAllReadBtn = new Button();
    private final Span unreadCountBadge = new Span();

    private String selectedFilter = "ALL"; // ALL, UNREAD, Academic, Exams, Placement, Administrative, Events
    private String searchQuery = "";

    @Autowired
    public AnnouncementsView(ApiClient apiClient) {
        this.apiClient = apiClient;

        addClassNames("view-container", "neo-announcements-view");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        // Header with Search and Mark All Read
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search announcements or circulars...");
        searchField.setAriaLabel("Search announcements or circulars");
        searchField.setWidth("300px");
        searchField.addClassNames("neo-inset-input", "neo-announcements-search", "font-ui");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        markAllReadBtn.setText("Mark All Read");
        markAllReadBtn.addClassNames("btn-secondary", "neo-btn-mark-all-read", "font-ui");
        markAllReadBtn.getElement().setAttribute("aria-label", "Mark all announcements as read");
        unreadCountBadge.addClassNames("unread-count-badge", "neo-unread-badge");
        markAllReadBtn.setIcon(unreadCountBadge);
        markAllReadBtn.addClickListener(e -> {
            apiClient.markAllAnnouncementsRead();
            Notification.show("All announcements marked as read.", 2500, Notification.Position.BOTTOM_END);
            refreshView();
        });

        HorizontalLayout headerActions = new HorizontalLayout();
        headerActions.setSpacing(true);
        headerActions.setAlignItems(FlexComponent.Alignment.CENTER);
        headerActions.add(searchField, markAllReadBtn);

        PageHeader header = new PageHeader(
                "Announcements",
                "Official circulars, examination schedules, and institutional notices",
                headerActions
        );
        header.addClassName("neo-announcements-header");

        // Category Filter Chips Bar
        HorizontalLayout categoryFilterBar = createCategoryFilterBar();

        searchField.addValueChangeListener(e -> {
            searchQuery = e.getValue();
            renderList();
        });

        announcementsContainer.addClassNames("announcements-container", "neo-announcements-list");

        refreshView();

        add(header, categoryFilterBar, announcementsContainer);
    }

    private HorizontalLayout createCategoryFilterBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setSpacing(true);
        bar.addClassNames("tab-group", "neo-dept-filter-bar", "neo-announcements-filter-bar");

        Button tabAll = createFilterTab("All", "ALL", true);
        Button tabUnread = createFilterTab("Unread", "UNREAD", false);
        Button tabAcademic = createFilterTab("Academic", "Academic", false);
        Button tabExams = createFilterTab("Exams", "Exams", false);
        Button tabPlacement = createFilterTab("Placement", "Placement", false);
        Button tabAdmin = createFilterTab("Administrative", "Administrative", false);
        Button tabEvents = createFilterTab("Events", "Events", false);

        bar.add(tabAll, tabUnread, tabAcademic, tabExams, tabPlacement, tabAdmin, tabEvents);
        return bar;
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
            renderList();
        });
        return btn;
    }

    private void refreshView() {
        List<Announcement> all = apiClient.getAnnouncements();
        long unreadCount = all.stream().filter(a -> !a.isRead()).count();
        unreadCountBadge.setText(String.valueOf(unreadCount));
        markAllReadBtn.setEnabled(unreadCount > 0);

        renderList();
    }

    private void renderList() {
        announcementsContainer.removeAll();

        List<Announcement> list = apiClient.getAnnouncements();

        // Filter and sort: pinned first, then natural order
        List<Announcement> filtered = list.stream()
                .filter(a -> {
                    if ("ALL".equalsIgnoreCase(selectedFilter)) return true;
                    if ("UNREAD".equalsIgnoreCase(selectedFilter)) return !a.isRead();
                    return a.category().equalsIgnoreCase(selectedFilter);
                })
                .filter(a -> {
                    if (searchQuery == null || searchQuery.isBlank()) return true;
                    String q = searchQuery.toLowerCase().trim();
                    return a.title().toLowerCase().contains(q)
                            || a.summary().toLowerCase().contains(q)
                            || a.publisher().toLowerCase().contains(q)
                            || a.category().toLowerCase().contains(q);
                })
                .sorted(Comparator.comparing(Announcement::isPinned).reversed())
                .toList();

        if (filtered.isEmpty()) {
            Div empty = new Div();
            empty.addClassNames("campus-card", "empty-state-card", "neo-glass-panel");
            Paragraph p = new Paragraph("No circulars found for the selected filter.");
            p.addClassNames("font-ui", "text-muted");
            empty.add(p);
            announcementsContainer.add(empty);
            return;
        }

        for (Announcement a : filtered) {
            announcementsContainer.add(createAnnouncementCard(a));
        }
    }

    private Div createAnnouncementCard(Announcement a) {
        Div card = new Div();
        card.addClassNames("announcement-card", "neo-glass-card", "neo-announcement-card");
        if (a.isPinned()) {
            card.addClassNames("is-pinned", "neo-card-pinned");
        }
        if (!a.isRead()) {
            card.addClassNames("is-unread", "neo-card-unread");
        } else {
            card.addClassNames("is-read", "neo-card-read");
        }

        // Top Row: Pinned Tag + Category Chip + Unread Indicator + Date
        Div top = new Div();
        top.addClassNames("announcement-top", "neo-announcement-top");

        Div topLeft = new Div();
        topLeft.addClassNames("announcement-top-left", "neo-announcement-top-left");

        if (a.isPinned()) {
            Span pinChip = new Span("PINNED");
            pinChip.addClassNames("chip-pinned", "neo-chip-pinned", "font-mono");
            topLeft.add(pinChip);
        }

        Span categoryChip = new Span(a.category());
        categoryChip.addClassNames("announcement-category-chip", "neo-chip-cat", getCategoryClass(a.category()), "font-mono");
        topLeft.add(categoryChip);

        if (!a.isRead()) {
            Span dot = new Span();
            dot.addClassNames("unread-dot", "neo-unread-dot");
            Span unreadText = new Span("NEW");
            unreadText.addClassNames("chip-unread", "neo-chip-new", "font-mono");
            topLeft.add(dot, unreadText);
        }

        Span date = new Span(a.date());
        date.addClassNames("font-mono", "text-muted", "neo-announcement-date");

        top.add(topLeft, date);

        // Title
        H3 title = new H3(a.title());
        title.addClassNames("announcement-title", "neo-announcement-title", "font-ui");

        // Publisher Meta
        Paragraph meta = new Paragraph("Issued by " + a.publisher());
        meta.addClassNames("announcement-meta", "neo-announcement-publisher", "font-ui");

        // Body Summary
        Paragraph body = new Paragraph(a.summary());
        body.addClassNames("announcement-body", "neo-announcement-body", "font-ui");

        card.add(top, title, meta, body);

        // Attachment if available
        if (a.attachmentName() != null && !a.attachmentName().isBlank()) {
            Div attachmentBox = new Div();
            attachmentBox.addClassNames("announcement-attachment", "neo-attachment-pill");

            Span fileTag = new Span("PDF");
            fileTag.addClassNames("chip", "chip-neutral", "font-mono", "neo-chip-pdf");
            Span fileName = new Span(a.attachmentName());
            fileName.addClassNames("font-mono", "neo-attachment-filename");
            Span fileSize = new Span("(" + a.attachmentSize() + ")");
            fileSize.addClassNames("font-mono", "text-muted", "neo-attachment-filesize");

            attachmentBox.add(fileTag, fileName, fileSize);
            attachmentBox.addClickListener(e ->
                    Notification.show("Downloading " + a.attachmentName(), 2500, Notification.Position.BOTTOM_END));

            card.add(attachmentBox);
        }

        // Card Actions Footer
        Div actions = new Div();
        actions.addClassNames("announcement-actions", "neo-announcement-actions");

        if (!a.isRead()) {
            Button markReadBtn = new Button("Mark as read");
            markReadBtn.addClassNames("btn-secondary", "neo-btn-outline", "font-ui", "neo-mark-read-btn");
            markReadBtn.addClickListener(e -> {
                apiClient.markAnnouncementRead(a.id());
                Notification.show("Marked as read: " + a.title(), 2000, Notification.Position.BOTTOM_END);
                refreshView();
            });
            actions.add(markReadBtn);
        } else {
            Span readStatus = new Span("Read ✓");
            readStatus.addClassNames("font-mono", "text-muted", "neo-read-status");
            actions.add(readStatus);
        }

        Button viewFullBtn = new Button("View Full Notice");
        viewFullBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui", "neo-view-notice-btn");
        viewFullBtn.addClickListener(e -> {
            openFullNoticeModal(a);
            Notification.show("Opening official circular: " + a.title(), 2500, Notification.Position.BOTTOM_END);
        });
        actions.add(viewFullBtn);

        card.add(actions);

        return card;
    }

    private void openFullNoticeModal(Announcement a) {
        Dialog dialog = new Dialog();
        dialog.addClassNames("neo-glass-dialog", "neo-announcement-modal");
        dialog.setWidth("640px");
        dialog.setMaxWidth("92vw");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);

        // Header strip
        HorizontalLayout modalTop = new HorizontalLayout();
        modalTop.setWidthFull();
        modalTop.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        modalTop.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout tags = new HorizontalLayout();
        tags.setSpacing(true);
        if (a.isPinned()) {
            Span pin = new Span("PINNED");
            pin.addClassNames("chip-pinned", "neo-chip-pinned", "font-mono");
            tags.add(pin);
        }
        Span cat = new Span(a.category());
        cat.addClassNames("announcement-category-chip", "neo-chip-cat", getCategoryClass(a.category()), "font-mono");
        tags.add(cat);

        Span modalDate = new Span(a.date());
        modalDate.addClassNames("font-mono", "text-muted", "neo-announcement-date");
        modalTop.add(tags, modalDate);

        H2 modalTitle = new H2(a.title());
        modalTitle.addClassNames("neo-modal-title", "font-ui");

        Paragraph modalPub = new Paragraph("Issued by " + a.publisher());
        modalPub.addClassNames("neo-modal-publisher", "font-ui");

        Paragraph modalBody = new Paragraph(a.summary());
        modalBody.addClassNames("neo-modal-body", "font-ui");

        content.add(modalTop, modalTitle, modalPub, modalBody);

        if (a.attachmentName() != null && !a.attachmentName().isBlank()) {
            Div attachmentBox = new Div();
            attachmentBox.addClassNames("announcement-attachment", "neo-attachment-pill");

            Span fileTag = new Span("PDF");
            fileTag.addClassNames("chip", "chip-neutral", "font-mono", "neo-chip-pdf");
            Span fileName = new Span(a.attachmentName());
            fileName.addClassName("font-mono");
            Span fileSize = new Span("(" + a.attachmentSize() + ")");
            fileSize.addClassNames("font-mono", "text-muted");

            attachmentBox.add(fileTag, fileName, fileSize);
            attachmentBox.addClickListener(e ->
                    Notification.show("Downloading " + a.attachmentName(), 2500, Notification.Position.BOTTOM_END));
            content.add(attachmentBox);
        }

        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footer.setSpacing(true);

        if (!a.isRead()) {
            Button markReadBtn = new Button("Mark as read");
            markReadBtn.addClassNames("btn-secondary", "neo-btn-outline", "font-ui");
            markReadBtn.addClickListener(e -> {
                apiClient.markAnnouncementRead(a.id());
                Notification.show("Marked as read: " + a.title(), 2000, Notification.Position.BOTTOM_END);
                dialog.close();
                refreshView();
            });
            footer.add(markReadBtn);
        }

        Button closeBtn = new Button("Close");
        closeBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui");
        closeBtn.addClickListener(e -> dialog.close());
        footer.add(closeBtn);

        content.add(footer);
        dialog.add(content);
        dialog.open();
    }

    private String getCategoryClass(String category) {
        if (category == null) return "chip-category-academic";
        return switch (category.toLowerCase()) {
            case "exams", "examination" -> "chip-category-exams";
            case "placement", "careers" -> "chip-category-placement";
            case "administrative", "admin", "hostel" -> "chip-category-admin";
            case "events", "symposium" -> "chip-category-events";
            default -> "chip-category-academic";
        };
    }
}
